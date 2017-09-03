package fr.tsadeo.app.gwt.modulehelper.server.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.tsadeo.app.gwt.modulehelper.server.model.AbstractReplaceOrGenerateWith;
import fr.tsadeo.app.gwt.modulehelper.server.model.AbstractReplaceOrGenerateWith.Rules;
import fr.tsadeo.app.gwt.modulehelper.server.model.Application;
import fr.tsadeo.app.gwt.modulehelper.server.model.Compilation;
import fr.tsadeo.app.gwt.modulehelper.server.model.ConditionalProperty;
import fr.tsadeo.app.gwt.modulehelper.server.model.GenerateWith;
import fr.tsadeo.app.gwt.modulehelper.server.model.IXmlGwtFile;
import fr.tsadeo.app.gwt.modulehelper.server.model.KeyProperty;
import fr.tsadeo.app.gwt.modulehelper.server.model.Module;
import fr.tsadeo.app.gwt.modulehelper.server.model.MyInterface;
import fr.tsadeo.app.gwt.modulehelper.server.model.Permutation;
import fr.tsadeo.app.gwt.modulehelper.server.model.Property;
import fr.tsadeo.app.gwt.modulehelper.server.model.Property.Qualifier;
import fr.tsadeo.app.gwt.modulehelper.server.model.ReplaceWith;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.AbstractInterfaceXImplementationOrGeneratorDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.CompilationDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.CompilationLightDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.CompilationPropertyDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.InterfaceXGeneratorDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.InterfaceXImplementationDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.KeyPropertyDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.KeyPropertyLightDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.UltraLightDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.ModuleDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.ModuleLightDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.ModuleNodeDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.ModuleXPropertyDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.MyInterfaceDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.PermutationDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.PropertyLightDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.PropertyRulesDto;
import fr.tsadeo.app.gwt.modulehelper.shared.util.ISharedConstants;
import fr.tsadeo.app.gwt.modulehelper.shared.util.StringHelper;

/**
 * Utilitaire pour la construction de tous les DTO
 * @author sylvie
 *
 */
public class ModuleHelperDtoUtils {

	private static final ModuleHelperDtoUtils instance = new ModuleHelperDtoUtils();
	
	private static final Log log = LogFactory.getLog(ModuleHelperDtoUtils.class);

	public static final ModuleHelperDtoUtils getInstance() {
		return instance;
	}
	private ModuleHelperDtoUtils() {}
	
public ModuleNodeDto loadTreeModules(final String compilationName,  final Application application)  {
		
		if (compilationName == null) throw new IllegalArgumentException("loadTreeModules(): compilationName cannot be null!!");
		if (!application.hasCompilations()) {
			return null;
		}
		final Compilation compilation = application.getCompilationByName(compilationName);

		return this.buildRecursifModuleNodeDto(compilation.getModule(), true);
	}
	
	private ModuleNodeDto buildRecursifModuleNodeDto(final Module module, final boolean enabled) {
		
		final ModuleNodeDto moduleNodeDto = new ModuleNodeDto(this.buildModuleLightDto(module), enabled);
		if (!enabled) {
			return moduleNodeDto;
		}
		
		if (module.hasImports()) {
			
			for (Module importModule : module.getImportModulesList(false)) {
				
				final boolean importModuleEnabled = module.isImportModuleEnabled(importModule);
				final ModuleNodeDto importModuleNodeDto = this.buildRecursifModuleNodeDto(importModule, importModuleEnabled);
				moduleNodeDto.addImportModule(importModuleNodeDto);
			}
		}
		
		return moduleNodeDto;
	}
	public List<InterfaceXGeneratorDto> loadInterfaceXGenerators(
			final String interfaceName, final Application application) {

      log.info("loadInterfaceXGenerators()");
		
		if (interfaceName == null) throw new IllegalArgumentException("InterfaceXGeneratorDto(): interfaceName cannot be null!!");
		if (!application.hasInterfaces() || !application.hasModules()) {
			return new ArrayList<InterfaceXGeneratorDto>(0);
		}
		
		final List<InterfaceXGeneratorDto> interfaceXGeneratorList = new ArrayList<InterfaceXGeneratorDto>();
			
		final Map<Module, List<GenerateWith>> mapGenerateWithByModule = 
				ModuleHelperUtils.getInstance().buildGenerateWithListForInterfaceAndModuleList(interfaceName, application.getModuleList(), application);
		if (mapGenerateWithByModule != null) {
			for (Module module : mapGenerateWithByModule.keySet()) {
				final UltraLightDto moduleUltraLightDto = new UltraLightDto(module.getId(), module.getName());
				for (GenerateWith generateWith : mapGenerateWithByModule.get(module)) {
					interfaceXGeneratorList.add(this.buildInterfaceXGeneratorDto(generateWith, moduleUltraLightDto));
				}			
			}
		}
		return interfaceXGeneratorList;
	}
	
