package fr.tsadeo.app.gwt.modulehelper.server.model;

import java.io.Serializable;

public abstract class AbstractBusinessObject implements IBusinessObject, Serializable
{

	private static final long serialVersionUID = 1L;
	
	private final int _id;
	
	protected AbstractBusinessObject(final int id) {
		this._id = id;
	}
	
	@Override
	public int getId() {
		return this._id;
	}

}
