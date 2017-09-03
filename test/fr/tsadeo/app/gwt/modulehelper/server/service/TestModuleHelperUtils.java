package fr.tsadeo.app.gwt.modulehelper.server.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;

import fr.tsadeo.app.gwt.modulehelper.server.model.AbstractReplaceOrGenerateWith.Rules;
import fr.tsadeo.app.gwt.modulehelper.server.model.Compilation;
import fr.tsadeo.app.gwt.modulehelper.server.model.ConditionalProperty;
import fr.tsadeo.app.gwt.modulehelper.server.model.IXmlGwtFile;
import fr.tsadeo.app.gwt.modulehelper.server.model.KeyProperty;
import fr.tsadeo.app.gwt.modulehelper.server.model.Module;
import fr.tsadeo.app.gwt.modulehelper.server.model.MyInterface;
import fr.tsadeo.app.gwt.modulehelper.server.model.Permutation;
import fr.tsadeo.app.gwt.modulehelper.server.model.Property;

public class TestModuleHelperUtils extends AbstractModuleHelperUtils {

	private static final Log log = LogFactory.getLog(TestModuleHelperUtils.class);



	@Test
	public void testBuildDemo() {

		Assert.assertNotNull("application cannot be null!", application);
		//========================================================================
		log.info("");
		log.info("LISTE DES INTERFACES");
		final List<MyInterface> interfaceList = application.getInterfaceList();	
		Assert.assertNotNull("interfaceList cannot be null!", interfaceList);
		
		for (MyInterface myInterface : interfaceList) {
			log.info("");
			log.info("myInterface id : " + myInterface.getId() + " - name: " + myInterface.getName());
			if (myInterface.hasImplementations()) {
				for (String implementation : myInterface.getImplementationList()) {
					log.info(">> implementation: " + implementation);
				}
			}
			if(myInterface.hasGenerators()) {
				for (String generator : myInterface.getGeneratorList()) {
					log.info(">> generator: " + generator);
				}
			}
		}
				
		//========================================================================
		log.info("");
		log.info("LISTE DES COMPILATIONS");
		final List<Compilation> compilationList = application.getCompilationList();
		Assert.assertNotNull("compilationList cannot be null!", compilationList);
		
		for (Compilation compilation : compilationList) {
			log.info("");
			log.info("compilation id: " + compilation.getId() + " - name: " + compilation.getName());
			
			if(compilation.hasSetProperties()) {
				
				for (Property property : compilation.getSetPropertyList()) {
					log.info(">> set-property: " + property.toString());
				}
			}
			
			if (compilation.hasConditionalProperties()) {
				for (KeyProperty keyProperty : compilation.getKeyPropertiesForConditionalProperties()) {
					final List<ConditionalProperty> listConditionalProperties =
							compilation.getConditionalPropertiesByKey(keyProperty);
					for (ConditionalProperty conditionalProperty : listConditionalProperties) {
						log.info(">> set-property[conditional]: " + conditionalProperty.toString());
					}
					
				}
			}
			
			if (!compilation.isToComplete()) {
			Assert.assertTrue("compilation must have at least one permutation", compilation.hasPermutations());
			int compteur = 0;
			for (Permutation permutation : compilation.getPermutationList()) {
				log.info(">> permutation: " + compteur++);
				if (permutation.hasProperties()) {
					for (Property property : permutation.getPropertyList()) {
						log.info(">>>> property: " + property.toString());
					}
				}
			}
			}
		}

		//========================================================================
		log.info("");
		log.info("LISTE DES MODULES");
		final List<Module> moduleList = application.getModuleList();
		Assert.assertNotNull("moduleList cannot be null!", moduleList);

		for (Module module : moduleList) {

			
			//---------------------------------------------------- Module
			log.info("");
			log.info("module id: " + module.getId() + " - name: " + module.getName() + " - virtual: " + module.isVirtual());
            if (module.hasImports()) {
            	for (Module importModule : module.getImportModulesList(false)) {
            		log.info(">> import module: " + importModule.getName());
				}
            }
            if (module.hasSons()) {
            	for (Module sonModule : module.getSonModuleList()) {
            		log.info(">> son module: " + sonModule.getName());
				}
            }
			
			//---------------------------------------------------- XmlGwtFile
			final IXmlGwtFile xmlGwtFile = module.getXmlGwtFile();
			Assert.assertNotNull("XmlGwtFile cannot be null!", xmlGwtFile);
			log.info("rename-to: " + xmlGwtFile.getRenameTo());
			log.info("complete name: " + xmlGwtFile.getCompleteName());
			log.info("package: " + xmlGwtFile.getPackage());
			log.info("filename: " + xmlGwtFile.getFileName());


			if (xmlGwtFile.getInheritList() != null) {
				for (String inherit : xmlGwtFile.getInheritList()) {
					log.info(">> inherit: " + inherit);
				}
			}

			if (xmlGwtFile.hasImports()) {
				for (IXmlGwtFile importXmlGwtFile : xmlGwtFile
						.getImportXmlGwtFileList()) {
					log.info(">> import: " + importXmlGwtFile.getCompleteName());
				}
			}

			if (xmlGwtFile.hasSetProperties(true)) {
				for (Property property : xmlGwtFile.getSetPropertyList(true)) {
					log.info(">> set-property: " + property.toString());
					if (property.isConditional()) {
						final ConditionalProperty conditionalProperty = (ConditionalProperty)property;
						final List<Rules> listRules = conditionalProperty.getRulesList();
						for (Rules rules : listRules) {
							log.info(">>>> rule: " + rules.toString());
						}
					}
				}
			}

			if (xmlGwtFile.hasDefineProperties()) {
				for (Property property : xmlGwtFile.getDefinePropertyList()) {
					log.info(">> define-property: " + property.toString());
				}
			}
			final List<String> content =  xmlGwtFile.getContent();
			log.info(">> content: " + ((content != null)?content.toString():""));

		}
	}
	
	
	
	
	
}
