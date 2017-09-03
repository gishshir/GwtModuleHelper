package fr.tsadeo.app.gwt.modulehelper.server.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;

import fr.tsadeo.app.gwt.modulehelper.server.model.Compilation;
import fr.tsadeo.app.gwt.modulehelper.server.model.MyInterface;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.CompilationDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.CompilationLightDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.CompilationPropertyDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.InterfaceXGeneratorDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.InterfaceXImplementationDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.KeyPropertyDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.KeyPropertyLightDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.ModuleDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.ModuleLightDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.ModuleNodeDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.ModuleXPropertyDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.MyInterfaceDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.PermutationDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.PropertyLightDto;

public class TestModuleHelperDtoUtils extends AbstractModuleHelperUtils{

	private static final Log log = LogFactory.getLog(TestModuleHelperUtils.class);
	private static final ModuleHelperDtoUtils instance = ModuleHelperDtoUtils.getInstance();
	
	
	@Test
    public void testLoadInterfaces() {
    	
    	title("testLoadKeyProperties");
		Assert.assertNotNull("application cannot be null!", application);
		final List<MyInterfaceDto> result = instance.loadInterfaces(application);
		log.info("");
		log.info("LISTE DES Interfaces DTO");
		Assert.assertNotNull("result", result);
		for (MyInterfaceDto interfaceDto : result) {
			log.info("interface id: " + interfaceDto.getId() + " - name: " + interfaceDto.getName());
			
			if (interfaceDto.hasImplementations()) {
			  for (String implementation : interfaceDto.getImplementationList()) {
				log.info(">> implementation: " + implementation);
			  }
			}
			
			if (interfaceDto.hasGenerators()) {
				  for (String generator : interfaceDto.getGeneratorList()) {
					log.info(">> generator: " + generator);
				  }
				}
		}
    }
	@Test
	public void testLoadInterfaceXGenerators() {
		
		title("testLoadInterfaceXGenerators");
		Assert.assertNotNull("application cannot be null!", application);
		final List<MyInterface> interfaces = application.getInterfaceList();
		Assert.assertNotNull("interfaces", interfaces);
		log.info("");
		log.info("LISTE DES InterfaceXGenerators DTO");
		
		for (MyInterface myInterface : interfaces) {
			
			log.info("");
			log.info("Interface id: " + myInterface.getId() + " - name: " + myInterface.getName());
			
			final List<InterfaceXGeneratorDto> result = instance.loadInterfaceXGenerators(myInterface.getName(), application);
			Assert.assertNotNull("result", result);
			for (InterfaceXGeneratorDto interfaceXGeneratorDto : result) {
				log.info(">>InterfaceXGeneratorDto : " + interfaceXGeneratorDto.toString());
			}
			
		}
	}
	@Test
	public void testLoadInterfaceXImplementations() {
		
		title("testLoadInterfaceXImplementations");
		Assert.assertNotNull("application cannot be null!", application);
		final List<MyInterface> interfaces = application.getInterfaceList();
		Assert.assertNotNull("interfaces", interfaces);
		log.info("");
		log.info("LISTE DES InterfaceXImplementations DTO");
		
		for (MyInterface myInterface : interfaces) {
			
			log.info("");
			log.info("Interface id: " + myInterface.getId() + " - name: " + myInterface.getName());
			
			final List<InterfaceXImplementationDto> result = instance.loadInterfaceXImplementations(myInterface.getName(), application);
			Assert.assertNotNull("result", result);
			for (InterfaceXImplementationDto interfaceXImplementationDto : result) {
				log.info(">>InterfaceXImplementationDto : " + interfaceXImplementationDto.toString());
			}
			
		}
	}
	
	@Test
	public void testLoadKeyProperties() {
		
		title("testLoadKeyProperties");
		Assert.assertNotNull("application cannot be null!", application);
		final List<KeyPropertyLightDto> result = instance.loadKeyProperties(application);
		log.info("");
		log.info("LISTE DES KeyProperties DTO");
		Assert.assertNotNull("result", result);
		for (KeyPropertyLightDto keyPropertyDto : result) {
			log.info("KeyProperty: " + keyPropertyDto.toString());
		}
	}
	
