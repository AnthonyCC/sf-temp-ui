package com.freshdirect.mobileapi.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import com.freshdirect.mobileapi.util.MobileApiProperties;

/**
 * Enabling CORS support - Access-Control-Allow-Origin
 */
public class CORSFilter extends OncePerRequestFilter {

    public static final String X_FD_EXTRA_RESPONSE_HEADER = "X-FD-Extra-Response";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String ORIGIN_HEADER = "Origin";
    private static final String ACCESS_CONTROL_ALLOW_ORIGIN_HEARDER = "Access-Control-Allow-Origin";
    private static final String ACCESS_CONTROL_ALLOW_CREDENTIALS_HEADER = "Access-Control-Allow-Credentials";
    private static final String ACCESS_CONTROL_ALLOW_HEARDERS = "Access-Control-Allow-Headers";
    private static final String ACCESS_CONTROL_ALLOW_VALUE = X_FD_EXTRA_RESPONSE_HEADER + "," + CONTENT_TYPE_HEADER;
    private static final String P3P_HEADER = "P3P";
    private static final String P3P_POLICY_VALUE = "CP='This is not a P3P policy!'";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String origin = request.getHeader(ORIGIN_HEADER);
        String allowedOrigins = MobileApiProperties.getCORSDomain();

        if (origin != null && (allowedOrigins.equals("*") || allowedOrigins.contains(origin))) {
            response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN_HEARDER, origin);
            response.setHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS_HEADER, "true");
            response.setHeader(ACCESS_CONTROL_ALLOW_HEARDERS, ACCESS_CONTROL_ALLOW_VALUE);
            // P3P Policy workaround for IE third party integration
            if (MobileApiProperties.isP3PPolicyEnabled()) {
                response.setHeader(P3P_HEADER, P3P_POLICY_VALUE);
            }
        }

        filterChain.doFilter(request, response);
    }

}
