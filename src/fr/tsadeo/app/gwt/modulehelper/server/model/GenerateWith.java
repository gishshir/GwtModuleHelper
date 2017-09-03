package fr.tsadeo.app.gwt.modulehelper.server.model;

import java.io.Serializable;

public class GenerateWith extends AbstractReplaceOrGenerateWith implements
		Serializable {

private static final long serialVersionUID = 1L;
	
	//--------------------------------------- accessors
	public String getGenerator() {
		return super.getImplementationOrGenerator();
	}
	//------------------------------------------- overriding AbstractReplaceOrGenerateWith
	@Override
	public boolean isReplace() {
		return false;
	}
	//------------------------------------------- constructors
	public GenerateWith() {
		this(null, null);
	}
	public GenerateWith(final MyInterface myInterface, String generator) {
		super(myInterface, generator);
	}
}
