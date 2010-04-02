/**
 * 
 */
package com.freshdirect.fdstore.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.content.ContentFactory;
import com.freshdirect.fdstore.content.ProductModel;
import com.freshdirect.fdstore.content.SearchSortType;
import com.freshdirect.fdstore.util.AbstractNavigator.SortDisplay;
import com.freshdirect.framework.util.DateUtil;
import com.sun.org.apache.bcel.internal.generic.ISTORE;

/**
 * @author skrishnasamy
 *
 */
public class NewProductsGrouping {

	public List<TimeRange> timeRanges = new ArrayList<TimeRange>();
	
	public static final Logger LOGGER = Logger.getLogger(AbstractNavigator.class);
	
	private final static int COVER_UP_DAYS = 1;
	
	public NewProductsGrouping() {
		init();
	}
	
	private void init() {
		/*
		// Eg: <W2,W2-W4,M1-M2,M2-M3,>M3
		timeRanges.add(new TimeRange(1,2,0,TimeRange.WEEK, TimeRange.NEWER_THAN ));
		timeRanges.add(new TimeRange(2,2,4,TimeRange.WEEK, TimeRange.NULL ));
		timeRanges.add(new TimeRange(3,1,2,TimeRange.MONTH, TimeRange.NULL ));
		timeRanges.add(new TimeRange(4,2,3,TimeRange.MONTH, TimeRange.NULL));
		timeRanges.add(new TimeRange(5,3,0,TimeRange.MONTH, TimeRange.OLDER_THAN ));
		*/

		String groups = FDStoreProperties.getNewProductsGrouping();
		System.out.println();
		if(groups == null || groups.length() == 0) {
			LOGGER.error("New product grouping cannot be null or empty");
			return;
		}
		StringTokenizer tokens = new StringTokenizer(groups, ",");
outer:		while(tokens.hasMoreTokens()){
			int recencyType = -1;
			int fromValue = -1;
			int toValue = -1;
			int duration = -1;
			int sequence = 1;
			boolean tokenValid = true;
			int symbolCnt = 0;
			int validSymCnt = 0;
			
			String token = tokens.nextToken();
			if(token != null && token.length()> 0){
				if(token.indexOf("-") != -1){
					//contains both from and to value.
					StringTokenizer subTokens = new StringTokenizer(token, "-");
					if(subTokens.countTokens() > 2){
						//Token is invalid.
						LOGGER.debug("Invalid Token Level 2 Check");
					}
					//Read FROM Value;
					String subToken =subTokens.nextToken();
					if(subToken != null && subToken.length() > 1) {
						char symbol = subToken.charAt(0);
						try{
							Integer.parseInt(String.valueOf(symbol));
						}catch(NumberFormatException nfe){
							//Not a number. Parse the symbol.
							if(isDurationSymbol(symbol) != -1){
								duration = isDurationSymbol(symbol);
								String remainingSubToken = subToken.substring(1);
								try{
									fromValue = Integer.parseInt(String.valueOf(remainingSubToken));
								}catch(NumberFormatException e){
									//invalid token
									LOGGER.debug("Invalid fromValue");
									tokenValid = false;
									 continue outer; //scan next token.
								}
							} else {
								//invalid token
								LOGGER.debug("Invalid duration");
								tokenValid = false;
								continue outer; //scan next token.
							}
						}
					}
					if(!tokenValid) 
						continue outer; //scan next token.
					//Read TO Value;
					subToken =subTokens.nextToken();
					if(subToken != null && subToken.length() > 1) {
						char symbol = subToken.charAt(0);
						try{
							Integer.parseInt(String.valueOf(symbol));
						}catch(NumberFormatException nfe){
							//Not a number. Parse the symbol.
							if(isDurationSymbol(symbol) != -1){
								duration = isDurationSymbol(symbol);
								String remainingSubToken = subToken.substring(1);
								try{
									toValue = Integer.parseInt(String.valueOf(remainingSubToken));
								}catch(NumberFormatException e){
									//invalid token
									LOGGER.debug("Invalid toValue");
									tokenValid = false;
									continue outer; //scan next token.
								}
							} else {
								//invalid token
								LOGGER.debug("Invalid duration");
								tokenValid = false; 
								continue outer; //scan next token.
							}
						}
					}
					if(tokenValid){
						TimeRange tr = new TimeRange(sequence,fromValue,toValue,duration, TimeRange.NULL); 
						timeRanges.add(tr);
					}
				} else {
					//contains only from value.
					for(int i= 0; i < token.length(); i++){
						char symbol = token.charAt(i);
						symbolCnt++;
						try{
							Integer.parseInt(String.valueOf(symbol));
						}catch(NumberFormatException nfe){
							//Not a number. Parse the symbol.
							if(isRecencySymbol(symbol) != -1) {
								recencyType = isRecencySymbol(symbol);
							} 
							if(isDurationSymbol(symbol) != -1){
								duration = isDurationSymbol(symbol);
							}
							if(recencyType == -1 && duration == -1){
								//invalid token
								LOGGER.debug("Invalid duration or recency type");
								tokenValid = false;
								break;
							}
							
							if(symbolCnt == 2 && duration == -1) {
								//invalid token
								LOGGER.debug("Invalid duration");
								tokenValid = false;
								break;
							}
							validSymCnt++;
						}
						if(symbolCnt == 2) break; //Read 
					}
					if(tokenValid){
						String remainingToken = token.substring(symbolCnt);
						try{
							fromValue = Integer.parseInt(String.valueOf(remainingToken));
							TimeRange tr = new TimeRange(sequence,fromValue,0,duration, recencyType); 
							timeRanges.add(tr);
							sequence++;
						}catch(NumberFormatException nfe){
							//invalid token
							LOGGER.debug("Invalid fromValue");
							tokenValid = false;
							continue outer; //scan next token.
						}
					} else {
						continue outer; //scan next token.
					}
				} 
			} else {
				//Token is invalid.
				LOGGER.debug("Invalid Token Level 1 Check");
				continue outer; //scan next token.
			}
		}
		
	}
	
