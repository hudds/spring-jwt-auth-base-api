package dev.hudsonprojects.simplechat.config;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import dev.hudsonprojects.simplechat.config.converter.OffsetDateTimeToStringConverter;
import dev.hudsonprojects.simplechat.config.converter.StringToOffsetDateTimeConverter;
import dev.hudsonprojects.simplechat.properties.PropertiesService;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

	@Value("${spring.profiles.active:#{null}}")
	private String profile;
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedMethods("*").allowedOrigins("*").allowedHeaders("*").exposedHeaders("*");
	}

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new StringToOffsetDateTimeConverter());
		registry.addConverter(new OffsetDateTimeToStringConverter());
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
		lci.setParamName("lang");
		return lci;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}
	
	@Bean
	public LocaleResolver localeResolver() {
	    SessionLocaleResolver slr = new SessionLocaleResolver();
	    slr.setDefaultLocale(new Locale("pt", "BR"));
	    return slr;
	}
	
	@Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
	
	@Bean
	public PropertiesService propertiesService() {
		PropertiesService propertiesService = PropertiesService.getInstance(this.profile);
		propertiesService.validateAllRequiredProperties();
		return propertiesService;
	}
	
//	  @Bean
//	    public WebMvcConfigurer corsConfigurer() {
//	        return new WebMvcConfigurer() {
//	            @Override
//	            public void addCorsMappings(CorsRegistry registry) {
//	                registry.addMapping("/**").allowedHeaders("*").allowedMethods("*").exposedHeaders("*");
//	            }
//	        };
//	    }
}
