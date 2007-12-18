/*
 * CustomerInfo.java
 *
 * Created on November 28, 2001, 5:46 PM
 */

package com.freshdirect.webapp.taglib.fdstore;

import java.util.*;
import java.io.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

import org.apache.log4j.*;

import com.freshdirect.common.address.AddressModel;
import com.freshdirect.common.address.PhoneNumber;
import com.freshdirect.customer.*;
import com.freshdirect.fdstore.*;
import com.freshdirect.fdstore.customer.*;
import com.freshdirect.framework.util.MD5Hasher;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.*;
import com.freshdirect.webapp.util.*;

public class CustomerInfoTag extends com.freshdirect.framework.webapp.BodyTagSupport implements SessionName { //, AddressName

    private static Category LOGGER = LoggerFactory.getInstance( CustomerInfoTag.class );

    // Var's declared in the TLD for this tag
    String actionName = null;
    String successPage = null;
    String resultName = null;
    ErpCustomerModel erpCustomer = null;
    FDCustomerModel fdCustomer = null;

    //
    // Customer data variable from the form
    //
    String title = null;
    String firstname = null;
    String middlename = null;
    String lastname = null;
    String homephone = null;
    String ext1 = null;
    String businessphone = null;
    String ext2 = null;
    String cellphone = null;
    String ext3 = null;
    String workdept = null;
    String employeeId = null;
    String emailAddress = null;
    String altEmail = null;
    String password = null;
    String repeatPassword = null;
    String passwordHint = null;
    boolean receiveEmail = false;
    boolean htmlEmail = false;
    String depotCode = null;
    String depotAccessCode = null;

    //
    // Delivery address data variable from the form
    //
    String [] addrFirstName = null;
    String [] addrLastName = null;
    String [] address1 = null;
    String [] address2 = null;
    String [] scrubbedAddress = null;
    String [] apartment = null;
    String [] city = null;
    String [] state = null;
    String [] zipCode = null;
    String [] addrPhone = null;
    String [] instructions = null;
    String [] completeInstructions = null;
    String [] addressPK = null;
    String [] altDelivery = null;
    String [] altAddrFirstName = null;
    String [] altAddrLastName = null;
    String [] altApartment = null;
    String [] altAddrPhone = null;
    String [] altAddrDlvExt = null;

    FDSessionUser user = null;

    public String getSuccessPage() {
        return this.successPage;
    }

    public void setSuccessPage(String sp) {
        this.successPage = sp;
    }

