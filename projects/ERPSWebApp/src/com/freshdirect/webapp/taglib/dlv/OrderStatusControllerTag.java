/*
 * 
 * OrderStatusControllerTag.java
 * Date: Sep 23, 2002 Time: 3:26:35 PM
 */
package com.freshdirect.webapp.taglib.dlv;

/**
 * 
 * @author knadeem
 */
import javax.servlet.http.*;
import javax.servlet.jsp.*;

import java.util.*;
import java.text.*;
import java.io.*;

import com.freshdirect.customer.*;
import com.freshdirect.delivery.*;

import com.freshdirect.framework.util.log.LoggerFactory;
import org.apache.log4j.Category;

public class OrderStatusControllerTag extends com.freshdirect.framework.webapp.BodyTagSupport{
	
	private static Category LOGGER = LoggerFactory.getInstance( OrderStatusControllerTag.class );
	private static final SimpleDateFormat sf = new SimpleDateFormat("MM-dd-yyyy");
	private static final String PROCESSING_REQUEST = "processingRequest";
	
	private String successPage;
	
	public void setSuccessPage(String successPage){
		this.successPage = successPage;
	}
	
	public int doStartTag() throws JspException {
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		HttpSession session = pageContext.getSession();
		
		if("POST".equalsIgnoreCase(request.getMethod())){
			//check if same request is submitted twice if true ignore the second request
			if(session.getAttribute(PROCESSING_REQUEST) != null){
				LOGGER.info("Double Click ignoring second request");
				return EVAL_BODY_BUFFERED;
			}
			
			//Starting to process so set a session marker that double clicks can be handeled
			session.setAttribute(PROCESSING_REQUEST, Boolean.TRUE);
			
			String selectedDate = request.getParameter("date_to_confirm");
			String truckNumber = request.getParameter("truck_number");
			List confirmedTrucks = (List) session.getAttribute("confirmed_trucks");
			if(confirmedTrucks == null){
				confirmedTrucks = new ArrayList();
			}
			try{
				List orders = DlvPaymentManager.getInstance().getOrdersByTruckNumber(truckNumber, sf.parse(selectedDate));
				ArrayList delivered = new ArrayList();
				ArrayList redelivery = new ArrayList();
				ArrayList refused = new ArrayList();
				
				for(int i = 0, size = orders.size(); i < size; i++){
					DlvSaleInfo sale = (DlvSaleInfo)orders.get(i);
					String status = request.getParameter("deliverystatus_"+sale.getSaleId());
					if("delivered".equalsIgnoreCase(status)){
						delivered.add(sale.getSaleId());
					}
					if("redelivery".equalsIgnoreCase(status)){
						redelivery.add(sale.getSaleId());
					}
					if("refused".equalsIgnoreCase(status)){
						boolean fullReturn = request.getParameter("full_"+sale.getSaleId()) != null;
						boolean alcoholOnly = request.getParameter("alcohol_"+sale.getSaleId()) != null;
						refused.add(new DlvReturnRecord(sale.getSaleId(), fullReturn, alcoholOnly));
					}
				}
				
				DlvPaymentManager.getInstance().updateSaleDlvStatus(delivered, redelivery, refused);
				
				confirmedTrucks.add(truckNumber);
				session.setAttribute("confirmed_trucks", confirmedTrucks);
				
				if(successPage != null){
					try{
						HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
						response.sendRedirect(response.encodeRedirectURL(successPage));
						JspWriter writer = pageContext.getOut();
						writer.close();
						return SKIP_PAGE;
					}catch(IOException ie){
						throw new JspException(ie.getMessage());
					}
				}
				
			}catch(ParseException pe){
				LOGGER.warn("Unreconized date format"+selectedDate, pe);
				throw new JspException(pe);
			}catch(DlvResourceException de){
				throw new JspException(de);
			}finally{
				session.removeAttribute(PROCESSING_REQUEST);
			}
			
		}
		
		return EVAL_BODY_BUFFERED;
	}
}
