package fr.tsadeo.app.gwt.modulehelper.shared.dto;

import java.util.List;

/**
 * Encapsule une Property de compilation
 * - cle
 * - set property multi-value
 * - list of conditional property
 * @author sylvie
 *
 */
public class CompilationPropertyDto extends AbstractContainerPropertyRulesDto
		implements IContainerPropertyRules, Comparable<CompilationPropertyDto> {


	private static final long serialVersionUID = 1L;
	
	private PropertyLightDto _setProperty;
	
	//--------------------------------------------- accessor
	public PropertyLightDto getProperty() {
		return this._setProperty;
	}
	// ---------------------------------------------- overriding IContainerProperyRules
	@Override
	public String getKeyPropertyName() {
			return this._setProperty.getKey();
	}
    @Override
	public String getSetValues() {
		return this._setProperty.getValues();
	}
	//--------------------------------------------- constructor
	public CompilationPropertyDto() {
	}
	public CompilationPropertyDto(final PropertyLightDto setProprety) {
         this._setProperty = setProprety;
	}
	public CompilationPropertyDto(
			final PropertyLightDto setProprety, final List<PropertyRulesDto> propertyRulesList) {
        this._setProperty = setProprety;
        this._listPropertyRulesDto = propertyRulesList; 
	}
	
	//--------------------------------------
	@Override
	public String toString() {
		
		final StringBuilder sb = new StringBuilder();
		sb.append("[");
		
		sb.append(this._setProperty.toString());
		if (this.hasConditionalProperties()) {
			sb.append(" Conditional properties: ");
			for (PropertyRulesDto propertyRulesDto : this.getListPropertyRules()) {
				sb.append(propertyRulesDto.getValue());
				sb.append(", ");
			}
		}
		
		sb.append("]");
		
		return sb.toString();
	}
	
	//------------------------------------------------------- implementing Comparable
	@Override
	public int compareTo(CompilationPropertyDto o) {
		return this.getKeyPropertyName().toLowerCase().compareTo(o.getKeyPropertyName().toLowerCase());
	}

}
