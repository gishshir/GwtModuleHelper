package fr.tsadeo.app.gwt.modulehelper.shared.dto;


public class ModuleXPropertyDto extends AbstractContainerPropertyRulesDto implements IContainerPropertyRules {

	private static final long serialVersionUID = 1L;

	private ModuleLightDto _module;
	private KeyPropertyLightDto _keyProperty;

	private String _defineValues;
	private String _extendValues;
	private String _setValues;

	// ---------------------------------------------- accessors
	public String getDefineValues() {
		return (this._defineValues == null)?"":this._defineValues;
	}

	public void setDefineValues(String _defineValues) {
		this._defineValues = _defineValues;
	}

	public String getExtendValues() {
		return (this._extendValues == null)?"":this._extendValues;
	}

	public void setExtendValues(String _extendValues) {
		this._extendValues = _extendValues;
	}


	public void setSetValues(String _setValues) {
		this._setValues = _setValues;
	}

	public ModuleLightDto getModule() {
		return _module;
	}

	public KeyPropertyLightDto getKeyProperty() {
		return _keyProperty;
	}
	
	// ---------------------------------------------- overriding IContainerProperyRules
	@Override
	public String getKeyPropertyName() {
		return this._keyProperty.getName();
	}
    @Override
	public String getSetValues() {
		return (this._setValues == null)?"":this._setValues;
	}

	// ------------------------------------------------ constructors
	public ModuleXPropertyDto() {
		this(null, null);
	}

	public ModuleXPropertyDto(final ModuleLightDto module,
			KeyPropertyLightDto keyProperty) {
		this._module = module;
		this._keyProperty = keyProperty;
	}
	
	// ------------------------------------------------ overriding Object
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		
		sb.append("[");
		sb.append("module: ");
		sb.append(this._module.getName());
		sb.append(" - key: ");
		sb.append(this._keyProperty.getName());
		
		if (this._setValues != null) {
		  sb.append(" - setValues: ");
	  	  sb.append(this.getSetValues());
		}
		if (this._extendValues != null) {
			sb.append(" - extendValues: ");
			sb.append(this.getExtendValues() );
		}
		if (this._defineValues != null) {
		  sb.append(" - defineValues: ");
		  sb.append(this.getDefineValues());
		}
		
		if (this.hasConditionalProperties()) {
			 sb.append(" - conditionalValues: ");
			for (PropertyRulesDto propertyRulesDto : this._listPropertyRulesDto) {
				 sb.append(propertyRulesDto.getValue());
				 sb.append(" | ");
			}
		}
		
		sb.append("]");
		
		return sb.toString();
	}

}
