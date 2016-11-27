package com.freshdirect.fdlogistics.pointcut;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Category;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import com.bea.core.repackaged.aspectj.lang.ProceedingJoinPoint;
import com.bea.core.repackaged.aspectj.lang.annotation.Around;
import com.bea.core.repackaged.aspectj.lang.annotation.Aspect;
import com.freshdirect.framework.util.log.LoggerFactory;
import com.freshdirect.logistics.controller.data.request.RequestContext;

@Component
@Aspect
public class LoggingAspect {
	
	private final static Category LOGGER = LoggerFactory
			.getInstance(LoggingAspect.class);

	@Around("execution(* com.freshdirect.fdlogistics..*.*(..))")
	public Object logTimeMethod(ProceedingJoinPoint joinPoint) throws Throwable {
			StopWatch stopWatch = new StopWatch();
			stopWatch.start();
			Object retVal = joinPoint.proceed();
			stopWatch.stop();
			String payload = null;
			StringBuffer logMessage = new StringBuffer();
			logMessage.append(joinPoint.getTarget().getClass().getName());
			logMessage.append(".");
			logMessage.append(joinPoint.getSignature().getName());
			logMessage.append("(");
			// append args
			Object[] args = joinPoint.getArgs();
			for (int i = 0; i < args.length; i++) {
				if(args[i] instanceof RequestContext) {
					if (!StringUtils.isBlank(((RequestContext)args[i]).getApplicationId()) && !StringUtils.isBlank(((RequestContext)args[i]).getInitiator())) {
						payload = "Called from Application "+ ((RequestContext)args[i]).getApplicationId()+" by "+((RequestContext)args[i]).getInitiator();
					}
				}
				logMessage.append(args[i]).append(",");
			}
			if (args.length > 0) {
				logMessage.deleteCharAt(logMessage.length() - 1);
			}
			logMessage.append(")");
			logMessage.append(" execution time: ");
			logMessage.append(stopWatch.getTotalTimeMillis());
			logMessage.append(" ms. ");
			if (payload != null) {
				logMessage.append(payload);
			}
			LOGGER.info(logMessage.toString());
			return retVal;
	}
}