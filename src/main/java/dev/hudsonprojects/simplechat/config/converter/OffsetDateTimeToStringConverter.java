package dev.hudsonprojects.simplechat.config.converter;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.core.convert.converter.Converter;

public class OffsetDateTimeToStringConverter implements Converter<OffsetDateTime, String> {

	@Override
	public String convert(OffsetDateTime source) {
		return DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(source);
	}

}
