package fr.tsadeo.app.gwt.modulehelper.server.model;

import java.util.List;

import fr.tsadeo.app.gwt.modulehelper.server.model.AbstractReplaceOrGenerateWith.Rules;
import fr.tsadeo.app.gwt.modulehelper.shared.util.ISharedConstants.TypeRule;

public interface IRulesContainer {
	
	public List<Rules>  getRulesList();
	public void addRule(final Rules rule);
	public boolean hasRules();
	public Rules getRules(final TypeRule typeRule);

}
