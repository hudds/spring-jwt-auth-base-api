package dev.hudsonprojects.simplechat.common.reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("squid:S3011")
public final class ReflectionUtils {

	private ReflectionUtils() {}

	public static boolean compareUsingFields(List<Field> fields, Object obj1, Object obj2) {
		for (Field field : fields) {
			if(!Objects.equals(getValue(obj1, field), getValue(obj2, field))){
				return false;
			}
		}
		return true;
	}
	
	public static int hashCodeUsingFields(List<Field> fields, Object obj) {
		if(obj == null) {
			return Objects.hash(obj);
		}
		return Objects.hash(fields.stream().map(f -> getValue(obj, f)).toArray());
	}
	
	public static List<Field> getAllAnnotatedFields(Class<?> clazz, Class<? extends Annotation> annotationType){
		Field[] declaredFields = clazz.getDeclaredFields();
		List<Field> annotatedFields = new ArrayList<>();
		for(Field field : declaredFields) {
			if(field.isAnnotationPresent(annotationType)) {
				annotatedFields.add(field);
			}
		}
		if(!Object.class.equals(clazz.getSuperclass())) {
			annotatedFields.addAll(getAllAnnotatedFields(clazz.getSuperclass(), annotationType));
		}
		return annotatedFields;
	}
	
	public static List<Field> getAllFieldsWithTypeAnnotated(Class<?> clazz, Class<? extends Annotation> annotationType){
		Field[] declaredFields = clazz.getDeclaredFields();
		List<Field> annotatedFields = new ArrayList<>();
		for(Field field : declaredFields) {
			if(field.getType().isAnnotationPresent(annotationType)) {
				annotatedFields.add(field);
			}
		}
		if(!Object.class.equals(clazz.getSuperclass())) {
			annotatedFields.addAll(getAllAnnotatedFields(clazz.getSuperclass(), annotationType));
		}
		return annotatedFields;
	}
	
	public static Object getValue(Object target, Field field) {
		if(target == null) {
			return null;
		}
		
		try {
			field.setAccessible(true);
			return field.get(target);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new ReflectiveOperationException(e);
		}
	}

	@SuppressWarnings("squid:S3011")
	public static void forceValueSilently(Object target, Field field, Object value){
		if(field.getType().isPrimitive() && value == null) {
			return;
		}
		if(value != null && !field.getType().isAssignableFrom(value.getClass())) {
			return;
		}
		try{
			field.setAccessible(true);
			field.set(target, value);
		} catch (Exception ignored) {}
	}


	@SuppressWarnings("squid:S3776")
	public static Object getDefaultValueForPrimitiveType(Class<?> type) {
		if(type == null) {
			return null;
		}
		if(!type.isPrimitive()) {
			return null;
		}
		
		DefaultPrimitiveValues defaults = DefaultPrimitiveValues.getDefaults();
		if(type.isAssignableFrom(Integer.class) || type.isAssignableFrom(int.class)) {
			return defaults.getInt();
		}
		
		if(type.isAssignableFrom(Long.class) || type.isAssignableFrom(long.class)) {
			return defaults.getLong();
		}
		
		if(type.isAssignableFrom(Short.class) || type.isAssignableFrom(short.class)) {
			return defaults.getShort();
		}
		
		if(type.isAssignableFrom(Byte.class) || type.isAssignableFrom(byte.class)) {
			return defaults.getByte();
		}
		
		if(type.isAssignableFrom(Float.class) || type.isAssignableFrom(float.class)) {
			return defaults.getFloat();
		}
		
		if(type.isAssignableFrom(Double.class) || type.isAssignableFrom(double.class)) {
			return defaults.getDouble();
		}
		
		if(type.isAssignableFrom(Boolean.class) || type.isAssignableFrom(boolean.class)) {
			return defaults.getBoolean();
		}
		
		if(type.isAssignableFrom(Character.class) || type.isAssignableFrom(char.class)) {
			return defaults.getChar();
		}
		
		return null;
	}
}
