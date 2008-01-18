/*
 * Geocoder.java
 *
 * Created on February 11, 2002, 8:13 PM
 */

package com.freshdirect.delivery.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;

import com.freshdirect.common.address.AddressInfo;
import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.address.EnumAddressType;
import com.freshdirect.delivery.AddressScrubber;
import com.freshdirect.delivery.DlvApartmentRange;
import com.freshdirect.delivery.EnumAddressExceptionReason;
import com.freshdirect.delivery.EnumAddressVerificationResult;
import com.freshdirect.delivery.ExceptionAddress;
import com.freshdirect.delivery.InvalidAddressException;
import com.freshdirect.fdstore.FDStoreProperties;
import com.freshdirect.fdstore.StateCounty;
import com.freshdirect.framework.core.SequenceGenerator;
import com.freshdirect.framework.util.StringUtil;
import com.freshdirect.framework.util.log.LoggerFactory;

/**
 *
 * @author  mrose
 * @version
 */
public class GeographyDAO {
	
	private static final Category LOGGER = LoggerFactory.getInstance(GeographyDAO.class);

	public GeographyDAO() {
		super();
	}

	public ArrayList findSuggestionsForAmbiguousAddress(AddressModel address, Connection conn)
		throws SQLException,
		InvalidAddressException {

		ArrayList suggestions = new ArrayList();

		//
		// run address through the scrubber
		//
		String streetAddress = null;
		try {
			streetAddress = AddressScrubber.standardizeForUSPS(address.getAddress1());
		} catch (InvalidAddressException iae1) {
			if ((address.getAddress2() == null) || "".equals(address.getAddress2())) {
				throw iae1;
			} else {
				try {
					streetAddress = AddressScrubber.standardizeForUSPS(address.getAddress2());
					//
					// swap streets so the real one is in the first position
					//
					String s1 = address.getAddress1();
					String s2 = address.getAddress2();
					address.setAddress1(s2);
					address.setAddress2(s1);
				} catch (InvalidAddressException iae2) {
					throw iae2;
				}
			}
		}
		//
		// separate street number and street name
		//
		int numBreak = streetAddress.indexOf(' ');
		if (numBreak == -1) {
			return suggestions;
		}
		String streetName = streetAddress.substring(numBreak + 1, streetAddress.length());
		String bldgNumber = streetAddress.substring(0, numBreak);

		String streetNormal = compress(streetName);
		//String apt = address.getApartment()!=null?address.getApartment().toUpperCase():"";
		PreparedStatement ps = null;
		ResultSet rs = null;
		//
		// find suggestions, ignore apartment
		//
		ps = conn.prepareStatement(zp4StreetIncompleteQuery);
		ps.setString(1, "%" + streetNormal + "%");
		ps.setString(2, address.getZipCode());
		ps.setString(3, String.valueOf(bldgNumber));
		rs = ps.executeQuery();
		processSuggestions(rs, suggestions, address, bldgNumber);
		rs.close();
		ps.close();

		return suggestions;
	}

	private static void processSuggestions(ResultSet rs, ArrayList addresses, AddressModel origAddr, String bldgNum)
		throws SQLException {
		while (rs.next()) {
			String s_pre = rs.getString("STREET_PRE_DIR");
			String s_name = rs.getString("STREET_NAME");
			String s_suff = rs.getString("STREET_SUFFIX");
			String s_post = rs.getString("STREET_POST_DIR");

			AddressModel sugg = new AddressModel();
			sugg.setAddress1(bldgNum + " " + composeStreetName(s_pre, s_name, s_suff, s_post));
			sugg.setAddress2(origAddr.getAddress2());
			sugg.setApartment(origAddr.getApartment());
			sugg.setCity(origAddr.getCity());
			sugg.setState(origAddr.getState());
			sugg.setZipCode(origAddr.getZipCode());
			addresses.add(sugg);
		}
	}

	private static String composeStreetName(String preDir, String name, String suffix, String postDir) {
		StringBuffer buff = new StringBuffer();
		if ((preDir != null) && !"".equals(preDir)) {
			buff.append(preDir);
		}
		if ((name != null) && !"".equals(name)) {
			if ((buff.length() > 0) && (buff.charAt(buff.length() - 1) != ' '))
				buff.append(" ");
			buff.append(name);
		}
		if ((suffix != null) && !"".equals(suffix)) {
			if ((buff.length() > 0) && (buff.charAt(buff.length() - 1) != ' '))
				buff.append(" ");
			buff.append(suffix);
		}
		if ((postDir != null) && !"".equals(postDir)) {
			if ((buff.length() > 0) && (buff.charAt(buff.length() - 1) != ' '))
				buff.append(" ");
			buff.append(postDir);
		}
		return buff.toString();
	}

	String zp4AptQuery = "select * from dlv.zipplusfour zpf, dlv.city_state cs "
		+ "where zpf.city_state_key = cs.city_state_key "
		+ "and bldg_num_low = ? and bldg_num_high = ? and street_normal = ? and zipcode = ? "
		+ "and (lpad(replace(replace(replace(upper(?),'-'),' '),'PH'),30,'0') between lpad(replace(replace(replace(apt_num_low,'-'),' '),'PH'), 30, '0') and lpad(replace(replace(replace(apt_num_high,'-'),' '),'PH'), 30, '0'))";

	String zp4AptStreetIncompleteQuery = "select distinct street_pre_dir, street_name, street_suffix, street_post_dir from dlv.zipplusfour "
		+ "where bldg_num_low = ? and bldg_num_high = ? and street_normal like ? and zipcode = ? "
		+ "and (lpad(replace(replace(replace(upper(?),'-'),' '),'PH'),30,'0') between lpad(replace(replace(replace(apt_num_low,'-'),' '),'PH'), 30, '0') and lpad(replace(replace(replace(apt_num_high,'-'),' '),'PH'), 30, '0'))";

	String zp4BldgQuery = "select count(*) from dlv.zipplusfour "
		+ "where bldg_num_low = ? and bldg_num_high = ? and street_normal = ? and zipcode = ? ";

	String zp4StreetQuery = "select * from dlv.zipplusfour zpf, dlv.city_state cs "
		+ "where zpf.city_state_key = cs.city_state_key "
		+ "and street_normal = ? and zipcode = ? and (lpad(?,10,'0') between lpad(bldg_num_low,10,'0') and lpad(bldg_num_high,10,'0')) "
		+ "and apt_num_low is null and apt_num_high is null";

	String zp4StreetIncompleteQuery = "select distinct street_pre_dir, street_name, street_suffix, street_post_dir, street_normal from dlv.zipplusfour "
		+ "where street_normal like ? and zipcode = ? and (lpad(?,10,'0') between lpad(bldg_num_low,10,'0') and lpad(bldg_num_high,10,'0'))";

	String simpleStreetQuery = "select * from dlv.zipplusfour " + "where street_normal like ? and zipcode = ?";

