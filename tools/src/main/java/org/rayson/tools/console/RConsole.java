/**
 * Copyright © 2020 Creativor Studio.
 * About license information, please see LICENSE.txt.
 */

package org.rayson.tools.console;

import java.io.Console;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.rayson.api.annotation.NotNull;
import org.rayson.share.util.ReflectionTool;

/**
 * Base console class for Rayson. <br>
 * By default , the console has the following logic:
 * <ul>
 * <li>If the arguments passed from console fulfill the</li>
 * </ul>
 * 
 * @param <P> Parameters type. The console will initialize the parameters
 *            instance by reflection .
 * @author creativor
 */
public abstract class RConsole<P extends ConsoleParameters> {
	private class ArgumentsParser {
		private final String[] args;
		final List<ConsoleArgument> arguments;
		private int index;

		final List<ConsoleOption> options;

		private int parameterIndex = 0;

		ArgumentsParser(final String[] args) {
			this.args = args;
			index = 0;
			options = new ArrayList<ConsoleOption>();
			arguments = new ArrayList<ConsoleArgument>();

		}

		ConsoleArgument[] getArguments() {
			return arguments.toArray(new ConsoleArgument[0]);
		}

		ConsoleOption[] getOptions() {
			return options.toArray(new ConsoleOption[0]);
		}

		public void parse() throws ConsoleException {
			// Parse arguments.
			if (args == null || args.length == 0)
				return;
			String arg;
			for (; index < args.length; index++) {
				arg = args[index];
				if (arg == null || arg.isEmpty())
					continue;
				if (arg.startsWith(OPTION_PREFIX_CHAR)) {
					parseOption(arg);
				} else {
					parseArgument(arg, parameterIndex++);

				}
			}
		}

		/**
		 * @param parameter
		 * @throws ConsoleException
		 */
		private void parseArgument(final String parameter, int index) throws ConsoleException {
			for (final ConsoleArgumentDef<?> argDef : argumentDefs) {
				if (argDef.match(parameter, index)) {
					this.arguments.add(argDef.parse(parameter));
				}
			}
		}

		/**
		 * @param option
		 * @throws ConsoleException
		 */
		private void parseOption(final String option) throws ConsoleException {
			String parameter;
			for (final ConsoleOptionDef<?> optDef : optionDefs) {
				parameter = null;
				if (optDef.match(option)) {
					if (optDef.hasParameter()) {
						if (index >= args.length - 1) {
							parameter = null;
						} else {
							parameter = args[index + 1];
							if (parameter.startsWith(OPTION_PREFIX_CHAR)) {
								parameter = null;
							}
						}
					}
					if (parameter != null)
						index++;
					this.options.add(optDef.parse(parameter));
					break;
				}
			}
		}
	}

	private static final String OPTION_PREFIX_CHAR = "-";
	private static final String SHOW_USAGE_OPTION = "--help";
	private final List<ConsoleArgumentDef<?>> argumentDefs;

	private final List<ConsoleOptionDef<?>> optionDefs;
	private P parameters;

	protected RConsole() throws ConsoleException {
		optionDefs = new ArrayList<ConsoleOptionDef<?>>();
		argumentDefs = new ArrayList<ConsoleArgumentDef<?>>();

		// Load parameters type.
		final Type[] typeParameters = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments();
		if (typeParameters.length == 0)
			throw new ConsoleException("No parameters type parameters in the console class definition");
		final Class typeParameter = (Class) typeParameters[0];
		if (!ConsoleParameters.class.isAssignableFrom(typeParameter)) {
			throw new ConsoleException("Parameters type must be interface of " + ConsoleParameters.class);
		}
		try {
			this.parameters = (P) ReflectionTool.newInstance(typeParameter);
		} catch (SecurityException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
			throw new ConsoleException("Failed to construct parameters type " + typeParameter + " :" + e.getMessage());
		}

		// Load options and arguments.
		final ConsoleConfig config = this.getClass().getAnnotation(ConsoleConfig.class);
		if (config == null)
			return;

		final Class<? extends ConsoleOptionDef<?>>[] optionTypes = config.options();
		final Class<? extends ConsoleArgumentDef<?>>[] argumentTypes = config.arugments();

		addOptions(optionTypes);

		addArguments(argumentTypes);

	}

	/**
	 * @param argumentTypes
	 * @throws ConsoleException
	 */
	private void addArguments(final Class<? extends ConsoleArgumentDef<?>>[] argumentTypes) throws ConsoleException {
		if (argumentTypes == null)
			return;
		ConsoleArgumentDef argDef;
		for (final Class<? extends ConsoleArgumentDef<?>> argType : argumentTypes) {
			try {
				argDef = ReflectionTool.newInstance(argType);
				this.argumentDefs.add(argDef);
			} catch (SecurityException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
				throw new ConsoleException("Initialize argument definition type error: " + e.getMessage());
			}
		}
	}

