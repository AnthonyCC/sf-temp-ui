package com.freshdirect.cms.ui.model.attributes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.freshdirect.cms.ui.client.nodetree.StringTokenizer;
import com.freshdirect.cms.ui.model.EnumModel;


public class ProductConfigAttribute extends OneToOneAttribute {
	
	public static class ProductConfigParams implements Serializable {
		List<EnumModel>		salesUnits;
		List<EnumAttribute>	configEnums;
				
		public ProductConfigParams() {
		}
		
		public ProductConfigParams( List<EnumModel> salesUnits, List<EnumAttribute>	configEnums ) {
			this.salesUnits = salesUnits;
			this.configEnums = configEnums;
		}
	}
	
	private ProductConfigParams			confParams	= new ProductConfigParams();
	
	private double						quantity;
	private String						salesUnit;
	private Map<String, String>			configOptions;
	
	protected static final Set<String> aTypes = new TreeSet<String>();	
	static {
		aTypes.add( "Sku" );
	}
	
	public ProductConfigAttribute() {
		super();
	}	
	
	public ProductConfigAttribute( ProductConfigAttribute pcAttr ) {
		super( pcAttr );
		this.quantity = pcAttr.quantity;
		this.salesUnit = pcAttr.salesUnit;
		this.configOptions = pcAttr.configOptions == null ? null : new HashMap<String, String>( pcAttr.configOptions );
		List<EnumModel> su = pcAttr.confParams.salesUnits == null ? null : new ArrayList<EnumModel>( pcAttr.confParams.salesUnits );
		List<EnumAttribute> ce = pcAttr.confParams.configEnums == null ? null : new ArrayList<EnumAttribute>( pcAttr.confParams.configEnums );
		this.confParams = new ProductConfigParams( su, ce );
	}

	public ProductConfigParams getConfigParams() {
		return confParams;
	}
	public void setConfigParams( ProductConfigParams confParams ) {
		this.confParams = confParams;
	}

	public double getQuantity() {
		return quantity;
	}
	public void setQuantity( double quantity ) {
		this.quantity = quantity;
	}
	
	public List<EnumModel> getSalesUnits() {
		return confParams.salesUnits;
	}
	public void setSalesUnits( List<EnumModel> salesUnits ) {
		this.confParams.salesUnits = salesUnits;
	}
	
	public String getSalesUnit() {
		return salesUnit;
	}
	public void setSalesUnit( String salesUnit ) {
		this.salesUnit = salesUnit;
	}
	
	public List<EnumAttribute> getConfigEnums() {
		return confParams.configEnums;
	}
	public void setConfigEnums( List<EnumAttribute> configEnums ) {
		this.confParams.configEnums = configEnums;
	}
	public void setConfigOptions( Map<String,String> configurationOptions ) {
		this.configOptions = configurationOptions;
	}	
	public void setConfigOptions( String configurationOptions ) {
		this.configOptions = parseConfigOptions( configurationOptions );
	}	
	public Map<String,String> getConfigOptions() {
		return configOptions;
	}
	public String getConfigOption( String id ) {
		if ( configOptions == null ) {
			return null;
		} else {
			return configOptions.get( id );
		}
	}
	public String getConfigOptionsString() {
		return buildConfigOptionsString( configOptions );
	}
	
	public static Map<String,String> parseConfigOptions( String confOpts ) {
		Map<String,String> options = new HashMap<String, String>();
		if ( confOpts != null ) {
			StringTokenizer tok = new StringTokenizer( confOpts, ",=" );
			while( tok.hasMoreTokens() ) {
				String optionName = tok.nextToken().trim();
				String optionValue = tok.nextToken().trim();
				options.put( optionName, optionValue );
			}
		}
		return options;
	}
	
	public static String buildConfigOptionsString( Map<String,String> confOpts ) {
		StringBuilder sb = new StringBuilder();
		if ( confOpts != null ) {
			for ( Map.Entry<String, String> opt : confOpts.entrySet() ) {
				sb.append( opt.getKey() );			
				sb.append( '=' );			
				sb.append( opt.getValue() );			
				sb.append( ',' );			
			}
			if ( sb.length() > 0 ) {
				sb.deleteCharAt( sb.length()-1 );
			}
		}
		return sb.toString();
	}

	@Override
	public String getType() {
		return "productconfig";
	}
	
	@Override
	public void setAllowedTypes( Set<String> aTypes ) {
		// ignore, must not set allowed types explicitly
	}
	
	@Override
	public Set<String> getAllowedTypes() {
		return aTypes;
	}
		
	@Override
	public String toString() {
		return "ProductConfigAttribute[" + label + ',' + value + ',' + quantity + ',' + salesUnit + ',' + configOptions + ']';
	}
	
	@Override
	public boolean equals( Object obj ) {
		if ( obj instanceof ProductConfigAttribute ) {
			ProductConfigAttribute pcAttr = (ProductConfigAttribute)obj;
			if ( quantity == pcAttr.quantity && 
				( salesUnit == null ? pcAttr.salesUnit == null : salesUnit.equals( pcAttr.salesUnit ) ) && 
				( configOptions == null ? pcAttr.configOptions == null : configOptions.equals( pcAttr.configOptions ) )	) {
				return true;
			}
		}
		return false;
	}		
}