	@Test
	public void testLoadKeyProperty() {
		
		title("testLoadKeyProperty");
		Assert.assertNotNull("application cannot be null!", application);		
		
		final List<KeyPropertyLightDto> keyPropertyList = instance.loadKeyProperties(application);
		Assert.assertNotNull("keyPropertyList", keyPropertyList);
		
		for (KeyPropertyLightDto keyPropertyDto : keyPropertyList) {
			log.info("");
			log.info("Key: " + keyPropertyDto.getName());
			
			final KeyPropertyDto result = instance.loadKeyProperty(keyPropertyDto, application);
			Assert.assertNotNull("result", result);
			Assert.assertEquals("KeyPropertyDto.name", result.getName(), keyPropertyDto.getName());
		
			// list modules
			final List<ModuleXPropertyDto> moduleXPropertyList = result.getModuleXPropertyList();
			Assert.assertNotNull("moduleXPropertyList", moduleXPropertyList);
			for (ModuleXPropertyDto moduleProperty : moduleXPropertyList) {
				log.info(">> ModuleXProperty: " + moduleProperty.toString());
			}
			
			// ---------list interfaces X implementation
			final List<String> interfaceXImplementationNames = result.getInterfaceWithImplementationList();
			Assert.assertNotNull("interfaceXImplementationNames cannot be null!", interfaceXImplementationNames);
			
			// for each interface
			for (String interfaceName : interfaceXImplementationNames) {
				
				log.info(">> interface: " + interfaceName);
				final List<InterfaceXImplementationDto> listImplementationDtos = result.getInterfaceXImplementationList(interfaceName);
				Assert.assertNotNull("listImplementationDtos cannot be null!", listImplementationDtos);
				for (InterfaceXImplementationDto interfaceXImplementationDto : listImplementationDtos) {
					log.info(">>>> InterfaceXImplementationDto: " + interfaceXImplementationDto.toString());
				}
			}
			
				
			// -----------list interfaces X generator
			final List<String> interfaceXGeneratorNames = result.getInterfaceWithGeneratorList();
			Assert.assertNotNull("interfaceXGeneratorNames cannot be null!", interfaceXGeneratorNames);
			
			// for each interface
			for (String interfaceName : interfaceXGeneratorNames) {
				
				log.info(">> interface: " + interfaceName);
				final List<InterfaceXGeneratorDto> listGeneratornDtos = result.getInterfaceXGeneratorList(interfaceName);
				Assert.assertNotNull("listGeneratornDtos cannot be null!", listGeneratornDtos);
				for (InterfaceXGeneratorDto interfaceXGeneratornDto : listGeneratornDtos) {
						log.info(">>>> interfaceXGeneratornDto: " + interfaceXGeneratornDto.toString());
				}
			}
			
		   
		}		

	}
	
