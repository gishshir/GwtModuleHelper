package fr.tsadeo.app.gwt.modulehelper.shared.dto;

import java.util.List;

public interface IContainerPropertyRules {
	
	public String getKeyPropertyName();
	
	public String getSetValues();
	
	public boolean hasConditionalProperties();
	
	public String getConditionalValues();
	
	public List<PropertyRulesDto> getListPropertyRules();
	
	public void addPropertyRules(final PropertyRulesDto propertyRulesDto);

}
