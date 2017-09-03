package fr.tsadeo.app.gwt.modulehelper.server.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fr.tsadeo.app.gwt.modulehelper.shared.util.ISharedConstants.TypeRule;

public abstract class AbstractReplaceOrGenerateWith  implements  IRulesContainer, Serializable {

	private static final long serialVersionUID = 1L;

	private final MyInterface _myInterface;
	private final String _implementationOrGenerator;
	private List<Rules> _rulesList = new ArrayList<Rules>(3);
	
	public abstract boolean isReplace();
	
	//--------------------------------------- accessors
	

	public MyInterface getMyInterface() {
		return _myInterface;
	}
	protected String getImplementationOrGenerator() {
		return _implementationOrGenerator;
	}
	//------------------------------------------- overriding IRulesContainer
	@Override
	public List<Rules>  getRulesList() {
		return _rulesList;
	}
	@Override
	public void addRule(final Rules rule) {
		this._rulesList.add(rule);
	}
	@Override
	public boolean hasRules() {
		return this._rulesList != null && !this._rulesList.isEmpty();
	}
	@Override
	public Rules getRules(final TypeRule typeRule) {
		if (typeRule == null) return null;
		for (Rules rules : this._rulesList) {
		 if (rules.getTypeRule() == typeRule) return rules;
		}
		return null;
	}
	//------------------------------------------- public methods
	public boolean hasKeyProperty (final KeyProperty keyProperty) {
		
		for (Rules rule : this._rulesList) {
			if (rule.hasKeyProperty(keyProperty)) return true;
		}
		return false;
	}
	public boolean isDefault() {
		return !hasRules();
	}
	
	//------------------------------------------- private methods
	
	//------------------------------------------- constructors
	public AbstractReplaceOrGenerateWith() {
		this(null, null);
	}
	public AbstractReplaceOrGenerateWith(final MyInterface myInterface, 
			String implementationOrGenerator) {
		this._myInterface = myInterface;
		this._implementationOrGenerator = implementationOrGenerator;
	}
	
	
	//============================================= INNER CLASS
	public final static class Rules implements Serializable {
		
		private static final long serialVersionUID = 1L;
		
		private final TypeRule _typeRule;		
		private final List<Property> _properties;
		
		//---------------------------------------- accessors
		public TypeRule getTypeRule() {
			return _typeRule;
		}
		public List<Property> getProperties() {
			return _properties;
		}
		//---------------------------------------- public methods
		public boolean hasKeyProperty (final KeyProperty keyProperty) {
			if (this._properties == null) return false;
			for (Property property : _properties) {
				if (property.getKey() == keyProperty) return true;
			}
			return false;
		}
		//---------------------------------------- constructor
		public Rules(final TypeRule typeRule,final List<Property> properties) {
			this._typeRule = typeRule;
			this._properties = properties;
		}
		
		//------------------------------------ overriding Object
		public String toString() {
			
				final StringBuilder sb = new StringBuilder();
				sb.append("[");
				sb.append(this._typeRule);
				sb.append(": ");

		        sb.append(this._properties);
				
				sb.append("]");

				return sb.toString();
			
		}
		
		
	}
}
