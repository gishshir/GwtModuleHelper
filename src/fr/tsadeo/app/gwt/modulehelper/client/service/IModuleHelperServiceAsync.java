package fr.tsadeo.app.gwt.modulehelper.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

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

public interface IModuleHelperServiceAsync {


	void buildApplication(final String sourceDirName, AsyncCallback<Boolean> callback);

	void loadCompilations(AsyncCallback<List<CompilationLightDto>> callback);

	void loadInterfaces(AsyncCallback<List<MyInterfaceDto>> callback);

	void loadModules(AsyncCallback<List<ModuleLightDto>> callback);

	void loadKeyProperties(AsyncCallback<List<KeyPropertyLightDto>> callback);

	void buildDemo(AsyncCallback<Boolean> callback);

	void loadCompilation(final String compilationName, AsyncCallback<CompilationDto> callback);

	void loadModule(String moduleName, AsyncCallback<ModuleDto> callback);

	void loadInterfaceXImplementations(String interfaceName,
			AsyncCallback<List<InterfaceXImplementationDto>> callback);

	void loadTreeModules(String compilationName,
			AsyncCallback<ModuleNodeDto> callback);

	void reinit(AsyncCallback<Void> callback);

	void loadInterfaceXGenerators(String interfaceName,
			AsyncCallback<List<InterfaceXGeneratorDto>> callback);

	void loadKeyProperty(KeyPropertyLightDto keyProperty,
			AsyncCallback<KeyPropertyDto> callback);

	void buildApplicationFromMapContent(String sourceDirName,
			AsyncCallback<Boolean> callback);


}