	@Test
	public void testLoadModules() {
		
		title("testLoadModules");
		Assert.assertNotNull("application cannot be null!", application);
		final List<ModuleLightDto> result = instance.loadModules(application);
		
		log.info("");
		log.info("LISTE DES MODULES DTO");
		Assert.assertNotNull("result", result);
		for (ModuleLightDto moduleDto : result) {
			log.info("module: " + moduleDto.toString());
		}
	}
	@Test
	public void testLoadModule() {
		title("testLoadModule");
		Assert.assertNotNull("application cannot be null!", application);
		
		final List<ModuleLightDto> moduleList = instance.loadModules(application);
		Assert.assertNotNull("moduleList", moduleList);
		
		for (ModuleLightDto moduleLightDto : moduleList) {
			
			log.info("");
			log.info("MODULES DTO");
			final ModuleDto module = instance.loadModule(moduleLightDto.getName(), application);
			Assert.assertNotNull("module", module);
			
			log.info("module: " + module.toString());
			
			for (ModuleXPropertyDto modulePropertyDto : module.getModuleXPropertyList()) {
				log.info(" >> module property: " + modulePropertyDto.toString());
			}
			for (ModuleLightDto importModule : module.getImportModuleList()) {
				log.info(" >> import module: " + importModule.toString());
			}
			
			for (String interfaceName: module.getInterfaceWithImplementationList()) {
				log.info(" >> interface: " + interfaceName.toString());
				final List<InterfaceXImplementationDto> implementationList = module.getInterfaceXImplementationList(interfaceName);
				if (implementationList != null) {
					for (InterfaceXImplementationDto interfaceXImplementationDto : implementationList) {
						log.info(" >>>> interfaceXImplementationDto: " + interfaceXImplementationDto.toString());
					}
				}
			}
			
			for (String interfaceName: module.getInterfaceWithGeneratorList()) {
				log.info(" >> interface: " + interfaceName.toString());
				final List<InterfaceXGeneratorDto> generatorList = module.getInterfaceXGeneratorList(interfaceName);
				if (generatorList != null) {
					for (InterfaceXGeneratorDto interfaceXGeneratorDto : generatorList) {
						log.info(" >>>> interfaceXGeneratorDto: " + interfaceXGeneratorDto.toString());
					}
				}
			}
			
			final List<String> content =  module.getContent();
			log.info(" >> content: " + ((content != null)?content.toString():""));
			
		}
	}
	//@Test
	public void testLoadTreeModules() {
		
		Assert.assertNotNull("application cannot be null!", application);
		final List<Compilation> compilationList = application.getCompilationList();
		log.info("");
		log.info("LISTE DES ModuleNode DTO");
		Assert.assertNotNull("compilationList", compilationList);
		
		for (Compilation compilation : compilationList) {
			log.info("compilation: " + compilation.getName());
			
			final ModuleNodeDto result = instance.loadTreeModules(compilation.getName(), application);
			Assert.assertNotNull("result", result);
			log.info("tree: " + result.displayTree(0));
		}
	}
	
	@Test
	public void testLoadCompilations() {
		
		Assert.assertNotNull("application cannot be null!", application);
		final List<CompilationLightDto> result = instance.loadCompilations(application);
		log.info("");
		log.info("LISTE DES COMPILATIONS DTO");
		Assert.assertNotNull("result", result);
		for (CompilationLightDto compilationDto : result) {
			log.info("compilation: " + compilationDto.toString());
		}
	}
	@Test
	public void testLoadCompilation() {
		
		this.title("testLoadCompilation");
		Assert.assertNotNull("application cannot be null!", application);
		
		final List<Compilation> compilationList = application.getCompilationList();
		Assert.assertNotNull("compilationList", compilationList);
		
		//for each compilation
		for (Compilation compilation : compilationList) {
			
			final CompilationDto result = instance.loadCompilation(application, compilation.getName());
			
			log.info("");
			log.info("UNE COMPILATION");
			Assert.assertNotNull("result", result);

			log.info("compilation: [ " + result.toString() + "]");
				
			    // modules
				if (result.hasModules()) {
					for (ModuleLightDto moduleDto : result.getModuleList()) {
						log.info(">> module: " + moduleDto.toString());
					}
				}
				// properties
				if (result.hasCompilationProperties()) {
					for (CompilationPropertyDto compilationPropertyDto : result.getCompilationPropertyList()) {
						log.info(">> compilation property: " + compilationPropertyDto.toString());
					}
				}
				//permutation
				Assert.assertTrue("compilation must have at least one permutation!", compilation.hasPermutations());
				int compteur = 0;
				for (PermutationDto permutationDto : result.getPermutationList()) {
					log.info(">> permutation: " + compteur++);
					
					// permutation properties
					for (CompilationPropertyDto compilationPropertyDto:  permutationDto.getCompilationPropertyList()) {
						log.info(">>>> compilation property: " + compilationPropertyDto.toString());
					}
					
					// implementations
					if (permutationDto.hasImplementations()) {
						for (InterfaceXImplementationDto implementation : permutationDto.getInterfaceXImplementationList()) {
							log.info(">>>> implementation: " + implementation.toString());
						}
					}
					// generator
					if (permutationDto.hasGenerators()) {
						for (InterfaceXGeneratorDto generator : permutationDto.getInterfaceXGeneratorList()) {
							log.info(">>>> generator: " + generator.toString());
						}
					}
					// delta properties
					if (permutationDto.hasDeltaProperties()) {
						for (PropertyLightDto deltaProperty : permutationDto.getListDeltaProperties()) {
							log.info(">>>> deltaProperty: " + deltaProperty.toString());
						}
					}
				}
				

		}
			
	}
}
