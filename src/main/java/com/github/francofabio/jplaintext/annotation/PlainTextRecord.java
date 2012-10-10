package com.github.francofabio.jplaintext.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PlainTextRecord {
	String name() default "";
	int size() default 0;
	boolean fillSpaceToCompleteSize() default false;
}
