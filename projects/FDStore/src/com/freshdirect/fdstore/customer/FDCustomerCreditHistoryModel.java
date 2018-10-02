package com.freshdirect.fdstore.customer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.freshdirect.customer.EnumComplaintLineMethod;
import com.freshdirect.framework.util.FormatterUtil;

/** @author ekracoff */
public class FDCustomerCreditHistoryModel implements Serializable {

    private List<FDCustomerCreditModel> creditHistory;
    private String customerId;

    public FDCustomerCreditHistoryModel(FDIdentity identity, List<FDCustomerCreditModel> creditHistory) {
        this.customerId = identity.getErpCustomerPK();
        this.creditHistory = creditHistory;
    }

    public List<FDCustomerCreditModel> getCreditHistory() {
        return this.creditHistory;
    }

    public void setCreditHistory(List<FDCustomerCreditModel> creditHistory) {
        this.creditHistory = creditHistory;
    }

    public String getCustomerId() {
        return this.customerId;
    }

    public void setCustomerId(FDIdentity identity) {
        this.customerId = identity.getErpCustomerPK();
    }

    public double getRemainingAmount() {
        double remainingAmount = 0.0;
        for (FDCustomerCreditModel credit : creditHistory) {
            remainingAmount = remainingAmount + credit.getRemainingAmount();
        }
        return remainingAmount;
    }

    public List<FDCustomerCreditModel> getCreditsForComplaint(String complaintId) {
        List<FDCustomerCreditModel> complaintCredits = new ArrayList<FDCustomerCreditModel>();
        for (FDCustomerCreditModel credit : creditHistory) {
            if (credit.getComplaintPk().equals(complaintId)) {
                complaintCredits.add(credit);
            }
        }
        return complaintCredits;
    }

    public double getTotalCashBack() {
        double cashBack = 0.0;
        for (FDCustomerCreditModel credit : creditHistory) {
            if (EnumComplaintLineMethod.CASH_BACK.equals(credit.getMethod())) {
                cashBack = cashBack + credit.getOriginalAmount();
            }
        }
        return cashBack;
    }

    public int getSumRefund() {
        int sumRefund = 0;
        for (FDCustomerCreditModel credit : creditHistory) {
            if (EnumComplaintLineMethod.CASH_BACK.equals(credit.getMethod())) {
                sumRefund++;
            }
        }
        return sumRefund;
    }

    public double getTotalCreditsIssued() {
        double total = 0.0;
        for (FDCustomerCreditModel credit : creditHistory) {
            if (EnumComplaintLineMethod.STORE_CREDIT.equals(credit.getMethod())) {
                total = total + credit.getOriginalAmount();
            }
        }
        return total;
    }

    public int getSumCredit() {
        int sumCredit = 0;
        for (FDCustomerCreditModel credit : creditHistory) {
            if (EnumComplaintLineMethod.STORE_CREDIT.equals(credit.getMethod())) {
                sumCredit++;
            }
        }
        return sumCredit;
    }

    public double getRemainingAmountByEStore(String eStore) {
        double remainingAmount = 0.0;
        for (FDCustomerCreditModel credit : creditHistory) {
            if (eStore.equals(credit.geteStore())) {
                remainingAmount = remainingAmount + credit.getRemainingAmount();
            }
        }
        return remainingAmount;
    }
}
