package com.spring.boot.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.Filter;
import javax.sql.DataSource;

import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.util.Factory;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.spring.boot.oauth2client.filter.OAuth2AuthenticationFilter;
import com.spring.boot.oauth2client.realm.OAuth2Realm;
import com.spring.boot.oauth2client.token.OAuth2Token;
@Configuration
public class Config {
	@Autowired
	DataSource dataSource;
	@Autowired
	OAuth2AuthenticationFilter oAuth2AuthenticationFilter;
	@Autowired
	OAuth2Realm oAuth2Realm;

/*	public @Bean  Factory<org.apache.shiro.mgt.SecurityManager> factory(){
		return new  IniSecurityManagerFactory("classpath:shiro.ini");  
	}*/
	
	public @Bean org.apache.shiro.cache.MemoryConstrainedCacheManager cacheManager(){
		return new MemoryConstrainedCacheManager();
	}
	
	public @Bean org.apache.shiro.mgt.SecurityManager securityManager(){
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		Collection<Realm> realms = new ArrayList<Realm>();
		//oAuth2Realm.setAuthenticationTokenClass(OAuth2Token.class);
		realms.add(oAuth2Realm);
		realms.add(this.jdbcRealm());
		securityManager.setRealms(realms );
		securityManager.setCacheManager(cacheManager());
		return securityManager;
	}
	
	public @Bean JdbcRealm jdbcRealm(){
		JdbcRealm jdbcRealm = new JdbcRealm();
		jdbcRealm.setDataSource(dataSource);
		return jdbcRealm;
	}
	
	public @Bean org.apache.shiro.spring.web.ShiroFilterFactoryBean shiroFilter(){
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(securityManager());
		shiroFilterFactoryBean.setUnauthorizedUrl("/");
		shiroFilterFactoryBean.setLoginUrl("/");
		Map<String, String> filterChainDefinitionMap = new HashMap<String, String>();
		filterChainDefinitionMap.put("/", "anon");
		filterChainDefinitionMap.put("/login", "anon");
		
		filterChainDefinitionMap.put("/authorize", "anon");
		filterChainDefinitionMap.put("/accessToken", "anon");
		filterChainDefinitionMap.put("/userInfo", "anon");
		filterChainDefinitionMap.put("/oauth2Failure.jsp", "anon");
		filterChainDefinitionMap.put("/oauth2/client/login", "oAuth2AuthenticationFilter");
		
		
		//filterChainDefinitionMap.put("/yes/*", "authc");
		filterChainDefinitionMap.put("/yes/*", "user");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap );
		
		Map<String, Filter> filters = new HashMap<String, Filter>();
		filters.put("oAuth2AuthenticationFilter", oAuth2AuthenticationFilter);
		shiroFilterFactoryBean.setFilters(filters );
		
		
		return shiroFilterFactoryBean;
	}
	
	
	
	/*public @Bean org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor advisor(){
		AuthorizationAttributeSourceAdvisor  advisor = new AuthorizationAttributeSourceAdvisor();
		advisor.setSecurityManager(securityManager());
		return advisor;
	}*/
	
	/*public @Bean org.springframework.web.servlet.handler.SimpleMappingExceptionResolver smer(){
		SimpleMappingExceptionResolver smer = new SimpleMappingExceptionResolver();
		Properties mappings = new Properties();
		mappings.setProperty("org.apache.shiro.authz.UnauthorizedException", "/no/noqx.jsp");
		mappings.setProperty("org.apache.shiro.authz.UnauthenticatedException", "/no/nosf.jsp");
		smer.setExceptionMappings(mappings);
		return smer;
	}*/
	
	public @Bean org.springframework.web.servlet.view.InternalResourceViewResolver irvr(){
		InternalResourceViewResolver irvr = new InternalResourceViewResolver();
		irvr.setPrefix("/");
		irvr.setSuffix(".jsp");
		return irvr;
	}
	

}
