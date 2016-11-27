package com.freshdirect.webapp.taglib.test.fdstore;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Category;

import com.freshdirect.cms.ContentKey;
import com.freshdirect.cms.ContentType;
import com.freshdirect.cms.application.CmsManager;
import com.freshdirect.fdstore.FDSkuNotFoundException;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.framework.conf.ResourceUtil;
import com.freshdirect.framework.util.CSVUtils;
import com.freshdirect.framework.util.ConfigHelper;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.fdstore.SessionName;

public class DistributionHelper {
	
	
	private static Category LOGGER = LoggerFactory.getInstance(DistributionHelper.class);
	
	private static Properties defaults = new Properties();
	static {
		defaults.put(SessionName.TEST_DISTRIBUTION_DIRECTORY, "classpath:/com/freshdirect/resource/test/distro/");
	}
	private ContentFactory fact = ContentFactory.getInstance();
	private CmsManager cms = CmsManager.getInstance();
	
	static private DistributionHelper instance = new DistributionHelper();
	
	static public DistributionHelper getInstance() { return instance; }
	
	
	private static String getPath(String dir, String fileName) {
		return dir.endsWith("/") ? (dir + fileName) : (dir + '/' + fileName);
	}
	
	
	public static List getSearhTerms() throws IOException {
		
		String distroDir = ConfigHelper.getPropertiesFromClassLoader(
				"tests.properties",defaults).getProperty(SessionName.TEST_DISTRIBUTION_DIRECTORY);

		InputStream is = null;
		try {
			is = ResourceUtil.openResource(getPath(distroDir,"terms.csv"));
			
			return CSVUtils.parse(is,false,false);
		} catch (IOException e) {
			LOGGER.warn("Could not read/open terms.csv in " + distroDir,e);
			throw e;
		} finally {
			is.close();
		}
	}
	
	public static List getDistributions() throws IOException {
		List files = new ArrayList();
	
		String distroDir = ConfigHelper.getPropertiesFromClassLoader(
			"tests.properties",defaults).getProperty(SessionName.TEST_DISTRIBUTION_DIRECTORY);

		InputStream is = null;
		try {
			is = ResourceUtil.openResource(getPath(distroDir, "manifest.csv"));
		
			
			List manifest = CSVUtils.parse(is, false, false);
			for(Iterator i = manifest.iterator(); i.hasNext();) {
				List row = (List)i.next();
			
				String file = (String)row.get(0);
			
				InputStream fis = null;
				String path = getPath(distroDir, file);
				try {
					fis = ResourceUtil.openResource(path);
				} catch (IOException e) {
					LOGGER.warn("Could not verify " + path, e);
					continue;
				} finally {
					if (fis != null) fis.close();
				}
				files.add(row.get(0));
			}
		} catch (IOException e) {
			LOGGER.warn("Could not load distributions from manifest", e);
			throw e;
		} finally {
			if (is != null) is.close();
		}
		return files;
	}
	
	private static void addValue(Map frequencies, Object key, int frequency) {
		Integer v = (Integer)frequencies.get(key);
		int newVal = (v == null? 0 : v.intValue()) + frequency;
		
		if (newVal == 0) frequencies.remove(key);
		else frequencies.put(key, new Integer(newVal));
	}
	
	public static InputStream openDistribution(String fileName) throws IOException {
		String distroRepository = 
			ConfigHelper.getPropertiesFromClassLoader("tests.properties",defaults).getProperty(SessionName.TEST_DISTRIBUTION_DIRECTORY);
		return ResourceUtil.openResource(getPath(distroRepository,fileName));
	}
	
	
	private boolean checkExists(ContentType type, String id) {
		return cms.getContentNode(new ContentKey(type,id)) != null;
	}

	/**
	 * Get product distribution from explicit sku-frequency CSV file.
	 * 
	 * Expects a CSV file where the first two columns are the sku and
	 * the frequency. If a sku occurs several times, the frequencies will be added.
	 * @param is input stream
	 * @return Map<String,Integer> where the keys are the product id's and the values are the frequencies
	 * @throws IOException 
	 */
	public Map getProductDistributionFromSKUs(InputStream is) throws IOException {
		Map distro = new HashMap();
		
		List skus = CSVUtils.parse(is, true, false);
		for(Iterator i = skus.iterator(); i.hasNext(); ) {
			List row = (List)i.next();
			try {
				ProductModel productModel = fact.getProduct((String)row.get(0));
				addValue(distro, productModel.getContentKey().getId(), Integer.parseInt((String)row.get(1)));
			} catch(FDSkuNotFoundException e) {
				continue;
			} catch(NumberFormatException e) {
				continue;
			}
		}
		return distro;
	}
	
	/**
	 * Get category distribution from a CSV file.
	 * Expects a CSV file where the first two columns are the category id and
	 * the frequency.
	 * @param is input stream
	 * @return Map<String,Integer> where keys are the category id's and the values are the frequencies
	 * @throws IOException
	 */
	public Map getContentDistributionFromCSVStream(InputStream is, ContentType type, boolean check) throws IOException {
		Map distro = new HashMap();
		
		List skus = CSVUtils.parse(is, true, false);
		for(Iterator i = skus.iterator(); i.hasNext(); ) {
			List row = (List)i.next();
			try {
				String id = (String)row.get(0);
			
				if (check) {
					try {
						if (!checkExists(type,id)) continue;
					} catch (Throwable t) {
						continue;
					}
				}
				addValue(distro, id, Integer.parseInt((String)row.get(1)));
			} catch(NumberFormatException e) {
				continue;
			}
		}
		return distro;
	}
	
	/**
	 * Get the value of a param from a query string.
	 * @param query
	 * @param key
	 * @return param value or null if the param does not occur or its value is ""
	 */
	private String getQueryValue(String query, String key) {
		String lookFor = key + '=';
		int s = query.indexOf(key);
		if (s == -1) return null;
		
		StringBuffer value = new StringBuffer();
		for(int i = s + lookFor.length(); i< query.length(); ++i) {
			char c = query.charAt(i);
			switch(c) {
				case '&':
				case '#':
				case ' ':
				case '\t':
				case '\n':
				case '\r':
					break;
				default:
					value.append(c);
			}
		}
		
		return value.length() > 0 ? value.toString() : null;
	}
	
	/**
	 * Get product distribution from a file of query strings (possibly with URLs).
	 * @param is
	 * @param checkExists
	 * @return
	 */
	public Map getContentDistributionFromQueryStrings(InputStream is, String qparam, ContentType type, boolean check) {
		Map distro = new HashMap();
		
		BufferedReader bis = new BufferedReader(new InputStreamReader(is));
		
		for(;;) {
			try {
				String line = bis.readLine();
				String id = getQueryValue(line,qparam);
				
				if (id != null && (!check || checkExists(type,id))) addValue(distro, id, 1);
			} catch (EOFException e) {
				break;
			} catch (IOException e) {
				continue;
			}
		}
		
		return distro;
	}
	
	
	
	
}
