package dev.hudsonprojects.simplechat.requestdata;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import dev.hudsonprojects.simplechat.common.lib.util.Locales;
import dev.hudsonprojects.simplechat.user.User;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

@Component
@Order(1)
public class RequestDataWebFilter implements Filter {

	@Autowired
	private RequestData requestData;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;

		Optional.ofNullable(SecurityContextHolder.getContext())
				.map(SecurityContext::getAuthentication)
				.map(Authentication::getPrincipal)
				.filter(User.class::isInstance)
				.map(User.class::cast)
				.ifPresent(requestData::setUser);
		
		Locale locale = req.getLocale();
		locale = locale == null ? Locales.PT_BR.getLocale() : locale;
		requestData.setLocale(locale);
		chain.doFilter(request, response);
	}

}