	public EnumAddressVerificationResult verify(AddressModel address, Connection conn) throws SQLException {
		return verify(address, true, conn);
	}

	private class StreetName {

		public StreetName() {
			super();
		}

		public String preDir;
		public String name;
		public String suffix;
		public String postDir;
		public String normal;
	}

	public EnumAddressVerificationResult verify(AddressModel address, boolean useApt, Connection conn) throws SQLException {

		AddressInfo info = address.getAddressInfo();
		if (info == null) {
			info = new AddressInfo();
		}

		//
		// run address through the scrubber
		//
		String streetAddress = null;
		try {
			streetAddress = AddressScrubber.standardizeForUSPS(address.getAddress1());
		} catch (InvalidAddressException iae1) {
			if ((address.getAddress2() == null) || "".equals(address.getAddress2())) {
				return EnumAddressVerificationResult.ADDRESS_BAD;
			} else {
				try {
					streetAddress = AddressScrubber.standardizeForUSPS(address.getAddress2());
					//
					// swap streets so the real one is in the first position
					//
					String s1 = address.getAddress1();
					String s2 = address.getAddress2();
					address.setAddress1(s2);
					address.setAddress2(s1);
				} catch (InvalidAddressException iae2) {
					return EnumAddressVerificationResult.ADDRESS_BAD;
				}
			}
		}
		//
		// separate street number and street name
		//
		int numBreak = streetAddress.indexOf(' ');
		String streetName = streetAddress.substring(numBreak + 1, streetAddress.length());
		String bldgNumber = streetAddress.substring(0, numBreak);

		EnumAddressVerificationResult result = EnumAddressVerificationResult.NOT_VERIFIED;

		String streetNormal = compress(streetName);
		String apt = address.getApartment() != null ? address.getApartment().toUpperCase() : "";

		// check exceptions table to see if address has an entry
		if (this.checkAddressExceptions(conn, streetAddress, address, info)) {
			info.setScrubbedStreet(streetAddress);
			address.setAddressInfo(info);
			return EnumAddressVerificationResult.ADDRESS_OK;
		}

		PreparedStatement ps = null;
		ResultSet rs = null;
		//
		// check for incomplete addresses and see if we can automatically fix it
		//
		String[] zipCodeList = getZipCodeFromAddress(address);
		String zipCode = null;
		if(zipCodeList != null) {
			int intLength = zipCodeList.length;
			for(int intCount=0; intCount<intLength; intCount++) {
				zipCode = zipCodeList[intCount];
				
				if (EnumAddressVerificationResult.NOT_VERIFIED.equals(result)) {
					ps = conn.prepareStatement(zp4StreetIncompleteQuery);
					ps.setString(1, "%" + streetNormal + "%");
					ps.setString(2, zipCode);
					ps.setString(3, String.valueOf(bldgNumber));
					rs = ps.executeQuery();
					ArrayList streetNames = new ArrayList();
					while (rs.next()) {
						StreetName sname = new StreetName();
						sname.preDir = rs.getString("STREET_PRE_DIR");
						sname.name = rs.getString("STREET_NAME");
						sname.suffix = rs.getString("STREET_SUFFIX");
						sname.postDir = rs.getString("STREET_POST_DIR");
						sname.normal = rs.getString("STREET_NORMAL");
						streetNames.add(sname);
					}
					rs.close();
					ps.close();
					boolean multiples = (streetNames.size() > 1);
					//
					// reject false positives...
					//
					StreetName correctName = null;
					if (streetNames.size() == 1) {
						correctName = (StreetName) streetNames.get(0);
					} else if (multiples) {
						for (Iterator nIter = streetNames.iterator(); nIter.hasNext();) {
							StreetName aName = (StreetName) nIter.next();
							if (aName.normal.equals(streetNormal)) {
								multiples = false;
								correctName = aName;
								break;
							}
						}
					}
					if (streetNames.size() == 0) {
						// if this didn't match anything at all, the subsequent queries wouldn't help either
						if(intCount == intLength -1) {
							return zipplusfourGeocodeHack(address, conn, result);
						} else {
							continue;
						}
		
					} else if (!multiples) {
						//
						// hey, it worked! fix the address and call it a day
						//
						String origStreet = address.getAddress1();
						int b = origStreet.indexOf(" ");
						String bldg = origStreet.substring(0, b);
						String correctedStreetName = composeStreetName(
							correctName.preDir,
							correctName.name,
							correctName.suffix,
							correctName.postDir);
						bldg = stripImproperDir(bldg);
						address.setAddress1(bldg + " " + correctedStreetName);
		
						b = streetAddress.indexOf(" ");
						bldg = streetAddress.substring(0, b);
		
						streetAddress = bldg + " " + correctedStreetName;
						streetNormal = compress(correctedStreetName);
						result = EnumAddressVerificationResult.NOT_VERIFIED;
						break;
					} else {
						//
						// hmmm, there's more than one street that satisfies the criteria
						//
						result = EnumAddressVerificationResult.ADDRESS_NOT_UNIQUE;
						break;
					}
				}
			}
		}
		//
		// street address should be clean by now
		//
		info.setScrubbedStreet(streetAddress);
		address.setAddressInfo(info);

		//
		// try and locate down to the apt number
		//
		if (useApt && EnumAddressVerificationResult.NOT_VERIFIED.equals(result)) {
			ps = conn.prepareStatement(zp4AptQuery);
			ps.setString(1, String.valueOf(bldgNumber));
			ps.setString(2, String.valueOf(bldgNumber));
			ps.setString(3, streetNormal);
			ps.setString(4, zipCode);
			ps.setString(5, apt);
			rs = ps.executeQuery();
			if (rs.next()) {
				address.setCity(StringUtils.capitaliseAllWords(rs.getString("CITY").toLowerCase()));
				address.setState(rs.getString("STATE"));
				info.setCounty(rs.getString("COUNTY"));
				info.setAddressType(EnumAddressType.getEnum(rs.getString("ADDRESS_TYPE")));
				result = EnumAddressVerificationResult.ADDRESS_OK;
			}
			rs.close();
			ps.close();
		}
		//
		// next see if they got the address right, but the apartment wrong
		//
		if (useApt && EnumAddressVerificationResult.NOT_VERIFIED.equals(result)) {
			ps = conn.prepareStatement(zp4BldgQuery);
			ps.setString(1, String.valueOf(bldgNumber));
			ps.setString(2, String.valueOf(bldgNumber));
			ps.setString(3, streetNormal);
			ps.setString(4, zipCode);
			rs = ps.executeQuery();
			if (rs.next()) {
				int rows = rs.getInt(1);
				if (rows > 1) {
					result = EnumAddressVerificationResult.APT_WRONG;
				}
			} else {
				result = EnumAddressVerificationResult.ADDRESS_BAD;
			}
			rs.close();
			ps.close();
		}
		//
		// now see if this is just a street address (not a highrise)
		//
		if (EnumAddressVerificationResult.NOT_VERIFIED.equals(result)) {
			ps = conn.prepareStatement(zp4StreetQuery);
			ps.setString(1, streetNormal);
			ps.setString(2, zipCode);
			ps.setString(3, String.valueOf(bldgNumber));
			rs = ps.executeQuery();
			if (rs.next()) {
				address.setCity(StringUtils.capitaliseAllWords(rs.getString("CITY").toLowerCase()));
				address.setState(rs.getString("STATE"));
				info.setCounty(rs.getString("COUNTY"));
				info.setAddressType(EnumAddressType.getEnum(rs.getString("ADDRESS_TYPE")));
				result = EnumAddressVerificationResult.ADDRESS_OK;
			}
			rs.close();
			ps.close();
		}
		//
		// finally, try to figure out if it was the bldg number or the street that's the problem
		//
		if (EnumAddressVerificationResult.NOT_VERIFIED.equals(result)) {
			ps = conn.prepareStatement(simpleStreetQuery);
			ps.setString(1, "%" + streetNormal + "%");
			ps.setString(2, zipCode);
			rs = ps.executeQuery();
			if (rs.next()) {
				result = EnumAddressVerificationResult.BUILDING_WRONG;
			} else {
				result = EnumAddressVerificationResult.STREET_WRONG;
			}
			rs.close();
			ps.close();
		}
		//System.out.println(info + " city = " + address.getCity());

		if (!EnumAddressVerificationResult.ADDRESS_OK.equals(result) && !EnumAddressVerificationResult.APT_WRONG.equals(result)) {
			result = this.zipplusfourGeocodeHack(address, conn, result);
		}

		return result;
	}

