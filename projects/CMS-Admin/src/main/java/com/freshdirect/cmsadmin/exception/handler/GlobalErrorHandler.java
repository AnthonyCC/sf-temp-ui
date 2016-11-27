package com.freshdirect.cmsadmin.exception.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.freshdirect.cmsadmin.domain.ErrorView;
import com.freshdirect.cmsadmin.exception.BusinessException;

/**
 * Catches exceptions globally in the project.
 *
 */
@ControllerAdvice
public class GlobalErrorHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalErrorHandler.class);

    /**
     * Catches all the Business Exceptions and all of its child exceptions.
     *
     * @param e
     *            Business exception.
     * @return A new instance of the PageError as a ModelAndView.
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorView handle(BusinessException e) {
        LOGGER.error("Exception happened", e);
        return new ErrorView(e.getErrors());
    }

}