	public List<InterfaceXImplementationDto> loadInterfaceXImplementations(
			final String interfaceName, final Application application) {
		
		log.info("loadInterfaceXImplementations()");
		
		if (interfaceName == null) throw new IllegalArgumentException("loadInterfaceXImplementations(): interfaceName cannot be null!!");
		if (!application.hasInterfaces() || !application.hasModules()) {
			return new ArrayList<InterfaceXImplementationDto>(0);
		}
		
		final List<InterfaceXImplementationDto> interfaceXImplementationList = new ArrayList<InterfaceXImplementationDto>();
			
		final Map<Module, List<ReplaceWith>> mapReplaceWithByModule =
				ModuleHelperUtils.getInstance().buildReplaceWithListForInterfaceAndModuleList(interfaceName, application.getModuleList(), application);
		if (mapReplaceWithByModule != null) {
			for (Module module : mapReplaceWithByModule.keySet()) {
				final UltraLightDto moduleUltraLightDto = new UltraLightDto(module.getId(), module.getName());
				for (ReplaceWith replaceWith : mapReplaceWithByModule.get(module)) {
					interfaceXImplementationList.add(this.buildInterfaceXImplementationDto(replaceWith, moduleUltraLightDto));
				}			
			}
		}
		return interfaceXImplementationList;
	}


	
	public List<MyInterfaceDto> loadInterfaces(final Application application)  {
		
		log.info("loadInterfaces()");
		if (!application.hasInterfaces()) {
			return new ArrayList<MyInterfaceDto>(0);
		}
		final List<MyInterface> interfaceList = application.getInterfaceList();
		final List<MyInterfaceDto> interfaceDtoList = new ArrayList<MyInterfaceDto>(interfaceList.size());
		
		for (MyInterface myInterface : interfaceList) {
			interfaceDtoList.add(this.buildInterfaceDto(myInterface));
		}
		 return interfaceDtoList;
	}
	

	public List<KeyPropertyLightDto> loadKeyProperties(final Application application)  {
		
		log.info("loadKeyProperties()");
		
		if(!application.hasKeyProperties()) {
			return new ArrayList<KeyPropertyLightDto>(0);
		}
		final List<KeyProperty> keyPropertyList = application.getKeyPropertyList();
		

		final  List<KeyPropertyLightDto> keyPropertyDtoList = new ArrayList<KeyPropertyLightDto>(keyPropertyList.size());
		for (KeyProperty keyProperty : keyPropertyList) {
			keyPropertyDtoList.add(this.buildKeyPropertyLightDto(keyProperty));
		}
		
		return keyPropertyDtoList;
	}

	public KeyPropertyDto loadKeyProperty(KeyPropertyLightDto keyPropertyLightDto, final Application application) {

		log.info("loadKeyProperty()");
		
		if (keyPropertyLightDto == null) throw new IllegalArgumentException("loadKeyProperty(): keyPropertyDto cannot be null!!");
        final KeyPropertyDto keyPropertyDto = new KeyPropertyDto(keyPropertyLightDto.getId(), keyPropertyLightDto.getName(), keyPropertyLightDto.getValues());
        
        final KeyProperty keyProperty = application.geKeyPropertyByName(keyPropertyDto.getName());
        keyPropertyDto.setModuleXPropertyList(this.buildModuleXPropertyList(keyProperty, application));
        keyPropertyDto.setInterfaceXGeneratorList(this.buildInterfaceXGeneratorList(keyProperty, application));
        keyPropertyDto.setInterfaceXImplementationList(this.buildInterfaceXImplementationList(keyProperty, application));
        
		return keyPropertyDto;
	}


