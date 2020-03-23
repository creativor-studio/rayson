
package org.rayson.server;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.rayson.api.server.HttpServerRequest;
import org.rayson.server.exception.InternalServerError;
import org.rayson.share.logger.RaysonLoggerFactory;
import org.slf4j.Logger;

/**
 * An manager role used to manage {@link Worker}s.
 * 
 * @author Nick Zhang
 *
 */
public class WorkerManager implements Manager, Runnable {

	private static class ManagedWorker extends Worker {

		private AtomicBoolean idle;
		private long lastContact;

		ManagedWorker(long workerId, Container container, WorkerManager manager) {
			super(workerId, manager, container);
			idle = new AtomicBoolean(true);
			// Initialize the last contact time.
			touch();
		}

		/**
		 * Touch this worker.Update the last contact time to current time.
		 */
		void touch() {
			lastContact = System.currentTimeMillis();
		}

		/**
		 * @return <code>true</code> if time
		 *         {@value #WORKER_HANDLING_REQUEST_TIMEOUT} is out without be
		 *         touching.
		 */
		boolean isTimeOut() {
			return System.currentTimeMillis() - lastContact > WORKER_HANDLING_REQUEST_TIMEOUT ? true : false;
		}

		void interrupt() {
			this.thread.interrupt();
		}
	}

	private final Container container;
	private boolean running = false;
	private Thread thread;
	private long workerId;
	private final static int MINIMUM_WORKER_COUNT = 4;
	private final static int MAXIMUM_WORKER_COUNT = 1000;
	private final Lock lock;
	private final AtomicInteger workerCounter;
	private final AtomicInteger idleCounter;
	private final Map<Long, ManagedWorker> workers;

	private static final Logger LOGGER = RaysonLoggerFactory.getLogger(WorkerManager.class);

	private final static int WORKER_HANDLING_REQUEST_TIMEOUT = 1000 * 60 * 1;
	private final static int CHECK_WORKER_INTERVAL = 1000 * 60 * 5;

	private final Condition condAllBusy;

	/**
	 * Construct a new worker manager with it's container.
	 * 
	 * @param container Server container which manage this worker manager.
	 */
	public WorkerManager(final Container container) {
		super();
		this.container = container;
		workerId = 0;
		workers = new ConcurrentHashMap<>();
		lock = new ReentrantLock();
		condAllBusy = lock.newCondition();
		workerCounter = new AtomicInteger(0);
		idleCounter = new AtomicInteger(0);
	}

	@Override
	public Container getContainer() {
		return container;
	}

	@Override
	public String getName() {
		return "Worker Manager of " + getContainer().getServer().getName();
	}

	@Override
	public void run() {
		lock.lock();
		try {
			while (running) {
				condAllBusy.await(CHECK_WORKER_INTERVAL, TimeUnit.MILLISECONDS);

				// 1. Check whether all workers are busy.
				if (idleCounter.get() == 0) {
					try {
						tryStartWorker();
					} catch (Throwable e) {
						LOGGER.error(getName() + " failed to start new worker");
					}
					// Since all workers are busy, we just ignore other tasks.
					continue;
				}

				// 2. Check abandon workers every {@value
				// #CHECK_WORKER_INTERVAL}.
				if (idleCounter.get() > MINIMUM_WORKER_COUNT) {
					tryReduceWorkers();
				}

				// 3. Check timeout workers.
				tryInterruptWorkers();
			}
		} catch (final InterruptedException e) {
			if (running)
				LOGGER.error(getName() + "  is interrupted");
		} catch (final Throwable e) {
			LOGGER.error(getName() + " got fatal error: ", e);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Try to interrupt timeout workers.
	 */
	private void tryInterruptWorkers() {
		Collection<ManagedWorker> workerValues = workers.values();
		for (ManagedWorker managedWorker : workerValues) {
			if (managedWorker.isTimeOut())
				managedWorker.interrupt();
		}
	}

	private void tryReduceWorkers() {
		int busyCount = workerCounter.get() - idleCounter.get();
		int halfBusy = busyCount / 2;
		int keepIdle = (halfBusy > MINIMUM_WORKER_COUNT) ? halfBusy : MINIMUM_WORKER_COUNT;
		int reduceCount = idleCounter.get() - keepIdle;
		while (reduceCount > 0) {
			Collection<ManagedWorker> workerValues = workers.values();
			for (ManagedWorker managedWorker : workerValues) {
				if (managedWorker.isRunning()) {
					if (managedWorker.isTimeOut()) {
						// To ignore quitting worker frequently, we just stop
						// time out worker.
						managedWorker.stop();
					}
					reduceCount--;
					if (reduceCount == 0)
						break;
				}
			}
		}
	}

	@Override
	public void start() throws InternalServerError {
		LOGGER.info(getName() + " starting ...");

		// Set running flag true first.
		running = true;

		thread = new Thread(this, getName());
		thread.start();

		for (int i = 0; i < MINIMUM_WORKER_COUNT; i++) {
			tryStartWorker();
		}

	}

	private void tryStartWorker() throws InternalServerError {
		if (workerCounter.get() == MAXIMUM_WORKER_COUNT)
			return;
		final ManagedWorker worker = new ManagedWorker(workerId++, container, this);
		LOGGER.info("Staring new worker: " + worker.getName() + " ...");
		worker.start();
		workerCounter.incrementAndGet();
		idleCounter.incrementAndGet();
		workers.put(worker.getId(), worker);
	}

	@Override
	public void stop() {

		if (!this.running)
			return;

		LOGGER.info(getName() + " stopping ...");

		// try stop workers.
		for (Worker worker : this.workers.values()) {
			worker.stop();
		}

		// try stop underling thread.
		this.thread.interrupt();

		running = false;
	}

	/**
	 * Notify that one worker is quit.
	 * 
	 * @param worker Target worker.
	 */
	void notifyQuit(Worker worker) {
		workerCounter.decrementAndGet();
		idleCounter.decrementAndGet();
		workers.remove(worker.getId());
	}

	/**
	 * Notify that one worker is begin to handler a new request.
	 * 
	 * @param worker Target worker.
	 * @param request Target request.
	 */
	void notifyBeginRequest(Worker worker, HttpServerRequest request) {
		ManagedWorker mWorker = (ManagedWorker) worker;
		mWorker.idle.set(false);
		mWorker.touch();
		idleCounter.decrementAndGet();
		if (idleCounter.get() == 0) {
			// Then, all busy.
			lock.lock();
			try {
				condAllBusy.signalAll();
			} finally {
				lock.unlock();
			}
		}
	}

	/**
	 * Notify that one worker is finish to handler a new request.
	 * 
	 * @param worker Target worker.
	 * @param request Target request.
	 */
	void notifyFinishRequest(Worker worker, HttpServerRequest request) {
		ManagedWorker mWorker = (ManagedWorker) worker;
		mWorker.idle.set(true);
		mWorker.touch();
		idleCounter.incrementAndGet();
	}

}