package fr.tsadeo.app.gwt.modulehelper.server.model;

import java.util.ArrayList;
import java.util.List;

import fr.tsadeo.app.gwt.modulehelper.server.model.AbstractReplaceOrGenerateWith.Rules;
import fr.tsadeo.app.gwt.modulehelper.shared.util.ISharedConstants.TypeRule;

/**
 * Property which mono-value depends on rules
 * @author sylvie
 *
 */
public class ConditionalProperty extends Property implements  IRulesContainer {


	private static final long serialVersionUID = 1L;
	
	private List<Rules> _rulesList = new ArrayList<Rules>(3);
	
	//--------------------------------------- overriding @IRulesContainer
	@Override
	public List<Rules> getRulesList() {
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
		if (typeRule == null)
			return null;
		for (Rules rules : this._rulesList) {
			if (rules.getTypeRule() == typeRule)
				return rules;
		}
		return null;
	}

	//--------------------------------------- public methods
	public String getValue() {
		return (this.isMonoValue())?this.getValueList().get(0):null;
	}
	//----------------------------------------- constructor
	public ConditionalProperty () {
		this(null, null);
	}
	public ConditionalProperty(final KeyProperty key,  final Qualifier qualifier) {
		this(key, qualifier, null);
	}
	public ConditionalProperty(final KeyProperty key,  final Qualifier qualifier, final String value) {
			
		super(key, qualifier, value, true);
	}
	


}
