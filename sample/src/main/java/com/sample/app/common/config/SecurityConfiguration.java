package com.sample.app.common.config;
import com.sample.app.common.security.AuthToken;
import com.sample.app.common.security.Sanitizer;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import java.io.IOException;
import java.util.Base64;

public class SecurityConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfiguration.class);
    @Value("${token.headerName}")
    private  String tokenHeaderName;

    @Value("${user.userName}")
    private  String userName;

    @Value("${user.password}")
    private  String password;

    @Bean
    SecurityFilterChain config (HttpSecurity httpSecurity) throws Exception {
        httpSecurity.httpBasic().disable().formLogin().disable().formLogin();
        httpSecurity.csrf().disable();
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity.authorizeHttpRequests(requests -> {
            try {
                requests
                        .requestMatchers("swagger-ui/**","/swagger-resources/**","swagger-ui.html/**", "/v3/api-docs", "/v3/api-docs/**", "actuator/**").permitAll()
                        .anyRequest().authenticated().and()
                        .addFilterAfter(new SecurityContextFilter(), SecurityContextPersistenceFilter.class)
                        .exceptionHandling()
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return  httpSecurity.build();
    }
private  class  SecurityContextFilter implements Filter
{
    @Override
    public  void doFilter (ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws ServletException, IOException, ServletException {
        HttpServletRequest request= (HttpServletRequest) servletRequest;
        String token = request.getHeader(tokenHeaderName);
        if(StringUtils.isEmpty(token))
        {
            LOGGER.info("token header [{}] not present in request.", Sanitizer.encodeForLog(tokenHeaderName));
            chain.doFilter(servletRequest, servletResponse);
            return;
        }
        if(!token.startsWith("Basic "))
        {
            chain.doFilter(servletRequest,servletResponse);
            return;
        }
        String credentialBase64 = token.replaceFirst("Basic ", "");
        String credential;
        try {
            credential = new String(Base64.getDecoder().decode(credentialBase64));
        } catch (IllegalArgumentException e)
        {
        LOGGER.error("credential is not base64 String", e);
        chain.doFilter(servletRequest,servletResponse);
        return;
        }
        String[] parts = credential.split(":");
        if(parts.length!=2)
        {
            chain.doFilter(servletRequest,servletResponse);
            return;
        }
        if(!parts[0].equalsIgnoreCase(userName))
        {
            chain.doFilter(servletRequest, servletResponse);
            return;
        }
        if(!parts[1].equalsIgnoreCase(password))
        {
            chain.doFilter(servletRequest, servletResponse);
            return;
        }
        AuthToken authentication = new AuthToken(token, parts[0]);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(servletRequest,servletResponse);
    }



}
}
