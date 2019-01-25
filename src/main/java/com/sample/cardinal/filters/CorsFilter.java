package com.sample.cardinal.filters;

import com.google.common.collect.ImmutableList;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by pwilson on 1/25/17.
 */
public class CorsFilter implements Filter {


    /*
Use this list to configure the headers that will be exposed via CORS declarations.
 */
    private static final List<String> allowedHeaders = ImmutableList.<String>builder()
            .add("origin")
            .add("content-type")
            .add("accept")
            .build();

    /*
    This can be cleaned up when Google finally supports Java 8
    and we can use Streams instead.
     */
    private static final String join(List<String> items) {
        StringBuilder result = new StringBuilder();

        for (String item: items) {
            result.append(item).append(", ");
        }

        //trim the trailing comma and space.
        result.delete(result.lastIndexOf(", "), result.length());

        return result.toString();
    }
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        httpServletResponse.addHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        httpServletResponse.addHeader("Access-Control-Allow-Headers", join(allowedHeaders));

        httpServletResponse.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        httpServletResponse.addHeader("Pragma", "no-cache");
        httpServletResponse.addHeader("Expires", "Thu, 01 Dec 1994 16:00:00");

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        if ("OPTIONS".equals(httpServletRequest.getMethod())) {
            return;
        }

        chain.doFilter(httpServletRequest, httpServletResponse);
    }

    @Override
    public void destroy() {

    }
}