/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.server.filter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.rayson.api.Protocol;
import org.rayson.api.annotation.NotNull;
import org.rayson.api.annotation.ServiceConfig;
import org.rayson.api.annotation.UrlMapping;
import org.rayson.api.exception.InvalidApiException;
import org.rayson.api.http.HttpConstants;
import org.rayson.api.http.HttpResponseStatus;
import org.rayson.api.protocol.mirror.ProtocolMirror;
import org.rayson.api.server.HttpServerFilter;
import org.rayson.api.server.HttpServerRequest;
import org.rayson.api.server.HttpServerResponse;
import org.rayson.api.server.ServiceFilter;
import org.rayson.api.server.exception.DuplicateServiceException;
import org.rayson.api.server.exception.ServiceException;
import org.rayson.server.Container;
import org.rayson.server.ServiceManager;
import org.rayson.server.exception.InternalServerError;
import org.rayson.server.http.DefaultHttpFilterChain;
import org.rayson.server.http.HttpServerRequestImpl;
import org.rayson.server.http.HttpServerResponseImpl;
import org.rayson.server.service.ServerInfoImpl;
import org.rayson.share.logger.RaysonLoggerFactory;
import org.rayson.share.util.ReflectionTool;
import org.slf4j.Logger;

/**
 * Implementation of {@link ServiceManager} on server side.
 * 
 * @author Nick Zhang
 *
 */
public class ServiceManagerImpl implements ServiceManager {

	private static final ServiceConfig DEFAULT_SERVICE_DEFINITION = new ServiceConfig() {

		@Override
		public Class<ServiceConfig> annotationType() {
			return ServiceConfig.class;
		}

		@Override
		public Class<? extends ServiceFilter>[] filters() {
			return new Class[0];
		}
	};

	static final Logger LOGGER = RaysonLoggerFactory.getLogger(ServiceManager.class);

	private static final String SLASH_TOKEN = "/";
	private final Container container;

	private final Map<Class<? extends Protocol>, DefaultFilterChain> filterChains;

	private DefaultHttpFilterChain firstHttpChain;

	private FirstHttpFilter firstHttpFilter;
	private final List<Class<? extends ServiceFilter>> globalFilterTypes;

	private LastHttpFilter lastHttpFilter;

	private DefaultHttpFilterChain secondHttpChain;
	private final Map<String, MethodChain> urlMappings;

	/**
	 * @param container Server container of this service manager.
	 */
	public ServiceManagerImpl(final Container container) {
		super();
		this.container = container;
		globalFilterTypes = new ArrayList<>();
		lastHttpFilter = new LastHttpFilter(this);
		firstHttpFilter = new FirstHttpFilter(this);
		secondHttpChain = new DefaultHttpFilterChain(lastHttpFilter, null);
		firstHttpChain = new DefaultHttpFilterChain(firstHttpFilter, secondHttpChain);
		filterChains = new HashMap<>();
		urlMappings = new HashMap<>();
	}

	@Override
	public void addHttpFilter(Class<? extends HttpServerFilter> type) throws ServiceException {
		if (type == null)
			throw new IllegalArgumentException("type should not be null");

		HttpServerFilter instance;
		try {
			instance = ReflectionTool.newInstance(type);
			instance.init(container.getServer());
		} catch (Exception e) {
			throw new ServiceException("Failed to construct the filter instance", e);
		}
		DefaultHttpFilterChain newChain = new DefaultHttpFilterChain(instance, secondHttpChain);
		secondHttpChain = newChain;
		firstHttpChain = new DefaultHttpFilterChain(firstHttpFilter, secondHttpChain);
	}

	@Override
	public void addServiceFilter(Class<? extends ServiceFilter> type) throws ServiceException {
		if (type == null)
			throw new IllegalArgumentException("type should not be null");

		globalFilterTypes.add(type);
	}

