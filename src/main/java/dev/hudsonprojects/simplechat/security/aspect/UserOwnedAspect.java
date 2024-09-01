package dev.hudsonprojects.simplechat.security.aspect;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.hudsonprojects.simplechat.common.lib.objects.AnnotatedFieldsComparatorWrapper;
import dev.hudsonprojects.simplechat.common.lib.objects.AnnotatedFieldsComparatorWrapperFactory;
import dev.hudsonprojects.simplechat.common.reflections.ReflectionUtils;
import dev.hudsonprojects.simplechat.requestdata.RequestData;
import dev.hudsonprojects.simplechat.security.annotation.UserBoundByIdProperty;
import dev.hudsonprojects.simplechat.security.annotation.UserBoundByUsernameProperty;
import dev.hudsonprojects.simplechat.security.annotation.UserOwned;
import dev.hudsonprojects.simplechat.user.User;

@Component
@Aspect
public class UserOwnedAspect {

	@Autowired
	private RequestData requestData;
	
	private static final AnnotatedFieldsComparatorWrapperFactory<Object> comparatorWrapperFactory = new AnnotatedFieldsComparatorWrapperFactory<>(List.of(UserBoundByUsernameProperty.class, UserBoundByIdProperty.class));
	
	
	@Around("@annotation(dev.hudsonprojects.simplechat.security.annotation.SecureUserOwned)")
	public Object bindUser(ProceedingJoinPoint joinPoint) throws Throwable {

		User user = requestData.getUserOrUnauthorized();
		List<Object> args = handleAnnotatedParameters(joinPoint, user);

		this.bingObjectToUser(user, args);

		return joinPoint.proceed(args.toArray());
	}

	private void bingObjectToUser(User user, Object arg) {
		this.bingObjectToUser(user, arg, new HashSet<>());
	}

	private void bingObjectToUser(User user, Object arg, Set<AnnotatedFieldsComparatorWrapper<Object>> mappedObjects) {
		if (arg == null) {
			return;
		}
		if(mappedObjects.contains(comparatorWrapperFactory.build(arg))) {
			return;
		}

		if (arg.getClass().isAnnotationPresent(UserOwned.class)) {
			
			ReflectionUtils.getAllAnnotatedFields(arg.getClass(), UserBoundByIdProperty.class)
					.forEach(f -> ReflectionUtils.forceValueSilently(arg, f, user.getUserId()));
			ReflectionUtils.getAllAnnotatedFields(arg.getClass(), UserBoundByUsernameProperty.class)
					.forEach(f -> ReflectionUtils.forceValueSilently(arg, f, user.getUsername()));
			
			mappedObjects.add(comparatorWrapperFactory.build(arg));
			
			ReflectionUtils.getAllFieldsWithTypeAnnotated(arg.getClass(), UserOwned.class)
					.forEach(obj -> bingObjectToUser(user, obj, mappedObjects));
		} else if (arg.getClass().isAssignableFrom(Collection.class)) {
			((Collection<?>) arg).forEach(obj -> this.bingObjectToUser(user, obj, mappedObjects));
		} else if (arg.getClass().isAssignableFrom(Map.class)) {
			((Map<?, ?>) arg).keySet().forEach(k -> this.bingObjectToUser(user, k, mappedObjects));
			((Map<?, ?>) arg).values().forEach(k -> this.bingObjectToUser(user, k, mappedObjects));
		} else if (arg.getClass().isArray()) {
			for (Object obj : (Object[]) arg) {
				bingObjectToUser(user, obj, mappedObjects);
			}
		} else if (arg.getClass().isAssignableFrom(Iterable.class)) {
			for (Object obj : (Iterable<?>) arg) {
				bingObjectToUser(user, obj, mappedObjects);
			}
		}

	}

	private List<Object> handleAnnotatedParameters(ProceedingJoinPoint joinPoint, User user) {
		List<Object> args = new ArrayList<>();
		if (joinPoint.getSignature() instanceof MethodSignature signature) {
			int i = 0;
			for (Parameter parameter : signature.getMethod().getParameters()) {
				if (parameter.isAnnotationPresent(UserBoundByIdProperty.class)) {
					addParameter(user.getUserId(), args, parameter);
				} else if (parameter.isAnnotationPresent(UserBoundByUsernameProperty.class)) {
					addParameter(user.getUsername(), args, parameter);
				} else {
					args.add(joinPoint.getArgs()[i]);
				}
				i++;
			}

		} else {
			args.addAll(Arrays.asList(joinPoint.getArgs()));
		}
		return args;
	}

	private void addParameter(Object value, List<Object> args, Parameter parameter) {
		if (value != null && value.getClass().isAssignableFrom(parameter.getType())) {
			args.add(value);
		} else if (!parameter.getType().isPrimitive()) {
			args.add(null);
		} else {
			args.add(ReflectionUtils.getDefaultValueForPrimitiveType(parameter.getType()));
		}
	}
}
