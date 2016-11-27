package com.freshdirect.webapp.taglib.fdstore;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Category;

import com.freshdirect.customer.EnumUnattendedDeliveryFlag;
import com.freshdirect.customer.ErpAddressModel;
import com.freshdirect.fdlogistics.model.FDDeliveryZoneInfo;
import com.freshdirect.fdlogistics.model.FDInvalidAddressException;
import com.freshdirect.fdstore.FDDeliveryManager;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.webapp.taglib.AbstractGetterTag;

/**
 * Controller/getter tag for unattended deliveries.
 * 
 * The tag will return a delivery zone info; only if the address is in a zone where unattended delivery is enabled OR the user has set unattended delivery preferences for this
 * zone.
 * 
 * If the address is null, the tag will simply skip the body.
 * 
 * The address may be given az an id (primary key's id) or as an instance of ErpAddressModel.
 * 
 * The date for checking, by default is "now", but it can be overridden by specifying another date.
 * 
 * @author istvan
 *
 */
public class UnattendedDeliveryTag extends AbstractGetterTag {

    private static Category LOGGER = LoggerFactory.getInstance(UnattendedDeliveryTag.class);

    private static final long serialVersionUID = 2975587401864977813L;

    private String addressId = null;
    private boolean newAddress = false;

    public void setAddressId(String addressId) {
        this.addressId = addressId;
        if (this.addressId == null)
            newAddress = true;
    }

    private boolean checkUserOptions = true;

    public void setCheckUserOptions(boolean checkUserOptions) {
        this.checkUserOptions = checkUserOptions;
    }

    private ErpAddressModel address = null;

    public void setAddress(ErpAddressModel address) {
        this.address = address;
        if (this.address == null)
            newAddress = true;
    }

    private Date date = null;

    /**
     * 
     * @param date
     *            to check zone info
     */
    public void setDate(Date date) {
        this.date = date;
    }

    private static FDDeliveryZoneInfo getAddressZone(ErpAddressModel thisAddress, Date date) throws FDResourceException, FDInvalidAddressException {
        return FDDeliveryManager.getInstance().getZoneInfo(thisAddress, date != null ? date : new Date(), null, null, thisAddress.getCustomerId());
    }

    private static boolean checkZone(FDDeliveryZoneInfo zoneInfo, ErpAddressModel thisAddress, boolean checkUserOptions) {
        if (zoneInfo != null && thisAddress != null) {
            if (zoneInfo.isUnattended() || (checkUserOptions && !EnumUnattendedDeliveryFlag.NOT_SEEN.equals(thisAddress.getUnattendedDeliveryFlag()))) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkUnattendedDelivery(ErpAddressModel address) {
        boolean result = false;
        try {
            if (address != null) {
                FDDeliveryZoneInfo zoneInfo = getAddressZone(address, null);
                result = (checkZone(zoneInfo, address, true));
            }
        } catch (FDResourceException e) {
            LOGGER.warn("FDResourceException during UnattendedDelivery availability check : " + e.getMessage());
        } catch (FDInvalidAddressException e) {
            LOGGER.info("FDInvalidAddressException during UnattendedDelivery availability check : " + e.getMessage());
        } finally {
            return result;
        }
    }

    protected Object getResult() throws JspException {

        // for new addresses, do not bother checking zone
        // the address is not scrubbed
        if (this.newAddress)
            return null;

        try {

            if (address != null) {
                // don't care whose address
                FDDeliveryZoneInfo zoneInfo = getAddressZone(address, date);
                if (checkZone(zoneInfo, address, checkUserOptions))
                    return zoneInfo;

            } else if (addressId != null) {

                // assumes current user's address
                HttpSession session = pageContext.getSession();
                FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);

                Collection shippingAddresses = FDCustomerManager.getShipToAddresses(user.getIdentity());

                for (Iterator i = shippingAddresses.iterator(); i.hasNext();) {
                    ErpAddressModel thisAddress = (ErpAddressModel) i.next();
                    if (addressId.equals(thisAddress.getPK().getId())) {
                        FDDeliveryZoneInfo zoneInfo = getAddressZone(thisAddress, date);
                        if (checkZone(zoneInfo, thisAddress, checkUserOptions))
                            return zoneInfo;
                    }
                }
            } else {
                throw new JspException("Neither addressId nor address was provided");
            }
        } catch (FDResourceException re) {
            LOGGER.warn("FDResourceException in UnattendedDeliveryTag.getResult() while getting zone info : " + re.getMessage());
            throw new JspException(re);
        } catch (FDInvalidAddressException iae) {
            LOGGER.info("FDInvalidAddressException in UnattendedDeliveryTag.getResult() while getting zone info : " + iae.getMessage());
            // if an address fails because of geocoding, it will be treated as if it were not
            // enabled for unattended delivery
            return null;
        }

        return null;
    }

    public static class TagEI extends AbstractGetterTag.TagEI {

        protected String getResultType() {
            return FDDeliveryZoneInfo.class.getName();
        }
    }

}
