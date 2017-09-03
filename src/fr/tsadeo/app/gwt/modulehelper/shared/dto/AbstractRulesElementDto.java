package fr.tsadeo.app.gwt.modulehelper.shared.dto;

import java.io.Serializable;

import fr.tsadeo.app.gwt.modulehelper.shared.util.ISharedConstants.TypeRule;

public class AbstractRulesElementDto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	protected String _rulesAll;
	protected String _rulesAny;
	protected String _rulesNone;
	
	//----------------------------------------------- constructor
	protected AbstractRulesElementDto() {
	}
	
	// -------------------------------------------------- accessors
	public String getRulesAll() {
		return (this._rulesAll == null) ? "" : this._rulesAll;
	}

	public String getRulesAny() {
		return (this._rulesAny == null) ? "" : this._rulesAny;
	}

	public String getRulesNone() {
		return (this._rulesNone == null) ? "" : this._rulesNone;
	}

	// -------------------------------------------------------- public methods
	public void setRules(final TypeRule typeRule, final String rulesValue) {
		switch (typeRule) {
		case all:
			this._rulesAll = rulesValue;
			break;
		case any:
			this._rulesAny = rulesValue;
			break;
		case none:
			this._rulesNone = rulesValue;
			break;
		}
	}

}
