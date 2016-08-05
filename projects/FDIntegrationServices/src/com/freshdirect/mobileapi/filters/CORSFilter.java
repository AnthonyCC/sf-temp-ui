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

    private static final String ORIGIN_HEADER = "Origin";
    private static final String ACCESS_CONTROL_ALLOW_ORIGIN_HEARDER = "Access-Control-Allow-Origin";
    private static final String ACCESS_CONTROL_ALLOW_CREDENTIALS_HEADER = "Access-Control-Allow-Credentials";
    private static final String P3P_HEADER = "P3P";
    private static final String P3P_POLICY_VALUE = "CP='This is not a P3P policy!'";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String origin = request.getHeader(ORIGIN_HEADER);
        String allowedOrigins = MobileApiProperties.getCORSDomain();

        if (origin != null && (allowedOrigins.equals("*") || allowedOrigins.contains(origin))) {
            response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN_HEARDER, origin);
            response.setHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS_HEADER, "true");
            // P3P Policy workaround for IE third party integration
            if (MobileApiProperties.isP3PPolicyEnabled()) {
                response.setHeader(P3P_HEADER, P3P_POLICY_VALUE);
            }
        }

        filterChain.doFilter(request, response);
    }

}
