package dev.hudsonprojects.simplechat.requestdata;

import java.util.Locale;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import dev.hudsonprojects.simplechat.common.exception.UnauthorizedException;
import dev.hudsonprojects.simplechat.common.messages.builder.ErrorDetailsBuilder;
import dev.hudsonprojects.simplechat.user.User;

@Component
@RequestScope
public class RequestData {

	private Locale locale;
	private User user;

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Optional<User> getUser() {
		return Optional.ofNullable(user);
	}

	public User getUserOrUnauthorized() {
		return this.getUser().orElseThrow(() -> new UnauthorizedException(
				ErrorDetailsBuilder.unauthorized().setMessage("user.notAuthenticated").build()));
	}
}
