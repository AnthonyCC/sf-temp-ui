package com.freshdirect.transadmin.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;

import com.freshdirect.transadmin.datamanager.GenericBeanDataManager;
import com.freshdirect.transadmin.datamanager.RoutingResult;
import com.freshdirect.transadmin.web.model.GenericBeanCommand;
import com.freshdirect.transadmin.web.model.LookupCommand;

public class GenericBeanController extends BaseFormController {
			
	protected Map referenceData(HttpServletRequest request)
													throws ServletException {
			Map refData = new HashMap();
			List refDataLst = new ArrayList();
			refDataLst.add(new LookupCommand("transportation.format.orderlocationsapout","Order File From SAP"));
			refDataLst.add(new LookupCommand("transportation.format.orderroutingin","Order File For RoadNet"));
			refDataLst.add(new LookupCommand("transportation.format.locationroutingin","Location File For RoadNet"));
			refDataLst.add(new LookupCommand("transportation.format.orderrouteroutingout","RoadNet Order Route Output"));
			refDataLst.add(new LookupCommand("transportation.format.ordersapin","Order File For SAP"));
			refDataLst.add(new LookupCommand("transportation.format.routesapin","Truck File For SAP"));
			refData.put("filetypes", refDataLst);		
			return refData;
	}

	protected Object formBackingObject(HttpServletRequest request)
		throws Exception {
				
		GenericBeanCommand bean = new GenericBeanCommand();
		
		return bean;
	}
	
	protected ModelAndView onSubmit(HttpServletRequest request,HttpServletResponse response,
		      Object command, BindException errors) throws ServletException, IOException
		  {
		    // cast the bean
		    logger.info("GenericBeanController -- executing onSubmit!");
		    GenericBeanCommand bean = (GenericBeanCommand) command;
		    //let's see if there's content there
		    byte[] bytes = bean.getFile();
		   			   
		    
		    GenericBeanDataManager manager = new GenericBeanDataManager();
		    RoutingResult result = manager.process(bytes, bean.getFileType()
		    											, null);
		   
		    bean.setFileHtml(result.getAdditionalInfo());
		    ModelAndView mav = new ModelAndView(getSuccessView(), errors.getModel());
			mav.getModel().put(this.getCommandName(), command);		
			mav.getModel().putAll(referenceData(request));
			
			return mav;
	}

	  protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws ServletException  {
	    // to actually be able to convert Multipart instance to byte[]
	    // we have to register a custom editor
	    logger.info("FileUploadController -- custom editor to convert Multipart instance to byte[] is registered here.");
	    binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
	    // now Spring knows how to handle multipart object and convert them
	  }
}