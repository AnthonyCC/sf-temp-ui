package com.freshdirect.transadmin.web.ui.callback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.extremecomponents.table.bean.Column;
import org.extremecomponents.table.core.TableModel;
import org.extremecomponents.util.ExtremeUtils;

import com.freshdirect.transadmin.util.IDateContants;

/**
 * Filter Predicate implementation which enable date comparison.
 * 
 */
public class DateFilterPredicate implements Predicate, IDateContants {
    
    
    private static final Logger logger = Logger.getLogger(DateFilterPredicate.class);
    
    private TableModel model;

    /**
     * Creates a new DateFilterPredicate object.
     *
     * @param model table model
     */
    public DateFilterPredicate(TableModel model)
    {
        this.model = model;
    }

    /**
     * Use the filter parameters to filter out the table.
     */
    public boolean evaluate(Object bean)
    {
        boolean match = false;

        try
        {
            Iterator iter = model.getColumnHandler().getColumns().iterator();

            while (iter.hasNext())
            {
                Column column = (Column) iter.next();
                String alias = column.getAlias();
                String filterValue = model.getLimit().getFilterSet()
                                          .getFilterValue(alias);

                if (StringUtils.isEmpty(filterValue))
                {
                    continue;
                }

                String property = column.getProperty();
                Object value = PropertyUtils.getProperty(bean, property);

                if (value == null)
                {
                    continue;
                }

                if (column.isDate())
                {
                    Locale locale = model.getLocale();
                    value = ExtremeUtils.formatDate(column.getParse(),
                            column.getFormat(), value, locale);
                }
                else if (column.isCurrency())
                {
                    Locale locale = model.getLocale();
                    value = ExtremeUtils.formatNumber(column.getFormat(),
                            value, locale);
                }

                if (!isSearchMatch(value, filterValue, column.isDate(),
                            column.getFormat(), model.getLocale()))
                {
                    match = false; // as soon as fail just short circuit

                    break;
                }

                match = true;
            }
        }
        catch (Exception e)
        {
            logger.error("FilterPredicate.evaluate() had problems", e);
        }

        return match;
    }

    private boolean isSearchMatch(Object value, String search, boolean isDate,
        String format, Locale locale)
    {
        String valueStr = value.toString().toLowerCase().trim();
        search = search.toLowerCase().trim();

        if (search.startsWith(asterisk) &&
                valueStr.endsWith(StringUtils.replace(search, asterisk,
                        emptyString)))
        {
            return true;
        }
        else if (search.endsWith(asterisk) &&
                valueStr.startsWith(StringUtils.replace(search, asterisk,
                        emptyString)))
        {
            return true;
        }
        else if (isDate)
        {
            DateFormat dateFormat = new SimpleDateFormat(format, locale);

            Date dateToCompare = null;
            Date dateToCompare2 = null;

            try {
                Date dateValue = dateFormat.parse(value.toString());

                String[] result = search.split(DELIM);

                String operator = result[0];
                
                if (operator.equals(LESS_THAN)) {
                    dateToCompare = dateFormat.parse(result[1]);

                    return dateValue.getTime() < dateToCompare.getTime();
                }
                else if (operator.equals(GREATER_THAN)) {
                    dateToCompare = dateFormat.parse(result[1]);

                    return dateValue.getTime() > dateToCompare.getTime();
                }
                else if (operator.equals(LESS_THAN_OR_EQUAL)) {
                    dateToCompare = dateFormat.parse(result[1]);

                    return dateValue.getTime() <= dateToCompare.getTime();
                }
                else if (operator.equals(GREATER_THAN_OR_EQUAL)) {
                    dateToCompare = dateFormat.parse(result[1]);

                    return dateValue.getTime() >= dateToCompare.getTime();
                }
                else if (operator.equals(BETWEEN))  {
                    dateToCompare = dateFormat.parse(result[1]);
                    dateToCompare2 = dateFormat.parse(result[2]);

                    return (dateValue.getTime() >= dateToCompare.getTime()) &&
                    (dateValue.getTime() <= dateToCompare2.getTime());
                }
                else if (operator.equals(NOT_EQUAL)) {
                    dateToCompare = dateFormat.parse(result[1]);

                    return dateValue.getTime() != dateToCompare.getTime();
                }
                else {
                    //return StringUtils.contains(valueStr, search);
                	//Modified to make equals the default condition
                	dateToCompare = dateFormat.parse(result[0]);
                    return dateValue.getTime() == dateToCompare.getTime();
                }
            }
            catch (Exception e) {
                logger.error(
                    "The parse was incorrectly defined for date String [" +
                    search + "].");

                // date comparions failed. Campare it as normal string.
                return StringUtils.contains(valueStr, search);
            }
        }
        else if (StringUtils.contains(valueStr, search))
        {
            return true;
        }

        return false;
    }
    
    
}
