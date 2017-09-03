package fr.tsadeo.app.gwt.modulehelper.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import fr.tsadeo.app.gwt.modulehelper.shared.dto.CompilationDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.CompilationLightDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.InterfaceXGeneratorDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.InterfaceXImplementationDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.KeyPropertyDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.KeyPropertyLightDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.ModuleDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.ModuleLightDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.ModuleNodeDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.MyInterfaceDto;

@RemoteServiceRelativePath("MyService")
public interface IModuleHelperService extends RemoteService {


    public void reinit() throws Exception;
	
	public boolean buildApplication(final String sourceDirName)  throws Exception;
	
	public boolean buildApplicationFromMapContent(final String sourceDirName)  throws Exception;
	
	public boolean buildDemo() throws Exception;
	
	public List<ModuleLightDto> loadModules() throws Exception;
	
	public ModuleDto loadModule(final String moduleName) throws Exception;
	
	public List<CompilationLightDto> loadCompilations()  throws Exception;
	
	public CompilationDto loadCompilation(final String compilationName)  throws Exception;
	
	public List<MyInterfaceDto> loadInterfaces()  throws Exception;
	
	public List<KeyPropertyLightDto> loadKeyProperties()  throws Exception;
	
	public KeyPropertyDto loadKeyProperty(final KeyPropertyLightDto keyProperty)  throws Exception;

	public List<InterfaceXImplementationDto> loadInterfaceXImplementations(final String interfaceName) throws Exception;
	
	public List<InterfaceXGeneratorDto> loadInterfaceXGenerators(final String interfaceName) throws Exception;
	
	public ModuleNodeDto loadTreeModules (final String compilationName) throws Exception;
}
