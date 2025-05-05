package com.cagan.walletapi.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
public class WebConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    public FilterRegistrationBean<OncePerRequestFilter> podInfoFilter() {
        FilterRegistrationBean<OncePerRequestFilter> registrationBean = new FilterRegistrationBean<>();
        
        OncePerRequestFilter podInfoFilter = new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                    throws ServletException, IOException {
                
                String hostname;
                try {
                    hostname = InetAddress.getLocalHost().getHostName();
                } catch (UnknownHostException e) {
                    hostname = "unknown";
                }
                
                response.setHeader("X-Pod-Name", hostname);
                response.setHeader("X-Application", applicationName);
                
                filterChain.doFilter(request, response);
            }
        };
        
        registrationBean.setFilter(podInfoFilter);
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }
}