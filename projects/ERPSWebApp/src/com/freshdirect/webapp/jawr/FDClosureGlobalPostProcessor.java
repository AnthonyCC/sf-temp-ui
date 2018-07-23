package com.freshdirect.webapp.jawr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPOutputStream;

import net.jawr.web.JawrConstant;
import net.jawr.web.config.JawrConfig;
import net.jawr.web.exception.BundlingProcessException;
import net.jawr.web.exception.ResourceNotFoundException;
import net.jawr.web.resource.bundle.IOUtils;
import net.jawr.web.resource.bundle.JoinableResourceBundle;
import net.jawr.web.resource.bundle.factory.global.postprocessor.GlobalPostProcessingContext;
import net.jawr.web.resource.bundle.global.processor.AbstractChainedGlobalProcessor;
import net.jawr.web.util.FileUtils;
import net.jawr.web.util.StringUtils;
import net.jawr.web.util.io.TeeOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.freshdirect.fdstore.FDStoreProperties;
import com.google.common.io.CharStreams;
import com.google.javascript.jscomp.CommandLineRunner;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.SourceFile;

public class FDClosureGlobalPostProcessor extends AbstractChainedGlobalProcessor<GlobalPostProcessingContext> {

	/** The logger */
	private static final Logger LOGGER = LoggerFactory.getLogger(FDClosureGlobalPostProcessor.class);

	/** The list of unallowed compiler argument in the Jawr config */
	private List<String> UNALLOWED_COMPILER_ARGS = Arrays.asList("js", "module");

	/** The closure compiler argument name prefix */
	private static final String CLOSURE_ARGUMENT_NAME_PREFIX = "--";

	/** The Jawr closure argument prefix in the Jawr configuration */
	private static final String JAWR_JS_CLOSURE_PREFIX = "jawr.js.closure.";

	/** The module argument for the closure command line runner */
	private static final String MODULE_ARG = "--module";

	/** The module argument pattern */
	private static final Pattern MODULE_ARG_PATTERN = Pattern.compile("[^:]+:\\d+:(.*)");

	/** The module dependencies separator */
	private static final String MODULE_DEPENDENCIES_SEPARATOR = ",";

	/** The js argument for the closure command line runner */
	private static final String JS_ARG = "--js";

	/** The js argument for the closure command line runner */
	private static final String JS_OUTPUT_ARG = "--js_output_file";

	/** The compilation level argument for the closure command line runner */
	private static final String COMPILATION_LEVEL = "compilation_level";

	/** The compilation level argument for the closure command line runner */
	private static final String COMPILATION_LEVEL_ARG = CLOSURE_ARGUMENT_NAME_PREFIX + COMPILATION_LEVEL;

	/** The whitespace_only compilation level */
	private static final String WHITESPACE_ONLY_COMPILATION_LEVEL = "WHITESPACE_ONLY";

	/** The simple optimization compilation level */
	private static final String SIMPLE_OPTIMIZATIONS_COMPILATION_LEVEL = "SIMPLE_OPTIMIZATIONS";

	/** The advanced optimizations compilation level */
	private static final String ADVANCED_OPTIMIZATIONS_COMPILATION_LEVEL = "ADVANCED_OPTIMIZATIONS";

	/** The warning level argument for the closure command line runner */
	private static final String WARNING_LEVEL_ARG = "--warning_level";

	/** The verbose warning level for the closure compiler */
	private static final String VERBOSE_WARNING_LEVEL = "VERBOSE";

	/** The property for the excluded bundles */
	private static final String JAWR_JS_CLOSURE_BUNDLES_EXCLUDED = "jawr.js.closure.bundles.excluded";

	/** The property for disable thread */
	private static final String JAWR_JS_CLOSURE_DISABLE_THREAD = "jawr.js.closure.disableThread";

	/**
	 * The closure modules property, whose the value will be passed to the closure
	 * compiler
	 */
	private static final String JAWR_JS_CLOSURE_MODULES = "jawr.js.closure.modules";

	/** The list of the jawr js closure properties */
	private static final List<String> JAWR_JS_CLOSURE_SPECIFIC_PROPERTIES = Arrays
			.asList(JAWR_JS_CLOSURE_BUNDLES_EXCLUDED, JAWR_JS_CLOSURE_DISABLE_THREAD, JAWR_JS_CLOSURE_MODULES);

