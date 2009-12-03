package com.freshdirect.mobileapi.model.tagwrapper;

import java.io.IOException;
import java.util.Enumeration;

import javax.el.ELContext;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.el.ExpressionEvaluator;
import javax.servlet.jsp.el.VariableResolver;

public class PageContextWrapper extends PageContext {

    private HttpServletRequest request = new HttpRequestWrapper(new HttpSessionWrapper());

    private HttpServletResponse response = new HttpResponseWrapper();

    public void setSuccessUrl(String url) {

    }

    @Override
    public void forward(String arg0) throws ServletException, IOException {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public Exception getException() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public Object getPage() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public ServletRequest getRequest() {
        return request;
    }

    @Override
    public ServletResponse getResponse() {
        return response;
    }

    @Override
    public ServletConfig getServletConfig() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public ServletContext getServletContext() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public HttpSession getSession() {
        return request.getSession();
    }

    @Override
    public void handlePageException(Exception arg0) throws ServletException, IOException {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public void handlePageException(Throwable arg0) throws ServletException, IOException {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public void include(String arg0) throws ServletException, IOException {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public void include(String arg0, boolean arg1) throws ServletException, IOException {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public void initialize(Servlet arg0, ServletRequest arg1, ServletResponse arg2, String arg3, boolean arg4, int arg5, boolean arg6)
            throws IOException, IllegalStateException, IllegalArgumentException {
    }

    @Override
    public void release() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public Object findAttribute(String arg0) {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public Object getAttribute(String arg0) {
        return request.getAttribute(arg0);
    }

    @Override
    public Object getAttribute(String arg0, int arg1) {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public Enumeration<String> getAttributeNamesInScope(int arg0) {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public int getAttributesScope(String arg0) {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public ELContext getELContext() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public ExpressionEvaluator getExpressionEvaluator() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public JspWriter getOut() {
        //Return dummy writer so that rest of the tag code executes w/o any issue
        return new JspWriter(0, false) {
            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                // TODO Auto-generated method stub

            }

            @Override
            public void println(Object arg0) throws IOException {
                // TODO Auto-generated method stub

            }

            @Override
            public void println(String arg0) throws IOException {
                // TODO Auto-generated method stub

            }

            @Override
            public void println(char[] arg0) throws IOException {
                // TODO Auto-generated method stub

            }

            @Override
            public void println(double arg0) throws IOException {
                // TODO Auto-generated method stub

            }

            @Override
            public void println(float arg0) throws IOException {
                // TODO Auto-generated method stub

            }

            @Override
            public void println(long arg0) throws IOException {
                // TODO Auto-generated method stub

            }

            @Override
            public void println(int arg0) throws IOException {
                // TODO Auto-generated method stub

            }

            @Override
            public void println(char arg0) throws IOException {
                // TODO Auto-generated method stub

            }

            @Override
            public void println(boolean arg0) throws IOException {
                // TODO Auto-generated method stub

            }

            @Override
            public void println() throws IOException {
                // TODO Auto-generated method stub

            }

            @Override
            public void print(Object arg0) throws IOException {
                // TODO Auto-generated method stub

            }

            @Override
            public void print(String arg0) throws IOException {
                // TODO Auto-generated method stub

            }

            @Override
            public void print(char[] arg0) throws IOException {
                // TODO Auto-generated method stub

            }

            @Override
            public void print(double arg0) throws IOException {
                // TODO Auto-generated method stub

            }

            @Override
            public void print(float arg0) throws IOException {
                // TODO Auto-generated method stub

            }

            @Override
            public void print(long arg0) throws IOException {
                // TODO Auto-generated method stub

            }

            @Override
            public void print(int arg0) throws IOException {
                // TODO Auto-generated method stub

            }

            @Override
            public void print(char arg0) throws IOException {
                // TODO Auto-generated method stub

            }

            @Override
            public void print(boolean arg0) throws IOException {
                // TODO Auto-generated method stub

            }

            @Override
            public void newLine() throws IOException {
                // TODO Auto-generated method stub

            }

            @Override
            public int getRemaining() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public void flush() throws IOException {
                // TODO Auto-generated method stub

            }

            @Override
            public void close() throws IOException {
                // TODO Auto-generated method stub
            }

            @Override
            public void clearBuffer() throws IOException {
                // TODO Auto-generated method stub

            }

            @Override
            public void clear() throws IOException {
                // TODO Auto-generated method stub

            }
        };
    }

    @Override
    public VariableResolver getVariableResolver() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public void removeAttribute(String arg0) {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public void removeAttribute(String arg0, int arg1) {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public void setAttribute(String arg0, Object arg1) {
        request.setAttribute(arg0, arg1);
    }

    @Override
    public void setAttribute(String arg0, Object arg1, int arg2) {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

}