	private int isRecencySymbol(char symbol) {
		if(symbol == '<') return TimeRange.NEWER_THAN;
		if(symbol == '>') return TimeRange.OLDER_THAN;
		return -1;
			
	}
	
	private int isDurationSymbol(char symbol) {
		if(symbol == 'D') return TimeRange.DAY;
		if(symbol == 'W') return TimeRange.WEEK;
		if(symbol == 'M') return TimeRange.MONTH;
		return -1;
			
	}
	
	public List getTimeRanges(){
		return this.timeRanges;
	}
	
	/**
	 * Return the products new in the last n days
	 * 
	 * @param inDays
	 *            the n days criteria
	 * @return the list of the products in the sorted order where the most recent product comes first
	 */
	public List<ProductModel> getProductsNewerThan(List prods, int inDays) {
		List<ProductModel> products = new ArrayList<ProductModel>(prods.size());
		for(Iterator<ProductModel> it = prods.iterator(); it.hasNext();){
			ProductModel pm = (ProductModel) it.next();
			if(Math.ceil(pm.getAge()) <= inDays) {
				products.add(pm);
				//break;
			}
		}
		/*
		Collections.sort(products, new Comparator<ProductModel>() {
			@Override
			public int compare(ProductModel o1, ProductModel o2) {
				return - new Double(o1.getAge()).compareTo(new Double(o2.getAge()));
			}
		});*/
		return products;
	}
	
	public List<ProductModel> getProductsOlderThan(List prods, int inDays) {
		List<ProductModel> products = new ArrayList<ProductModel>(prods.size());
		for(Iterator<ProductModel> it = prods.iterator(); it.hasNext();){
			ProductModel pm = (ProductModel) it.next();
			if(Math.ceil(pm.getAge()) >= inDays) {
				products.add(pm);
				//break;
			}
			
		}
		/*
		Collections.sort(products, new Comparator<ProductModel>() {
			@Override
			public int compare(ProductModel o1, ProductModel o2) {
				return - new Double(o1.getAge()).compareTo(new Double(o2.getAge()));
			}
		});*/
		return products;
	}
	
	public List<ProductModel> getProductsBetween(List prods, int startDays, int endDays) {
		List<ProductModel> products = new ArrayList<ProductModel>(prods.size());
		for(Iterator<ProductModel> it = prods.iterator(); it.hasNext();){
			ProductModel pm = (ProductModel) it.next();
			if(Math.ceil(pm.getAge()) >= startDays && pm.getAge() <= endDays) {
				products.add(pm);
				//break;
			}
		}
		/*
		Collections.sort(products, new Comparator<ProductModel>() {
			@Override
			public int compare(ProductModel o1, ProductModel o2) {
				return - new Double(o1.getAge()).compareTo(new Double(o2.getAge()));
			}
		});*/
		return products;
	}
	
	public Map groupBy(List<ProductModel> prods, boolean inverse){
		if(this.timeRanges.isEmpty()) return null;
		List tempList = new ArrayList<ProductModel>(prods);
		Map groupedMap = new LinkedHashMap<TimeRange, List<ProductModel>>();
		//if(inverse) Collections.reverse(timeRanges);
		for(Iterator<TimeRange> it = timeRanges.iterator(); it.hasNext();){
			TimeRange range = (TimeRange) it.next();
			List filtered = null;
			if(range.getFromValue() > 0 && range.getToValue() == 0 && range.getRecencyType() == TimeRange.NEWER_THAN){
				filtered = getProductsNewerThan(tempList, range.getDaysRangeFrom() + COVER_UP_DAYS);
			} else if(range.getFromValue() > 0 && range.getToValue() == 0 && range.getRecencyType() == TimeRange.OLDER_THAN){
				filtered = getProductsOlderThan(tempList, range.getDaysRangeFrom() - COVER_UP_DAYS);
			} else {
				filtered = getProductsBetween(tempList, range.getDaysRangeFrom() - COVER_UP_DAYS, range.getDaysRangeTo() + COVER_UP_DAYS);
			}
			if(filtered != null && filtered.size() > 0){
				groupedMap.put(range, filtered);
				//remove filtered from main list.
				tempList.removeAll(filtered);
			}
		}
		/*
		if(inverse) {
			List mapKeys = new ArrayList(groupedMap.keySet());
			Collections.sort(mapKeys); 
		}*/
		return groupedMap;
	}

}
