package fr.tsadeo.app.gwt.modulehelper.server.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.tsadeo.app.gwt.modulehelper.server.model.AbstractReplaceOrGenerateWith;
import fr.tsadeo.app.gwt.modulehelper.server.model.AbstractReplaceOrGenerateWith.Rules;
import fr.tsadeo.app.gwt.modulehelper.server.model.Application;
import fr.tsadeo.app.gwt.modulehelper.server.model.Compilation;
import fr.tsadeo.app.gwt.modulehelper.server.model.ConditionalProperty;
import fr.tsadeo.app.gwt.modulehelper.server.model.GenerateWith;
import fr.tsadeo.app.gwt.modulehelper.server.model.IRulesContainer;
import fr.tsadeo.app.gwt.modulehelper.server.model.KeyProperty;
import fr.tsadeo.app.gwt.modulehelper.server.model.Module;
import fr.tsadeo.app.gwt.modulehelper.server.model.MyInterface;
import fr.tsadeo.app.gwt.modulehelper.server.model.Permutation;
import fr.tsadeo.app.gwt.modulehelper.server.model.Property;
import fr.tsadeo.app.gwt.modulehelper.server.model.ReplaceWith;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.InterfaceXGeneratorDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.InterfaceXImplementationDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.UltraLightDto;
import fr.tsadeo.app.gwt.modulehelper.shared.util.ISharedConstants.TypeRule;

public class PermutationUtils {
	
	private static final Log log = LogFactory.getLog(PermutationUtils.class);
	
	private static final PermutationUtils instance = new PermutationUtils();
	public static final PermutationUtils getInstance() {
		return instance;
	}
	
	private static final ModuleHelperUtils moduleHelperUtils = ModuleHelperUtils.getInstance();
	
	//------------------------------------------------------------- Implementation
	
	  private boolean applyRules (final Rules rules, final List<Property> permutationPropertyList) {
		  
		  if (rules == null) return true;
		  final List<Property> ruleProperyList = rules.getProperties();
		  
		  switch (rules.getTypeRule()) {
		  
		  case none:
			  for (Property propertyRule : ruleProperyList) {
			    if (permutationPropertyList.contains(propertyRule)) return false;
		      }
			  return true;

		  case all:	
			  for (Property propertyRule : ruleProperyList) {
				    if (!permutationPropertyList.contains(propertyRule)) return false;
			   }
			  return true;
				
		  case any:		
			  for (Property propertyRule : ruleProperyList) {
				    if (permutationPropertyList.contains(propertyRule)) return true;
			   }
			  return false;
		}		  
		  return false;
	  }
	  

	  private boolean applyRules (final IRulesContainer rulesContainer, final List<Property> permutationPropertyList) {
		  
		  // for each Rules : un seul echec et ReplaceWith est éliminé
			 for (TypeRule typeRule : TypeRule.values()) {
			 
				final Rules rules = rulesContainer.getRules(typeRule);
			     if(!this.applyRules(rules, permutationPropertyList)) return false;	
			}
			 // Toutes les règles ont été appliquées avec succès
			 return true;
	  }
	  
	  

	  /**
	   * Pour un module donné c'est la dernière implémentation trouvée qui est la bonne
	   * Il faut donc prendre la liste des replace-with à l'envers
	   * @param replaceWithListByInterface
	   * @param permutationPropertyList
	   * @return
	   */
	  private AbstractReplaceOrGenerateWith findReplaceOrGenerateWithWithRules (
			  final List<? extends AbstractReplaceOrGenerateWith> replaceWithListByInterface, final List<Property> permutationPropertyList) {
		  
		  if (replaceWithListByInterface == null || replaceWithListByInterface.isEmpty() || permutationPropertyList == null || permutationPropertyList.isEmpty()) return null;
		  AbstractReplaceOrGenerateWith result = null;

		  // for each ReplaceWith (liste inversée)
		  final int size = replaceWithListByInterface.size();
		  for (int i = size - 1; i >= 0; i--) {
			  AbstractReplaceOrGenerateWith replaceOrGenerateWith = replaceWithListByInterface.get(i);

			 if (this.applyRules(replaceOrGenerateWith, permutationPropertyList)) {
				// Toutes les règles ont été appliquées avec succès
				 result = replaceOrGenerateWith;	  
				 break;
			 }	 

		 }
		   	 
		return result;
	  }
	  

