package com.freshdirect.payment.ejb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Category;

import com.freshdirect.enums.EnumDAOI;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.payment.BillingCountryInfo;
import com.freshdirect.payment.BillingRegionInfo;


public class BillingCountryDAO implements EnumDAOI{

	private static final String QUERY="SELECT bc.code, bc.name, br.code rc , br.name rn,bc.zip_check_regex,br.code_external FROM "+
	                                  " cust.country bc, cust.region br "+
	                                  "	WHERE bc.code=br.country_code ORDER BY bc.name, br.name";
	
	private static Category		LOGGER	= LoggerFactory.getInstance( BillingCountryDAO.class );
	private static final long serialVersionUID = -2037054694950130536L;

	public List<BillingCountryInfo> loadAll(Connection conn) throws SQLException {
		LOGGER.debug("loadAll()--start");
		PreparedStatement ps = conn.prepareStatement(QUERY);
		ResultSet rs = ps.executeQuery();

		List<BillingCountryInfo> l = new ArrayList<BillingCountryInfo>();
		String activeCountry="";
		String _countryName="";
		String _zipCheck="";
		
		String countryCode="";
		String countryName="";
		String regionCode="";
		String regionName="";
		String zipCheck="";
		String regionCodeExt="";
		List<BillingRegionInfo> regions=new ArrayList<BillingRegionInfo>();
		while (rs.next()) {
			countryCode= rs.getString(1);
			countryName = rs.getString(2);
			regionCode= rs.getString(3);
			regionName = rs.getString(4);
			zipCheck=rs.getString(5);
			regionCodeExt=rs.getString(6);
			if(activeCountry=="" ) {
				activeCountry=countryCode;
				_countryName=countryName;
				_zipCheck=zipCheck;
				regions=new ArrayList<BillingRegionInfo>();
				regions.add(new BillingRegionInfo(countryCode,regionCode,regionName,regionCodeExt));
			} else if(activeCountry.equals(countryCode)) {
				regions.add(new BillingRegionInfo(countryCode,regionCode,regionName,regionCodeExt));
			} else {
				Collections.sort(regions,BillingRegionInfo.COMPARE_BY_NAME);
				l.add(new BillingCountryInfo(activeCountry, _countryName,Collections.unmodifiableList(regions),_zipCheck));
				regions=new ArrayList<BillingRegionInfo>();
				regions.add(new BillingRegionInfo(countryCode,regionCode,regionName,regionCodeExt));
				activeCountry=countryCode;
				_countryName=countryName;
				_zipCheck=zipCheck;
			}
			
		}
		if(regions.size()>0) {
			Collections.sort(regions,BillingRegionInfo.COMPARE_BY_NAME);
			l.add(new BillingCountryInfo(activeCountry, _countryName,Collections.unmodifiableList(regions),_zipCheck));
		}

		rs.close();
		ps.close();
		//Collections.sort(l,BillingCountryInfo.COMPARE_BY_NAME);
		LOGGER.debug("loadAll()--end");
		return l;
	}

}
