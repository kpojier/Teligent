package ru.teligent.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.teligent.models.WeatherResponse;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Cache servlet filter implementation
 * This filter get data from cache and put it into appropriate request attribute
 * @author Max Zhuravlov
 */
@Component
public class CacheFilter implements Filter {
    public static final String REQ_ATT_NAME = "ru.teligent.services.cache";
    public static final String RES_ATT_NAME = "ru.teligent.services.result";

    @Autowired
    LRUCache<WeatherResponse> weatherResponseLRUCache;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        String cacheKey = null;
        HttpServletRequest req = (HttpServletRequest)request;
        String[] requestPath = req.getRequestURI().split("/");
        if (requestPath.length > 2) {
            cacheKey = requestPath[1]+"|"+requestPath[2].toUpperCase();
            if (weatherResponseLRUCache.containsActual(cacheKey)) {
                request.setAttribute(CacheFilter.REQ_ATT_NAME, weatherResponseLRUCache.get(cacheKey));
            }
        }
        chain.doFilter(request, response);
        try {
            WeatherResponse weatherResponse = (WeatherResponse) request.getAttribute(CacheFilter.RES_ATT_NAME);
            if (weatherResponse != null && cacheKey != null) {
                weatherResponseLRUCache.put(cacheKey, weatherResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {

    }
}