	 /**
	  * 
	  * @param permutation
	  * @param inverseCompilationModuleList : ordre des modules (+fort --> plus faible)
	  * @param application
	  * @return
	  */
	 protected List<InterfaceXImplementationDto> buildInterfaceXImplementationList (final Permutation permutation,
			 final List<Module> inverseCompilationModuleList,  final Application application) {
		  
		// log.info("buildInterfaceXImplementationList()");
		  // Properties of permutation
		  final List<Property> propertyList = permutation.getPropertyList();
		  
		  final List<MyInterface> interfaceList = application.getInterfaceList();
		  if (interfaceList == null) return null;
		  
		  final List<InterfaceXImplementationDto> implementationListAfterRules = new ArrayList<InterfaceXImplementationDto>();
		  
		  // for each interface -->> implementation pour les modules de la permutation
		  for (MyInterface myInterface : interfaceList) {

			  
			  if (!myInterface.hasImplementations()) {
				  continue; // next interface
			  }
			  //log.info("interface >> " + myInterface.getName());			  
			  
			  final Map<Module, List<ReplaceWith>> mapReplaceWithByModule = 
					  moduleHelperUtils.buildReplaceWithListForInterfaceAndModuleList(myInterface.getName(), inverseCompilationModuleList, application);
			  
			if (mapReplaceWithByModule != null && !mapReplaceWithByModule.isEmpty()) {
				
				// dernière implementation du premier module trouvée est la bonne
				// car la liste des modules est du +fort au +faible (liste inversée)
				for (Module module :inverseCompilationModuleList) {
			
					if (module.isVirtual()) {
						continue; //next module
					}
					//log.info(" >> module: " + module.getName());
					  // chercher l'implementation pour cette permutation	
					final ReplaceWith replaceWith = (ReplaceWith)
							this.findReplaceOrGenerateWithWithRules(mapReplaceWithByModule.get(module), propertyList);
					if (replaceWith != null) {
						//log.info("  >> add implementation: " + replaceWith.getImplementation());
						implementationListAfterRules.add(
								ModuleHelperDtoUtils.getInstance().buildInterfaceXImplementationDto(replaceWith,
										new UltraLightDto(module.getId(), module.getName())));
						break;
					}
				}
			}
			
		  }

		  
	      return implementationListAfterRules;
	  }
	 /**
	  * 
	  * @param permutation
	  * @param inverseCompilationModuleList ordre des modules (+fort --> plus faible)
	  * @param application
	  * @return
	  */
	 protected List<InterfaceXGeneratorDto> buildInterfaceXGeneratorList (final Permutation permutation,
			 final List<Module> inverseCompilationModuleList,  final Application application) {
		  
		 //log.info("buildInterfaceXGeneratorList()");
		  // Properties of permutation
		  final List<Property> propertyList = permutation.getPropertyList();
		  
		  final List<MyInterface> interfaceList = application.getInterfaceList();
		  if (interfaceList == null) return null;
		  
		  final List<InterfaceXGeneratorDto> generatorListAfterRules = new ArrayList<InterfaceXGeneratorDto>();
		  
		  // for each interface -->> generator pour les modules de la permutation
		  for (MyInterface myInterface : interfaceList) {

			  if (!myInterface.hasGenerators()) {
				  continue; // next
			  }
			  //log.info("interface >> " + myInterface.getName());
			  
			  final Map<Module, List<GenerateWith>> mapGenerateWithByModule = 
					  moduleHelperUtils.buildGenerateWithListForInterfaceAndModuleList(myInterface.getName(), inverseCompilationModuleList, application);
			  
			if (mapGenerateWithByModule != null) {
				for (Module module : inverseCompilationModuleList) {
					
					if (module.isVirtual()) {
						continue; //next module
					}
					//log.info(" >> module: " + module.getName());
					 // chercher le generator pour cette permutation	
					// première implementation trouvée est la bonne
					// car la liste des modules est du +fort au +faible (liste inversée)
					final GenerateWith generateWith = (GenerateWith)
							this.findReplaceOrGenerateWithWithRules(mapGenerateWithByModule.get(module), propertyList);
					if (generateWith != null) {
						generatorListAfterRules.add(
								ModuleHelperDtoUtils.getInstance().buildInterfaceXGeneratorDto(generateWith,
										new UltraLightDto(module.getId(), module.getName())));
						break;
					}
				}
			}
			
		  }

		  
	      return generatorListAfterRules;
	  }
	//--------------------------------------------------------------- permutations
	protected void buildPermutations(final Compilation compilation, final String sessionId) {
		
		if (compilation == null) return;
		
		final List<Property> compilationPropertyList = compilation.getSetPropertyList();
		final int compilationPropertyCount = compilationPropertyList.size();
		
		// init la liste des permutations avec la première proprieté
		final List<Permutation> permutationListInit = 
				this.buildPermutationFromProperty(compilation, 
						(compilation.hasSetProperties())?compilationPropertyList.get(0):null);
		
		// only one property
		if (compilationPropertyCount <= 1) {
			compilation.addPermutations(permutationListInit);
			return;
		}
		
		List<Permutation> currentListPermutation = permutationListInit;
		
		// produits Permutation X Properties
		for (int i = 1; i < compilationPropertyCount; i++) {
			currentListPermutation =
			   this.produitPermutationListXProperty(currentListPermutation, compilationPropertyList.get(i));
		}
		
		compilation.addPermutations(currentListPermutation);
		
		//Traiter les properties conditionnelles....
		if (compilation.hasConditionalProperties()) {
			
			final Set<KeyProperty> keyConditionalPropertySet = compilation.getKeyPropertiesForConditionalProperties();
			         
			// for each permutation
			for (Permutation permutation : currentListPermutation) {
				this.manageConditionalProperties(keyConditionalPropertySet, compilation, permutation);
			}
			
		}
		compilation.setHasBeenCompleted();
	}

