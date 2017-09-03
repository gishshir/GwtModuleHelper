package fr.tsadeo.app.gwt.modulehelper.server.model;

import java.util.HashSet;
import java.util.Set;


public class KeyProperty extends AbstractBusinessObject implements  Comparable<KeyProperty> {
	
	private static final long serialVersionUID = 1L;
	
    //------------------------------------------------ instance
	private final String _name;
	private final Set<String> _values = new HashSet<String>();

	//-------------------------------------- accessors
	public Set<String> getValues() {
		return _values;
	}
	public String getName() {
		return this._name;
	}
	//----------------------------------------- constructor
	public KeyProperty() {
		this(ID_UNDEFINED, null);
	}
	protected KeyProperty(final int id, final String name) {
		super(id);
		this._name = name;
	}
	//-------------------------------------- public methods
	public void addValue(final String value) {
		if(value == null) return;
		this._values.add(value);
	}
	//---------------------------------- overriding Comparable
	@Override
	public int compareTo(KeyProperty o) {
		return this.getName().toLowerCase().compareTo(o.getName().toLowerCase());
	}
	
	//------------------------------------ overriding Object
	@Override
	public boolean equals(Object obj) {
      
		if (this == obj) return true;
		if (obj == null || this.getClass() != obj.getClass()) return false;
		
		final KeyProperty key = (KeyProperty) obj;
		
       return  (this.getName().equals(key.getName()));
	}
	@Override
	public int hashCode() {

        int hash = 7;
        hash = 31 * hash + this._name.hashCode();
		return hash;
	}
	
	

}
