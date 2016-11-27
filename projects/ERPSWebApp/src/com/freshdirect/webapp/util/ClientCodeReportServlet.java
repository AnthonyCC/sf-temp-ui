package com.freshdirect.webapp.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.freshdirect.customer.ErpClientCodeReport;
import com.freshdirect.fdstore.FDResourceException;
import com.freshdirect.fdstore.customer.FDCustomerManager;
import com.freshdirect.fdstore.customer.FDIdentity;
import com.freshdirect.framework.util.log.LoggerFactory;

public class ClientCodeReportServlet extends HttpServlet {
	private static final long serialVersionUID = -2798407311725486565L;

	private static final Logger LOGGER = LoggerFactory.getInstance(ClientCodeReportServlet.class);

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	private static final NumberFormat CURRENCY_FORMATTER = NumberFormat.getCurrencyInstance(Locale.US);

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<ErpClientCodeReport> ccrs = null;
		String saleId = request.getParameter("sale");
		String customerId = null;
		Date start = null;
		Date end = null;
		if (saleId != null) {
			// process sale
			try {
				ccrs = FDCustomerManager.findClientCodesBySale(saleId);
			} catch (FDResourceException e) {
				LOGGER.error("error while retrieving client code report for sale: " + saleId, e);
			}
		} else {
			// process date range
			customerId = request.getParameter("customer");
			String startStr = request.getParameter("start");
			String endStr = request.getParameter("end");
			try {
				start = DATE_FORMAT.parse(startStr);
			} catch (ParseException e) {
				LOGGER.warn("cannot parse start date", e);
			} catch (NullPointerException e) {
				LOGGER.warn("cannot parse missing start date");
			}
			try {
				end = DATE_FORMAT.parse(endStr);
			} catch (ParseException e) {
				LOGGER.warn("cannot parse end date", e);
			} catch (NullPointerException e) {
				LOGGER.warn("cannot parse missing end date");
			}
			if (customerId != null && start != null) {
				customerId = customerId.trim();
				try {
					ccrs = FDCustomerManager.findClientCodesByDateRange(new FDIdentity(customerId), start, end);
					end = new Date();
				} catch (FDResourceException e) {
					LOGGER.error("error while retrieving client code report for customer: " + customerId, e);
				}
			} else {
				LOGGER.error("either sale id or customer id is required");
			}
		}

		if (ccrs != null) {
			// generate output
			response.setContentType("application/vnd.ms-excel");
			response.addHeader("Content-Disposition", "attachment; filename=clientCodes-report-" + DATE_FORMAT.format(new Date())
					+ ".csv");
			response.addHeader("Cache-Control", "must-revalidate");
			response.addHeader("Pragma", "must-revalidate");
			generateHeader(response, saleId, customerId, start, end);
			for (ErpClientCodeReport item : ccrs)
				generateRow(response, item);
		} else {
			response.sendError(400);
		}
	}

	private void generateHeader(HttpServletResponse response, String saleId, String customerId, Date start, Date end)
			throws IOException {
		PrintWriter writer = response.getWriter();
		if (saleId != null) {
			writer.println("\"Client Code Billing Details\",,,,,,\"Order:\",\"" + saleId + "\"");
		} else {
			writer.println("\"Client Code Billing Details\",,,,\"From:\"," + DATE_FORMAT.format(start) + ",\"To:\","
					+ DATE_FORMAT.format(end));
		}
		writer.println("\"Client Code\",\"Delivery Date\",\"Order Number\",\"Product Name\",\"Quantity\",\"Unit Price\","
				+ "\"Total Tax (if applicable)\",\"Total Price (inc. tax)\"");
	}

	private void generateRow(HttpServletResponse response, ErpClientCodeReport item) throws IOException {
		StringBuilder buf = new StringBuilder();
		quote(buf, item.getClientCode());
		comma(buf);
		buf.append(DATE_FORMAT.format(item.getDeliveryDate()));
		comma(buf);
		buf.append(item.getOrderId());
		comma(buf);
		quote(buf, item.getProductDescription());
		comma(buf);
		buf.append(item.getQuantity());
		comma(buf);
		buf.append(CURRENCY_FORMATTER.format(item.getUnitPrice()));
		comma(buf);
		if (item.getTaxRate() != 0.0)
			buf.append(CURRENCY_FORMATTER.format(item.getQuantity() * item.getUnitPrice() * item.getTaxRate()));
		comma(buf);
		buf.append(CURRENCY_FORMATTER.format(item.getQuantity() * item.getUnitPrice() * (1.0 + item.getTaxRate())));
		response.getWriter().println(buf);
	}

	private void quote(StringBuilder buf, String clientCode) {
		buf.append('"');
		buf.append(clientCode);
		buf.append('"');
	}

	private StringBuilder comma(StringBuilder buf) {
		return buf.append(',');
	}
}