	/**
	 * @param optionTypes
	 * @throws ConsoleException
	 */
	private void addOptions(final Class<? extends ConsoleOptionDef<?>>[] optionTypes) throws ConsoleException {
		if (optionTypes == null)
			return;

		ConsoleOptionDef optDef;

		for (final Class<? extends ConsoleOptionDef<?>> optType : optionTypes) {

			try {
				optDef = ReflectionTool.newInstance(optType);
				this.optionDefs.add(optDef);
			} catch (SecurityException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
				throw new ConsoleException("Initialize option definition type error:" + e.getMessage());
			}

		}

	}

	/**
	 * Execute command using parsed options and arguments.
	 * 
	 * @param parameters Parameters parsed from input parameters strings.
	 * @throws ConsoleException If the execution got error.
	 * @throws InterruptedException If the current thread is interrupted.
	 */
	protected abstract void execute(@NotNull P parameters) throws ConsoleException, InterruptedException;

	/**
	 * @return the parameters of this console.
	 */
	protected P getParameters() {
		return parameters;
	}

	/**
	 * @return Underling java system Console.
	 */
	protected Console getUnderlingConsole() {
		return System.console();
	}

	/**
	 * Print a message to console. Using underling console writer. And flush the
	 * console before returns.
	 * 
	 * @param type Log message type.
	 * @param msg Message string.
	 */
	public void printMsg(@NotNull final MessageType type, final String msg) {
		final String realMsg = type + ": " + msg + " <- " + new Date();
		printMsg(realMsg);
	}

	/**
	 * Print a message to this console. Using underling console writer. And
	 * flush the console before returns.
	 * 
	 * @param msg
	 */
	public void printMsg(final String msg) {
		Console console = getUnderlingConsole();
		if (console != null) {
			getUnderlingConsole().writer().write(msg);
			getUnderlingConsole().flush();
		} else {
			System.out.print(msg);
			System.out.flush();
		}
	}

	/**
	 * Print a message and a new line to this console. Using underling console
	 * writer. And flush the console before returns.
	 * 
	 * @param msg
	 */
	public void printMsgLn(final String msg) {
		printMsg(msg + System.lineSeparator());
	}

	/**
	 * Run the console using arguments.
	 * 
	 * @param args
	 * @throws ConsoleException If got any error.
	 * @throws InterruptedException If the current thread is interrupted.
	 */
	public final void run(final String[] args) throws ConsoleException, InterruptedException {
		// Test usage parameter.
		if (shouldShowUsage(args)) {
			showUsage();
			return;
		}
		final ArgumentsParser argsParser = new ArgumentsParser(args);
		try {
			argsParser.parse();

			setupParameters(parameters, argsParser.getOptions(), argsParser.getArguments());
		} catch (ConsoleException e) {

			// Failed to parse parameters.
			printMsg(e.getMessage());
			showUsage();
			return;
		}

		// Then, execute the command.
		execute(this.parameters);
	}

	/**
	 * Setup parameters of this console by giving an <code>argument</code>
	 * parsed out from the input parameters string. By default, it do nothing.
	 * 
	 * @param parameters Parameters of this console to be setup.
	 * @param options Option list. Length 0 means no option.
	 * @param arguments arguments list. Length 0 means no argument.
	 * @throws ConsoleException If failed to setup.
	 */
	protected void setupParameters(@NotNull final P parameters, ConsoleOption[] options, @NotNull final ConsoleArgument[] arguments) throws ConsoleException {

		for (ConsoleOption option : options) {
			option.setupParameters(parameters);
		}

		for (ConsoleArgument arg : arguments) {
			arg.setupParameters(parameters);
		}
	}

	/**
	 * Test whether the console should show usage info by giving arguments. <br>
	 * Child implementation could override this method.
	 * 
	 * @param args Arguments passed by console.
	 * 
	 * @return True if no arguments found. Or one of the argument equals to
	 *         {@value #SHOW_USAGE_OPTION}.
	 */
	protected boolean shouldShowUsage(final String[] args) {

		// May has no argument.
		// if (args == null | args.length == 0)
		// return true;
		for (final String arg : args) {
			if (SHOW_USAGE_OPTION.endsWith(arg))
				return true;
		}

		return false;
	}

	/**
	 * 
	 */
	protected void showUsage() {
		printMsg(usage());
		printMsg(System.lineSeparator());
	}

	/**
	 * @return Usage information of this console.
	 */
	protected abstract String usage();
}