    private List<InterfaceXImplementationDto> buildInterfaceXImplementationList(final KeyProperty keyProperty, final Application application) {
    	log.info("buildInterfaceXImplementationList()");
    	
    	final  List<InterfaceXImplementationDto> interfaceImplementationList = new ArrayList<InterfaceXImplementationDto>();
		if (keyProperty == null) return interfaceImplementationList;
		
		final List<MyInterface> interfaceList = application.getInterfaceList();
		if (interfaceList == null) return interfaceImplementationList;
		
		// for each interface
		for (MyInterface myInterface : interfaceList) {
			
			final Map<Module, List<ReplaceWith>> mapReplaceWithByModule =
					ModuleHelperUtils.getInstance().buildReplaceWithListForInterfaceAndModuleList(myInterface.getName(), application.getModuleList(), application);
		
			if (mapReplaceWithByModule != null) {
				
			  //for each module
			  for (Module module : mapReplaceWithByModule.keySet()) {
				final List<ReplaceWith> listReplaceWithForInterfaceAndModule = mapReplaceWithByModule.get(module);
			    this.filterReplaceOrGenerateWithForProperty(keyProperty, listReplaceWithForInterfaceAndModule);
			    
			    final UltraLightDto moduleUltraLightDto = new UltraLightDto(module.getId(), module.getName());
			    for (ReplaceWith replaceWith : listReplaceWithForInterfaceAndModule) {
			    	interfaceImplementationList.add(this.buildInterfaceXImplementationDto(replaceWith, moduleUltraLightDto));
				}
			  }
			}			
		}
		
    	return interfaceImplementationList;
    }
    

