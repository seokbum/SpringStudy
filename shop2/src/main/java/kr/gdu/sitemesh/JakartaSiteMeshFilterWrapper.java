package kr.gdu.sitemesh;

import jakarta.servlet.Filter; 
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.io.IOException;

public class JakartaSiteMeshFilterWrapper implements Filter {

    private final SiteMeshFilter siteMeshFilter;

    public JakartaSiteMeshFilterWrapper(SiteMeshFilter siteMeshFilter) {
        this.siteMeshFilter = siteMeshFilter;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        siteMeshFilter.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        siteMeshFilter.doFilter(request, response, chain);
    }

    @Override
    public void destroy() {
        siteMeshFilter.destroy();
    }
}