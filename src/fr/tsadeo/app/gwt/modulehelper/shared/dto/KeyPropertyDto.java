package fr.tsadeo.app.gwt.modulehelper.shared.dto;

import java.util.List;
import java.util.Set;

public class KeyPropertyDto extends KeyPropertyLightDto implements IContainerInterface {


	private static final long serialVersionUID = 1L;
	
	private List<ModuleXPropertyDto> _moduleXPropertyList;
	
	private InterfaceContenerDto interfaceContenerDto = new InterfaceContenerDto();

	//-------------------------------------- constructor
		public KeyPropertyDto() {}
		public KeyPropertyDto(final int id, final String name, final Set<String> values) {
			super(id, name, values);
		}
	//------------------------------------------------------------ accessors
	public List<ModuleXPropertyDto> getModuleXPropertyList() {
		return _moduleXPropertyList;
	}
	public void setModuleXPropertyList(List<ModuleXPropertyDto> moduleXPropertyList) {
		this._moduleXPropertyList = moduleXPropertyList;
	}
	@Override
	public List<String> getInterfaceWithImplementationList() {
		return this.interfaceContenerDto.getInterfaceWithImplementationList();
	}
	@Override
	public List<String> getInterfaceWithGeneratorList() {
		return this.interfaceContenerDto.getInterfaceWithGeneratorList();
	}
	@Override
	public List<InterfaceXImplementationDto> getInterfaceXImplementationList(final String interfaceName) {
		return this.interfaceContenerDto.getInterfaceXImplementationList(interfaceName);
	}
	@Override
	public List<InterfaceXGeneratorDto> getInterfaceXGeneratorList(final String interfaceName) {
		return this.interfaceContenerDto.getInterfaceXGeneratorList(interfaceName);
	}
	@Override
    public String getName() {
    	return super.getName();
    }
	//---------------------------------------------------- public methods
	public void setInterfaceXImplementationList(
			List<InterfaceXImplementationDto> interfaceXImplementationList) {
		
		if (interfaceXImplementationList == null) return;
		for (InterfaceXImplementationDto interfaceXImplementation : interfaceXImplementationList) {
			this.interfaceContenerDto.addInterfaceXImplementation(interfaceXImplementation);
		}
	}

	public void setInterfaceXGeneratorList(
			List<InterfaceXGeneratorDto> interfaceXGeneratorList) {
		
		if (interfaceXGeneratorList == null) return;
		for (InterfaceXGeneratorDto interfaceXGenerator : interfaceXGeneratorList) {
			this.interfaceContenerDto.addInterfaceXGenerator(interfaceXGenerator);
		}
	}
	
	

}
