package com.minano.runtime.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.web.SortHandlerMethodArgumentResolver;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewInterceptor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.ui.context.support.ResourceBundleThemeSource;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ThemeResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
import org.springframework.web.servlet.theme.CookieThemeResolver;
import org.springframework.web.servlet.theme.ThemeChangeInterceptor;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.spring3.SpringTemplateEngine;
import org.thymeleaf.spring3.dialect.SpringStandardDialect;
import org.thymeleaf.spring3.messageresolver.SpringMessageResolver;
import org.thymeleaf.spring3.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import com.minano.runtime.support.UserDetailsHandlerMethodArgumentResolver;
import com.minano.runtime.support.web.BaseURLInterceptor;
import com.minano.runtime.support.web.DefaultRequestInterceptor;

@Configuration
@ComponentScan({ "com.minano.runtime.web" })
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {

	public WebConfig() {
		super();
	}

	@Configuration
	public static class FaviconConfiguration {
		@Bean
		public SimpleUrlHandlerMapping faviconHandlerMapping() {
			final SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
			mapping.setOrder(Integer.MIN_VALUE + 1);
			mapping.setUrlMap(Collections.singletonMap("**/favicon.ico",
					faviconRequestHandler()));
			return mapping;
		}

		@Bean
		protected ResourceHttpRequestHandler faviconRequestHandler() {
			final ResourceHttpRequestHandler requestHandler = new ResourceHttpRequestHandler();
			requestHandler.setLocations(Arrays
					.<Resource> asList(new ClassPathResource("/")));
			return requestHandler;
		}
	}

	@Bean
	public ThreadPoolTaskExecutor taskExecutor() {
		final ThreadPoolTaskExecutor x = new ThreadPoolTaskExecutor();
		x.setCorePoolSize(2);
		x.setMaxPoolSize(20);
		x.setQueueCapacity(100);
		x.setWaitForTasksToCompleteOnShutdown(true);
		return x;
	}

	@Override
	public void addViewControllers(final ViewControllerRegistry registry) {
		registry.addViewController("/");
		registry.addViewController("/index.htm");
	}

	@Bean
	public StandardServletMultipartResolver multipartResolver() {
		final StandardServletMultipartResolver m = new StandardServletMultipartResolver();
		return m;
	}

	@Bean
	public Set<IDialect> thymeleafDialects() {
		final Set<IDialect> dialects = new HashSet<IDialect>();
		dialects.add(new SpringStandardDialect());
		dialects.add(new org.thymeleaf.extras.conditionalcomments.dialect.ConditionalCommentsDialect());
		dialects.add(new nz.net.ultraq.thymeleaf.LayoutDialect());
		dialects.add(new org.thymeleaf.extras.springsecurity3.dialect.SpringSecurityDialect());
		return dialects;
	}

	@Bean
	public SpringMessageResolver springMessageResolver() {
		final SpringMessageResolver smr = new SpringMessageResolver();
		smr.setMessageSource(messageSource());
		return smr;
	}

	@Bean
	public ReloadableResourceBundleMessageSource messageSource() {
		final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasenames("WEB-INF/Languages/lang");
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setCacheSeconds(0);
		messageSource.setUseCodeAsDefaultMessage(true);
		return messageSource;
	}

	@Bean
	@Autowired
	public SpringTemplateEngine templateEngine() {
		final SpringTemplateEngine engine = new SpringTemplateEngine();
		engine.setTemplateResolver(templateResolver());
		engine.setDialects(thymeleafDialects());
		engine.setMessageResolver(springMessageResolver());
		return engine;
	}

	@Bean
	public ITemplateResolver templateResolver() {
		final ServletContextTemplateResolver resolver = new ServletContextTemplateResolver();
		resolver.setTemplateMode("XHTML");
		resolver.setPrefix("/WEB-INF/templates/");
		resolver.setSuffix(".html");
		resolver.setCacheable(false);
		resolver.setOrder(1);
		return resolver;
	}

	@Bean
	@Autowired
	public ViewResolver viewResolver(final SpringTemplateEngine templateEngine) {
		final ThymeleafViewResolver resolver = new ThymeleafViewResolver();
		resolver.setTemplateEngine(templateEngine);
		resolver.setCharacterEncoding("UTF-8");
		resolver.setContentType("text/html; charset=UTF-8");
		resolver.setOrder(2);
		return resolver;
	}

	@Bean
	public ViewResolver beanNameViewResolver() {
		final BeanNameViewResolver b = new BeanNameViewResolver();
		b.setOrder(3);
		return b;
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		final LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("lang");
		return localeChangeInterceptor;
	}

	@Bean
	public LocaleResolver localeResolver() {
		final CookieLocaleResolver localeResolver = new CookieLocaleResolver();
		localeResolver.setCookieName("lang");
		localeResolver.setDefaultLocale(Locale.ENGLISH);
		return localeResolver;
	}

	@Bean
	public ResourceBundleThemeSource themeSource() {
		final ResourceBundleThemeSource source = new ResourceBundleThemeSource();
		source.setBasenamePrefix("themes/theme-");
		return source;
	}

	@Bean
	public ThemeChangeInterceptor themeChangeInterceptor() {
		final ThemeChangeInterceptor themeChangeInterceptor = new ThemeChangeInterceptor();
		themeChangeInterceptor.setParamName("theme");
		return themeChangeInterceptor;
	}

	@Value("${cdnurl}")
	private String cdnURL;

	@Override
	public void addInterceptors(final InterceptorRegistry registry) {
		registry.addWebRequestInterceptor(new DefaultRequestInterceptor());
		registry.addInterceptor(new BaseURLInterceptor(cdnURL));
		registry.addInterceptor(localeChangeInterceptor());
		registry.addInterceptor(themeChangeInterceptor());
		registry.addWebRequestInterceptor(openEntityManagerInViewInterceptor());
	}

	@Bean
	public SortHandlerMethodArgumentResolver sortResolver() {
		return new SortHandlerMethodArgumentResolver();
	}

	@Bean
	public UserDetailsHandlerMethodArgumentResolver udhmar() {
		return new UserDetailsHandlerMethodArgumentResolver();
	}

	@Override
	public void addArgumentResolvers(
			final List<HandlerMethodArgumentResolver> argumentResolversToSet) {

		argumentResolversToSet.add(udhmar());
		argumentResolversToSet.add(sortResolver());
	}

	@Autowired
	public LocalContainerEntityManagerFactoryBean emf;

	@Bean
	public OpenEntityManagerInViewInterceptor openEntityManagerInViewInterceptor() {
		final OpenEntityManagerInViewInterceptor x = new OpenEntityManagerInViewInterceptor();
		x.setEntityManagerFactory(emf.getObject());
		return x;
	}

	@Bean
	public ThemeResolver themeResolver() {
		final CookieThemeResolver themeResolver = new CookieThemeResolver();
		themeResolver.setDefaultThemeName("default");
		return themeResolver;
	}
}