	public class StreetSegment {

		public String street;

		public String leftZipcode;

		public String rightZipcode;

		public int leftNearNum;

		public int leftFarNum;

		public int rightNearNum;

		public int rightFarNum;

		public double nearX;

		public double nearY;

		public double farX;

		public double farY;

	}

	private final static double offsetFromStreet = 10; // meters
	private final static double XmetersToDeg = 1.0 / 84515.0; // meters per degree longitude approx at 40.7 deg latitude
	private final static double YmetersToDeg = 1.0 / 111048.0; // meters per degree latitude approx at 40.7 deg latitude

	private final static double slopeAdjustment = YmetersToDeg / XmetersToDeg;
	private final static String streetSegQuery = "SELECT ng.link_id, ng.l_addrsch, ng.r_addrsch, ng.l_nrefaddr, ng.l_refaddr, "
		+ "ng.r_nrefaddr, ng.r_refaddr, ng.l_postcode, ng.r_postcode, "
		+ "gg.column_value AS coord "
		+ "FROM dlv.navtech_geocode ng, TABLE (ng.geoloc.sdo_ordinates) gg "
		+ "WHERE ng.st_normal = ? "
		+ "AND (ng.l_postcode = ? OR ng.r_postcode = ?) "
		+ "AND ((((? BETWEEN ng.l_nrefaddr AND ng.l_refaddr) OR (? BETWEEN ng.l_refaddr AND ng.l_nrefaddr)) "
		+ "AND DECODE (l_addrsch,'M', 1,'E', decode(mod(?,2), 0, 1, 0), 'O', decode(mod(?,2), 1, 1, 0),0) = 1) "
		+ "OR   (((? BETWEEN ng.r_nrefaddr AND ng.r_refaddr) OR (? BETWEEN ng.r_refaddr AND ng.r_nrefaddr)) "
		+ "AND DECODE (r_addrsch,'M', 1,'E', decode(mod(?,2), 0, 1, 0), 'O', decode(mod(?,2), 1, 1, 0),0) = 1)) ";
	
	public static final String GEOCODE_OK = "GEOCODE_OK";
	public final static String GEOCODE_FAILED = "GEOCODE_FAILED";

	//
	// this is a wrapper for the geocoder that retries to geocode after the first attempt fails
	// by guessing where a '-' might be in the building number
	//
	public String geocode(AddressModel address, boolean munge, Connection conn) throws SQLException, InvalidAddressException {
		
		//LOGGER.debug("--------- START GEOCODE BASE---------------------\n"+address);
		String result = reallyGeocode(address, conn, false);
		if (GEOCODE_OK.equals(result)) {
			//LOGGER.debug(address+"\n--------- END GEOCODE BASE1---------------------\n");
			return result;
		}
		
		result = reallyGeocode(address, conn, true);
		if (GEOCODE_OK.equals(result)) {
			//LOGGER.debug(address+"\n--------- END GEOCODE BASE2---------------------\n");
			return result;
		}
		
		//
		// failed, check exceptions table
		//
		result = checkGeocodeExceptions(address, conn);
		if (GEOCODE_OK.equals(result)) {
			//LOGGER.debug(address+"\n--------- END GEOCODE BASE2---------------------\n");
			return result;
		}
		
		if (munge) {
			//
			// failed, guess where the '-' is
			//
			String streetAddress = address.getAddress1();
			int numBreak = streetAddress.indexOf(' ');
			String bldgNum = streetAddress.substring(0, numBreak);
			//
			// if the bldgNum is too short, or already contained a '-' and the first attempt failed anyway, this won't help at all
			//
			if (bldgNum.length() < 3 || bldgNum.indexOf('-') > -1)
				return GEOCODE_FAILED;
			//
			// OK, now inset the '-' and try again
			//
			String theRest = streetAddress.substring(numBreak + 1);
			bldgNum = bldgNum.substring(0, bldgNum.length() - 2) + "-" + bldgNum.substring(bldgNum.length() - 2, bldgNum.length());
			address.setAddress1(bldgNum + " " + theRest);
			String resultTmp = reallyGeocode(address, conn, false);
			
			//LOGGER.debug(address+"\n--------- END GEOCODE BASE3---------------------\n");
			return resultTmp;
		}
		//LOGGER.debug(address+"\n--------- END GEOCODE BASE4---------------------\n");
		return result;


	}

	public String geocode(AddressModel address, Connection conn) throws SQLException, InvalidAddressException {
		return geocode(address, true, conn);
	}

