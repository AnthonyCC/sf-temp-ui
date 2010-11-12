package com.freshdirect.weblogic.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LibUtil {
	static final String PSEP = System.getProperty("file.separator");

	static final String projects = "CMS,CRM,DataLoader,Delivery,DlvAdmin,DlvConfirm,ERPSAdmin,ERPSWebApp,ERPServices,FDIntegrationServices,FDStore,FDWebSite,Framework,Media,OCF,RefAdmin,Resources,RoutingServices,RulesAdmin,StandingOrdersService,Tests,Tools,TransportationAdmin,WebAppCommon,cms-gwt,listadmin,ocf-adm";


	/**
	 * These libs will be excluded from the final classpath list.
	 */
	static final String[] excLibs = {
		"javaee-api",
		"jakarta-slide-webdavlib",
		"wsdl4j","wstx-asl","xercesImpl","xml-apis","XmlSchema",
		"gwt-dev-oophm", "gwt-maps", "gwt-user", "gxt",
		"xxxxxspring", "Zql"
	};


	/**
	 * Utility method to collect dependency libs from Eclipse projects
	 * Only newer version of libs are retained
	 * 
	 * @param projectsDir File handle to point to FD source base
	 * 
	 * @return list of dependent libs
	 */
	public static List<String> collectLibs(File projectsDir) {
		Pattern p = Pattern.compile(".+path=\"(.+\\.jar)\".+");

		LibUtil.LibSet libs = new LibUtil.LibSet();
		String line = null;
		for (File fprj : projectsDir.listFiles()) {
			if (fprj.isDirectory()) {
				// Skip tests and tools projects
				if ( 	"Tests".equals(fprj.getName()) ||
						"Tools".equals(fprj.getName()) ||
						"DevEAR".equals(fprj.getName()) ||
						"DevServer".equals(fprj.getName())
					) {
					continue;
				}

				File classpath = new File(fprj + PSEP + ".classpath");
				if (classpath == null || !classpath.isFile()) {
					continue;
				}

				// load classpath
				try {
					BufferedReader reader = new BufferedReader(new FileReader(classpath));
					while ( (line = reader.readLine()) != null) {
						Matcher m = p.matcher(line);
						if (m.matches()) {
							String libPath = m.group(1);

							// non-fdlibs -> skip
							if (!libPath.startsWith("FD_LIBS")) {
								continue;
							}
							
							final Lib aLib = new Lib(libPath);
							boolean doExcludeLib = false;
							// filter excluded libs
							for (String exc : excLibs) {
								if ( aLib.getName().equals(exc) ) {
									System.err.println("-- " + aLib);
									doExcludeLib = true;
									break;
								}
							}


							if (!doExcludeLib) {
								System.err.println("++ " + aLib);
								libs.add(aLib );
							}
						}
					}
				} catch (FileNotFoundException e) {
					continue;
				} catch (IOException e) {
					e.printStackTrace();
					continue;
				}
			}
		}


		// append gwt-servlet
		//
		boolean shallIncludeLib = true;
		for (LibUtil.Lib aLib : libs) {
			if ( aLib.getName().equals("gwt-servlet" ) ) {
				shallIncludeLib = false;
				break;
			}
		}
		if (shallIncludeLib) {
			final Lib aLib = new Lib("FD_LIBS/thirdparty/gwt/gwt-servlet.jar");
			System.err.println(">> " + aLib);
			libs.add( aLib );
		}
		// libs.add( new Lib("FD_LIBS/thirdparty/gwt/spring-2.5.jar") );
		

		List<LibUtil.Lib> sortedLibs = new ArrayList<LibUtil.Lib>(libs);
		Collections.<LibUtil.Lib>sort(sortedLibs);

		List<String> out = new ArrayList<String>(sortedLibs.size());
		for (LibUtil.Lib lib : sortedLibs) {
			out.add(lib.getPath());
		}
		return out;
	}




	public static class LibSet extends HashSet<Lib> {
		private static final long serialVersionUID = -2511338842514877810L;

		/**
		 * Add lib if not added yet or newer than the one in list
		 * 
		 * @param aLib
		 * @return
		 */
		@Override
		public boolean add(Lib aLib) {
			for (Lib l : this) {
				if (l.getName().equals(aLib.getName())) {
					int cmp = l.getVersion().compareTo(aLib.getVersion());
					if (cmp >= 0) {
						// System.err.println("Skip " + aLib);
						return false;
					} else {
						// System.err.println("Keep newer " + aLib + " over " + l);
						this.remove(l);
						break;
					}
				}
			}

			return super.add(aLib);
		}
	}
	
	
	/**
	 * Class representing a library
	 * 
	 * @author segabor
	 *
	 */
	public static class Lib implements Comparable<Lib> {
		public final String PSEP = System.getProperty("file.separator");

		String path;
		
		String base = null;
		String name = null;
		Version version = Version.NONE;
		
		public Lib(String path) {
			this.path = path;

			int k = path.lastIndexOf(PSEP);
			if (k == -1) {
				base = path;
			} else {
				base = path.substring(k+1, path.length());
			}
			
			// System.out.println(base);
			
			Pattern p = Pattern.compile("([\\w\\-]+)\\-(\\d(\\.\\d(\\.\\d)?)?).+");
			Matcher m = p.matcher(base);
			if (m.matches()) {
				// new name = file name - version - extension
				this.name = m.group(1);
				this.version = new Version(m.group(2));
			} else {
				// new name = file name - extension
				this.name = base.substring(0, base.indexOf("."));
				this.version = Version.NONE;
			}
		}

		public String getPath() {
			return path;
		}
		
		public String getName() {
			return name;
		}
		
		public Version getVersion() {
			return version;
		}
		
		@Override
		public String toString() {
			return "n: " +name + " / v: " + version.toString();
		}
		
		@Override
		public int compareTo(Lib other) {
			if (!(other instanceof Lib)) {
				return 1;
			}

			Lib l2 = (Lib) other;

			int nc = this.name.compareToIgnoreCase(l2.getName());
			if (nc == 0) {
				return this.version.compareTo(l2.getVersion());
			} else {
				return nc;
			}
		}
		
		@Override
		public int hashCode() {
			//System.err.println(name + " -> " + name.hashCode());
			return base.hashCode();
		}
	}

	
	public static class Version implements Comparable<Version> {
		public static Version NONE = new Version("0"); 
		
		int[] digits;
		String _str;

		public Version(String v) {
			_str = v;
			
			String[] x = v.split("\\.");
			digits = new int[x.length];
			if (x.length > 0) {
				for(int k=0; k<x.length;k++) {
					digits[k] = Integer.parseInt(x[k]);
				}
			} else {
				digits = new int[]{0};
			}
		}

		public int[] getDigits() {
			return digits;
		}

		@Override
		public int compareTo(Version paramT) {
			int[] _otherDigits = paramT.digits;
			
			final int ml = Math.min(digits.length, _otherDigits.length);
			
			for (int k=0; k<ml; k++) {
				if (digits[k] > _otherDigits[k])
					return 1;
				else if (digits[k] < _otherDigits[k])
					return -1;
			}

			if (digits.length > ml)
				return digits[ml] > 0 ? 1 : 0;
			else if (_otherDigits.length > ml)
				return _otherDigits[ml] > 0 ? -1 : 0;

			return 0;
		}


		@Override
		public boolean equals(Object paramObject) {
			if (!(paramObject instanceof Version)) {
				return false;
			}
			
			int[] _otherDigits = ((Version) paramObject).digits;
			
			if (digits.length != _otherDigits.length)
				return false;
			
			for (int k=0; k<digits.length; k++) {
				if (digits[k] != _otherDigits[k])
					return false;
			}
			return true;
		}
		
		@Override
		public String toString() {
			return _str;
		}
	}
}