    private List<InterfaceXGeneratorDto> buildInterfaceXGeneratorList(final KeyProperty keyProperty, final Application application) {
    	log.info("buildInterfaceXGeneratorList()");
    	
    	final  List<InterfaceXGeneratorDto> interfaceGeneratorList = new ArrayList<InterfaceXGeneratorDto>();
		if (keyProperty == null) return interfaceGeneratorList;
		
		final List<MyInterface> interfaceList = application.getInterfaceList();
		if (interfaceList == null) return interfaceGeneratorList;
		
		// for each interface
		for (MyInterface myInterface : interfaceList) {
			
			final Map<Module, List<GenerateWith>> mapGenerateWithByModule =
					ModuleHelperUtils.getInstance().buildGenerateWithListForInterfaceAndModuleList(myInterface.getName(), application.getModuleList(), application);
		
			if (mapGenerateWithByModule != null) {
				
			  //for each module
			  for (Module module : mapGenerateWithByModule.keySet()) {
				final List<GenerateWith> listGenerateWithForInterfaceAndModule= mapGenerateWithByModule.get(module);
			    this.filterReplaceOrGenerateWithForProperty(keyProperty, listGenerateWithForInterfaceAndModule);
			    
			    final UltraLightDto moduleUltraLightDto = new UltraLightDto(module.getId(), module.getName());
			    for (GenerateWith replaceWith : listGenerateWithForInterfaceAndModule)
			    	interfaceGeneratorList.add(this.buildInterfaceXGeneratorDto(replaceWith, moduleUltraLightDto));
				}
			  }
						
		}
		
    	return interfaceGeneratorList;
    }
    /**
     * Ne retenir que les ReplaceWith ou GenerateWith qui contienne la property
     * @param keyProperty
     * @param listReplaceOrGenerateWithByModule
     */
    private void filterReplaceOrGenerateWithForProperty(final KeyProperty keyProperty,
    		final List<? extends AbstractReplaceOrGenerateWith> listReplaceOrGenerateWithByModule) {
    	
    	final Iterator<? extends AbstractReplaceOrGenerateWith> iter = listReplaceOrGenerateWithByModule.iterator();
    	while (iter.hasNext()) {
			final AbstractReplaceOrGenerateWith replaceOrGenerateWith = iter.next();
			if (!replaceOrGenerateWith.hasKeyProperty(keyProperty)) iter.remove();
		}
    }
    
    
	private List<ModuleXPropertyDto> buildModuleXPropertyList (final KeyProperty keyProperty, final Application application) {
		
		log.info("buildModuleXPropertyList()");
		
		final  List<ModuleXPropertyDto> modulePropertyList = new ArrayList<ModuleXPropertyDto>();
		if (keyProperty == null) return modulePropertyList;
		
		// liste de tous les modules
		final List<Module> moduleList = application.getModuleList();
		if (moduleList == null) return modulePropertyList;
		
		// for each module
		for (Module module : moduleList) {
			final IXmlGwtFile xmlGwtFile = module.getXmlGwtFile();
			final Property setProperty = xmlGwtFile.getStandardSetProperty(keyProperty);
			final Property defineProperty = xmlGwtFile.getDefineProperties(keyProperty);
			final List<ConditionalProperty> conditionalProperties = xmlGwtFile.getConditionalSetProperties(keyProperty);
			if (setProperty == null && defineProperty == null && conditionalProperties == null) continue; //next module
			
			modulePropertyList.add(this.buildModuleXPropertyDto(module, this.buildKeyPropertyLightDto(keyProperty),
					setProperty, defineProperty, conditionalProperties));
		}
		
		return modulePropertyList;
	}
	
	
	public List<CompilationLightDto> loadCompilations(final Application application) {
		final List<CompilationLightDto> moduleDtoList = new ArrayList<CompilationLightDto>();
		
		if (!application.hasCompilations()) return moduleDtoList;
		
		for (Compilation compilation : application.getCompilationList()) {
			moduleDtoList.add(buildCompilationLightDto(compilation));
		}

		return moduleDtoList;
    }
	public CompilationDto loadCompilation(final Application application, final String compilationName) {
		
		if (!application.hasCompilations()) return null;
		
		final Compilation compilation = application.getCompilationByName(compilationName);
		return this.buildCompilationDto(compilation, application);
	}
	public List<ModuleLightDto> loadModules(final Application application) {
		
		final List<ModuleLightDto> moduleDtoList = new ArrayList<ModuleLightDto>();
		
		if (!application.hasModules()) return moduleDtoList;
		
		for (Module module : application.getModuleList()) {
			moduleDtoList.add(buildModuleLightDto(module));
		}
		return moduleDtoList;
	}
	
	public ModuleDto loadModule(final String moduleName, final Application application) {
		
		final Module module = application.getModuleByName(moduleName);
		return this.buildModuleDto(module);
	}


  //----------------------------------------------------------- protected methods	
    protected InterfaceXImplementationDto buildInterfaceXImplementationDto(final ReplaceWith replaceWith,
    		final UltraLightDto moduleUltraLightDto) {
    	
    	return (InterfaceXImplementationDto)this._buildInterfaceXImplementationOrGenerateDto(replaceWith, moduleUltraLightDto);
    }
    protected InterfaceXGeneratorDto buildInterfaceXGeneratorDto(final GenerateWith generateWith,
    		final UltraLightDto moduleUltraLightDto) {
    	
    	return (InterfaceXGeneratorDto)this._buildInterfaceXImplementationOrGenerateDto(generateWith, moduleUltraLightDto);
    }
    private AbstractInterfaceXImplementationOrGeneratorDto _buildInterfaceXImplementationOrGenerateDto(final AbstractReplaceOrGenerateWith replaceOrGenerateWith,
    		final UltraLightDto moduleUltraLightDto) {
    	
    	final AbstractInterfaceXImplementationOrGeneratorDto interfaceXImplementationOrGeneratorDto = 
    			(replaceOrGenerateWith.isReplace())?
    			new InterfaceXImplementationDto(this.buildInterfaceDto(replaceOrGenerateWith.getMyInterface()), 
    					((ReplaceWith)replaceOrGenerateWith).getImplementation(), moduleUltraLightDto ):
    			new InterfaceXGeneratorDto(this.buildInterfaceDto(replaceOrGenerateWith.getMyInterface()),
    					((GenerateWith)replaceOrGenerateWith).getGenerator(),moduleUltraLightDto);
    	
    	if (replaceOrGenerateWith.hasRules()) {
    	   for (Rules rules : replaceOrGenerateWith.getRulesList()) {
			    
    		   final List<String> displayPropertyList = new ArrayList<String>(rules.getProperties().size());
    		   for (Property property : rules.getProperties()) {
    			   displayPropertyList.add(property.getDisplayKeyValue(ISharedConstants.SEPARATOR_KEY_VALUE));
			   }
    		   final String displayRules = StringHelper.valuesToString(displayPropertyList, ISharedConstants.SEPARATOR_PROPERTY);
    		   interfaceXImplementationOrGeneratorDto.setRules(rules.getTypeRule(), displayRules);
		    }
    	}
    	return interfaceXImplementationOrGeneratorDto;
    }
    
