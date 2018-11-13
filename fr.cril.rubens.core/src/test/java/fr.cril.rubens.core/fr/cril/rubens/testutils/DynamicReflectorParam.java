package fr.cril.rubens.testutils;

import java.lang.annotation.Annotation;

import fr.cril.rubens.reflection.ReflectorParam;

public class DynamicReflectorParam implements ReflectorParam {

	private final boolean enabled;
	
	private final String name;

	public DynamicReflectorParam(final boolean enabled, final String name) {
		this.enabled = enabled;
		this.name = name;
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		return DynamicReflectorParam.class;
	}

	@Override
	public String name() {
		return this.name;
	}

	@Override
	public boolean enabled() {
		return this.enabled;
	}

}