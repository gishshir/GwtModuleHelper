package fr.tsadeo.app.gwt.modulehelper.shared.dto;


public class InterfaceXGeneratorDto extends
		AbstractInterfaceXImplementationOrGeneratorDto  {
	private static final long serialVersionUID = 1L;
	
	//-------------------------------------------------- overriding AbstractInterfaceXImplementationOrGeneratorDto
	@Override
	protected boolean isImplementation() {
		return false;
	}
	//-------------------------------------------------- accessors
	public String getGenerator() {
		return super.getImplementationOrGenerator();
	}

	//-------------------------------------------------------- constructor
	public InterfaceXGeneratorDto() {
		this(null, null, null);
	}
	public InterfaceXGeneratorDto(
			final MyInterfaceDto myInterface, final String generator,
			final UltraLightDto moduleUltraLightDto) {
		super(myInterface, generator, moduleUltraLightDto);
	}
}
