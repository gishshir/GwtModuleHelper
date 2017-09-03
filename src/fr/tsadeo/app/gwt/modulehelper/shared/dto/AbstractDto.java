package fr.tsadeo.app.gwt.modulehelper.shared.dto;

import java.io.Serializable;

public abstract class AbstractDto implements IDto, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int _id;
	
	protected AbstractDto(int id) {
		this._id = id;
	}

	@Override
	public int getId() {
		return this._id;
	}

}
