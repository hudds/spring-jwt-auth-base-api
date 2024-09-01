package dev.hudsonprojects.simplechat.properties;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Stream;

public class PropertiesService {
	
	private static PropertiesService singleton = null;
	
	private final String profile;
	
	private Properties defaultProperties;
	
	private Map<String, Properties> profilesProperties =  new HashMap<>();

	private PropertiesService(String profile) {
		this.profile = profile;
		loadDefaultProperties();
	}
	
	private void loadDefaultProperties() {
		this.defaultProperties = this.loadProperties(null);
	}

	private Properties loadProperties(String profile) {
		profile = profile != null ? "-"+ profile : "";
		Properties properties = new Properties();
		try (InputStream is = PropertiesService.class.getResourceAsStream(File.separator+"application"+profile+".properties")) {
			properties.load(is);
			return properties;
		} catch (FileNotFoundException | NullPointerException e) {
			return properties;
		} catch (IOException e) {
			throw new IllegalStateException("Failed to initialize properties", e);
		}
	}
	
	public <T> T getProperty(AppProperty appProperty, Class<T> type) {
		return type.cast(this.getProperty(appProperty));
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getOr(AppProperty appProperty, T defaultValue) {
		return this.getProperty(appProperty) != null ? (T) this.getProperty(appProperty) : defaultValue;
	}
	
	public Object getProperty(AppProperty appProperty) {
		if(getProfile() == null) {
			return this.defaultProperties.get(appProperty.key);
		}
		Properties profileProperties = this.profilesProperties.computeIfAbsent(getProfile(), this::loadProperties);
		if(profileProperties.containsKey(appProperty.key)) {
			Object value = profileProperties.get(appProperty.key);
			return value != null ? value : appProperty.defaultValue;
		}
		return this.defaultProperties.get(appProperty.key);
	}
	
	public boolean hasProperty(AppProperty appProperty) {
		if(getProfile() == null) {
			return this.defaultProperties.containsKey(appProperty.key);
		}
		Properties profileProperties = this.profilesProperties.computeIfAbsent(getProfile(), this::loadProperties);
		return profileProperties.containsKey(appProperty.key) || this.defaultProperties.containsKey(appProperty.key);
	}
	
	public String getProfile() {
		return this.profile == null ? getProfileFromFile() : this.profile;
	}
	
	public String getProfileFromFile() {
		return this.defaultProperties.get(AppProperty.PROFILE.key) == null ? null : String.valueOf(this.defaultProperties.get(AppProperty.PROFILE.key));
	}

	public static synchronized PropertiesService getInstance(String profile) {
		if(singleton == null) {
			singleton = new PropertiesService(profile);
		}
		return singleton;
	}
	
	public void validateAllRequiredProperties() {
		Optional<AppProperty> propertyNotFound = Stream.of(AppProperty.values()).filter(p -> p.required).filter(p -> !this.hasProperty(p)).findAny();
		if(propertyNotFound.isPresent()) {
			throw new IllegalStateException("Required App Property " + propertyNotFound.get().key + " not found");
		}
	}
}
