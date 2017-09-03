package fr.tsadeo.app.gwt.modulehelper.server.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Permutation implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final Compilation _compilation;
	
	// liste de property no conditionnelles (set) ou conditionnelles
	// une seule Property mono_valeur par clé
	private final List<Property> _propertyList = new ArrayList<Property>();
	
	//calculé
	private Map<KeyProperty, Property> _mapSetProperties;
	
	//------------------------------------------------ accessors
	public Compilation getCompilation() {
		return _compilation;
	}
	public List<Property> getPropertyList() {
		return this._propertyList;
	}
	
	//------------------------------------------------ public method
	public Permutation clone() {
		final Permutation permutation = new Permutation(this._compilation);
		permutation.getPropertyList().addAll(this._propertyList);
		return permutation;
	}
	public boolean hasProperties() {
		return this._propertyList != null && !this._propertyList.isEmpty();
	}
	public Property getProperty(final KeyProperty keyProperty) {
		if (!this.hasProperties()) return null;
		if (this._mapSetProperties == null) {
			this.populateMapSetProperies();
		}
		return this._mapSetProperties.get(keyProperty);
	}
	public void removeProperty (final Property propertyToRemove) {
		if(!this.hasProperties()) return;
		this._propertyList.remove(propertyToRemove);
		if (this._mapSetProperties == null) {
		  this._mapSetProperties.remove(propertyToRemove.getKey());
		}
	}
	
	//------------------------------------------------ constructor
	public Permutation() {
		this(null);
	}
	public Permutation(final Compilation compilation) {
		this(compilation, null);
	}
	public Permutation(final Compilation compilation, final Property property) {
		this._compilation = compilation;
		if (property != null) {
		  this._propertyList.add(property);		
		}
	}
	//---------------------------------------------------- private methods
	private void populateMapSetProperies() {
		if (!this.hasProperties()) return;
		if (this._mapSetProperties == null) {
			this._mapSetProperties = new HashMap<KeyProperty, Property>(this._propertyList.size());
		}
		else {
			this._mapSetProperties.clear();
		}
       for (Property property : this._propertyList) {
			this._mapSetProperties.put(property.getKey(), property);
		}
	}
	
}
