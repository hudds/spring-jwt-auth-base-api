package dev.hudsonprojects.simplechat.common.lib.objects;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dev.hudsonprojects.simplechat.common.reflections.ReflectionUtils;

public class AnnotatedFieldsComparatorWrapper<T> {

	private final T object;
	private final List<Class<? extends Annotation>> annotations;
	private final List<Field> fieldsToCompare;
	public AnnotatedFieldsComparatorWrapper(T object, List<Class<? extends Annotation>> annotations) {
		this.object = object;
		this.annotations = annotations;
		this.fieldsToCompare = this.getFieldsToCompare();
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if(obj instanceof AnnotatedFieldsComparatorWrapper wrapper) {
			return this.objectEquals(wrapper.object);
		}
		
		return false;
		
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.object == null ? null : this.object.getClass(), ReflectionUtils.hashCodeUsingFields(fieldsToCompare, object));
	}
	
	private boolean objectEquals(Object obj) {
		if(this.object != null && obj == null) {
			return false;
		}
		
		if(this.object == null && obj != null) {
			return false;
		}
		
		if(this.object == null) {
			return true;
		}
		
		if(!obj.getClass().isAssignableFrom(this.object.getClass())) {
			return false;
		}
				
		return ReflectionUtils.compareUsingFields(this.fieldsToCompare, this.object, obj);
	}
	
	private List<Field> getFieldsToCompare(){
		if(this.object == null) {
			return new ArrayList<>();
		}
		return this.annotations.stream()
			.map(annotation -> ReflectionUtils.getAllAnnotatedFields(this.object.getClass(), annotation))
			.flatMap(List::stream)
			.toList();
		}
}
