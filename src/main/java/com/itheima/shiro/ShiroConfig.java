package com.itheima.shiro;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.eis.MemorySessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.springframework.context.annotation.DependsOn;

/**
 * Shiro的配置类
 * 
 * 关于Configuration的讲解，可参考一下博客：https://www.cnblogs.com/WUXIAOCHANG/p/10877266.html
 * @author lenovo
 *
 */
@Configuration
public class ShiroConfig {

	/**
	 * 创建ShiroFilterFactoryBean
	 */
	@Bean
	public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager")SecurityManager securityManager){
		
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		
		//设置安全管理器
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		
		//添加Shiro内置过滤器
		/**
		 * Shiro内置过滤器，可以实现权限相关的拦截器
		 *    常用的过滤器：
		 *       anon: 无需认证（登录）可以访问
		 *       authc: 必须认证才可以访问
		 *       user: 如果使用rememberMe的功能可以直接访问
		 *       perms： 该资源必须得到资源权限才可以访问
		 *       role: 该资源必须得到角色权限才可以访问
		 */
		Map<String,String> filterMap = new LinkedHashMap<String,String>();
		/*filterMap.put("/add", "authc");
		filterMap.put("/update", "authc");*/
		
		filterMap.put("/testThymeleaf", "anon");
		//放行login.html页面
		filterMap.put("/login", "anon");
		filterMap.put("/sendDirectMessage","anon");
		
		//授权过滤器
		//注意：当前授权拦截后，shiro会自动跳转到未授权页面
		//perms括号中的内容是权限的值
		//filterMap.put("/add", "perms[user:add]");
		//filterMap.put("/update", "perms[user:update]");
		
		filterMap.put("/*", "authc");
		
		//修改调整的登录页面
		shiroFilterFactoryBean.setLoginUrl("/toLogin");
		//设置未授权提示页面
		shiroFilterFactoryBean.setUnauthorizedUrl("/noAuth");
		
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
		
		
		return shiroFilterFactoryBean;
	}

//	/**
//	 * 创建DefaultWebSecurityManager
//	 *
//	 * 里面主要定义了登录，创建subject，登出等操作
//	 */
//	@Bean(name="securityManager")
//	public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm")UserRealm userRealm){
//		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
//		//关联realm
//		securityManager.setRealm(userRealm);
//		return securityManager;
//	}


	@Bean(name="ehCacheManager")
	public EhCacheManager ehCacheManager(){
		EhCacheManager cacheManager = new EhCacheManager();
		cacheManager.setCacheManagerConfigFile("classpath:ehcache-shiro.xml");
		return cacheManager;
	}


	/**
	 * 创建DefaultWebSecurityManager
	 *
	 * 里面主要定义了登录，创建subject，登出等操作
	 */
	@Bean(name="sessionManager")
	public DefaultWebSessionManager getDefaultWebSecurityManager(@Qualifier("sessionIdDAO")MemorySessionDAO sessionIdDAO,@Qualifier("sessionIdCookie")SimpleCookie sessionIdCookie){
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		//关联realm
		sessionManager.setSessionDAO(sessionIdDAO);
		sessionManager.setSessionIdCookie(sessionIdCookie);

		return sessionManager;
	}


	/**
	 * 创建DefaultWebSecurityManager
	 *
	 * 里面主要定义了登录，创建subject，登出等操作
	 */
	@Bean(name="securityManager")
	public SecurityManager securityManager(@Qualifier("sessionManager")DefaultWebSessionManager sessionManager, @Qualifier("userRealm")UserRealm userRealm, @Qualifier("ehCacheManager")EhCacheManager ehCacheManager){
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		//关联realm
		securityManager.setSessionManager(sessionManager);
		securityManager.setRealm(userRealm);
	//	securityManager.setCacheManager(ehCacheManager);
		return securityManager;
	}



	//配置session的缓存管理器
	@Bean(name= "shiroCacheManager")
	public MemoryConstrainedCacheManager getMemoryConstrainedCacheManager()
	{
		return new MemoryConstrainedCacheManager();
	}


	/**
	 * 创建Realm
	 */
	@Bean(name="userRealm")
	public UserRealm getRealm(){
		return new UserRealm();
	}


	/**
	 * 实现单一登录的配置
	 * **/
	@Bean(name="sessionIdDAO")
	public MemorySessionDAO getMemorySessionDAO(){
		return  new MemorySessionDAO();
	}

	@Bean(name="sessionIdCookie")
	public SimpleCookie getSimpleCookie(){
		SimpleCookie simpleCookie = new SimpleCookie();
		simpleCookie.setName("SHIROSESSIONID");
		return  simpleCookie;
	}
	
	/**
	 * 配置ShiroDialect，用于thymeleaf和shiro标签配合使用
	 */
	/**
	 * Shiro生命周期处理器
	 */
	@Bean(name = "lifecycleBeanPostProcessor")
	public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

	/**
	 * 开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
	 */
	@Bean
	@DependsOn("lifecycleBeanPostProcessor")
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
		creator.setProxyTargetClass(true);
		return creator;
	}

	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("securityManager")SecurityManager securityManager) {
   		 AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
    	authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
   		return authorizationAttributeSourceAdvisor;
	}

}
