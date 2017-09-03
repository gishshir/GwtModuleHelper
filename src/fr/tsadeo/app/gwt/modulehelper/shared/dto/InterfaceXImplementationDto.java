package fr.tsadeo.app.gwt.modulehelper.shared.dto;


public class InterfaceXImplementationDto extends AbstractInterfaceXImplementationOrGeneratorDto  {

	private static final long serialVersionUID = 1L;
	
	//-------------------------------------------------- overriding AbstractInterfaceXImplementationOrGeneratorDto
	@Override
	protected boolean isImplementation() {
		return true;
	}
	//-------------------------------------------------- accessors
	public String getImplementation() {
		return super.getImplementationOrGenerator();
	}

	//-------------------------------------------------------- constructor
	public InterfaceXImplementationDto() {
		this(null, null, null);
	}
	public InterfaceXImplementationDto(final MyInterfaceDto myInterface, final String implementation,
			final UltraLightDto moduleUltraLightDto) {
		super(myInterface, implementation, moduleUltraLightDto);
	}

	

}
