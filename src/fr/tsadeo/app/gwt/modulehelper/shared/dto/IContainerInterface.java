package fr.tsadeo.app.gwt.modulehelper.shared.dto;

import java.util.List;

public interface IContainerInterface {
	
	public String getName();
	
	public List<String> getInterfaceWithImplementationList();
	
	public List<String> getInterfaceWithGeneratorList();

	public List<InterfaceXGeneratorDto> getInterfaceXGeneratorList(final String interfaceName);
	
	public List<InterfaceXImplementationDto> getInterfaceXImplementationList(final String interfaceName);
}