	//
	// geocodes an address that is already known to be scrubbed and verified
	// if successful, it returns GEOCODE_OK and sets the longitude and latitude of the address it was given as a paramter
	// otherwise, it just returns GEOCODE_FAILED
	//
	private String reallyGeocode(AddressModel address, Connection conn
									, boolean reverseDirectionSubstition) throws SQLException, InvalidAddressException {
		String streetAddress = null;
		String oldStreetAddress = address.getAddress1();
		try {
			streetAddress = (reverseDirectionSubstition ? AddressScrubber.standardizeForGeocodeNoSubstitution(address.getAddress1())
								: AddressScrubber.standardizeForGeocode(address.getAddress1()));
			//streetAddress = AddressScrubber.standardizeForGeocode(address.getAddress1());	
		} catch (InvalidAddressException iae1) {
			try {
				streetAddress = (reverseDirectionSubstition ? AddressScrubber.standardizeForGeocodeNoSubstitution(address.getAddress2())
						: AddressScrubber.standardizeForGeocode(address.getAddress2()));
				//streetAddress = AddressScrubber.standardizeForGeocode(address.getAddress2());
			} catch (InvalidAddressException iae2) {
				throw iae2;
			}
		}

		//
		// separate street number and street name
		//
		int numBreak = streetAddress.indexOf(' ');
		String streetName = streetAddress.substring(numBreak + 1, streetAddress.length());
		int bldgNumber = Integer.valueOf(streetAddress.substring(0, numBreak)).intValue();

		numBreak = oldStreetAddress.indexOf(' ');
		String bldgString = oldStreetAddress.substring(0, numBreak);

		int newBldgNumber = bldgNumber;
		if (!bldgString.equals("" + bldgNumber)) {
			String clean = cleanBldgNum(bldgString);
			try {
				newBldgNumber = Integer.parseInt(clean);
			} catch (NumberFormatException e) {
				//i tried, i dont know what else i can do here, just set it back
				newBldgNumber = bldgNumber;
			}
		}

		String streetNormal = compress(streetName);
		StreetSegment segment = null;

		String leftAddressScheme = "";
		String rightAddressScheme = "";
		String[] zipCode = getZipCodeFromAddress(address);
		
		if(zipCode != null) {
			int intLength = zipCode.length;
			for(int intCount=0; intCount<intLength; intCount++) {
				//
				// find row in db for street segment with a zip code
				//
				//System.out.println("zipCode[intCount]>"+zipCode[intCount]);
				PreparedStatement ps = null;
				ps = conn.prepareStatement(streetSegQuery);
				ps.setString(1, streetNormal);
				ps.setString(2, zipCode[intCount]);
				ps.setString(3, zipCode[intCount]);
				ps.setInt(4, bldgNumber);
				ps.setInt(5, bldgNumber);
				ps.setInt(6, newBldgNumber);
				ps.setInt(7, newBldgNumber);
				ps.setInt(8, bldgNumber);
				ps.setInt(9, bldgNumber);
				ps.setInt(10, newBldgNumber);
				ps.setInt(11, newBldgNumber);
				ResultSet rs = ps.executeQuery();
				
				//LOGGER.debug("--------- START REALLY GEOCODE ---------------------\n"+address);
				if (rs.next()) {
					//LOGGER.debug("--------- HAS GEOCODE SEGMENT---------------------");
					segment = new StreetSegment();
					leftAddressScheme = rs.getString("L_ADDRSCH");
					rightAddressScheme = rs.getString("R_ADDRSCH");
					segment.leftNearNum = rs.getInt("L_REFADDR");
					segment.leftFarNum = rs.getInt("L_NREFADDR");
		
					segment.rightNearNum = rs.getInt("R_REFADDR");
					segment.rightFarNum = rs.getInt("R_NREFADDR");
		
					segment.leftZipcode = rs.getString("L_POSTCODE");
					segment.rightZipcode = rs.getString("R_POSTCODE");
		
					segment.nearX = rs.getDouble("COORD");
		
					if (rs.next()) {
						segment.nearY = rs.getDouble("COORD");
					}
		
					if (rs.next()) {
						segment.farX = rs.getDouble("COORD");
					}
		
					if (rs.next()) {
						segment.farY = rs.getDouble("COORD");
					}
				}
				rs.close();
				ps.close();
				if (segment == null) {
					//LOGGER.debug("--------- GEOCODE FAILED ---------------------");
					if(intCount==intLength-1) {
						return GEOCODE_FAILED;
					} else {
						continue;
					}
				}
				//
				// which of the street segments returned and which side of the the street is the correct one?
				//
				if (leftAddressScheme == null || leftAddressScheme.equals("")) {
					leftAddressScheme = "";
					if (rightAddressScheme != null) {
						if (rightAddressScheme.equals("E")) {
							leftAddressScheme = "O";
						}
						if (rightAddressScheme.equals("O")) {
							leftAddressScheme = "E";
						}
					}
				}
		
				boolean onLeft = false;
				boolean doOffset = true;
				if (leftAddressScheme.equals("E") || leftAddressScheme.equals("O")) {
		
					if (leftAddressScheme.equals("E") && (newBldgNumber % 2 == 0)) {
						onLeft = true;
					}
					if (leftAddressScheme.equals("O") && (newBldgNumber % 2 == 1)) {
						onLeft = true;
					}
				} else { // Don't do offset.  If can't figure out; always default to right side of street
					doOffset = false;
				}
		
				double dX = segment.farX - segment.nearX;
				double dY = segment.farY - segment.nearY;
				double distance = Math.sqrt(dX * dX + dY * dY);
		
				double slope = dY / dX;
		
				//
				// how far along the segment is the street address?
				//
				double perc = 0.0;
				if (onLeft) {
					perc = ((double) (bldgNumber - segment.leftNearNum)) / ((double) (segment.leftFarNum - segment.leftNearNum));
				} else {
					perc = ((double) (bldgNumber - segment.rightNearNum)) / ((double) (segment.rightFarNum - segment.rightNearNum));
				}
		
				// Fix boundary conditions
				if (new Double(perc).isNaN() || (perc == Double.NEGATIVE_INFINITY) || (perc == Double.POSITIVE_INFINITY)) {
					perc = 0.5;
				}
		
				// This is one one corner, offset it by a wee bit.
				if (perc == 0)
					perc = 0.05;
		
				// On another corner
				if (perc == 1.0)
					perc = 0.95;
		
				//
				// find the X,Y point on the street
				//
				double pX, pY;
				double newDistance = distance * perc;
		
				if (new Double(slope).isNaN() || (slope == Double.NEGATIVE_INFINITY) || (slope == Double.POSITIVE_INFINITY)) {
					pX = segment.nearX;
					pY = newDistance + segment.nearY;
				} else if (slope == 0) {
					pX = newDistance + segment.nearX;
					pY = segment.nearY;
				} else {
					if (segment.nearX > segment.farX) {
						pX = -newDistance / Math.sqrt(slope * slope + 1) + segment.nearX;
					} else {
						pX = newDistance / Math.sqrt(slope * slope + 1) + segment.nearX;
					}
					pY = slope * (pX - segment.nearX) + segment.nearY;
				}
		
				// Now offset this 20 meters
				double newSlope = -1 / slope;
		
				// Approximate actual offset
				double newX, newY;
				if (doOffset) {
					double offsetDistance;
		
					if (pX >= segment.farX) {
						offsetDistance = offsetFromStreet * XmetersToDeg;
					} else {
						offsetDistance = -offsetFromStreet * XmetersToDeg;
					}
		
					//	Assign Left is negative
					if (onLeft)
						offsetDistance *= -1;
		
					if (newSlope < 0)
						offsetDistance *= -1;
		
					newX = offsetDistance / Math.sqrt(newSlope * newSlope + 1) + pX;
		
					if (slope == 0.0)
						newY = pY;
					else {
						newY = newSlope * (newX - pX) + pY;
					}
		
				} else {
					newX = pX;
					newY = pY;
				}
		
				AddressInfo info = address.getAddressInfo();
				
				//LOGGER.debug("--------- LAT, LONG---------------------"+newX+"-->"+newY);
		
				if (info == null) {
					info = new AddressInfo();
				}
		
				info.setLongitude(newX);
				info.setLatitude(newY);
				/*
				 if (onLeft) {
				 //
				 // rotate unit vector counter-clockwise
				 // find displacement by (-dY, dX)
				 //
				 info.setLongitude(pX - dYunit * offsetFromStreet * XmetersToDeg);
				 info.setLatitude(pY + dXunit * offsetFromStreet * YmetersToDeg);
				 } else {
				 //
				 // rotate unit vector clockwise
				 // find displacement by (dY, -dX)
				 //
				 info.setLongitude(pX + dYunit * offsetFromStreet * XmetersToDeg);
				 info.setLatitude(pY - dXunit * offsetFromStreet * YmetersToDeg);
				 }
				 */
				address.setAddressInfo(info);
		
				//System.out.println("point offset from street is X = " + retval.x + ", Y = " + retval.y);
				
				LOGGER.debug(address+"\n--------- END REALLY GEOCODE ---------------------");
				return GEOCODE_OK;
			}
		}
		return GEOCODE_FAILED;
	}