	private ServiceFilter createFilter(final Protocol service, final Class<? extends ServiceFilter> filterCls) throws ServiceException {
		if (filterCls == null)
			throw new ServiceException("filter class should not be null");

		final ServiceFilter filter;
		try {
			filter = ReflectionTool.newInstance(filterCls);
		} catch (Exception e) {
			throw new ServiceException("Failed to create new instance of filter class " + filterCls, e);
		}

		filter.init(container.getServer(), service);
		return filter;
	}

	/*
	 * @return First chain.
	 */
	private DefaultFilterChain createFilterChain(Class<? extends Protocol> protocol, final Protocol service, final List<ServiceFilter> filters)
			throws InvalidApiException {

		DefaultFilterChain currentChain = null;
		DefaultFilterChain nextChain = null;
		// From tail to head.

		for (int i = filters.size() - 1; i >= 0; i--) {
			currentChain = new DefaultFilterChain(protocol, service, filters.get(i));
			currentChain.setNext(nextChain);
			nextChain = currentChain;
		}

		return currentChain;
	}

	@Override
	public HttpServerResponse doFilter(@NotNull final HttpServerRequest request) throws ServiceException {

		//LOGGER.debug("Invoking request " + request);

		HttpServerRequestImpl realReq = (HttpServerRequestImpl) request;

		final HttpServerResponse response = new HttpServerResponseImpl(realReq);

		try {
			firstHttpChain.doFilter(request, response);
		} catch (Throwable e) {
			String msg = e.getMessage();
			if (msg == null)
				msg = "No error message specified";

			response.setException(HttpResponseStatus.INTERNAL_SERVER_ERROR, msg);
		}

		// Setup request id header finally..
		try {
			final String requestId = request.getRequestId();
			if (requestId != null)
				response.addHeader(HttpConstants.REQUEST_ID_HEADER_NAME, requestId);
		} catch (final Exception e) {
			LOGGER.error("Failed to set request id of response", e);

		}

		return response;
	}

	@Override
	public Container getContainer() {
		return container;
	}

	private Method getMethod(Method[] svrClsMethos, Method m) {
		Class<?>[] sourceParamTypes = m.getParameterTypes();
		OUT: for (Method method : svrClsMethos) {
			if (method.isBridge())
				continue;
			if (!method.getName().equals(m.getName()))
				continue;
			if (method.getParameterCount() != sourceParamTypes.length)
				continue;

			Class<?>[] targetParamTypes = method.getParameterTypes();
			for (int i = 0; i < targetParamTypes.length; i++) {
				if (!targetParamTypes[i].isAssignableFrom(sourceParamTypes[i]))
					continue OUT;
			}
			return method;
		}
		throw new RuntimeException("No method match " + m + " in service class");
	}

	MethodChain getMethodChain(String url) {
		if (url == null)
			throw new IllegalArgumentException("url should not be null");
		return urlMappings.get(url);
	}

	@Override
	public String getName() {
		return "Service Manager of " + container;
	}

