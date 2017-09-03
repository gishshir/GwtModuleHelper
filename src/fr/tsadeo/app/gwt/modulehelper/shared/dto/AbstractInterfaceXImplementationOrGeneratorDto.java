package fr.tsadeo.app.gwt.modulehelper.shared.dto;


public abstract class AbstractInterfaceXImplementationOrGeneratorDto extends AbstractRulesElementDto  {

private static final long serialVersionUID = 1L;
	
	private MyInterfaceDto _myInterface;
	private String _implementationOrGenerator;
	private UltraLightDto _moduleUltraLightDto;
	
	protected abstract boolean isImplementation();
	
	//-------------------------------------------------- accessors
	public MyInterfaceDto getMyInterface() {
		return _myInterface;
	}
	public String getImplementationOrGenerator() {
		return _implementationOrGenerator;
	}
	public String getModuleName() {
		return (_moduleUltraLightDto != null)?this._moduleUltraLightDto.getName():"";
	}
	public int getModuleId() {
		return (this._moduleUltraLightDto != null)?this._moduleUltraLightDto.getId():IDto.ID_UNDEFINED;
	}

	//-------------------------------------------------------- constructor
	public AbstractInterfaceXImplementationOrGeneratorDto() {
		this(null, null, null);
	}
	public AbstractInterfaceXImplementationOrGeneratorDto(final MyInterfaceDto myInterface,
			final String implementationOrGenerator,
			final UltraLightDto moduleUltraLightDto) {
		this._myInterface = myInterface;
		this._implementationOrGenerator = implementationOrGenerator;
		this._moduleUltraLightDto = moduleUltraLightDto;
	}

	//------------------------------------------------------- overriding Object
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(100);
		sb.append("[interface: ");
		sb.append(this._myInterface.getName());
		
		sb.append((this.isImplementation())?" - implementation: ":" - generator: ");
		sb.append(this._implementationOrGenerator);
		
		sb.append(" - module: ");
		sb.append(this.getModuleName());
		
		if (this._rulesAll != null) {
		  sb.append(" - all: ");
		  sb.append(this._rulesAll);
		}
		
		if (this._rulesAny != null) {
		  sb.append(" - any: ");
		  sb.append(this._rulesAny);
		}
		
		if (this._rulesNone != null) {
		  sb.append(" - none: ");
		  sb.append(this._rulesNone);
		}
		sb.append("]");
		
		return  sb.toString();
	}
}