	/*
	 private String reallyGeocode(AddressModel address, Connection conn) throws SQLException, InvalidAddressException {
	 //
	 // run address through the scrubber
	 //
	 String streetAddress = null;
	 try {
	 streetAddress = AddressScrubber.standardizeForGeocode(address.getAddress1());
	 } catch (InvalidAddressException iae1) {
	 try {
	 streetAddress = AddressScrubber.standardizeForGeocode(address.getAddress2());
	 } catch (InvalidAddressException iae2) {
	 throw iae2;
	 }
	 }

	 //
	 // separate street number and street name
	 //
	 int numBreak = streetAddress.indexOf(' ');
	 String streetName = streetAddress.substring(numBreak + 1, streetAddress.length());
	 int bldgNumber = Integer.valueOf(streetAddress.substring(0, numBreak)).intValue();

	 String streetNormal = compress(streetName);
	 StreetSegment segment = null;

	 //
	 // find row in db for street segment with a zip code
	 //
	 PreparedStatement ps = conn.prepareStatement(streetSegQuery);
	 ps.setString(1, streetNormal);
	 ps.setString(2, address.getZipCode());
	 ps.setString(3, address.getZipCode());
	 ps.setInt(4, bldgNumber);
	 ps.setInt(5, bldgNumber);
	 ps.setInt(6, bldgNumber);
	 ps.setInt(7, bldgNumber);
	 ps.setInt(8, bldgNumber);
	 ps.setInt(9, bldgNumber);
	 ps.setInt(10, bldgNumber);
	 ps.setInt(11, bldgNumber);
	 ResultSet rs = ps.executeQuery();
	 
	 if (rs.next()) {
	 segment = new StreetSegment();
	 segment.leftNearNum = rs.getInt("L_REFADDR");
	 segment.leftFarNum = rs.getInt("L_NREFADDR");

	 segment.rightNearNum = rs.getInt("R_REFADDR");
	 segment.rightFarNum = rs.getInt("R_NREFADDR");

	 
	 
	 segment.leftZipcode = rs.getString("L_POSTCODE");
	 segment.rightZipcode = rs.getString("R_POSTCODE");
	 
	 segment.nearX = rs.getDouble("COORD");
	 
	 if (rs.next()) {
	 segment.nearY = rs.getDouble("COORD");
	 }
	 
	 if (rs.next()) {
	 segment.farX = rs.getDouble("COORD");
	 }
	 
	 if (rs.next()) {
	 segment.farY = rs.getDouble("COORD");
	 }
	 }
	 rs.close();
	 ps.close();
	 if (segment == null) {
	 return GEOCODE_FAILED;
	 }

	 //
	 // which of the street segments returned and which side of the the street is the correct one?
	 //
	 boolean onLeft = false;
	 
	 double distanceNear = segment.nearX * segment.nearX + segment.nearY * segment.nearY;
	 double distanceFar = segment.farX * segment.farX + segment.farY * segment.farY;

	 if(distanceNear > distanceFar) {
	 double farX = segment.farX;
	 double farY = segment.farY;
	 segment.farX = segment.nearX;
	 segment.farY = segment.nearY;
	 segment.nearX = farX;
	 segment.nearY = farY;
	 }

	 if((segment.leftNearNum % 2) == (bldgNumber % 2)) {
	 onLeft = true;
	 }

	 //
	 // find the unit vector for the street segment
	 //
	 double dX = segment.farX - segment.nearX;
	 double dY = segment.farY - segment.nearY;
	 double length = Math.sqrt(dX * dX + dY * dY);
	 
	 double slope = dX / dY;
	 
	 double dXunit = dX / length;
	 //System.out.println("dXunit = " + dXunit);
	 double dYunit = dY / length;
	 //System.out.println("dYunit = " + dYunit);
	 //
	 // how far along the segment is the street address?
	 //
	 double perc = 0.0;
	 boolean useFar = false;
	 if (onLeft) {
	 if (segment.leftNearNum < segment.leftFarNum) {
	 perc =
	 ((double) (bldgNumber - segment.leftNearNum)) / Math.abs((double) (segment.leftFarNum - segment.leftNearNum));
	 } else {
	 perc = ((double) (bldgNumber - segment.leftFarNum)) / Math.abs((double) (segment.leftFarNum - segment.leftNearNum));
	 useFar = true;
	 }

	 } else {
	 if (segment.rightNearNum < segment.rightFarNum) {
	 perc =
	 ((double) (bldgNumber - segment.rightNearNum))
	 / Math.abs((double) (segment.rightFarNum - segment.rightNearNum));
	 } else {
	 perc =
	 ((double) (bldgNumber - segment.rightFarNum)) / Math.abs((double) (segment.rightFarNum - segment.rightNearNum));
	 useFar = true;
	 }
	 }
	 //if ((perc == Double.NaN) || (perc == Double.NEGATIVE_INFINITY) || (perc == Double.POSITIVE_INFINITY)) perc = 0.5;
	 if (new Double(perc).isNaN() || (perc == Double.NEGATIVE_INFINITY) || (perc == Double.POSITIVE_INFINITY)) {
	 perc = 0.5;
	 }
	 //System.out.println("perc is " + perc);

	 //
	 // move the endpoints of the street back from the intersection
	 //
	 
	 segment.nearX += dXunit * offsetFromStreet * XmetersToDeg;
	 segment.nearY += dYunit * offsetFromStreet * YmetersToDeg;
	 segment.farX -= dXunit * offsetFromStreet * XmetersToDeg;
	 segment.farY -= dYunit * offsetFromStreet * YmetersToDeg;
	 
	 //
	 // find the X,Y point on the street
	 //
	 double pX, pY;
	 pX = segment.nearX + perc * (segment.farX - segment.nearX);
	 pY = segment.nearY + perc * (segment.farY - segment.nearY);


	 //System.out.println("geocoded point on street is X = " + pX + ", Y = " + pY);
	 //
	 // offset the X,Y point back from the street
	 //
	 //System.out.println(" px: = "+pX+" offset from street = "+offsetFromStreet+" xmetersToDeg = "+XmetersToDeg);
	 //System.out.println(" pY: = "+pY+" offset from street = "+offsetFromStreet+" YmetersToDeg = "+YmetersToDeg);
	 AddressInfo info = address.getAddressInfo();

	 if (info == null) {
	 info = new AddressInfo();
	 }
	 
	 info.setLongitude(pX);
	 info.setLatitude(pY);

	 if (onLeft) {
	 //
	 // rotate unit vector counter-clockwise
	 // find displacement by (-dY, dX)
	 //
	 info.setLongitude(pX - dYunit * offsetFromStreet * XmetersToDeg);
	 info.setLatitude(pY + dXunit * offsetFromStreet * YmetersToDeg);
	 } else {
	 //
	 // rotate unit vector clockwise
	 // find displacement by (dY, -dX)
	 //
	 info.setLongitude(pX + dYunit * offsetFromStreet * XmetersToDeg);
	 info.setLatitude(pY - dXunit * offsetFromStreet * YmetersToDeg);
	 }
	 address.setAddressInfo(info);

	 //System.out.println("point offset from street is X = " + retval.x + ", Y = " + retval.y);

	 return GEOCODE_OK;

	 }
	 */

