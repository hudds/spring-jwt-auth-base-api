package dev.hudsonprojects.simplechat.common.lib.objects;

import java.lang.annotation.Annotation;
import java.util.List;

public class AnnotatedFieldsComparatorWrapperFactory<T>{
	
	private final List<Class<? extends Annotation>> annotation;

	public AnnotatedFieldsComparatorWrapperFactory(List<Class<? extends Annotation>> annotation) {
		this.annotation = annotation;
		
	}

	
	public AnnotatedFieldsComparatorWrapper<T> build(T value) {
		return new AnnotatedFieldsComparatorWrapper<>(value, this.annotation);
	}

}
