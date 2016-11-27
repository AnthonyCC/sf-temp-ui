package com.freshdirect.webapp.taglib.test.fdstore;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.metaparadigm.jsonrpc.JSONRPCBridge;
import com.metaparadigm.jsonrpc.JSONRPCServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Category;

import java.io.IOException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;


/**
 * 
 * 
 * @author istvan
 *
 */
public class JSONRPCServletSnooper extends JSONRPCServlet {
	
	// An actual call 
	// 
	// {"id": <id>, 
	//  "method": "CCLFacade.addItemsToList", 
	//  "params": ["<list-name>", 
	//             {"javaClass": "com.freshdirect.fdstore.lists.FDCustomerCreatedList", 
	//              "selection": [
	//                 { "configuration":
	//                      {"salesUnit": "E03", 
	//                       "quantity": 1, 
	//                       "options": {
	//                          "C_MT_BF_PAK": "ST",
	//                          "C_MT_BF_TW4": "N",
	//                          "C_MT_BF_MAR": "GRS"}
	//                      }, 
	//                   "skuCode": "MEA0004664"
	//                 }
	//              ]
	//             }
	//            ]
	// }	
	//
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4462074228294357584L;

	private static Category LOGGER = LoggerFactory.getInstance(JSONRPCServletSnooper.class);
	
	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ClassCastException {
		
		
		Enumeration e = request.getParameterNames();
		
		while(e.hasMoreElements()) {
			String name = (String)e.nextElement();
			String [] values = request.getParameterValues(name);
			
			StringBuffer info = new StringBuffer("REQUEST PARAM ").append(name).append(':');
			for(int i=0; i< values.length; ++i) {
				if (i > 0) info.append(i);
				info.append(values[i]);
			}
			LOGGER.info(info);
			System.err.println(info);
		}	
		HttpSession session = request.getSession();
		
		e = session.getAttributeNames();
		while(e.hasMoreElements()) {
			String name = (String)e.nextElement();
			StringBuffer info = new StringBuffer("SESSION ATTRIBUTE ").append(name).append(':').append(session.getAttribute(name));
			LOGGER.info(info);
			System.err.println(info);
		}
		
		
		JSONRPCBridge bridge = (JSONRPCBridge)session.getAttribute("JSONRPCBridge");
		if (bridge == null) {
			bridge = JSONRPCBridge.getGlobalBridge();
			if (bridge == null) bridge = new JSONRPCBridge();
			session.setAttribute("JSONRPCBridge", bridge);
		}

		Logger log = LogManager.getLogManager().getLogger(JSONRPCServlet.class.getName());
		log.setLevel(Level.FINEST);
		bridge.setDebug(true);
		
		super.service(request,response);
		System.err.println("DATA " + request.getAttribute("DATA"));
		
	}
	
}