	private static String compress(String input) {
		StringBuffer buff = new StringBuffer();
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (!Character.isWhitespace(c))
				buff.append(c);
		}
		return buff.toString();
	}

	public ArrayList findApartmentRanges(AddressModel address, Connection conn) throws InvalidAddressException, SQLException {
		//
		// run address through the scrubber
		//
		String streetAddress = null;
		try {
			streetAddress = AddressScrubber.standardizeForUSPS(address.getAddress1());
		} catch (InvalidAddressException iae1) {
			if ((address.getAddress2() == null) || "".equals(address.getAddress2())) {
				throw new InvalidAddressException("Couldn't find a valid street address");
			} else {
				streetAddress = AddressScrubber.standardizeForUSPS(address.getAddress2());
				//
				// swap streets so the real one is in the first position
				//
				String s1 = address.getAddress1();
				String s2 = address.getAddress2();
				address.setAddress1(s2);
				address.setAddress2(s1);
			}
		}
		//
		// separate street number and street name
		//
		int numBreak = streetAddress.indexOf(' ');
		String streetName = streetAddress.substring(numBreak + 1, streetAddress.length());
		String bldgNumber = streetAddress.substring(0, numBreak);

		//EnumAddressVerificationResult result = EnumAddressVerificationResult.NOT_VERIFIED;

		String streetNormal = compress(streetName);

		ArrayList ranges = new ArrayList();
		
		String[] zipCodeList = getZipCodeFromAddress(address);
		String zipCode = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		if(zipCodeList != null) {
			int intLength = zipCodeList.length;
			for(int intCount=0; intCount<intLength; intCount++) {
				zipCode = zipCodeList[intCount];
				ps = conn
						.prepareStatement("SELECT apt_num_low, apt_num_high, address_type FROM (SELECT apt_num_low, apt_num_high, address_type FROM dlv.zipplusfour WHERE zipcode=? AND street_normal=? AND bldg_num_low=? AND bldg_num_high=? "
								+ "UNION SELECT apt_num_low, apt_num_high, address_type FROM dlv.zipplusfour_exceptions WHERE zipcode=? AND scrubbed_address =?) "
								+ "ORDER BY LPAD(apt_num_low,10,'0')");
				ps.setString(1, zipCode);
				ps.setString(2, streetNormal);
				ps.setString(3, bldgNumber);
				ps.setString(4, bldgNumber);
				ps.setString(5, zipCode);
				ps.setString(6, streetAddress);
				rs = ps.executeQuery();
				while (rs.next()) {
					ranges.add(new DlvApartmentRange(rs.getString(1), rs.getString(2), rs.getString(3)));
				}
				rs.close();
				ps.close();
				if(ranges.size() > 0) {
					break;
				}
			}			
		}
		return ranges;

	}

	private static final String BLDG_TO_ADD_APT = "select bldg_num_low, bldg_num_high, street_pre_dir, street_name, street_suffix, street_post_dir, zipcode, plusfour, street_normal "
		+ "from dlv.zipplusfour "
		+ "where (lpad(?,10,'0') between lpad(bldg_num_low,10,'0') and lpad(bldg_num_high,10,'0')) and zipcode = ? and street_normal = ? ";

	public void addApartment(Connection conn, AddressModel address) throws SQLException, InvalidAddressException {
		String streetAddress = AddressScrubber.standardizeForUSPS(address.getAddress1());
		int numBreak = streetAddress.indexOf(' ');

		if (numBreak == -1) {
			throw new InvalidAddressException(streetAddress + " is an invalid addres");
		}
		String streetName = streetAddress.substring(numBreak + 1, streetAddress.length());
		String bldgNumber = streetAddress.substring(0, numBreak);
		String streetNormal = compress(streetName);

		PreparedStatement ps = conn.prepareStatement(BLDG_TO_ADD_APT);
		ps.setString(1, bldgNumber);
		ps.setString(2, address.getZipCode());
		ps.setString(3, streetNormal);

		ResultSet rs = ps.executeQuery();

		if (rs.next()) {
			PreparedStatement ps1 = conn
				.prepareStatement("insert into dlv.zipplusfour (bldg_num_low, bldg_num_high, street_pre_dir, street_name, street_suffix, street_post_dir, apt_num_low, apt_num_high, zipcode, plusfour, street_normal) values(?,?,?,?,?,?,?,?,?,?,?)");
			ps1.setString(1, bldgNumber);
			ps1.setString(2, bldgNumber);
			ps1.setString(3, rs.getString("STREET_PRE_DIR"));
			ps1.setString(4, rs.getString("STREET_NAME"));
			ps1.setString(5, rs.getString("STREET_SUFFIX"));
			ps1.setString(6, rs.getString("STREET_POST_DIR"));
			ps1.setString(7, address.getApartment().toUpperCase());
			ps1.setString(8, address.getApartment().toUpperCase());
			ps1.setString(9, rs.getString("ZIPCODE"));
			ps1.setString(10, rs.getString("PLUSFOUR"));
			ps1.setString(11, rs.getString("STREET_NORMAL"));
			if (ps1.executeUpdate() != 1) {
				throw new SQLException("Unable to add apartment# to zipplusfour");
			}
			ps1.close();
		} else {
			rs.close();
			ps.close();
			throw new InvalidAddressException("The given address does not exist in zipplusfour data");
		}
		rs.close();
		ps.close();
	}

	public void addExceptionAddress(Connection conn, ExceptionAddress ex) throws SQLException {
		PreparedStatement ps = conn
			.prepareStatement("Insert into dlv.ZIPPLUSFOUR_EXCEPTIONS (scrubbed_address, apt_num_low, apt_num_high, zipcode, address_type, reason, created_by, modtime, county, state,city,id) values (?,?,?,?,?,?,?,sysdate,?,?,?,?)");

		ps.setString(1, ex.getStreetAddress().toUpperCase());
		ps.setString(2, ex.getAptNumLow().toUpperCase());
		ps.setString(3, ex.getAptNumHigh().toUpperCase());
		ps.setString(4, ex.getZip());
		ps.setString(5, ex.getAddressType().getName());
		ps.setString(6, ex.getReason().getName());
		ps.setString(7, ex.getUserId());
		ps.setString(8, ex.getCounty());
		ps.setString(9, ex.getState());
		ps.setString(10, ex.getCity().toUpperCase());
		ps.setString(11, SequenceGenerator.getNextId(conn, "CUST"));

		if (ps.executeUpdate() != 1) {
			throw new SQLException("Unable to add address to exceptions table");
		}
		ps.close();
	}

	private boolean checkAddressExceptions(Connection conn, String streetAddress, AddressModel address, AddressInfo info)
		throws SQLException {
		boolean found = false;
		String sql = "SELECT * FROM DLV.ZIPPLUSFOUR_EXCEPTIONS "
			+ "WHERE REPLACE(SCRUBBED_ADDRESS, ' ') = REPLACE(?, ' ') "
			+ "AND LPAD(REPLACE(REPLACE(UPPER(CONCAT(?, 1)),'-'),'PH'),30,'0') >= LPAD(REPLACE(REPLACE(UPPER(CONCAT(apt_num_low, 1)),'-'),'PH'),30,'0') "
			+ "AND LPAD(REPLACE(REPLACE(UPPER(CONCAT(?, 1)),'-'),'PH'),30,'0') <= LPAD(REPLACE(REPLACE(UPPER(CONCAT(apt_num_high, 1)),'-'),'PH'),30,'0') "
			+ "AND ZIPCODE = ? ";

		PreparedStatement ps = conn.prepareStatement(sql);

		String apt = address.getApartment();
		String city = null;

		ps.setString(1, streetAddress.toUpperCase());
		ps.setString(2, apt != null ? apt.toUpperCase().trim() : null);
		ps.setString(3, apt != null ? apt.toUpperCase().trim() : null);
		ps.setString(4, address.getZipCode());

		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			city = rs.getString("CITY");
			if(city != null) {
				address.setCity(StringUtils.capitaliseAllWords(city.toLowerCase()));
			}
			info.setAddressType(EnumAddressType.getEnum(rs.getString("ADDRESS_TYPE")));
			info.setCounty(rs.getString("COUNTY"));
			address.setAddressInfo(info);
			found = true;
		}
		rs.close();
		ps.close();

