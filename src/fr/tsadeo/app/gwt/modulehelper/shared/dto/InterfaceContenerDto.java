package fr.tsadeo.app.gwt.modulehelper.shared.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InterfaceContenerDto extends AbstractDto {
	
	private static final long serialVersionUID = 1L;
	
	// [interfaceName / list of implementation]
	private Map<String, List<InterfaceXImplementationDto>> _interfaceXImplementationMap =
			         new HashMap<String,List<InterfaceXImplementationDto>>();
	// [interfaceName / list of generator]
	private Map<String, List<InterfaceXGeneratorDto>> _interfaceXGeneratorMap =
			new HashMap<String, List<InterfaceXGeneratorDto>>();
	
	//--------------------------------------------- constructor
	public InterfaceContenerDto() {
		super(ID_UNDEFINED);
	}
	public InterfaceContenerDto(final int id) {
		super(id);
	}
	
	//-------------------------------------- accessors
	public List<String> getInterfaceWithImplementationList() {
		return new ArrayList<String>(this._interfaceXImplementationMap.keySet());
	}
	public List<String> getInterfaceWithGeneratorList() {
		return new ArrayList<String>(this._interfaceXGeneratorMap.keySet());
	}
	public List<InterfaceXImplementationDto> getInterfaceXImplementationList(final String interfaceName) {
		return this._interfaceXImplementationMap.get(interfaceName);
	}
	public List<InterfaceXGeneratorDto> getInterfaceXGeneratorList(final String interfaceName) {
		return this._interfaceXGeneratorMap.get(interfaceName);
	}
	
	//---------------------------------------- public methods
    public void addInterfaceXImplementation(final InterfaceXImplementationDto interfaceXImplementation) {

    	final String interfaceName = interfaceXImplementation.getMyInterface().getName();
        List<InterfaceXImplementationDto> implementationList =
        		this._interfaceXImplementationMap.get(interfaceName);
        if (implementationList == null) {
        	implementationList = new ArrayList<InterfaceXImplementationDto>();
        	this._interfaceXImplementationMap.put(interfaceName, implementationList);   	
        }
        implementationList.add(interfaceXImplementation);
        				
    }
    public void addInterfaceXGenerator(final InterfaceXGeneratorDto interfaceXGenerator) {

    	final String interfaceName = interfaceXGenerator.getMyInterface().getName();
        List<InterfaceXGeneratorDto> generatorList =
        		this._interfaceXGeneratorMap.get(interfaceName);
        if (generatorList == null) {
        	generatorList = new ArrayList<InterfaceXGeneratorDto>();
        	this._interfaceXGeneratorMap.put(interfaceName, generatorList);   	
        }
        generatorList.add(interfaceXGenerator);
        				
    }

}
