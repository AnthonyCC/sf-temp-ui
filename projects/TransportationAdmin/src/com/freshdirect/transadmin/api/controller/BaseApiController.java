package com.freshdirect.transadmin.api.controller;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Category;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.freshdirect.framework.service.ServiceException;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.framework.webapp.ActionResult;
import com.freshdirect.transadmin.api.model.Message;

public abstract class BaseApiController extends AbstractController {

    private static final String JSON = "JSON";

    private static final Category LOGGER = LoggerFactory.getInstance(BaseApiController.class);

    public static final String JSON_RENDERED = "json-rendered";
    
    
    protected void postInit() throws Exception {
    }

    /**
     * init method for initializing freemarket template engine
     */
    public final void init() throws Exception {
        postInit();
    }

    
    public String getPostData(HttpServletRequest request, HttpServletResponse response) {
        String data = request.getParameter("data");
        return data;
    }

   
    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public final ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
        response.setContentType("text/xml");
        ModelAndView model = getModelAndView(JSON_RENDERED);
        String action = request.getParameter("action");
        String version = request.getParameter("ver");
        String paramVersion = "0";
        model = processRequest(request, response, model, action);
        
        return model;
    }

    protected <T> T parseRequestObject(HttpServletRequest request, HttpServletResponse response, Class<T> valueType) throws ServiceException {
        try {
            return getMapper().readValue(getPostData(request, response), valueType);
        } catch (JsonGenerationException e) {
            throw new ServiceException(e);
        } catch (JsonMappingException e) {
            throw new ServiceException(e);
        } catch (IOException e) {
            throw new ServiceException(e);
        }
    }

    protected boolean validateCart() {
        return false;
    }

    protected boolean validateUser() {
        return true;
    }

    protected Message getErrorMessage(ActionResult result, HttpServletRequest request) {
        Message responseMessage = new Message();
        responseMessage.setStatus(Message.STATUS_FAILED);
        responseMessage.addErrorMessages(result.getErrors());
        return responseMessage;
    }

    protected Message getErrorMessage(String code, String message) {
        Message responseMessage = new Message();
        responseMessage.setStatus(Message.STATUS_FAILED);
        responseMessage.addErrorMessage(code, message);
        return responseMessage;
    }

    protected Message getWarningMessage(String code, String message) {
        Message responseMessage = new Message();
        responseMessage.setStatus(Message.STATUS_FAILED);
        responseMessage.addWarningMessage(code, message);
        return responseMessage;
    }
    protected void setResponseMessage(ModelAndView model, Message responseMessage) {
    	 try {
			model.addObject("data", getJsonString(responseMessage));
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }

    /**
     * The main method of concrete controllers
     * 
     * @param request
     * @param response
     * @param model
     * @param action
     * @param user
     * @return
     */
    protected abstract ModelAndView processRequest(HttpServletRequest request, HttpServletResponse response, ModelAndView model,
            String action) ;

    protected ObjectMapper getMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true); //May want to flip to false in production
        mapper.setSerializationInclusion( JsonInclude.Include.ALWAYS );
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        
        return mapper;
    }
    
       
    protected ModelAndView getModelAndView(String name) {
        ModelAndView model = new ModelAndView(name);
        model.addObject(JSON, getMapper());
        return model;
    }

    protected String getJsonString(Object obj) throws JsonGenerationException, JsonMappingException, IOException {
        StringWriter writer = new StringWriter();
        getMapper().writeValue(writer, obj);
        return writer.toString();
    }
}