	/** The google closure temporary directory */
	public static final String GOOGLE_CLOSURE_TEMP_DIR = "/googleClosure/temp/";

	/** The google closure result directory */
	public static final String GOOGLE_CLOSURE_RESULT_TEXT_DIR = "/googleClosure/text/";

	/** The google closure result zipped directory */
	public static final String GOOGLE_CLOSURE_RESULT_ZIP_DIR = "/googleClosure/gzip/";

	/** The JAWR root module file path */
	private static final String JAWR_ROOT_MODULE_JS = "/JAWR_ROOT_MODULE.js";

	/** The source directory */
	private String srcDir;

	/** The source directory of zipped bundle */
	private String srcZipDir;

	/** The destination directory for closure compiler compilation */
	private String destDir;

	/** The destination directory containing the zipped compiled bundle */
	private String destZipDir;

	/** The temporary directory */
	private String tempDir;

	/**
	 * Constructor
	 */
	public FDClosureGlobalPostProcessor() {
		super(JawrConstant.GLOBAL_GOOGLE_CLOSURE_POSTPROCESSOR_ID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.jawr.web.resource.bundle.global.processor.GlobalProcessor#processBundles
	 * (
	 * net.jawr.web.resource.bundle.global.processor.AbstractGlobalProcessingContext
	 * , java.util.List)
	 */
	public void processBundles(GlobalPostProcessingContext ctx, List<JoinableResourceBundle> bundles) {
		/* disable on local to speed up restart */
		if (!FDStoreProperties.isLocalDeployment() && ctx.hasBundleToBeProcessed()) {
			String workingDir = ctx.getRsReaderHandler().getWorkingDirectory();

			if (srcDir == null || destDir == null || tempDir == null || srcZipDir == null || destZipDir == null) {
				srcDir = ctx.getBundleHandler().getBundleTextDirPath();
				srcZipDir = ctx.getBundleHandler().getBundleZipDirPath();
				destDir = workingDir + GOOGLE_CLOSURE_RESULT_TEXT_DIR;
				destZipDir = workingDir + GOOGLE_CLOSURE_RESULT_ZIP_DIR;
				tempDir = workingDir + GOOGLE_CLOSURE_TEMP_DIR;
			}

			// Create result directory
			File dir = new File(destDir);
			if (!dir.exists() && !dir.mkdirs()) {
				throw new BundlingProcessException("Impossible to create temporary directory :" + destDir);
			}

			// Create result directory
			dir = new File(destZipDir);
			if (!dir.exists() && !dir.mkdirs()) {
				throw new BundlingProcessException("Impossible to create temporary directory :" + destZipDir);
			}

			// Create temporary directory
			dir = new File(tempDir);
			if (!dir.exists() && !dir.mkdirs()) {
				throw new BundlingProcessException("Impossible to create temporary directory :" + tempDir);
			}

			// Copy the bundle files in a temp directory
			try {
				File tempDirFile = new File(tempDir);
				FileUtils.copyDirectory(new File(srcDir), tempDirFile);

				compressJsFileInDirectory(ctx, tempDirFile, "");
				// Copy compiled bundles
				FileUtils.copyDirectory(new File(destDir), new File(srcDir));

				// Copy zipped compiled bundles
				FileUtils.copyDirectory(new File(destZipDir), new File(srcZipDir));

			} catch (Exception e) {

				throw new BundlingProcessException(e);
			}
		}
	}

	private void compressJsFileInDirectory(GlobalPostProcessingContext ctx, File directory, String path)
			throws Exception {

		File[] directoryListing = directory.listFiles();
		if (directoryListing != null) {
			for (File f : directoryListing) {
				if (f.isDirectory()) {
					compressJsFileInDirectory(ctx, f, path + f.getName() + "/");
				} else if (f.getName().endsWith(".js")) {
					JawrClosureCommandLineRunner cmdRunner = new JawrClosureCommandLineRunner(ctx, path + f.getName());
					cmdRunner.doRun();
				}

			}
		}
	}

	/**
	 * Returns the closure compiler arguments
	 * 
	 * @param ctx
	 *            the global processing context
	 * @param tmpBundles
	 *            the bundles
	 * @param resultBundlePathMapping
	 *            the object which defines the mapping between the bundle name and
	 *            the bundle path
	 * @return
	 */
	private String[] getClosureCompilerArgs(GlobalPostProcessingContext ctx, String fileName) {

		List<String> args = new ArrayList<String>();
		JawrConfig config = ctx.getJawrConfig();

		// Handle All closure parameters defined in Jawr config
		initCompilerClosureArgumentsFromConfig(args, config);

		args.add(JS_ARG);
		args.add(fileName);
		args.add(JS_OUTPUT_ARG);
		args.add("/output/" + fileName);
		if (LOGGER.isDebugEnabled()) {
			StringBuilder strArg = new StringBuilder();
			for (String arg : args) {
				strArg.append(arg + " ");
			}

			LOGGER.debug("Closure Compiler Args : " + strArg.toString());
		}
		return args.toArray(new String[] {});
	}

	/**
	 * Initialize the closure argument from the Jawr config
	 * 
	 * @param args
	 *            the arguments
	 * @param config
	 *            the Jawr config
	 */
	private void initCompilerClosureArgumentsFromConfig(List<String> args, JawrConfig config) {
		Set<Entry<Object, Object>> entrySet = config.getConfigProperties().entrySet();
		for (Entry<Object, Object> propEntry : entrySet) {
			String key = (String) propEntry.getKey();
			if (key.startsWith(JAWR_JS_CLOSURE_PREFIX) && !JAWR_JS_CLOSURE_SPECIFIC_PROPERTIES.contains(key)) {

				String compilerArgName = key.substring(JAWR_JS_CLOSURE_PREFIX.length());
				checkCompilerArgumentName(compilerArgName);
				String compilerArgValue = (String) propEntry.getValue();
				compilerArgValue = getCompilerArgValue(compilerArgName, compilerArgValue);
				args.add(CLOSURE_ARGUMENT_NAME_PREFIX + compilerArgName);
				args.add(propEntry.getValue().toString());
			}
		}

		// Add default compilation level argument
		if (!args.contains(COMPILATION_LEVEL_ARG)) {
			args.add(COMPILATION_LEVEL_ARG);
			args.add(WHITESPACE_ONLY_COMPILATION_LEVEL);
		}

		// Add default level warning argument if not defined
		if (!args.contains(WARNING_LEVEL_ARG)) {
			args.add(WARNING_LEVEL_ARG);
			args.add(VERBOSE_WARNING_LEVEL);
		}
	}

	/**
	 * Checks if the usage of the compiler argument name is allowed
	 * 
	 * @param compilerArgName
	 *            the compiler argument name
	 */
	private void checkCompilerArgumentName(String compilerArgName) {

		if (UNALLOWED_COMPILER_ARGS.contains(compilerArgName)) {
			throw new BundlingProcessException(
					"The usage of the closure argument \'" + compilerArgName + "\' is not allowed.");
		}
	}

	/**
	 * Returns the compiler argument value
	 * 
	 * @param compilerArgName
	 *            the compiler argument name
	 * @param compilerArgValue
	 *            the compiler argument name
	 * @return the compiler argument value
	 */
	private String getCompilerArgValue(String compilerArgName, String compilerArgValue) {
		if (compilerArgName.equals(COMPILATION_LEVEL)) {
			if (!ADVANCED_OPTIMIZATIONS_COMPILATION_LEVEL.equalsIgnoreCase(compilerArgValue)
					&& !WHITESPACE_ONLY_COMPILATION_LEVEL.equalsIgnoreCase(compilerArgValue)
					&& !SIMPLE_OPTIMIZATIONS_COMPILATION_LEVEL.equalsIgnoreCase(compilerArgValue)) {

				if (StringUtils.isNotEmpty(compilerArgValue)) {
					LOGGER.debug("Closure compilation level defined in config '" + compilerArgValue
							+ "' is not part of the available "
							+ "ones [WHITESPACE_ONLY, SIMPLE_OPTIMIZATIONS, ADVANCED_OPTIMIZATIONS");
				}
				compilerArgValue = WHITESPACE_ONLY_COMPILATION_LEVEL;
			}

			LOGGER.debug("Closure compilation level used : " + compilerArgValue);

		}
		return compilerArgValue;
	}

	/**
	 * Adds the module argument taking in account the module dependencies
	 * 
	 * @param jsFile
	 *            the bundle js file
	 * @param moduleName
	 *            the module name
	 * @param args
	 *            the list of arguments to update
	 * @param moduleArg
	 *            the module argument to add
	 */
	protected void addModuleArg(String jsFile, String moduleName, List<String> args, StringBuilder moduleArg) {
		int argIdx = 0;
		for (Iterator<String> iterArg = args.iterator(); iterArg.hasNext(); argIdx++) {
			String arg = iterArg.next();
			if (arg.equals(JS_ARG)) {
				iterArg.next();
				arg = iterArg.next();
				argIdx += 2;
			}
			if (arg.equals(MODULE_ARG)) {
				arg = iterArg.next();
				argIdx++;
				Matcher matcher = MODULE_ARG_PATTERN.matcher(arg);
				if (matcher.find()) {
					String dep = matcher.group(1);
					if (dep != null) {
						List<String> moduleDepdendencies = Arrays.asList(dep.split(MODULE_DEPENDENCIES_SEPARATOR));
						if (moduleDepdendencies.contains(moduleName)) {
							break;
						}
					}
				} else {
					throw new BundlingProcessException(
							"There were an error in the generation of the module dependencies.");
				}
			}
		}

		args.add(argIdx++, JS_ARG);
		args.add(argIdx++, jsFile);
		args.add(argIdx++, MODULE_ARG);
		args.add(argIdx++, moduleArg.toString());
	}

	private class JawrClosureCommandLineRunner extends CommandLineRunner {

		/**
		 * The global postprocessing context
		 */
		private GlobalPostProcessingContext ctx;

		/**
		 * Constructor
		 * 
		 * @param ctx
		 *            the global post processing context
		 */
		public JawrClosureCommandLineRunner(GlobalPostProcessingContext ctx, String fileName) {
			super(getClosureCompilerArgs(ctx, fileName));
			this.ctx = ctx;
		}

		@Override
		protected Compiler createCompiler() {
			Compiler compiler = new Compiler(getErrorPrintStream());

			// Disable thread if needed
			if (Boolean.getBoolean(ctx.getJawrConfig().getProperty(JAWR_JS_CLOSURE_DISABLE_THREAD, "false"))) {
				compiler.disableThreads();
			}
			return compiler;
		}

		@Override
		protected void checkModuleName(String name) throws FlagUsageException {

		}

		@Override
		protected List<SourceFile> createInputs(List<String> files, boolean allowStdIn) throws IOException {

			List<SourceFile> inputs = new ArrayList<SourceFile>(files.size());

			for (String filename : files) {
				if (filename.equals(JAWR_ROOT_MODULE_JS)) {

					SourceFile newFile = SourceFile.fromCode(filename, "");
					inputs.add(newFile);
				} else if (!"-".equals(filename)) {
					Reader rd = null;
					StringWriter swr = new StringWriter();
					InputStream is = null;
					try {
						try {
							is = new FileInputStream(new File(tempDir, filename));
							rd = Channels.newReader(Channels.newChannel(is),
									ctx.getJawrConfig().getResourceCharset().displayName());
						} catch (FileNotFoundException e) {
							// Do nothing
						}

						if (rd == null) {
							try {
								rd = ctx.getRsReaderHandler().getResource(filename);
							} catch (ResourceNotFoundException e1) {
								throw new BundlingProcessException(e1);
							}
						}

						String jsCode = CharStreams.toString(rd);
						SourceFile newFile = SourceFile.fromCode(filename, jsCode);
						inputs.add(newFile);
					} finally {
						IOUtils.close(is);
						IOUtils.close(rd);
						IOUtils.close(swr);
					}
				}
			}
			return inputs;
		}

		/**
		 * Converts a file name into a Writer. Returns null if the file name is null.
		 * 
		 * @throws IOException
		 */
		@Override
		protected OutputStream filenameToOutputStream(String fileName) throws IOException {

			fileName = fileName.replace("/output/", "");

			File outFile = new File(destDir, fileName);
			outFile.getParentFile().mkdirs();
			FileOutputStream fos = new FileOutputStream(outFile);

			File outZipFile = new File(destZipDir, fileName);
			outZipFile.getParentFile().mkdirs();
			GZIPOutputStream gzOs = new GZIPOutputStream(new FileOutputStream(outZipFile));
			return new TeeOutputStream(fos, gzOs);
		}

		public int doRun() throws FlagUsageException, IOException {
			int result = super.doRun();

			return result;
		}
	}
}