    //----------------------------------------------------------- private methods
	private MyInterfaceDto buildInterfaceDto (final MyInterface myInterface) {
		
		final List<String> implementationList = (myInterface.hasImplementations())?
				new ArrayList<String>(myInterface.getImplementationList()):null;
		final List<String> generatorList = (myInterface.hasGenerators())?
				new ArrayList<String>(myInterface.getGeneratorList()):null;
				
		return new MyInterfaceDto(myInterface.getId(), myInterface.getName(), implementationList, generatorList);
	}

	private ModuleXPropertyDto buildModuleXPropertyDto(final Module module, final KeyPropertyLightDto keyProperty,
			final Property setProperty, final Property defineProperty, final List<ConditionalProperty> conditionalProperties) {
		
		final ModuleXPropertyDto modulePropertyDto = new ModuleXPropertyDto(this.buildModuleLightDto(module), keyProperty);
		
		// setPropery no conditional
		if (setProperty != null) {
			  modulePropertyDto.setSetValues(setProperty.getValues());
		}
		// define property (or extended)
		if (defineProperty != null) {
			if (defineProperty.getQualifier() == Qualifier.extend) {
				modulePropertyDto.setExtendValues(defineProperty.getValues());
			}
			else {
				modulePropertyDto.setDefineValues(defineProperty.getValues());
			}
		}
		
		// conditional properties
		if (conditionalProperties != null) {
			
			for (ConditionalProperty conditionalProperty : conditionalProperties) {
				
				//build PropetyRulesDto
				final PropertyRulesDto propertyRulesDto = this._buildPropertyRulesDto(keyProperty, conditionalProperty);
				modulePropertyDto.addPropertyRules(propertyRulesDto);
			}
			

		}
		return modulePropertyDto;
	}
	private PropertyRulesDto _buildPropertyRulesDto(final KeyPropertyLightDto keyProperty, final ConditionalProperty conditionalProperty) {
		
		//build PropetyRulesDto
		final PropertyRulesDto propertyRulesDto = new PropertyRulesDto(keyProperty, conditionalProperty.getValue());
		
		// Ajouter les rules
		if (conditionalProperty.hasRules()) {
	    	   for (Rules rules : conditionalProperty.getRulesList()) {
				    
	    		   final List<String> displayPropertyList = new ArrayList<String>(rules.getProperties().size());
	    		   for (Property property : rules.getProperties()) {
	    			   displayPropertyList.add(property.getDisplayKeyValue(ISharedConstants.SEPARATOR_KEY_VALUE));
				   }
	    		   final String displayRules = StringHelper.valuesToString(displayPropertyList, ISharedConstants.SEPARATOR_PROPERTY);
	    		   propertyRulesDto.setRules(rules.getTypeRule(), displayRules);
			    }
	    }
		return propertyRulesDto;
		
	}
	private KeyPropertyLightDto buildKeyPropertyLightDto (final KeyProperty keyProperty) {
		return new KeyPropertyLightDto(keyProperty.getId(), keyProperty.getName(), keyProperty.getValues());
	}

