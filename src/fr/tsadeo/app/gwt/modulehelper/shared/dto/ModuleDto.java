package fr.tsadeo.app.gwt.modulehelper.shared.dto;

import java.util.ArrayList;
import java.util.List;

public class ModuleDto extends ModuleLightDto implements IContainerInterface {

	private static final long serialVersionUID = 1L;
	
	private List<ModuleLightDto> _importModuleList = new ArrayList<ModuleLightDto>();	
	private List<ModuleXPropertyDto> _modulePropertyList = new ArrayList<ModuleXPropertyDto>();

	private InterfaceContenerDto interfaceContenerDto = new InterfaceContenerDto();
	
	private List<String> _content = new ArrayList<String>();
	
	//-------------------------------------- accessors
	public List<ModuleXPropertyDto> getModuleXPropertyList() {
		return this._modulePropertyList;
	}
	@Override
    public String getName() {
    	return super.getName();
    }
	public List<ModuleLightDto> getImportModuleList() {
		return _importModuleList;
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
	public List<String> getContent() {
		return this._content;
	}
	//-------------------------------------- constructor
	public ModuleDto() {
		this(ID_UNDEFINED, null, null, null, null, null, false, null);
	}
	public ModuleDto(final int id, final String name, final String packageName, final String renameTo,
			final String fileName, final String entryPoint, final boolean virtual, final List<String> content) {
		super(id, name, packageName, renameTo, fileName, entryPoint, virtual);
		this._content = content;
	}

	//-------------------------------------- public methods
	public void addImportModule(final ModuleLightDto importModule) {
		this._importModuleList.add(importModule);
	}
	public void addModuleProperty(final ModuleXPropertyDto moduleProperty) {
		this._modulePropertyList.add(moduleProperty);
	}
    public void addInterfaceXImplementation(final InterfaceXImplementationDto interfaceXImplementation) {

    	this.interfaceContenerDto.addInterfaceXImplementation(interfaceXImplementation);
        				
    }
    public void addInterfaceXGenerator(final InterfaceXGeneratorDto interfaceXGenerator) {

    	this.interfaceContenerDto.addInterfaceXGenerator(interfaceXGenerator);
        				
    }
}