/*		System.out.println("Checked exceptions table for address: "
			+ streetAddress
			+ " apt: "
			+ apt
			+ " zip: "
			+ address.getZipCode()
			+ "    found = "
			+ found);*/

		return found;
	}

	private final static String GEOCODE_EXCEPTIONS = "select * from dlv.geocode_exceptions where scrubbed_address = ? and zipcode = ?";

	private String checkGeocodeExceptions(AddressModel address, Connection conn) throws SQLException, InvalidAddressException {
		String streetAddress = AddressScrubber.standardizeForGeocode(address.getAddress1());

		String result = GEOCODE_FAILED;
		PreparedStatement ps = conn.prepareStatement(GEOCODE_EXCEPTIONS);

		ps.setString(1, streetAddress);
		ps.setString(2, address.getZipCode());

		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			AddressInfo info = address.getAddressInfo() == null ? new AddressInfo() : address.getAddressInfo();
			info.setLatitude(rs.getDouble("LATITUDE"));
			info.setLongitude(rs.getDouble("LONGITUDE"));
			address.setAddressInfo(info);
			result = GEOCODE_OK;

		}
		rs.close();
		ps.close();

		return result;
	}

	private final static String SEARCH_GEOCODE_EXCEPTION = "select SCRUBBED_ADDRESS, ZIPCODE, LATITUDE, LONGITUDE from dlv.geocode_exceptions where scrubbed_address LIKE ? and zipcode LIKE ? ";
	
	public List searchGeocodeException(Connection conn, ExceptionAddress ex) throws SQLException {

			PreparedStatement ps = conn.prepareStatement(SEARCH_GEOCODE_EXCEPTION);
			ps.setString(1, "".equals(ex.getScrubbedAddress()) ? "%" : "%" + ex.getScrubbedAddress().toUpperCase() + "%");
			ps.setString(2, "".equals(ex.getZip()) ? "%" : ex.getZip());
			ResultSet rs = ps.executeQuery();
			
			List exceptions = new ArrayList();
			while(rs.next()){
				ExceptionAddress ea = new ExceptionAddress();
				ea.setScrubbedAddress(rs.getString("SCRUBBED_ADDRESS"));
				ea.setZip(rs.getString("ZIPCODE"));
				ea.setLatitude(rs.getDouble("LATITUDE"));
				ea.setLongitude(rs.getDouble("LONGITUDE"));				
				exceptions.add(ea);
			}
			rs.close();
			ps.close();
			return exceptions;
	}

	private final static String DELETE_GEOCODE_EXCEPTION = "delete from dlv.geocode_exceptions where scrubbed_address = ? and zipcode = ? ";
	
	public void deleteGeocodeException(Connection conn, ExceptionAddress ex) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(DELETE_GEOCODE_EXCEPTION);
		ps.setString(1, ex.getScrubbedAddress().toUpperCase());
		ps.setString(2, ex.getZip());
		if (ps.executeUpdate() !=1) {
			throw new SQLException ("Address: "+ex.getScrubbedAddress()+" and zipcode: "+ex.getZip()+" does not exist in geocode_exceptions table");
		}
		ps.close();			
	}
	
	private final static String ADD_GEOCODE_EXCEPTION = "insert into dlv.geocode_exceptions (scrubbed_address, zipcode, latitude, longitude, created_by) values (?,?,?,?,?)";
	
	public void addGeocodeException(Connection conn, ExceptionAddress ex, String userId) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(ADD_GEOCODE_EXCEPTION);
		ps.setString(1, ex.getScrubbedAddress().toUpperCase());
		ps.setString(2, ex.getZip());
		ps.setDouble(3, ex.getLatitude());
		ps.setDouble(4, ex.getLongitude());
		ps.setString(5, userId);

		if (ps.executeUpdate() != 1) {
			throw new SQLException("Unable to add address to geocode_exceptions table");
		}
		ps.close();

	}

	private final static String COUNTY = "select county from dlv.city_state where city=? and state=?";

	public String getCounty(Connection conn, String city, String state) throws SQLException {
		String county = null;

		PreparedStatement ps = conn.prepareStatement(COUNTY);
		ps.setString(1, city.toUpperCase());
		ps.setString(2, state.toUpperCase());
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			county = rs.getString("COUNTY");
		}

		rs.close();
		ps.close();

		return county;
	}

	private final static String COUNTIES_STATE = "select county from dlv.city_state where state = ? group by county";

	public List getCountiesByState(Connection conn, String stateAbbrev) throws SQLException {
		List counties = new ArrayList();

		PreparedStatement ps = conn.prepareStatement(COUNTIES_STATE);
		ps.setString(1, stateAbbrev);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			counties.add(rs.getString("COUNTY"));
		}

		rs.close();
		ps.close();

		return counties;
	}
	
	private final static String CITY_ZIP = "select city from dlv.zipplusfour zpf, dlv.city_state cs " + 
											"where zpf.CITY_STATE_KEY = cs.CITY_STATE_KEY " +
											"and zipcode = ? " +
											"group by city";
	public List lookupCitiesByZip(Connection conn, String zipcode) throws SQLException {
		List cities = new ArrayList();
		
		PreparedStatement ps = conn.prepareStatement(CITY_ZIP);
		ps.setString(1, zipcode);
		ResultSet rs = ps.executeQuery();
		
		while(rs.next()){
			cities.add(rs.getString("CITY").toUpperCase());
		}
		
		return cities;
	}

	private String stripImproperDir(String bldg) {
		String lower = bldg.toLowerCase();
		if (lower.indexOf("east") != -1) {
			return bldg.substring(0, lower.indexOf("east"));
		}

		if (lower.indexOf("west") != -1) {
			return bldg.substring(0, lower.indexOf("west"));
		}

		if (lower.indexOf("north") != -1) {
			return bldg.substring(0, lower.indexOf("north"));
		}

		if (lower.indexOf("south") != -1) {
			return bldg.substring(0, lower.indexOf("south"));
		}

		return bldg;
	}

	private String cleanBldgNum(String bldgNum) {
		StringBuffer clean = new StringBuffer();
		bldgNum = bldgNum.replaceAll("-", "");
		char[] chars = bldgNum.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (Character.isDigit(chars[i])) {
				clean.append(chars[i]);
			} else {
				break;
			}
		}

		return clean.toString();
	}

	//fix for verifying addresses like PO Boxes that zpf has no idea about
	private EnumAddressVerificationResult zipplusfourGeocodeHack(
		AddressModel address,
		Connection conn,
		EnumAddressVerificationResult result) throws SQLException {
		try {			
			if (GEOCODE_OK.equals(geocode(address, false, conn))) {
				String county = getCounty(conn, address.getCity(), address.getState());
				if(county == null){
					return EnumAddressVerificationResult.NOT_VERIFIED;
				}
				address.setCity(StringUtils.capitaliseAllWords(address.getCity().toLowerCase()));
				address.getAddressInfo().setCounty(county);
				address.getAddressInfo().setAddressType(EnumAddressType.STREET);
				
				//validate city
				if(lookupCitiesByZip(conn, address.getZipCode()).contains(address.getCity().toUpperCase())){
					return EnumAddressVerificationResult.ADDRESS_OK;
				}
			}
		} catch (InvalidAddressException e) {
			//address is still bad do nothing
		}
		return result;
	}
	
	private final static String ZPF_EXCEPTION_SEARCH = 
		"select  ID, SCRUBBED_ADDRESS, APT_NUM_LOW, APT_NUM_HIGH, ZIPCODE, ADDRESS_TYPE, REASON, CREATED_BY, MODTIME, COUNTY, STATE, CITY"
		+" from dlv.zipplusfour_exceptions where scrubbed_address like ? and zipcode like ? order by scrubbed_address";
	
	public List searchExceptionAddresses(Connection conn, ExceptionAddress address) throws SQLException{
		PreparedStatement ps = conn.prepareStatement(ZPF_EXCEPTION_SEARCH);
		ps.setString(1, "".equals(address.getStreetAddress()) ? "%" : "%" + address.getStreetAddress().toUpperCase() + "%");
		ps.setString(2, "".equals(address.getZip()) ? "%" : address.getZip());
		ResultSet rs = ps.executeQuery();
		
		List exceptions = new ArrayList();
		while(rs.next()){
			ExceptionAddress ea = new ExceptionAddress();
			ea.setId(rs.getString("ID"));
			ea.setStreetAddress(rs.getString("SCRUBBED_ADDRESS"));
			ea.setAptNumLow(rs.getString("APT_NUM_LOW"));
			ea.setAptNumHigh(rs.getString("APT_NUM_HIGH"));
			ea.setCity(rs.getString("CITY"));
			ea.setState(rs.getString("STATE"));
			ea.setZip(rs.getString("ZIPCODE"));
			ea.setCounty(rs.getString("COUNTY"));
			ea.setReason(EnumAddressExceptionReason.getEnum(rs.getString("REASON")));
			ea.setAddressType(EnumAddressType.getEnum(rs.getString("ADDRESS_TYPE")));
			
			exceptions.add(ea);
		}
		rs.close();
		ps.close();
		
		return exceptions;
	}
	
	public void deleteAddressException(Connection conn, String id) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("DELETE FROM DLV.ZIPPLUSFOUR_EXCEPTIONS WHERE ID = ?");
		
		ps.setString(1, id);
		ps.executeUpdate();
		
		ps.close();
		
	}
	
	private final static String STATE_BY_ZIP = "select cs.STATE, cs.COUNTY from dlv.zipplusfour zpf, dlv.city_state cs where zipcode = ? " +
											   " and zpf.CITY_STATE_KEY = cs.CITY_STATE_KEY group by state, county order by county";
    public StateCounty lookupStateCountyByZip(Connection conn, String zip) throws SQLException{
    	PreparedStatement ps = conn.prepareStatement(STATE_BY_ZIP);
    	ps.setString(1, zip);
    	ResultSet rs = ps.executeQuery();
    	
    	if(rs.next()){
    		return new StateCounty(rs.getString("STATE"), rs.getString("COUNTY"));
    	}
    	
    	rs.close();
    	ps.close();
    	
    	return null;
    }
    
    private String[] getZipCodeFromAddress(AddressModel addressModel) {    	
    	StringBuffer tmpBufZipCode = new StringBuffer();
    	tmpBufZipCode.append(addressModel.getZipCode());
    	String tmpZipCode = FDStoreProperties.getAlternateZipcodeForGeocode(addressModel.getZipCode());
    	if(!StringUtil.isEmpty(tmpZipCode)) {
    		tmpBufZipCode.append(",").append(tmpZipCode);
    	}    	
    	return StringUtil.decodeStrings(tmpBufZipCode.toString());
    }
}