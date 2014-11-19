package com.freshdirect.mobileapi.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.DispatcherServlet;

public class DispatchServlet extends DispatcherServlet {
	private static final long serialVersionUID = 8720972531313244617L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			super.service(request, response);
		} catch (Throwable t) {
			response.setStatus(215);
			String trace = traceFor(t);
			response.getWriter().println(trace);
		}
	}

	protected String traceFor(Throwable e) {
		if (e == null) return "No Exception";
		while (true) {
			if (e.getCause() == null) break;
			e = e.getCause();
		}
	    StringWriter trace = new StringWriter();
	    e.printStackTrace(new PrintWriter(trace));
	    String printedTrace = "Exception: " + e.getMessage() + "\n" + trace.toString();
	    return printedTrace;
    }
}