    public String getActionName() {
        return this.actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getResultName() {
        return this.resultName;
    }

    public void setResultName(String resultName) {
        this.resultName = resultName;
    }
    
    public void setErpCustomer(ErpCustomerModel erpCust) {
        this.erpCustomer = erpCust;
    }
    
    public ErpCustomerModel getErpCustomerModel() {
        return this.erpCustomer;
    }
    
    public void setFDCustomer(FDCustomerModel fdCust) {
        this.fdCustomer = fdCust;
    }
    
    public FDCustomerModel getFDCustomer() {
        return this.fdCustomer;
    }



    public int doStartTag() throws JspException {

        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        ActionResult result = new ActionResult();

        HttpSession session = pageContext.getSession();
        user = (FDSessionUser)session.getAttribute(USER);

        //
        // perform any actions requested by the user if the request was a POST
        //
        if (("POST".equalsIgnoreCase(request.getMethod()))) {

            JspMethods.dumpRequest(request);

            try {
                if ("editCustomerAndAddressInfo".equalsIgnoreCase(actionName) ) {
                    getFormData(request, result);
                    validateCustomerInfo(request, result);
                    validateAddresses(request, result);
                    if ( result.isSuccess() ) {
                        updateCustomerInfo(request, result);
                        updateCustomerAddresses(request, result);
                    }
                } else if ("removeDepotMembership".equalsIgnoreCase(actionName)) {
                    this.performRemoveDepotMembership(request, result);
                }
            } catch (FDResourceException ex) {
                LOGGER.warn("FDResourceException while trying to update customer info & addresses", ex);
                result.addError(new ActionError("technical_difficulty", "Could not update profile due to technical difficulty."));
            }
            //
            // redirect to success page if an action was successfully performed and a success page was defined
            //
            if (result.isSuccess() && (successPage != null)) {
                LOGGER.debug("Success, redirecting to: "+successPage);
                HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
                try {
                    response.sendRedirect(response.encodeRedirectURL(successPage));
                    JspWriter writer = pageContext.getOut();
                    writer.close();
                } catch (IOException ioe) {
                    //
                    // if there was a problem redirecting, continue and evaluate/skip tag body as usual
                    //
                    LOGGER.warn("IOException during redirect", ioe);
                }
            }
        }
        //
        // place the result as a scripting variable in the page
        //
        pageContext.setAttribute(resultName, result);
        return EVAL_BODY_BUFFERED;
    }



    /**
     * Retrieves form field data for processing by the tag.
     * @param HttpServletRequest contains the form fields to be retrieved
     */
    private void getFormData(HttpServletRequest request, ActionResult result){
        //
        // Get customer data
        //
        title = request.getParameter("title");
        firstname = request.getParameter("first_name");
        middlename = request.getParameter("middle_name");
        lastname = request.getParameter("last_name");
        homephone = request.getParameter("homephone");
        ext1 = request.getParameter("ext");
        businessphone = request.getParameter("busphone");
        ext2 = request.getParameter("ext2");
        cellphone = request.getParameter("cellphone");
        ext3 = request.getParameter("ext3");
        workdept = request.getParameter("workDepartment");
        employeeId = request.getParameter("employeeId");
        emailAddress = request.getParameter("email");
        altEmail = request.getParameter("alt_email");
        password = request.getParameter("password");
        repeatPassword = request.getParameter("repeat_password");
        passwordHint = request.getParameter("password_hint");
        receiveEmail = request.getParameter("receive_email")==null?false:true;
        htmlEmail = request.getParameter("isSendHTMLEmail")==null?false:true;
        depotCode = request.getParameter("depotCode");
        depotAccessCode = request.getParameter("depotAccessCode");
        //
        // Get delivery address data
        //
        addrFirstName = request.getParameterValues(EnumUserInfoName.DLV_FIRST_NAME.getCode());
        addrLastName = request.getParameterValues(EnumUserInfoName.DLV_LAST_NAME.getCode());
        address1 = request.getParameterValues(EnumUserInfoName.DLV_ADDRESS_1.getCode());
        address2 = request.getParameterValues(EnumUserInfoName.DLV_ADDRESS_2.getCode());
        apartment = request.getParameterValues(EnumUserInfoName.DLV_APARTMENT.getCode());
        city = request.getParameterValues(EnumUserInfoName.DLV_CITY.getCode());
        state = request.getParameterValues(EnumUserInfoName.DLV_STATE.getCode());
        zipCode = request.getParameterValues(EnumUserInfoName.DLV_ZIPCODE.getCode());
        addrPhone = request.getParameterValues(EnumUserInfoName.DLV_HOME_PHONE.getCode());
        instructions = request.getParameterValues(EnumUserInfoName.DLV_DELIVERY_INSTRUCTIONS.getCode());
        addressPK = request.getParameterValues("addressPK");
        //
        // Get ALTERNATE delivery address data
        //
        altDelivery 			= new String [addressPK.length];
        for (int i=0; i<altDelivery.length; i++) {
            altDelivery[i] = request.getParameter(EnumUserInfoName.DLV_ALTERNATE_DELIVERY.getCode() + addressPK[i]);
        }
        altAddrFirstName = request.getParameterValues(EnumUserInfoName.DLV_ALT_FIRSTNAME.getCode());
        altAddrLastName = request.getParameterValues(EnumUserInfoName.DLV_ALT_LASTNAME.getCode());
        altApartment = request.getParameterValues(EnumUserInfoName.DLV_ALT_APARTMENT.getCode());
        altAddrPhone = request.getParameterValues(EnumUserInfoName.DLV_ALT_PHONE.getCode());
        altAddrDlvExt = request.getParameterValues(EnumUserInfoName.DLV_ALT_EXT.getCode());

    } // method getFormData

    /**
     * Checks for customer data validity.
     * @param HttpServletRequest request
     * @param ActionResult result
     */
    private void validateCustomerInfo(HttpServletRequest request, ActionResult result) throws FDResourceException {

        if (emailAddress == null ||  emailAddress.length() < 1 ) {
            result.addError(new ActionError("email", "Invalid or missing email address."));
        }
        if (!result.hasError("email")) {
            if (!com.freshdirect.mail.EmailUtil.isValidEmailAddress(emailAddress)) {
                result.addError(new ActionError("email", "Please make sure your email address is in the format \"you@isp.com.\""));
            }
        }
        if (altEmail != null && !"".equals(altEmail.trim())) {
            if (!com.freshdirect.mail.EmailUtil.isValidEmailAddress(altEmail)) {
                result.addError(new ActionError("alt_email", "Please make sure your email address is in the format \"you@isp.com.\""));
            }
        }
        if (password==null || password.length() < 4) {
            result.addError(new ActionError("password", "Please enter a password that is at least four characters long."));
        }
        else {
            if (repeatPassword==null || repeatPassword.length()<4 || !password.equals(repeatPassword)){
                result.addError(new ActionError("repeat_password", "The password confirmation does not match the passsword"));
            }
        }
        if(lastname==null || lastname.length() < 1) {
            result.addError(new ActionError("last_name", "Invalid or missing last name"));
        }
        if(firstname==null || firstname.length() < 1) {
            result.addError(new ActionError("first_name", "Invalid or missing first name"));
        }
        if(homephone==null || homephone.length()<10) {
            result.addError(new ActionError("homephone", "Invalid or missing home phone number"));
        }
        if(passwordHint==null || passwordHint.length() < 1) {
            result.addError(new ActionError("password_hint", "Invalid or missing password hint"));
        }
        if (user.isDepotUser()) {
            if ((workdept == null) || workdept.length() < 1) {
                result.addError(new ActionError("workDepartment", "Invalid or missing work department."));
            }
            if ((businessphone == null) || businessphone.length() <10) {
                result.addError(new ActionError("busphone", "Invalid or missing work phone number."));
            }
            com.freshdirect.delivery.depot.DlvDepotModel depot = com.freshdirect.fdstore.FDDepotManager.getInstance().getDepot(getUser().getDepotCode());
            if (depot.getRequireEmployeeId()) {
                if ((employeeId == null) || employeeId.length() < 1)
                    result.addError(new ActionError("employeeId", "Invalid or missing employee id."));
            }
        } else if ((depotCode != null) && !"".equals(depotCode)) {
            if ((depotAccessCode == null) || "".equals(depotAccessCode.trim())) {
                result.addError(new ActionError("depotAccessCode", "Invalid or missing registration code."));
            }
            if (!FDDepotManager.getInstance().checkAccessCode(depotCode, depotAccessCode)) {
                result.addError(new ActionError("depotAccessCode", "Wrong registration code."));
            }
            if ((workdept == null) || workdept.length() < 1) {
                result.addError(new ActionError("workDepartment", "Invalid or missing work department."));
            }
            if ((businessphone == null) || businessphone.length() <10) {
                result.addError(new ActionError("busphone", "Invalid or missing work phone number."));
            }
        }

    } // method validateCustomerInfo
    
    private void validateAddresses(HttpServletRequest request, ActionResult result) throws FDResourceException {
        
        scrubbedAddress = new String[addressPK.length];
        
        for (int i = 0; i < addressPK.length; i++) {

            boolean isOK = true;
            
            if(addrFirstName[i]==null || addrFirstName[i].length() < 1) {
                result.addError(new ActionError(addressPK[i]+"firstname", "Invalid or missing first name"));
            }
            if(addrLastName[i]==null || addrLastName[i].length() < 1) {
                result.addError(new ActionError(addressPK[i]+"lastname", "Invalid or missing last name"));
            }
            if(address1[i]==null || address1[i].length() < 1) {
                result.addError(new ActionError(addressPK[i]+"address1", "Invalid or missing street address"));
                isOK = false;
            }
            if(city[i]==null || city[i].length() < 1) {
                result.addError(new ActionError(addressPK[i]+"city", "Invalid or missing city"));
                isOK = false;
            }
            if(state[i]==null || state[i].length() < 2 || !AddressUtil.validateState(state[i])) {
                result.addError(new ActionError(addressPK[i]+"state", "Invalid or missing state abbreviation"));
                isOK = false;
            }
            if(zipCode[i]==null || zipCode[i].length() < 5) {
                result.addError(new ActionError(addressPK[i]+"zipcode", "Invalid or missing zip code"));
                isOK = false;
            }
            if(addrPhone[i]==null || addrPhone[i].length() < 10) {
                result.addError(new ActionError(addressPK[i]+"phone", "Invalid or missing phone number"));
            }
            
            if( EnumDeliverySetting.NEIGHBOR.getDeliveryCode().equalsIgnoreCase(altDelivery[i]) ) {

                if(altAddrFirstName[i]==null || altAddrFirstName[i].length() < 1) {
                    result.addError(new ActionError(addressPK[i]+"altfirstname", "Invalid or missing first name"));
                }
                if(altAddrLastName[i]==null || altAddrLastName[i].length() < 1) {
                    result.addError(new ActionError(addressPK[i]+"altlastname", "Invalid or missing last name"));
                }
                if(altApartment[i]==null || altApartment[i].length() < 1) {
                    result.addError(new ActionError(addressPK[i]+"altapartment", "Invalid or missing apartment number"));
                }
                if(altAddrPhone[i]==null || altAddrPhone[i].length() < 1) {
                    result.addError(new ActionError(addressPK[i]+"altaddrphone", "Invalid or missing phone number"));
                }
            }
            
            if (isOK) {
                //
                // there is enough correct info here to scrub address
                //
                AddressModel dlvAddress =  new AddressModel();
                dlvAddress.setAddress1(address1[i]);
                dlvAddress.setAddress2(address2[i]);
                dlvAddress.setApartment(apartment[i]);
                dlvAddress.setCity(city[i]);
                dlvAddress.setState(state[i]);
                dlvAddress.setZipCode(zipCode[i]);
                int errorsBeforeScrub = result.getErrors().size();
                dlvAddress = AddressUtil.scrubAddress(dlvAddress, result);
                if (result.getErrors().size() == errorsBeforeScrub) {
                    //
                    // if scrub was successful, update some of the stuff we know about the address now
                    //
                    address1[i] = dlvAddress.getAddress1();
                    address2[i] = dlvAddress.getAddress2();
                    scrubbedAddress[i] = dlvAddress.getScrubbedStreet();
                }
            }
        
        }
        
    }

    /**
     * Updates customer information based on a validated ErpCustomerInfoModel constructed from instance variables
     */
    private void updateCustomerInfo(HttpServletRequest request, ActionResult result) throws FDResourceException {
        
        ErpCustomerInfoModel erpCustomerInfo = erpCustomer.getCustomerInfo();

        if (businessphone != null) {
            erpCustomerInfo.setBusinessPhone( new PhoneNumber(businessphone.trim(), ext2) );
        }
        if (middlename != null) {
            erpCustomerInfo.setMiddleName(middlename);
        }
        if (cellphone != null) {
            erpCustomerInfo.setCellPhone( new PhoneNumber(cellphone.trim(), ext3) );
        }
        if (title != null) {
            erpCustomerInfo.setTitle(title);
        }

        erpCustomerInfo.setFirstName(firstname);
        erpCustomerInfo.setLastName(lastname);
        // Only update customerinfo email if a new value has been entered
        if ( !this.emailAddress.equalsIgnoreCase(erpCustomer.getUserId()) )
            erpCustomerInfo.setEmail(emailAddress);
        erpCustomerInfo.setHomePhone( new PhoneNumber(homephone.trim(), ext1) );
        erpCustomerInfo.setReceiveNewsletter(receiveEmail);
        erpCustomerInfo.setEmailPlaintext(!htmlEmail);
        erpCustomerInfo.setAlternateEmail(altEmail);
        erpCustomerInfo.setWorkDepartment(workdept);
        erpCustomerInfo.setEmployeeId(employeeId);
        //
        // set depot status
        //
        if ((depotCode != null) && !"".equals(depotCode)) {
            if (FDDepotManager.getInstance().checkAccessCode(depotCode, depotAccessCode)) {
                user.setDepotCode(depotCode);
                FDIdentity identity = user.getIdentity();
                if(identity != null){
                	FDCustomerManager.setDepotCode(identity, depotCode);
                }
            }
        }
        //
        //
        // Done with set up, now update the information
        //
        //
        updateUserId(erpCustomer, result);

        if (result.isSuccess())
            updateUserPassword(erpCustomer, result);

        if (result.isSuccess()) {
			FDCustomerManager.updateCustomerInfo(AccountActivityUtil.getActionInfo(pageContext.getSession()), erpCustomerInfo);
        }
        if (result.isSuccess()) {
            FDCustomerManager.storeUser(user.getUser());
            HttpSession session = pageContext.getSession();
            session.setAttribute(SessionName.USER, user);
        }
        if (result.isSuccess())
            FDCustomerManager.updatePasswordHint(getIdentity(), passwordHint);

    } // method updateAllCustomerInfo


    private void updateUserId(ErpCustomerModel erpCustomer, ActionResult result) throws FDResourceException {
        //
        // Only update userId/Email if a new value has been entered
        //
        if ( !this.emailAddress.equals(erpCustomer.getUserId()) ) {
            StringBuffer buf = new StringBuffer("Updating User ID for user ");
            buf.append(erpCustomer.getPK().getId());
            buf.append("...");
            try {
                FDCustomerManager.updateUserId(AccountActivityUtil.getActionInfo(pageContext.getSession()), this.emailAddress);
                buf.append("SUCCESS!");
                LOGGER.debug(buf.toString());
            } catch (FDResourceException ex){
                result.addError(new ActionError("technical_difficulty", "Sorry, we're experiencing technical difficulties. Please try again later."));
                buf.append("FAILED! (technical difficulty)");
                LOGGER.warn(buf.toString(), ex);
            } catch (ErpDuplicateUserIdException ex){
                result.addError(new ActionError("email", "This email address is already in our database, please enter a different one."));
                buf.append("FAILED! (duplicate id)");
                LOGGER.warn(buf.toString(), ex);
            }
        }
    }


    private void updateUserPassword(ErpCustomerModel erpCustomer, ActionResult result) throws FDResourceException {
        //
        // Only update password if a new value has been entered
        //
        if ( !MD5Hasher.hash(this.password).equals(erpCustomer.getPasswordHash()) ) {
            StringBuffer buf = new StringBuffer("Updating password for user ");
            buf.append(erpCustomer.getPK().getId());
            buf.append("...");

            try {
				FDCustomerManager.changePassword(
					AccountActivityUtil.getActionInfo(pageContext.getSession()),
					this.emailAddress,
					this.password);
                buf.append("SUCCESS!");
            } catch (ErpInvalidPasswordException ex) {
                result.addError(new ActionError("password", "Please enter a password that is at least four characters long."));
                buf.append("FAILED!");
            }
            LOGGER.debug(buf.toString());
        }
    }

    private void updateCustomerAddresses(HttpServletRequest request, ActionResult result) {
    
        for (int i=0; i<addressPK.length; i++ ) {
            ErpAddressModel address = null;
            for (Iterator iter = erpCustomer.getShipToAddresses().iterator(); iter.hasNext(); ) {
                ErpAddressModel tmpAddress = (ErpAddressModel)iter.next();
                if (addressPK[i].equals(tmpAddress.getPK().getId())) {
                    address = tmpAddress;
                    break;
                }
            }
            
            if (address == null) continue; // this shouldn't happen...
            
            address.setFirstName(addrFirstName[i]);
            address.setLastName(addrLastName[i]);
            address.setAddress1(address1[i]);
            address.setAddress2(address2[i]);
            address.setApartment(apartment[i]);
            address.setCity(city[i]);
            address.setState(state[i]);
            address.setZipCode(zipCode[i]);
            address.setCountry("US");
            address.setPhone( new PhoneNumber(addrPhone[i], " ") );
            address.setInstructions(instructions[i]);

            address.setAltDelivery(EnumDeliverySetting.getDeliverySetting(altDelivery[i]));
            
            if (address.getAltDelivery().equals(EnumDeliverySetting.NEIGHBOR)) {
                if (altAddrFirstName != null) address.setAltFirstName(altAddrFirstName[i]);
                if (altAddrLastName != null) address.setAltLastName(altAddrLastName[i]);
                if (altApartment != null) address.setAltApartment(altApartment[i]);
                if (altAddrPhone != null && altAddrPhone[i] != null){
                    address.setAltPhone(new PhoneNumber(altAddrPhone[i], altAddrDlvExt[i]));
                } else if (altAddrPhone[i] != null) {
                    address.setAltPhone(new PhoneNumber(altAddrPhone[i], ""));
                } else {
                    address.setAltPhone(new PhoneNumber("", ""));
                }
            }
            
            try {
				FDCustomerManager.updateShipToAddress(
					AccountActivityUtil.getActionInfo(pageContext.getSession()),
					!getUser().isDepotUser(),
					address);
            } catch (FDResourceException ex) {
                LOGGER.error("FDResourceException while updating user address", ex);
                result.addError(new ActionError("technical_difficulty", "Could not update an address due to technical difficulty."));
            } catch (ErpDuplicateAddressException ex) {
                LOGGER.error("ErpDuplicateAddressException while updating user shipping address", ex);
                result.addError(new ActionError("duplicate_user_address", "The information entered for the marked address matches an existing address in your account."));
                result.addError(new ActionError("duplicate_address_"+address.getPK().getId(), "The information entered for this address matches an existing address in your account."));
            }
            
        }
        
        
    }

    protected void performRemoveDepotMembership(HttpServletRequest request, ActionResult result) throws FDResourceException {
        HttpSession session = request.getSession();
        FDSessionUser user = (FDSessionUser) session.getAttribute(SessionName.USER);
        //
        // remove depot code
        //
        user.setDepotCode(null);
        //
        // add zipcode, if necessary
        //
        FDIdentity identity = user.getIdentity();
        if ((user.getZipCode() == null) && (identity != null) && (identity.getErpCustomerPK() != null)) {
            ErpCustomerModel erpCustomer = FDCustomerFactory.getErpCustomer(identity.getErpCustomerPK());
            for (Iterator aIter = erpCustomer.getShipToAddresses().iterator(); aIter.hasNext(); ) {
                ErpAddressModel a = (ErpAddressModel) aIter.next();
                user.setZipCode(a.getZipCode());
                break;
            }
            
            FDCustomerManager.setDepotCode(identity, null);
        }
        FDCustomerManager.storeUser(user.getUser());
        session.setAttribute(SessionName.USER, user);
    }

    
    /* utility method */
    private FDIdentity getIdentity() {
        return getUser().getIdentity();
    }

    private FDUserI getUser() {
        HttpSession session = pageContext.getSession();
        FDUserI user = (FDUserI) session.getAttribute(USER);
        return user;
    }

}