	/**
	 * Pour chaque cle, chercher la conditionalProperty qui s'applique en
	 * fonction des properties de la permutation. Puis appliquer la valeur de la
	 * conditional Property sur la permutation - si la cle n'existe pas, la
	 * creer - si la cle existe, enlever la property et la remplacer par la conditional property
	 */
	private void manageConditionalProperties(
			final Set<KeyProperty> keyConditionalPropertySet,
			final Compilation compilation, final Permutation permutation) {

		final List<Property> permutationSetProperties = permutation
				.getPropertyList();

		final Map<KeyProperty, ConditionalProperty> mapConditionalPropertyForPermutation = new HashMap<KeyProperty, ConditionalProperty>();
		
		// 1 - Pour chaque conditional cle, chercher la première conditional property qui
		// s'applique et la stocker dans la map
		for (KeyProperty keyConditionalProperty : keyConditionalPropertySet) {
			final List<ConditionalProperty> conditionalProperties = compilation
					.getConditionalPropertiesByKey(keyConditionalProperty);

			// for each ConditionalProperty
			for (ConditionalProperty conditionalProperty : conditionalProperties) {

				final boolean rulesOk = this.applyRules(conditionalProperty,
						permutationSetProperties);
				if (rulesOk) {
					mapConditionalPropertyForPermutation.put(
							keyConditionalProperty, conditionalProperty);
					break;
				}

			}
		}

			// 2 - Appliquer les valeurs des conditional property sur les
			// setProperties de la permutation
			if (!mapConditionalPropertyForPermutation.isEmpty()) {

				for (KeyProperty keyPropertyToChange : mapConditionalPropertyForPermutation
						.keySet()) {

					final Property permutationProperty = permutation
							.getProperty(keyPropertyToChange);
					final ConditionalProperty conditionalProperty = mapConditionalPropertyForPermutation.get(keyPropertyToChange);
					if (permutationProperty != null) {

						// on elimine la property
						permutation.removeProperty(permutationProperty);
					}
					
					// on ajoute la conditional property à la permutation
					permutation.getPropertyList().add(conditionalProperty);
					
				}

			}
			
        // on rearange l'ordre
		Collections.sort(permutation.getPropertyList());
		
	}
	

    private List<Permutation> buildPermutationFromProperty(final Compilation compilation, final Property property) {
    	 
    	if (compilation == null) return null;
    	final List<Property> propertyMonoValueList = (property == null)?null:property.buildPropertyMonoValueList();
    	final List<Permutation> permutationList = new ArrayList<Permutation>((property == null)?0:
    			propertyMonoValueList.size());
    	
    	// cas particulier 
    	if (property == null) {
    		permutationList.add(new Permutation(compilation));
    		return permutationList;
    	}
    	
    	// cas standard
    	for (Property propertyMonoValue : propertyMonoValueList) {
			permutationList.add(new Permutation(compilation, propertyMonoValue));
		}
    	return permutationList;
    }
	private List<Permutation> produitPermutationListXProperty(final List<Permutation> permutationList, final Property property) {
		if (permutationList == null || property == null) return null;
		
		final List<Permutation> permutationListProduit = new ArrayList<Permutation>(permutationList.size() * property.getValuesCount());
		
		// for each permutation
		for (Permutation permutation : permutationList) {
			permutationListProduit.addAll(this.produitPermutationXProperty(permutation, property));
		}
		
		return permutationListProduit;
	}
	
	private List<Permutation> produitPermutationXProperty(final Permutation permutation, final Property property) {
		if (permutation == null || property == null) return null;
		
		final List<Permutation> permutationListProduit = new ArrayList<Permutation>(property.getValuesCount());
		
		// property mono value --> permutation  + property
		if (property.isMonoValue()) {
			permutation.getPropertyList().add(property);
			permutationListProduit.add(permutation);
			return permutationListProduit;
		}
		
		// for each property mono value
		final List<Property> propertyMonoValueList = property.buildPropertyMonoValueList();
		for (Property propertyMonoValue : propertyMonoValueList) {

			permutationListProduit.addAll(this.produitPermutationXProperty(
               permutation.clone(), propertyMonoValue));
		}
		return permutationListProduit;
	
	}

}
