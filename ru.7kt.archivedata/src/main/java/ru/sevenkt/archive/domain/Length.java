package ru.sevenkt.archive.domain;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value={ElementType.FIELD, ElementType.TYPE})
@Retention(value= RetentionPolicy.RUNTIME)
public @interface Length {
	int value();
}
