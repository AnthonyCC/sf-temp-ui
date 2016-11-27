package com.freshdirect.dataloader.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.freshdirect.erp.EnumAlcoholicContent;


/**
 * @author kkanuganti
 *
 */
@SuppressWarnings("javadoc")
public final class FDSapHelperUtils
{

	public static final Logger LOG = Logger.getLogger(FDSapHelperUtils.class.getName());

	/**
	 * Master default zone for price row
	 */
	public static final String MASTER_DEFAULT_ZONE = "100000";
	public static final String MASTER_REGION_ID = "1000";

	public static final String MASTER_SALES_ORGANIZATION_ID = "0001";
	public static final String MASTER_DISTRIBUTION_CHANNEL = "01";
	public static final String MASTER_DIVISION = "01";
	
	public static final String DEFAULT_BILLING_STREET = "23-30 borden ave";
	public static final String DEFAULT_BILLING_TOWN = "Long Island City";
	public static final String DEFAULT_BILLING_POSTALCODE = "11101";
	public static final String DEFAULT_BILLING_STATE = "NY";
	public static final String DEFAULT_BILLING_COUNTRY = "US";

	public static final String PROMO_PRICE_ROW_INDICATOR = "Promo"; //PBBS
	
	public static final Date CURRENT_DATE = new Date();
	public static final java.util.Date FUTURE_DATE = new java.util.GregorianCalendar(3000, java.util.Calendar.JANUARY, 1).getTime();

	// Pre-defined material numbers provided by SAP for different charges / promotions
	public static final String PROMOTION_MATERIAL_NO = "000000000000009999";
	public static final String DELIVERY_CHARGE_MATERIAL_NO = "000000000000008888";
	public static final String CC_DECLINED_MATERIAL_NO = "000000000000002222";
	public static final String PHONE_MATERIAL_NO = "000000000000001111";
	public static final String FUELSURCHARGE_MATERIAL_NO = "000000000000004444";
	public static final String PREMIUMDELIVERY_CHARGE_MATERIAL_NO = "000000000000008888";

	public static final String DEFAULT_VENDOR_AFFILIATE = "FD";

	private final static Map<EnumAlcoholicContent, String> alcoholTypeToVendorAffiliate = new HashMap<EnumAlcoholicContent, String>();

	public static final String MATERIAL_NUMBER = "MATERIAL_NUMBER";

	public static final String CLASS = "CLASS";

	public static final String CHARACTERISTIC_NAME = "CHARACTERISTIC_NAME";

	public static final String CHARACTERISTIC_VALUE = "CHARACTERISTIC_VALUE";

	static
	{
		alcoholTypeToVendorAffiliate.put(EnumAlcoholicContent.BEER, "FD");
		alcoholTypeToVendorAffiliate.put(EnumAlcoholicContent.BC_WINE, "BC");
		alcoholTypeToVendorAffiliate.put(EnumAlcoholicContent.USQ_WINE, "USQ");
		alcoholTypeToVendorAffiliate.put(EnumAlcoholicContent.FD_WINE, "FDW");
	}

	/**
	 * @param materialGroup
	 * @return VendorAffiliateEnum
	 */
	public static EnumAlcoholicContent convertToAlcoholContentType(String materialGroup)
	{
		if (materialGroup == null)
		{
			materialGroup = "";
		}
		materialGroup = materialGroup.toUpperCase();
		if (materialGroup.startsWith("B") || materialGroup.startsWith("A"))
		{
			return EnumAlcoholicContent.BEER;
		}
		else if (materialGroup.startsWith("W"))
		{
			return EnumAlcoholicContent.BC_WINE;
		}
		else if (materialGroup.startsWith("U"))
		{
			return EnumAlcoholicContent.USQ_WINE;
		}
		else if (materialGroup.startsWith("V"))
		{
			return EnumAlcoholicContent.FD_WINE;
		}
		return null;
	}

	/**
	 * @param obj
	 * @return Object
	 */
	public static Object trim(final Object obj)
	{
		if (obj == null || obj.toString().trim().isEmpty())
		{
			return obj;
		}
		else
		{
			return obj.toString().trim();
		}
	}

