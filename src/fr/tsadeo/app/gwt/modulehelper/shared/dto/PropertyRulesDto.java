package fr.tsadeo.app.gwt.modulehelper.shared.dto;


public class PropertyRulesDto extends AbstractRulesElementDto  {

	private static final long serialVersionUID = 1L;
	
	private KeyPropertyLightDto _keyProperty;
	private String _value;
	
	//---------------------------------- accessors
	public KeyPropertyLightDto getKeyProperty() {
		return this._keyProperty;
	}
	public String getValue() {
		return this._value;
	}
	
	//---------------------------------- constructor
	public PropertyRulesDto() {
		this(null, null);
	}
	public PropertyRulesDto(final  KeyPropertyLightDto keyProperty, final String value) {
		this._keyProperty = keyProperty;
		this._value = value;
	}


}
