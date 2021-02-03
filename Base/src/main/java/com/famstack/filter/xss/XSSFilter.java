package com.famstack.filter.xss;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class XSSFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void destroy() {
    	
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws ServletException {
        try {
			chain.doFilter(new XSSRequestWrapper((HttpServletRequest) request), response);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

}