	/**
	 * @param str
	 * @return String
	 */
	public static String getString(final String str)
	{
		if (StringUtils.isNotEmpty(str))
		{
			return str.trim();
		}
		return str;
	}

	public static int getInt(final String fieldName)
	{
		final String s = getString(fieldName);

		if (s == null || "".equals(s))
		{
			return 0;
		}
		try
		{
			return Integer.parseInt(s);
		}
		catch (final NumberFormatException nfe)
		{
			LOG.error("Unable to read field \"" + fieldName + "\" as an integer");
		}
		return 0;
	}

	public static double getDouble(final String fieldName)
	{
		final String s = getString(fieldName);

		if (s == null || "".equals(s))
		{
			return 0.0;
		}
		try
		{
			return Double.parseDouble(s);
		}
		catch (final NumberFormatException nfe)
		{
			LOG.error("Unable to read field \"" + fieldName + "\" as a double");
		}
		return 0;
	}


	/**
	 * @param list
	 * @param maxListSize
	 * @return List<List<?>>
	 */
	public static List<List<?>> splitList(final List<?> list, final int maxListSize)
	{
		final List<List<?>> splittedList = new ArrayList<List<?>>();
		int itemsRemaining = list.size();
		int start = 0;

		while (itemsRemaining != 0)
		{
			final int end = itemsRemaining >= maxListSize ? (start + maxListSize) : (start + itemsRemaining);

			splittedList.add(list.subList(start, end));

			final int sizeOfFinalList = end - start;
			itemsRemaining = itemsRemaining - sizeOfFinalList;
			start = start + sizeOfFinalList;
		}

		return splittedList;
	}

	/**
	 * @param ls
	 * @param maxListSize
	 * @return <T>List<List<T>>
	 */
	public static <T> List<List<T>> chopListIntoParts(final List<T> ls, final int maxListSize)
	{
		final List<List<T>> lsParts = new ArrayList<List<T>>();
		final int iChunkSize = ls.size() / maxListSize;
		int iLeftOver = ls.size() % maxListSize;
		int iTake = iChunkSize;

		for (int i = 0, iT = ls.size(); i < iT; i += iTake)
		{
			if (iLeftOver > 0)
			{
				iLeftOver--;

				iTake = iChunkSize + 1;
			}
			else
			{
				iTake = iChunkSize;
			}

			lsParts.add(new ArrayList<T>(ls.subList(i, Math.min(iT, i + iTake))));
		}

		return lsParts;
	}

	public static Double convertNumberToDouble(final Number sourceValue)
	{
		return convertNumber(sourceValue, new ConversionStrategy<Double>()
		{
			@Override
			public Double convert(final Number number)
			{
				return Double.valueOf(number.doubleValue());
			}
		});
	}

	public static Float convertNumberToFloat(final Number sourceValue)
	{
		return convertNumber(sourceValue, new ConversionStrategy<Float>()
		{
			@Override
			public Float convert(final Number number)
			{
				return Float.valueOf(number.floatValue());
			}
		});
	}

	public static Integer convertNumberToInteger(final Number sourceValue)
	{
		return convertNumber(sourceValue, new ConversionStrategy<Integer>()
		{
			@Override
			public Integer convert(final Number number)
			{
				return Integer.valueOf(number.intValue());
			}
		});
	}

	public static Long convertNumberToLong(final Number sourceValue)
	{
		return convertNumber(sourceValue, new ConversionStrategy<Long>()
		{
			@Override
			public Long convert(final Number number)
			{
				return Long.valueOf(number.longValue());
			}
		});
	}

	private static interface ConversionStrategy<N extends Number>
	{
		N convert(Number number);
	}

	private static <N extends Number> N convertNumber(final Number number, final ConversionStrategy<N> strategy)
	{
		N value = null;
		if (number != null)
		{
			value = strategy.convert(number);
		}
		return value;
	}

	public static Calendar toCalendar(final Date date)
	{
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}

	public static Date truncate(final Date date)
	{
		Calendar cal = toCalendar(date);
		cal = truncate(cal);
		return cal.getTime();
	}

	public static Calendar truncate(final Calendar cal)
	{
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}

}
