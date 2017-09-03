package fr.tsadeo.app.gwt.modulehelper.shared.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fr.tsadeo.app.gwt.modulehelper.shared.util.ISharedConstants;
import fr.tsadeo.app.gwt.modulehelper.shared.util.StringHelper;

public class PropertyLightDto implements Serializable {


	private static final long serialVersionUID = 1L;
	//private String _key;
	private UltraLightDto _keyProperty;
	private List<String> _valueList;
	
	//------------------------------ accessors
	public String getKey() {
		return _keyProperty.getName();
	}
	public int getKeyPropertyId() {
		return this._keyProperty.getId();
	}
	public List<String> getValueList() {
		return this._valueList;
	}
     public String getValues() {
    	 return StringHelper.valuesToString(this._valueList, ISharedConstants.SEPARATOR_VALUE);
     }
	//----------------------------------- constructor
	public PropertyLightDto() {
		this(null, new String(""));
	}
	public PropertyLightDto(final UltraLightDto keyProperty, final List<String> valueList) {
		this._keyProperty = keyProperty;
		this._valueList = valueList;
	}
	public PropertyLightDto(final UltraLightDto keyProperty, final String monoValue) {
		this._keyProperty = keyProperty;
		this._valueList = new ArrayList<String>(0);
		this._valueList.add(monoValue);
	}

	//------------------------------ overriding Object
		public String toString() {
			final StringBuilder sb = new StringBuilder();
			sb.append("[");
			sb.append(this._keyProperty.getName());
			sb.append(": ");

			sb.append(this.getValues());
			
			sb.append("]");

			return sb.toString();
		}
}
