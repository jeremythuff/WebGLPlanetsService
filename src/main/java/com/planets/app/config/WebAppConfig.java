/* 
 * TamuWebserviceSeedWebAppConfig.java 
 * 
 * Version: 
 *     $Id$ 
 * 
 * Revisions: 
 *     $Log$ 
 */
package com.planets.app.config;

import org.h2.server.web.WebServlet;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import com.planets.app.controller.interceptor.AppRestInterceptor;

import edu.tamu.framework.config.CoreWebAppConfig;

/**
 * TAMU Webservice Web App Configuration.
 * 
 */
@Configuration
@ComponentScan(basePackages = { "com.planets.app.config", "com.planets.app.controller" })
@ConfigurationProperties(prefix = "app.controller")
@EnableJpaRepositories(basePackages = { "com.planets.app.model.repo", "com.planets.game.model.repo" })
@EntityScan(basePackages = { "com.planets.app.model", "com.planets.game.model" })
public class WebAppConfig extends CoreWebAppConfig {

    /**
     * Rest interceptor bean.
     *
     * @return RestInterceptor
     *
     */
    @Bean
    public AppRestInterceptor restInterceptor() {
        return new AppRestInterceptor();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(restInterceptor()).addPathPatterns("/**");
    }
    
    @Bean
	public ServletRegistrationBean h2servletRegistration() {
		ServletRegistrationBean registrationBean = new ServletRegistrationBean(new WebServlet());
		registrationBean.addUrlMappings("/admin/h2console/*");
		registrationBean.addInitParameter("-webAllowOthers", "true");
		return registrationBean;
	}

}