	@Override
	public synchronized void registerService(@NotNull final Protocol service)
			throws DuplicateServiceException, ServiceException, IllegalArgumentException, InvalidApiException {
		if (service == null)
			throw new IllegalArgumentException("service should not be null");

		Class<? extends Protocol> srvCls = service.getClass();
		ServiceConfig sd = srvCls.getAnnotation(ServiceConfig.class);
		if (sd == null) {
			sd = DEFAULT_SERVICE_DEFINITION;
		}

		final Class<? extends Protocol>[] protocols = ReflectionTool.getDeclaredInterfaces(srvCls, Protocol.class);
		if (protocols.length == 0)
			throw new ServiceException("No service Protocol found");

		// Load protocol meta data.

		DefaultFilterChain filterChain;

		// Add protocol defined filters first.

		final Class<? extends ServiceFilter>[] filterClses = sd.filters();
		List<ServiceFilter> filters = new ArrayList<>();
		ServiceFilter filter;

		if (filterClses != null) {
			for (int i = 0; i < filterClses.length; i++) {
				filter = createFilter(service, filterClses[i]);
				filters.add(filter);
			}
		}

		// Then add global filters.

		for (Class<? extends ServiceFilter> filterType : globalFilterTypes) {
			filters.add(createFilter(service, filterType));
		}

		// Then add last filter.
		LastServiceFilter lastFilter = new LastServiceFilter();
		lastFilter.init(container.getServer(), service);
		filters.add(lastFilter);

		// Add filter chains.

		Method[] svrClsMethos = srvCls.getMethods();

		Method[] methods;
		String url;
		String nativeProtocolUrl;
		String mappedProtocolUrl;
		String nativeMethodUrl;
		String mappedMethodUrl = null;

		UrlMapping srvClsMapping = srvCls.getAnnotation(UrlMapping.class);

		for (final Class<? extends Protocol> kls : protocols) {

			nativeProtocolUrl = SLASH_TOKEN + kls.getName();
			mappedProtocolUrl = nativeProtocolUrl;

			filterChain = createFilterChain(kls, service, filters);

			// First, put into chains map.

			filterChains.put(kls, filterChain);

			// Map all API URL to this filter chain .
			if (srvClsMapping != null) {
				String v = srvClsMapping.value();
				if (v != null && !v.isEmpty()) {

					mappedProtocolUrl = (v.startsWith(SLASH_TOKEN)) ? v : SLASH_TOKEN + v;
				}
			}
			methods = kls.getMethods();

			MethodChain mc;
			for (Method m : methods) {
				mc = new MethodChain(m, filterChain);

				nativeMethodUrl = m.getName();
				mappedMethodUrl = nativeMethodUrl;

				// Add native URL mapping first.
				url = nativeProtocolUrl + SLASH_TOKEN + nativeMethodUrl;
				urlMappings.put(url, mc);

				// Then add mapped URL mapping.
				UrlMapping methodMapping = getMethod(svrClsMethos, m).getAnnotation(UrlMapping.class);

				if (methodMapping != null) {

					String v = methodMapping.value();
					if (v != null && !v.isEmpty()) {

						if (v.startsWith(SLASH_TOKEN)) {
							// absolute URL.
							urlMappings.put(v, mc);
							continue;
						}
						mappedMethodUrl = v;
					}
				}

				// Add mapped url or replace the native url.
				url = (mappedProtocolUrl.equals(SLASH_TOKEN) ? "" : mappedProtocolUrl) + SLASH_TOKEN + mappedMethodUrl;
				urlMappings.put(url, mc);
			}

		}
	}

	@Override
	public void start() throws InternalServerError {
		LOGGER.info(getName() + " starting ...");

		// Add built in services.
		final ServerInfoImpl serverInfo = new ServerInfoImpl(this.container);
		try {
			this.registerService(serverInfo);
		} catch (Exception e) {
			throw new InternalServerError("Failed to register server built in service", e);
		}
	}

	@Override
	public void stop() {
		LOGGER.info(getName() + " stopping ...");

		// Do nothing.
	}

	@Override
	public ProtocolMirror getMirror(Class<? extends Protocol> protocol) throws IllegalArgumentException {
		if (protocol == null)
			throw new IllegalArgumentException("protocol should not be null");
		if (!protocol.isInterface()) {
			throw new IllegalArgumentException("protocol should not be interface");
		}

		DefaultFilterChain chain = filterChains.get(protocol);
		if (chain == null)
			return null;

		return chain.getProtocolMirror();
	}

	@Override
	public ProtocolMirror[] getProtocols() {

		List<ProtocolMirror> result = new ArrayList<>();

		for (DefaultFilterChain chain : filterChains.values()) {
			result.add(chain.getProtocolMirror());
		}

		return result.toArray(new ProtocolMirror[0]);
	}

}