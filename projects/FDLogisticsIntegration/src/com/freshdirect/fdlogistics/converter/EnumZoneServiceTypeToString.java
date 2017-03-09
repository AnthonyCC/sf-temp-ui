package com.freshdirect.fdlogistics.converter;

import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;

import com.freshdirect.customer.EnumZoneServiceType;

public class EnumZoneServiceTypeToString extends
		CustomConverter<EnumZoneServiceType, String> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see ma.glasnost.orika.Converter#convert(java.lang.Object,
	 * ma.glasnost.orika.metadata.Type, ma.glasnost.orika.MappingContext)
	 */
	@Override
	public String convert(EnumZoneServiceType source,
			Type<? extends String> destinationType,
			MappingContext mappingContext) {
		String zoneServiceType = null;
		if (source != null) {
			zoneServiceType = source.getName();
		}
		return zoneServiceType;
	}

}
