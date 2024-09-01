package dev.hudsonprojects.simplechat.common.lib.util;

import java.util.Locale;

public enum Locales {
	
	PT_BR(new Locale("pt", "BR"));
	
	private final Locale locale;
	
	Locales(Locale locale){
		this.locale = locale;
	}
	
	public Locale getLocale() {
		return locale;
	}

}