	private ModuleDto buildModuleDto(final Module module) {
		final ModuleDto moduleDto = new ModuleDto(module.getId(), module.getName(), module.getPackage(),
				module.getXmlGwtFile().getRenameTo(), module.getXmlGwtFile().getFileName(),
				module.getXmlGwtFile().getEntryPoint(), module.isVirtual(), module.getXmlGwtFile().getContent());

        final IXmlGwtFile xmlGwtFile = module.getXmlGwtFile();
        
        // ----properties        
        final Map<KeyProperty, ModuleXPropertyDto> mapModulePropertyByKey = new TreeMap<KeyProperty, ModuleXPropertyDto>();      
       
    	// set properties (with conditional)
        if (xmlGwtFile.hasSetProperties(true)) {
        	
        	// set Property
        	for (Property property : xmlGwtFile.getSetPropertyList(true)) {
        		
        		final boolean isConditional = property.isConditional();
        		List<ConditionalProperty> listConditionalProperties = null;
        		
				final KeyProperty  keyProperty = property.getKey();
				final KeyPropertyLightDto keyPropertyLightDto = this.buildKeyPropertyLightDto(keyProperty);
				
				if (!mapModulePropertyByKey.containsKey(keyProperty)) {
					if (isConditional) {
						listConditionalProperties = new ArrayList<ConditionalProperty>(1);
						listConditionalProperties.add((ConditionalProperty)property);
					}
					mapModulePropertyByKey.put(keyProperty, 
					  this.buildModuleXPropertyDto(module, keyPropertyLightDto,
							  ((isConditional)?null:property), null, ((isConditional)?listConditionalProperties:null)));
				} else {
				  final ModuleXPropertyDto modulePropertyDto = mapModulePropertyByKey.get(keyProperty);
				  if (isConditional) {
					  final PropertyRulesDto propertyRulesDto = this._buildPropertyRulesDto(keyPropertyLightDto, (ConditionalProperty)property);
					  modulePropertyDto.addPropertyRules(propertyRulesDto);
				  }
				  else {
				     modulePropertyDto.setSetValues(property.getValues());
				  }
				}
			}
        	
        	// conditional properties
        }
        
        // define properties
		if (xmlGwtFile.hasDefineProperties()) {
			for (Property property : xmlGwtFile.getDefinePropertyList()) {
				
				final KeyProperty  keyProperty = property.getKey();
				if (!mapModulePropertyByKey.containsKey(keyProperty)) {
					mapModulePropertyByKey.put(keyProperty, 
					  this.buildModuleXPropertyDto(module, this.buildKeyPropertyLightDto(keyProperty), null, property, null));
				} else {
				  final ModuleXPropertyDto modulePropertyDto = mapModulePropertyByKey.get(keyProperty);
					if (property.getQualifier() == Qualifier.extend) {
						modulePropertyDto.setExtendValues(property.getValues());
					}
					else {
						modulePropertyDto.setDefineValues(property.getValues());
					}				  
				}				
			}
		}
		if (!mapModulePropertyByKey.isEmpty()) { 
		   moduleDto.getModuleXPropertyList().addAll(mapModulePropertyByKey.values());
		}
		
		// ----import modules
		if (module.hasImports()) {
			for (Module importModule : module.getImportModulesList(false)) {
				moduleDto.addImportModule(this.buildModuleLightDto(importModule));
			}
		}
		
		// ----Interfaces
		final UltraLightDto moduleUltraLightDto = new UltraLightDto(module.getId(), module.getName());
		if (xmlGwtFile.hasReplaceWith()) {
			for (ReplaceWith replaceWith : xmlGwtFile.getReplaceWithList()) {
				moduleDto.addInterfaceXImplementation(this.buildInterfaceXImplementationDto(replaceWith, moduleUltraLightDto));
			}
		}
		if (xmlGwtFile.hasGenerateWith()) {
			for (GenerateWith generateWith : xmlGwtFile.getGenerateWithList()) {
				moduleDto.addInterfaceXGenerator(this.buildInterfaceXGeneratorDto(generateWith, moduleUltraLightDto));
			}
		}
        
		return moduleDto;
	}
	private ModuleLightDto buildModuleLightDto(final Module module) {
		final ModuleLightDto  moduleDto = new ModuleLightDto(module.getId(), module.getName(), module.getPackage(), 
				module.getXmlGwtFile().getRenameTo(),  module.getXmlGwtFile().getFileName(),
				module.getXmlGwtFile().getEntryPoint(), module.isVirtual());
		return moduleDto;
	}
	private CompilationDto buildCompilationDto(final Compilation compilation, final Application application) {
		
		 // ne définir les permutations qu'à la demande
		if (compilation.isToComplete()) {
		 PermutationUtils.getInstance().buildPermutations(compilation, application.getSessionId());
		}
		
		final CompilationDto compilationDto = new CompilationDto(compilation.getId(), compilation.getName()); 
		final ModuleLightDto mainModuleDto = this.buildModuleLightDto(compilation.getModule());
		final List<Module> compilationModuleList = compilation.getCompilationModuleList();
		
		compilationDto.setMainModule(mainModuleDto);
		
		// ---------list of modules
		if (compilationModuleList != null) {
		  for (Module module : compilationModuleList) {
			compilationDto.getModuleList().add(this.buildModuleLightDto(module));
		  }
		}
				
		// ----------list of compilation properties (set and conditionnalProperties)
		final boolean hasConditionnalProperties = compilation.hasConditionalProperties();
		if (compilation.hasSetProperties()) {
			
			final List<CompilationPropertyDto> compilationPropertiesList = compilationDto.getCompilationPropertyList();
			
			final Set<KeyProperty> keyPropertyManaged = new HashSet<KeyProperty>();
			for (Property property : compilation.getSetPropertyList()) {
				
				keyPropertyManaged.add(property.getKey());
				final CompilationPropertyDto compilationPropertyDto = this.buildCompilationPropertyDto(property);
				
				// add conditional properties
				if (hasConditionnalProperties) {
					final KeyProperty keyProperty = property.getKey();
				
					final List<ConditionalProperty> conditionnalProperties =
							compilation.getConditionalPropertiesByKey(property.getKey());
					if (conditionnalProperties != null && !conditionnalProperties.isEmpty()) {
						final KeyPropertyLightDto keyPropertyLightDto = this.buildKeyPropertyLightDto(keyProperty);
						for (ConditionalProperty conditionalProperty : conditionnalProperties) {
							compilationPropertyDto.addPropertyRules(this._buildPropertyRulesDto(keyPropertyLightDto, conditionalProperty));
						}
					}
				}
				compilationPropertiesList.add(compilationPropertyDto);
			}
			
			//Traiter les conditional properties restantes
			final Set<KeyProperty> keyPropertyForConditionalProperties = compilation.getKeyPropertiesForConditionalProperties();
		    for (KeyProperty keyPropertyForConditionalProperty : keyPropertyForConditionalProperties) {
				if (keyPropertyManaged.contains(keyPropertyForConditionalProperty)) continue; // next key
				
				// key not managed yet
				final List<ConditionalProperty> conditionnalProperties =
						compilation.getConditionalPropertiesByKey(keyPropertyForConditionalProperty);
				if (conditionnalProperties != null && !conditionnalProperties.isEmpty()) {				
					compilationPropertiesList.add(this.buildCompilationPropertyDto(keyPropertyForConditionalProperty, conditionnalProperties));
				}
				
			}
		    
		    // ordonner la liste
		    Collections.sort(compilationPropertiesList);
		
		}
		
		// ---------- list of permutations

		final List<PermutationDto> permutationDtoList = new ArrayList<PermutationDto>(compilation.getPermutationCount());
		// for each permutation
		for (Permutation permutation : compilation.getPermutationList()) {
			permutationDtoList.add(this.buildPermutationDto(permutation, compilationModuleList, application));
		}
		compilationDto.setPermutationList(permutationDtoList);
		
		return compilationDto;
	}
	/**
	 * 
	 * @param permutation
	 * @param compilationModuleList : par ordre croissant (+faible --> plus fort)
	 * @param application
	 * @return
	 */
	 private PermutationDto buildPermutationDto(
			 final Permutation permutation, final List<Module> compilationModuleList, final Application application) {
		 log.info("buildPermutationDto()");
		 final List<CompilationPropertyDto> compilationPropertyList = new ArrayList<CompilationPropertyDto>();
		 if (permutation.hasProperties()) {
			 for (Property property : permutation.getPropertyList()) {
				 compilationPropertyList.add(this.buildCompilationPropertyDto(property));
			}
			// ordonner
			 Collections.sort(compilationPropertyList);
		 }
		 
		 final PermutationDto permutationDto = 
				 new PermutationDto(permutation.getCompilation().getName(), compilationPropertyList);
		 
			
		 // Inverser l'ordre des modules (+fort --> plus faible)
		 final List<Module> inverseCompilationModuleList = new ArrayList<Module>(compilationModuleList);
		 Collections.reverse(inverseCompilationModuleList);
		 
		 // liste des interfaces et implementations
		 final List<InterfaceXImplementationDto> implementationList =
		          PermutationUtils.getInstance().buildInterfaceXImplementationList(permutation, inverseCompilationModuleList, application);
		permutationDto.setInterfaceXImplementationList(implementationList);
		
		// liste des interfaces et generators	
		 final List<InterfaceXGeneratorDto> generatorList =
				 PermutationUtils.getInstance().buildInterfaceXGeneratorList(permutation, inverseCompilationModuleList, application);
		permutationDto.setInterfaceXGeneratorList(generatorList);
		
		// liste des delta properties (celles qui sont différentes s'une permutation à l'autre)
        final Compilation compilation = permutation.getCompilation();
        final List<PropertyLightDto> deltaProperties = new ArrayList<PropertyLightDto>();
        for (Property property : compilation.getSetPropertyList()) {
			if (property.isMonoValue()) continue;
			
			final Property monoValueProperty = permutation.getProperty(property.getKey());
			deltaProperties.add(this.buildPropertyLightDto(monoValueProperty));
		}
		permutationDto.setListDeltaProperties(deltaProperties);

		 return permutationDto;
	 }
	 
