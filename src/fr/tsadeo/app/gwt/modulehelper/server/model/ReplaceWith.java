package fr.tsadeo.app.gwt.modulehelper.server.model;

import java.io.Serializable;

public class ReplaceWith extends AbstractReplaceOrGenerateWith implements Serializable {

	private static final long serialVersionUID = 1L;
	
	//--------------------------------------- accessors
	public String getImplementation() {
		return super.getImplementationOrGenerator();
	}
	//------------------------------------------- overriding AbstractReplaceOrGenerateWith
	@Override
	public boolean isReplace() {
		return true;
	}
	//------------------------------------------- constructors
	public ReplaceWith() {
		this(null, null);
	}
	public ReplaceWith(final MyInterface myInterface, String implementation) {
		super(myInterface, implementation);
	}

}
