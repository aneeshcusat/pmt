package com.famstack.projectscheduler.security.expression;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * The Class CustomAuthenticationEntryPoint.
 * 
 * @author Aneeshkumar
 * @version 1.0
 */
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.security.web.AuthenticationEntryPoint#commence(javax
     * .servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * org.springframework.security.core.AuthenticationException)
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws ServletException {
        try {
			response.sendError(SC_UNAUTHORIZED);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
