package com.freshdirect.payment;

import java.io.Serializable;
import java.util.Date;

public class SettlementBatchInfo implements Serializable {
	private String merchant_id;
	private String batch_id;
	private Date settle_date_time;
	private String batch_status;
	private String batch_response_msg;
	private String processor_batch_id;
	private String submission_id;
	private int sales_transactions;
	private double sales_amount;
	private int return_transactions;
	private double return_amount;

	public SettlementBatchInfo() {
		super();
	}

	public String getBatch_id() {
		return batch_id;
	}

	public void setBatch_id(String batch_id) {
		this.batch_id = batch_id;
	}

	public String getBatch_response_msg() {
		return batch_response_msg;
	}

	public void setBatch_response_msg(String batch_response_msg) {
		this.batch_response_msg = batch_response_msg;
	}

	public String getBatch_status() {
		return batch_status;
	}

	public void setBatch_status(String batch_status) {
		this.batch_status = batch_status;
	}

	public String getMerchant_id() {
		return merchant_id;
	}

	public void setMerchant_id(String merchant_id) {
		this.merchant_id = merchant_id;
	}

	public String getProcessor_batch_id() {
		return processor_batch_id;
	}

	public void setProcessor_batch_id(String processor_batch_id) {
		this.processor_batch_id = processor_batch_id;
	}

	public double getReturn_amount() {
		return return_amount;
	}

	public void setReturn_amount(double return_amount) {
		this.return_amount = return_amount;
	}

	public int getReturn_transactions() {
		return return_transactions;
	}

	public void setReturn_transactions(int return_transactions) {
		this.return_transactions = return_transactions;
	}

	public double getSales_amount() {
		return sales_amount;
	}

	public void setSales_amount(double sales_amount) {
		this.sales_amount = sales_amount;
	}

	public int getSales_transactions() {
		return sales_transactions;
	}

	public void setSales_transactions(int sales_transactions) {
		this.sales_transactions = sales_transactions;
	}

	public Date getSettle_date_time() {
		return settle_date_time;
	}

	public void setSettle_date_time(Date settle_date_time) {
		this.settle_date_time = settle_date_time;
	}

	public String getSubmission_id() {
		return submission_id;
	}

	public void setSubmission_id(String submission_id) {
		this.submission_id = submission_id;
	}
	


}
