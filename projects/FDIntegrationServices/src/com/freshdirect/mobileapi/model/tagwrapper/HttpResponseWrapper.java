package com.freshdirect.mobileapi.model.tagwrapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

public class HttpResponseWrapper implements HttpServletResponse {

    private String successUrl;

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public boolean isSetToSuccessUrl() {
        //TODO: Validate this logic.  Also need to account for query string parameter.
        return StringUtils.equals(this.successUrl, this.sendRedirectUrl);
    }

    @Override
    public void addCookie(Cookie arg0) {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public void addDateHeader(String arg0, long arg1) {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public void addHeader(String arg0, String arg1) {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public void addIntHeader(String arg0, int arg1) {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public boolean containsHeader(String arg0) {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public String encodeRedirectURL(String redirectURL) {
        return redirectURL;
    }

    @Override
    public String encodeRedirectUrl(String arg0) {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public String encodeURL(String arg0) {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public String encodeUrl(String arg0) {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public void sendError(int arg0) throws IOException {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public void sendError(int arg0, String arg1) throws IOException {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    private String sendRedirectUrl;

    @Override
    public void sendRedirect(String sendRedirectUrl) throws IOException {
        this.sendRedirectUrl = sendRedirectUrl;
    }

    public String getSendRedirectUrl() {
        return sendRedirectUrl;
    }

    @Override
    public void setDateHeader(String arg0, long arg1) {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public void setHeader(String arg0, String arg1) {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public void setIntHeader(String arg0, int arg1) {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public void setStatus(int arg0) {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public void setStatus(int arg0, String arg1) {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public void flushBuffer() throws IOException {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public int getBufferSize() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public String getCharacterEncoding() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public String getContentType() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public Locale getLocale() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public boolean isCommitted() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public void reset() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public void resetBuffer() {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public void setBufferSize(int arg0) {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public void setCharacterEncoding(String arg0) {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public void setContentLength(int arg0) {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public void setContentType(String arg0) {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

    @Override
    public void setLocale(Locale arg0) {
        throw new IllegalAccessError("this method has not been implemented in this wrapper");
    }

}
