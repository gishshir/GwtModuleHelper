package fr.tsadeo.app.gwt.modulehelper.server.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.tsadeo.app.gwt.modulehelper.shared.util.ISharedConstants;
import fr.tsadeo.app.gwt.modulehelper.shared.util.StringHelper;

public class Property implements Serializable, Comparable<Property> {
	
	private static final long serialVersionUID = 1L;
	
	public enum Qualifier {set, define, extend, rules}
	
	private final KeyProperty _key;
	private final boolean _conditional;
	private final Qualifier _qualifier;
	private final List<String> _valueList = new ArrayList<String>(1);
	
	//------------------------------ accessors
	public KeyProperty getKey() {
		return _key;
	}
	public List<String> getValueList() {
		return this._valueList;
	}
	public Qualifier getQualifier() {
		return this._qualifier;
	}
	public boolean isConditional() {
		return this._conditional;
	}
	//----------------------------------------- constructor
	public Property () {
		this(null, null);
	}
	public Property(final KeyProperty key,  final Qualifier qualifier) {
		this(key, qualifier, null, false);
	}
	public Property(final KeyProperty key,  
			final Qualifier qualifier, final String value, final boolean conditional) {
				
		this._key = key;
		this._qualifier = qualifier;
		this._conditional = conditional;
		if (value != null) {
		  this._valueList.add(value);
		}
	}

	//-------------------------------------- public methods
	public boolean isMonoValue() {
		return (this._valueList.size() == 1);
	}
	public int getValuesCount() {
		return (this._valueList.size());
	}
	/**
	 * Construit une liste de property mono-value à partir d'une property multi-value
	 */
	public List<Property> buildPropertyMonoValueList() {
		final int countProperties = this._valueList.size();
		final List<Property> propertyMonoValueList = new ArrayList<Property>(countProperties);
		if (countProperties == 1) {
			propertyMonoValueList.add(this);
			return propertyMonoValueList;
		}
		
		for (String value : this._valueList) {
			propertyMonoValueList.add(new Property(this._key, this._qualifier, value, false));
			
		}
		
		return propertyMonoValueList;
	}
	public void addValue(final String value) {
			if(value == null) return;
			this._valueList.add(value);
			if (!this.isMonoValue()) {
				Collections.sort(this._valueList);
			}
	}

     public String getValues() {   	 
	    return StringHelper.valuesToString(this._valueList, ISharedConstants.SEPARATOR_VALUE);
	 }
     public String getDisplayKeyValue(final String separator) {
    	 return this._key.getName() + separator + this.getValues();
     }
	//------------------------------ overriding Object
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append(this._key.getName());
		sb.append(": ");

        sb.append(this.getValues());
		
		sb.append("]");
		if (this._qualifier != null) {
			sb.append(" (");
			sb.append(this._qualifier.name());
			sb.append(")");
		}
		if (this._conditional) {
		  sb.append("(cond)");
		}

		return sb.toString();
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || (this.getClass() != obj.getClass())) return false;
		
		final Property property = (Property)obj;
		
		return (this.getKey() == property.getKey()) && (this.getValues().equals(property.getValues()));
	}
	
	
	@Override
	public int hashCode() {
		int hash = 7;
		hash = 31 * hash  + this._key.hashCode();
		hash = 31 * hash + this.getValues().hashCode();
		return hash;
	}
	//----------------------------------- overriding comparable
	@Override
	public int compareTo(Property o) {
		return this.getDisplayKeyValue(ISharedConstants.SEPARATOR_KEY_VALUE).compareTo(o.getDisplayKeyValue((ISharedConstants.SEPARATOR_KEY_VALUE)));
	}
}
