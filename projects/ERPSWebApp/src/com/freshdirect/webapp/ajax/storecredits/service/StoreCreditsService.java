package com.freshdirect.webapp.ajax.storecredits.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.freshdirect.customer.ErpCustomerCreditModel;
import com.freshdirect.fdstore.EnumEStoreId;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerCreditHistoryModel;
import com.freshdirect.fdstore.customer.FDCustomerCreditModel;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.fdstore.customer.FDUserI;
import com.freshdirect.fdstore.referral.FDReferralManager;
import com.freshdirect.framework.util.FormatterUtil;
import com.freshdirect.webapp.ajax.storecredits.data.StoreCreditData;
import com.freshdirect.webapp.ajax.storecredits.data.StoreCredits;

public class StoreCreditsService {

    private static final StoreCreditsService INSTANCE = new StoreCreditsService();
    private static final String PENDING_CREDIT_STATUS = "Pending";
    private static final String REDEMPTION = "Redemption";
    private static final String CREDIT_REDEEMED = "Credit redeemed";
    private static final String STORE_CREDIT = "Store Credit";
    private static final String CREDIT_ISSUED = "Credit Issued";

    private StoreCreditsService() {
    }

    public static StoreCreditsService defaultService() {
        return INSTANCE;
    }

    public StoreCredits collectStoreCredits(FDUserI user, boolean isSelfCreditFeatureActive) throws FDResourceException {

        FDIdentity customerIdentity = user.getIdentity();

        StoreCredits storeCredits = new StoreCredits();
        storeCredits.setSort("date");
        storeCredits.setDir("asc");

        List<ErpCustomerCreditModel> mimList = new ArrayList<ErpCustomerCreditModel>();
        FDCustomerCreditHistoryModel pendingCreditHistory = null;
        if (null != customerIdentity) {
            mimList = FDReferralManager.getUserCredits(customerIdentity.getErpCustomerPK());
            pendingCreditHistory = isSelfCreditFeatureActive ? FDCustomerManager.getPendingCreditHistory(customerIdentity) : null;
            FDCustomerCreditHistoryModel fdCustomerCreditHistoryModel = FDCustomerManager.getCreditHistory(customerIdentity);

            double totalAmountFD = fdCustomerCreditHistoryModel.getRemainingAmountByEStore(EnumEStoreId.FD.getContentId());
            double totalAmountFK = fdCustomerCreditHistoryModel.getRemainingAmountByEStore(EnumEStoreId.FDX.getContentId());
            double totalAmount = totalAmountFD + totalAmountFK;

            storeCredits.setTotalAmountFD(totalAmountFD);
            storeCredits.setTotalAmountFK(totalAmountFK);
            storeCredits.setTotalAmount(totalAmount);
        }

        List<StoreCreditData> approvedCredits = new ArrayList<StoreCreditData>();
        for (ErpCustomerCreditModel cm : mimList) {
            StoreCreditData storeCreditData = new StoreCreditData();
            storeCreditData.setDate(cm.getcDate());
            storeCreditData.setStore(collectStoreName(cm.geteStore()));
            storeCreditData.setStatus(collectStatus(cm.getDepartment()));
            storeCreditData.setAmount(FormatterUtil.formatToTwoDecimal(cm.getAmount()));
            storeCreditData.setType(cm.getDepartment());
            storeCreditData.setOrder("Referral Credit".equals(cm.getDepartment()) ? "" : cm.getSaleId());
            approvedCredits.add(storeCreditData);
        }
        storeCredits.setApprovedCredits(approvedCredits);

        List<StoreCreditData> pendingCredits = new ArrayList<StoreCreditData>();
        DateFormat pendingCreditDateFormatter = new SimpleDateFormat("MM/dd/yyyy");
        List<FDCustomerCreditModel> creditHistory = null == pendingCreditHistory ? new ArrayList<FDCustomerCreditModel>() : pendingCreditHistory.getCreditHistory();
        for (FDCustomerCreditModel credit : creditHistory) {
                StoreCreditData storeCreditData = new StoreCreditData();
                storeCreditData.setDate(pendingCreditDateFormatter.format(credit.getCreateDate()));
            storeCreditData.setStore(collectStoreName(credit.geteStore()));
                storeCreditData.setStatus(PENDING_CREDIT_STATUS);
            storeCreditData.setAmount(FormatterUtil.formatToTwoDecimal(credit.getAmount()));
                storeCreditData.setType(credit.getMethod().getName());
                storeCreditData.setOrder(credit.getSaleId());
                pendingCredits.add(storeCreditData);
        }
        storeCredits.setPendingCredits(pendingCredits);

        return storeCredits;
    }

    private String collectStatus(String department) {
        String status = "";
        if (REDEMPTION.equals(department)) {
            status = CREDIT_REDEEMED;
        } else if (STORE_CREDIT.equals(department)) {
            status = CREDIT_ISSUED;
        } else {
            status = department;
        }
        return status;
    }

    private String collectStoreName(String eStore) {
        String storeName = EnumEStoreId.valueOfContentId(eStore).toString();
        return storeName.equals("FDX") ? "FK" : storeName;
    }
}
