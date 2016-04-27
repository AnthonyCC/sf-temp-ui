package com.freshdirect.fdlogistics.controller;

import java.util.Locale;

import org.apache.log4j.Category;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.logistics.controller.data.Result;

@Controller
@RequestMapping("/fdlogistics")
public class BaseController {

	/**
	 * Simply selects the home view to render by returning its name.
	 */

	private final static Category LOGGER = LoggerFactory
			.getInstance(BaseController.class);

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		return "home";
	}

	@ExceptionHandler(NoSuchRequestHandlingMethodException.class)
	public @ResponseBody
	Result handleNoHandlerExceptions(Exception e) {
		LOGGER.error("",e);
		if (e instanceof HttpMessageNotReadableException)
			return Result
					.createFailureMessage("There was an error with the input");
		else
			return Result.createFailureMessage(e.getMessage());
	} 
	
	@ExceptionHandler(Exception.class)
	public @ResponseBody
	Result handleExceptions(Exception e) {
		LOGGER.error("",e);
		if (e instanceof HttpMessageNotReadableException)
			return Result
					.createFailureMessage("There was an error with the input");
		else
			return Result.createFailureMessage(e.getMessage());
	}

	
}
