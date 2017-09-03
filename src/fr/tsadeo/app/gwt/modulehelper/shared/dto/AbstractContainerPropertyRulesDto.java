package fr.tsadeo.app.gwt.modulehelper.shared.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fr.tsadeo.app.gwt.modulehelper.shared.util.ISharedConstants;

public abstract class AbstractContainerPropertyRulesDto  implements Serializable,
		IContainerPropertyRules {


	private static final long serialVersionUID = 1L;
	
	protected List<PropertyRulesDto> _listPropertyRulesDto;
	
	// calculated : list of conditionnals values separated by ', '
	private String _conditionalValues;
	
	//----------------------------------------------- constructor
	protected AbstractContainerPropertyRulesDto() {
	}

	//----------------------------------------------- overriding IContainerPropertyRules
	public String getConditionalValues() {
		if (this._conditionalValues == null) {
			if (this.hasConditionalProperties()) {
				for (PropertyRulesDto propertyRulesDto : this.getListPropertyRules()) {
					this._conditionalValues += propertyRulesDto.getValue() + ISharedConstants.SEPARATOR_VALUE;
				}
			}
			else {
				this._conditionalValues = "";
			}
		}
		return this._conditionalValues;
	}
		@Override
		public boolean hasConditionalProperties() {
			return (this._listPropertyRulesDto != null && !this._listPropertyRulesDto.isEmpty());
		}
		  @Override
		public List<PropertyRulesDto> getListPropertyRules() {
			return this._listPropertyRulesDto;
		}
	     @Override
		public void addPropertyRules(final PropertyRulesDto propertyRulesDto) {
			if (this._listPropertyRulesDto == null) {
				this._listPropertyRulesDto = new ArrayList<PropertyRulesDto>();
			}
			this._listPropertyRulesDto.add(propertyRulesDto);
		}

}
