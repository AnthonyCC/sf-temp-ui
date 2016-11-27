package com.freshdirect.dlvadmin.map;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.freshdirect.delivery.DlvResourceException;
import com.freshdirect.delivery.admin.DlvAdminManager;

public class DlvMapperServlet  extends HttpServlet{
	
	

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try{
			
			List layers = DlvAdminManager.getInstance().getMapLayersForRegion();
			response.setContentType("image/svg+xml");
			Mapper m = new Mapper();
			
			m.drawMap(response.getWriter(), layers);
			
		}catch(DlvResourceException de){
			throw new ServletException(de);
		}
	}

}