	 //CompilationPropertyDto [key - value "" - list conditional property]
	 private CompilationPropertyDto buildCompilationPropertyDto(final KeyProperty keyProperty, final List<ConditionalProperty> conditionalProperties) {
		 //conditional property
		 // create a set property with no value
		 final PropertyLightDto propertyLightDto =
				 new PropertyLightDto(new UltraLightDto(keyProperty.getId(), keyProperty.getName()), "");

		 final KeyPropertyLightDto keyPropertyLightDto = this.buildKeyPropertyLightDto(keyProperty);
		 final List<PropertyRulesDto> listPropertyRules = new ArrayList<PropertyRulesDto>();
		 
		 if (conditionalProperties != null) {
		   for (ConditionalProperty conditionalProperty : conditionalProperties) {
			 listPropertyRules.add(this._buildPropertyRulesDto(keyPropertyLightDto,conditionalProperty));
		   }
		 }
		 
		 return new CompilationPropertyDto(propertyLightDto, listPropertyRules);

	 }
	 //CompilationPropertyDto [key - set values - no conditional]
	 private CompilationPropertyDto buildCompilationPropertyDto(final Property property) {
		 
		 if (!property.isConditional()) {
			 return new CompilationPropertyDto(this.buildPropertyLightDto(property));
		 }
		 else {
			 final List<ConditionalProperty> conditionalProperties = new ArrayList<ConditionalProperty>(1);
			 conditionalProperties.add((ConditionalProperty)property);
			 return this.buildCompilationPropertyDto(property.getKey(), conditionalProperties);
		 }

	 }
	private PropertyLightDto buildPropertyLightDto (final Property property) {
		
		KeyProperty keyProperty = property.getKey();
		return new PropertyLightDto(new UltraLightDto(keyProperty.getId(), keyProperty.getName()), property.getValueList());
	}
	private CompilationLightDto buildCompilationLightDto (final Compilation compilation) {
		final CompilationLightDto compilationDto = new CompilationLightDto(compilation.getId(), compilation.getName());
		final ModuleLightDto mainModuleDto = this.buildModuleLightDto(compilation.getModule());
		
		compilationDto.setMainModule(mainModuleDto);

		return compilationDto;
	}



}
