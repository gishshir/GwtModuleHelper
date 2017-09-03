package fr.tsadeo.app.gwt.modulehelper.server.service;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.tsadeo.app.gwt.modulehelper.server.model.Application;
import fr.tsadeo.app.gwt.modulehelper.server.model.Compilation;
import fr.tsadeo.app.gwt.modulehelper.server.model.ConditionalProperty;
import fr.tsadeo.app.gwt.modulehelper.server.model.GenerateWith;
import fr.tsadeo.app.gwt.modulehelper.server.model.IXmlGwtFile;
import fr.tsadeo.app.gwt.modulehelper.server.model.KeyProperty;
import fr.tsadeo.app.gwt.modulehelper.server.model.ModeleObjectFactory;
import fr.tsadeo.app.gwt.modulehelper.server.model.Module;
import fr.tsadeo.app.gwt.modulehelper.server.model.MyInterface;
import fr.tsadeo.app.gwt.modulehelper.server.model.Property;
import fr.tsadeo.app.gwt.modulehelper.server.model.ReplaceWith;
import fr.tsadeo.app.gwt.modulehelper.server.model.XmlGwtContent;
import fr.tsadeo.app.gwt.modulehelper.server.util.ExtraSession;
import fr.tsadeo.app.gwt.modulehelper.server.util.GwtXmlFileHelper;
import fr.tsadeo.app.gwt.modulehelper.server.util.IConstants;
import fr.tsadeo.app.gwt.modulehelper.server.util.StoreExtraSession;

/**
 * Utilitaire pour la construction de l'Application
 * @author sylvie
 *
 */
public class ModuleHelperUtils {

	private static final ModuleHelperUtils instance = new ModuleHelperUtils();
	private static final String SOURCE_DIR_NAME_DEFAULT = "src";
	
	private static final Log log = LogFactory.getLog(ModuleHelperUtils.class);

	public static final ModuleHelperUtils getInstance() {
		return instance;
	}
	private ModuleHelperUtils() {}
	
	public void reinit(final HttpSession session) throws Exception {
		log.debug("reinit()");
		session.removeAttribute(IConstants.SESSION_APPLICATION);
		session.removeAttribute(IConstants.SESSION_EXTRA_SESSION);
	}
	
	public void saveFileInSession (final File uploadedFile, final HttpSession session) {
		log.debug("saveFileInSession()");
		if (uploadedFile == null) return;
		
	}

	
	/**
	 * 
	 * @param mapContentFiles : [key: relative pathname / List of content]
	 * @param sourceDirName
	 * @param sessionId
	 * @return
	 */
	public Application buildApplication(final Map<String, List<String>> mapContentFiles,
			 String sourceDirName, final String sessionId) {
		
		log.info("buildApplication()");
		
		final String backslash = "\\";
		final String slash = "/";
		
		this.getExtraSession(sessionId).clearAll();
		
		if (sourceDirName == null) {
			sourceDirName = SOURCE_DIR_NAME_DEFAULT;
		}
		
		//reconstruire la map en transformant les cles
		final Map<String, List<String>> newMap = new HashMap<String, List<String>>(mapContentFiles.size());
		for (String key : mapContentFiles.keySet()) {
			final String pathname = key.replaceAll("\\" + backslash, slash);
			newMap.put(pathname, mapContentFiles.get(key));
		}
		
		
		final Application application = new Application(sessionId);
		final List<IXmlGwtFile> xmlGwtFileList = this.buildXmlGwtFileList(newMap, sourceDirName, sessionId);
		
		final List<Module> moduleList = this.buildModuleList(xmlGwtFileList, sessionId);
		this.buildCompilationList(moduleList, sessionId);

		return application;
	}
	
    /**
     * Build from root directory
     * @param rootDirectory
     * @param sourceDirName
     * @param sessionId
     * @return
     */
	public Application buildApplication(final String rootDirectory,
			String sourceDirName, final String sessionId) {

		log.info("buildApplication()");
				
		this.getExtraSession(sessionId).clearAll();
		
		if (sourceDirName == null) {
			sourceDirName = SOURCE_DIR_NAME_DEFAULT;
		}
		final Application application = new Application(sessionId);
		
		final List<File> gwtFiles = this.buildFileList(new File(rootDirectory),
				sourceDirName);
		final List<IXmlGwtFile> xmlGwtFileList = this.buildXmlGwtFileList(
				gwtFiles, rootDirectory, sourceDirName, sessionId);
		final List<Module> moduleList = this.buildModuleList(xmlGwtFileList, sessionId);
		this.buildCompilationList(moduleList, sessionId);

		return application;
	}
	/**
	 * Build from archive file
	 * @param archiveFile
	 * @param sourceDirName
	 * @param sessionId
	 * @return
	 */
	public Application buildApplication(final File archiveFile,
			String sourceDirName, final String sessionId) {

		log.info("buildApplication()");
				
		this.getExtraSession(sessionId).clearAll();
		
		if (sourceDirName == null) {
			sourceDirName = SOURCE_DIR_NAME_DEFAULT;
		}
		final Application application = new Application(sessionId);
		
		final List<IXmlGwtFile> xmlGwtFileList = this.buildXmlGwtFileList(
				archiveFile, sourceDirName, sessionId);
		final List<Module> moduleList = this.buildModuleList(xmlGwtFileList, sessionId);
		this.buildCompilationList(moduleList, sessionId);

		return application;

	}
  //----------------------------------------------------------- protected methods
	protected Map<Module, List<GenerateWith>> buildGenerateWithListForInterfaceAndModuleList(
			final String interfaceName, final List<Module> compilationModuleList, final Application application) {
		
		//log.trace("buildGenerateWithListForInterfaceAndModuleList()");
		
		if (interfaceName == null) throw new IllegalArgumentException("loadInterfaceXImplementations(): interfaceName cannot be null!!");
		if (!application.hasInterfaces() || !application.hasModules()) {
			return new HashMap<Module, List<GenerateWith>>(0);
		}
		
		// Get the interface
		final MyInterface myInterface = application.getInterfaceByName(interfaceName);
		if (myInterface == null)return new HashMap<Module, List<GenerateWith>>(0);
		
		final Map<Module, List<GenerateWith>> mapGenerateWithByModule = new HashMap<Module, List<GenerateWith>>();
		
		// for each module
		for (Module module : compilationModuleList) {
			final IXmlGwtFile xmlGwtFile = module.getXmlGwtFile();
			if (!xmlGwtFile.hasGenerateWith()) continue; // next module
			
			final List<GenerateWith> generateWithList = xmlGwtFile.getGenerateWithList();
			for (GenerateWith replaceWith : generateWithList) {
				if (replaceWith.getMyInterface() != myInterface ) continue; // next GenerateWith
				
				List<GenerateWith> generateWithByModuleList = mapGenerateWithByModule.get(module);
				if (generateWithByModuleList == null) {
					generateWithByModuleList = new ArrayList<GenerateWith>();
					mapGenerateWithByModule.put(module, generateWithByModuleList);
				}
				
				generateWithByModuleList.add(replaceWith);
			}
		}
		
		return mapGenerateWithByModule;
	}
	protected Map<Module, List<ReplaceWith>> buildReplaceWithListForInterfaceAndModuleList(
			final String interfaceName, final List<Module> compilationModuleList, final Application application) {
		
		//log.trace("buildReplaceWithListForInterface()");
		
		if (interfaceName == null) throw new IllegalArgumentException("loadInterfaceXImplementations(): interfaceName cannot be null!!");
		if (!application.hasInterfaces() || !application.hasModules()) {
			return new HashMap<Module, List<ReplaceWith>>(0);
		}
		
		// Get the interface
		final MyInterface myInterface = application.getInterfaceByName(interfaceName);
		if (myInterface == null)return new HashMap<Module, List<ReplaceWith>>(0);
		
		final Map<Module, List<ReplaceWith>> mapReplaceWithByModule = new HashMap<Module, List<ReplaceWith>>();
		
		// for each module
		for (Module module : compilationModuleList) {
			final IXmlGwtFile xmlGwtFile = module.getXmlGwtFile();
			if (!xmlGwtFile.hasReplaceWith()) continue; // next module
			
			final List<ReplaceWith> replaceWithList = xmlGwtFile.getReplaceWithList();
			for (ReplaceWith replaceWith : replaceWithList) {
				if (replaceWith.getMyInterface() != myInterface ) continue; // next ReplaceWith
				
				List<ReplaceWith> replaceWithByModuleList = mapReplaceWithByModule.get(module);
				if (replaceWithByModuleList == null) {
					replaceWithByModuleList = new ArrayList<ReplaceWith>();
					mapReplaceWithByModule.put(module, replaceWithByModuleList);
				}
				
				replaceWithByModuleList.add(replaceWith);
			}
		}
		
		return mapReplaceWithByModule;
	}
	

    //----------------------------------------------------------- private methods

	
	
	private List<Module> findRecursiveImportModuleList(final Module module)  {
		log.debug("findRecursiveImportModuleList() > module: " + module.getName());
		final List<Module> recursiveList = this.findRecursiveImportModuleList(module, null);
		for (Module mod : recursiveList) {
			log.debug("module: " + mod.getName());
		}
		return recursiveList;
	}
	
	/**
	 * Liste récursive de tous les import de modules (unique) - ordre écriture
	 * @param module
	 * @return
	 */
	private List<Module> findRecursiveImportModuleList(final Module module, Set<Module> uniqueModules) {
			
		if (module.isVirtual()) {
			return null;
		}
		//log.debug("findRecursiveImportModuleList() > module: " + module.getName());
		
		if (uniqueModules == null) {
			uniqueModules = new HashSet<Module>();
		}
		uniqueModules.add(module);
		
		if (!module.hasImports()) {
			final List<Module> onlyOne = new ArrayList<Module>(1);
			onlyOne.add(module);
			return onlyOne;
		}
				
		// with import
		final List<Module> importModuleList = new ArrayList<Module>();
		
		for (Module importModule : module.getImportModulesList(true)) {
			
			if (!uniqueModules.contains(importModule)) {

			
			final List<Module> listInterne = this.findRecursiveImportModuleList(importModule, uniqueModules);
			// ajouter que les modules qui n'existent pas encore
			if (listInterne != null) {
			  for (Module moduleInterne : listInterne) {
				//log.debug(" >> moduleInterne > " + moduleInterne.getName());
				if (!importModuleList.contains(moduleInterne)) {
					uniqueModules.add(moduleInterne);
					importModuleList.add(moduleInterne);
				}
			  }
			}
			
			}
		}	
		importModuleList.add(module);
		return importModuleList;
		
	}

	
	/**
	 * Parcourir le repertoire de manière récursive et lister tous les fichiers
	 * *.gwt.xml
	 * 
	 * @param rootDirectory
	 * @return
	 */
	private List<File> buildFileList(final File dir, final String sourceDirName) {

		
		final List<File> gwtFiles = new ArrayList<File>();
		if (dir == null)
			return gwtFiles;
		log.info("buildFileList: " + dir.getName());

		if (!dir.exists() || !dir.isDirectory())
			return gwtFiles;

		// retenir soit repertoire soit fichier gwt
		final File[] files = dir.listFiles(new FileFilter() {

			@Override
			public boolean accept(File file) {
				if (file.isDirectory())
					return true;
				if (file.isFile()
						&& file.getName().endsWith(IConstants.GWT_EXTENSION)
						&& file.getAbsolutePath().contains(sourceDirName))
					return true;
				return false;
			}
		});

		if (files == null) {
			return gwtFiles;
		}

		// for each file
		// - if directory >> find files
		// - if file >> add to list
		for (File file : files) {
			if (file.isDirectory()) {
				gwtFiles.addAll(this.buildFileList(file, sourceDirName));
			} else {
				gwtFiles.add(file);
			}
		}

		return gwtFiles;
	}

	/**
	 * Construire les objets XmlGwtFile à partir des fichiers *.gxt.xml
	 * 
	 * @param gwtFiles
	 * @return
	 */
	private List<IXmlGwtFile> buildXmlGwtFileList(final List<File> gwtFiles,
			final String rootPath, final String sourceDirName, final String sessionId) {
		
		if (gwtFiles == null)
			return null;

		log.info("buildXmlGwtFileList");
		try {
			
			final List<IXmlGwtFile> xmlGwtFileList = new ArrayList<IXmlGwtFile>(
					gwtFiles.size());
			for (File gwtFile : gwtFiles) {
				log.info("file: " + gwtFile.getName());
				xmlGwtFileList.add(this.buildXmlGwtFile(gwtFile, rootPath,
						sourceDirName, sessionId));
			}

			// definir les dependances entre XmlGwtFile
			this._buildXmlGwtDependances(xmlGwtFileList, sessionId);

			return xmlGwtFileList;
			
		} catch (Throwable e) {
			log.fatal(e.toString());
			throw new RuntimeException(e.getMessage());
		}
	
	}
	
	private List<IXmlGwtFile> buildXmlGwtFileList(final Map<String, List<String>> mapContentFiles,
			 String sourceDirName, final String sessionId) {
		
		log.info("buildXmlGwtFileList");
		if (mapContentFiles == null) return null;
		
		final List<IXmlGwtFile> xmlGwtFileList = new ArrayList<IXmlGwtFile>();
		
		for (String pathname : mapContentFiles.keySet()) {
			xmlGwtFileList.add(this.buildXmlGwtFile(pathname, sourceDirName,
					mapContentFiles.get(pathname), sessionId));
		}
		

		// definir les dependances entre XmlGwtFile
		this._buildXmlGwtDependances(xmlGwtFileList, sessionId);
		
		return xmlGwtFileList;
	}
	

	private List<IXmlGwtFile> buildXmlGwtFileList(final File archiveFile, final String sourceDirName, final String sessionId) {
		 
		if (archiveFile == null)
			return null;

		log.info("buildXmlGwtFileList");
		
	   ZipFile zipFile = null;
		try {
			
			zipFile = new ZipFile(archiveFile);
			final List<IXmlGwtFile> xmlGwtFileList = new ArrayList<IXmlGwtFile>();
			
			final Enumeration<? extends ZipEntry> e = zipFile.entries();
	         
	         while(e.hasMoreElements()) {
	            final ZipEntry entry = (ZipEntry) e.nextElement();
	            log.debug(">>Extracting: " +entry.getName() + " " + ((entry.isDirectory())?"directory":""));
	            if (entry.isDirectory() || !entry.getName().endsWith(IConstants.GWT_EXTENSION)) continue;
	            
	            xmlGwtFileList.add(this.buildXmlGwtFile(zipFile, entry, sourceDirName, sessionId));
	         }
	          
	      // definir les dependances entre XmlGwtFile
		 this._buildXmlGwtDependances(xmlGwtFileList, sessionId);
	         
	         return xmlGwtFileList;
			
		} catch (Exception e) {
			log.fatal(e.toString());
			throw new RuntimeException(e.getMessage());
		}
		finally {
			if (zipFile != null) {
				try {
					zipFile.close();
				} catch (IOException ignored) {
					log.warn("zipFile not closed!", ignored);
				}
			}
		}
		
		
	}
	private void _buildXmlGwtDependances(final List<IXmlGwtFile> xmlGwtFileList, final String sessionId) {

		log.debug("_buildXmlGwtDependances()");
		final List<IXmlGwtFile> virtualXmlGwtFileList = new ArrayList<IXmlGwtFile>();
		
		// definir les dependances entre XmlGwtFile
		for (IXmlGwtFile xmlGwtFile : xmlGwtFileList) {
			
			final List<String> inheritList = xmlGwtFile.getInheritList();
			if (inheritList == null || inheritList.size() == 0)
				continue; // next xmlGwtFile

			for (String inherit : inheritList) {
				final IXmlGwtFile importXmlGwtFile = this.getExtraSession(
						sessionId).getXmlGwtFileByName(inherit);
				
				if (importXmlGwtFile == null) {
					// build virtual XmlGwtFile
					IXmlGwtFile xmlGwtFileVirtual = ModeleObjectFactory.getInstance().createXmlGwtFile(inherit, sessionId);
					xmlGwtFile.addImport(xmlGwtFileVirtual);
					virtualXmlGwtFileList.add(xmlGwtFileVirtual);
					log.debug("xmlGwtFileVirtual: " + xmlGwtFileVirtual.getCompleteName());
				} else {
				   xmlGwtFile.addImport(importXmlGwtFile);
				}
			}
		}
		xmlGwtFileList.addAll(virtualXmlGwtFileList);
	}
	private IXmlGwtFile buildXmlGwtFile(final File gwtFile,
			final String rootPath, final String sourceDirName, final String sessionId) throws FileNotFoundException {
		
		final IXmlGwtFile xmlGwtFile = ModeleObjectFactory.getInstance().createXmlGwtFile(gwtFile, rootPath, sourceDirName, sessionId);
		GwtXmlFileHelper gwtXmlFileHelper = new GwtXmlFileHelper(gwtFile, sessionId);
		
		this._populateXmlGwtFile(xmlGwtFile, gwtXmlFileHelper);
		
		xmlGwtFile.setContent(
			    gwtXmlFileHelper.getContent(gwtFile.getName(), new FileInputStream(gwtFile)));

		return xmlGwtFile;
	}
	
	private IXmlGwtFile buildXmlGwtFile(final String pathname, 
			 final String sourceDirName, final List<String> content, final String sessionId) {
		final IXmlGwtFile xmlGwtFile = ModeleObjectFactory.getInstance().createXmlGwtFile(pathname,
				sourceDirName, content, sessionId);

		final XmlGwtContent xmlGwtContent = (XmlGwtContent)xmlGwtFile;
		final GwtXmlFileHelper gwtXmlFileHelper = new GwtXmlFileHelper(xmlGwtFile.getFileName(),
				xmlGwtContent.getInputStream(),
				sessionId);
		this._populateXmlGwtFile(xmlGwtFile, gwtXmlFileHelper);
		
		return xmlGwtFile;
	}
	private IXmlGwtFile buildXmlGwtFile(final ZipFile gwtzipFile, final ZipEntry zipEntry,
			final String sourceDirName, final String sessionId) throws IOException {
		
		final IXmlGwtFile xmlGwtFile = ModeleObjectFactory.getInstance().createXmlGwtFile(zipEntry, sourceDirName, sessionId);
		InputStream in = gwtzipFile.getInputStream(zipEntry);
		 GwtXmlFileHelper gwtXmlFileHelper = null;
		 
		try {
			gwtXmlFileHelper = new GwtXmlFileHelper(zipEntry.getName(), in, sessionId);
			this._populateXmlGwtFile(xmlGwtFile, gwtXmlFileHelper);
		} finally {
			in.close();
		}
		
		in = gwtzipFile.getInputStream(zipEntry);
		try  {
		xmlGwtFile.setContent(
		    gwtXmlFileHelper.getContent(zipEntry.getName(), gwtzipFile.getInputStream(zipEntry)));
		}
		finally {
			in.close();
		}

		return xmlGwtFile;
	}
	private void _populateXmlGwtFile(final IXmlGwtFile xmlGwtFile,
			final GwtXmlFileHelper gwtXmlFileHelper) {

			xmlGwtFile.setRenameTo(gwtXmlFileHelper.getRenameTo());
			xmlGwtFile.setEntryPoint(gwtXmlFileHelper.getEntryPoint());
			xmlGwtFile.setInheritList(gwtXmlFileHelper.getListInherits());
			xmlGwtFile.addDefineProperties(gwtXmlFileHelper.getListDefineProperties());
			xmlGwtFile.addSetProperties(gwtXmlFileHelper.getListSetProperties());
			xmlGwtFile.addReplaceWithList(gwtXmlFileHelper.getListReplaceWith());
			xmlGwtFile.addGenerateWithList(gwtXmlFileHelper.getListGenerateWith());

	}

	/**
	 * Construire la liste des modules à partir des objets XmlGwtFile
	 * 
	 * @param xmlGwtFileList
	 * @return
	 */
	private List<Module> buildModuleList(final List<IXmlGwtFile> xmlGwtFileList, final String sessionId) {
		
		log.info("buildModuleList");
		if (xmlGwtFileList == null)
			return null;

		final List<Module> moduleList = new ArrayList<Module>(
				xmlGwtFileList.size());
		for (IXmlGwtFile xmlGwtFile : xmlGwtFileList) {
			log.info("module: " + xmlGwtFile.getCompleteName());
			moduleList.add(ModeleObjectFactory.getInstance().createModule(xmlGwtFile, sessionId));
		}

		// definir les dependances (bidirectionnal)
		for (Module module : moduleList) {

			final IXmlGwtFile xmlGwtFile = module.getXmlGwtFile();
			if (xmlGwtFile.hasImports()) {
				for (IXmlGwtFile importXmlGwtFile : xmlGwtFile
						.getImportXmlGwtFileList()) {

					final String moduleName = importXmlGwtFile.getModuleName();
					final Module importModule =  this.getExtraSession(sessionId).getModuleByName(moduleName);
					if (importModule == null) continue; // next XmlGwtFile

					module.addImportModule(importModule);
				}
			}
		}
		
		// controle ref circulaire
		for (Module module : moduleList) {
			this.verifyCircularReferences(new ArrayList<String>(), module, null);
		}
		
		return moduleList;
	}
	
	private void verifyCircularReferences(final List<String> listAscendance, final Module module, final Module modulePere)  {
		
		final String moduleName = module.getName();
		if (listAscendance.contains(moduleName)) {
			final String errorMessage = "Référence circulaire : le module '" + modulePere.getName() + "' contient déjà le module '" + moduleName + "' dans son ascendance!";
            log.warn(errorMessage);
            modulePere.disabledImportModule(module);
            return;

			
		}
		final List<String> newListAscendance = new ArrayList<String>(listAscendance);
		newListAscendance.add(moduleName);
		
		for (Module importModule : module.getImportModulesList(true)) {
			this.verifyCircularReferences(newListAscendance, importModule, module);
		}
	}
	
	private List<Compilation> buildCompilationList(final List<Module> moduleList, final String sessionId) {
		
		log.info("buildCompilationList");
		if (moduleList == null) return null;
		final  List<Compilation> compilationList = new ArrayList<Compilation>();
		
		for (Module mainModule : moduleList) {
			if (mainModule.isCompilation()) {
				log.info("compilation: " + mainModule.getName());
			final Compilation compilation = ModeleObjectFactory.getInstance().createCompilation(mainModule, sessionId);
			
			  compilationList.add(compilation);
			  
				// list de tous les modules de la compilation par ordre de priorité (+faible --> + fort)
			    // inclu le mainModule en dernière position
				final List<Module> compilationModuleList = 
				          ModuleHelperUtils.getInstance().findRecursiveImportModuleList(mainModule);
				compilation.setCompilationModuleList(compilationModuleList);
			  
				// definir les proprietes de la compilation (no conditional properties)
			  final Map<KeyProperty, Property> mapKeyProperty = this.buildMapKeyProperty(compilationModuleList);
			  compilation.addAllSetProperties(mapKeyProperty.values());
			  
			   // definir les propriétés conditionnels de la compilation
			  final Map<KeyProperty, List<ConditionalProperty>> mapConditionalProperties =
					  this.buildMapConditionalProperties(compilationModuleList);
			  compilation.addAllConditionalSetProperties(mapConditionalProperties);
			  
			  // ne définir les permutations par defaut pour des raisons de performances
			  // PermutationUtils.getInstance().buildPermutations(compilation, sessionId);
			  
			}
		}

		return compilationList;
	}
	
	private Map<KeyProperty, List<ConditionalProperty>> buildMapConditionalProperties(final List<Module> compilationModuleList) {
		
	    Map<KeyProperty, List<ConditionalProperty>> map = new HashMap<KeyProperty, List<ConditionalProperty>>();
	    
	   // for each module : last setProperty is the good one
	   for (Module compilationModule : compilationModuleList) {
	 		
			if (compilationModule.getXmlGwtFile().hasSetProperties(true)) {
				
				final Map<KeyProperty, List<ConditionalProperty>> mapModule =
						compilationModule.getXmlGwtFile().getMapConditionalSetProperties();
				
				if (mapModule != null) {
					map = this.fusionMapConditionalProperties(map, mapModule);
				}				
			}
	   }
	    
	    
	    return map;
	}
	

	/**
	 * Fusion des Map avec les règles suivantes
     * Pour chaque KeyProperty, fusionner les listes de ConditionalProperty mais
     * ne garder qu'une property ayant une valeur donnée celle de la mère étant prioritaire
	 */
	private Map<KeyProperty, List<ConditionalProperty>> fusionMapConditionalProperties(
			final  Map<KeyProperty, List<ConditionalProperty>> mapFille,
			final  Map<KeyProperty, List<ConditionalProperty>> mapMere) {
		
		for (KeyProperty keyPropertyFille : mapFille.keySet()) {
			// la clé fille n'existe pas dans la map mere
			if (!mapMere.containsKey(keyPropertyFille)) {
				mapMere.put(keyPropertyFille, mapFille.get(keyPropertyFille));
			}
			// la cle existe : ne garder que les Property dont les valeurs n'existent pas
			else {
				//  construire set [value] pour la liste des Property mere
				final List<ConditionalProperty> conditionalPropertiesMere = mapMere.get(keyPropertyFille);
				final Set<String> setValuesMere = new HashSet<String>();
				for (ConditionalProperty conditionalPropertyMere : conditionalPropertiesMere) {
					setValuesMere.add(conditionalPropertyMere.getValue());
				}
				
				final List<ConditionalProperty> conditionalPropertiesFille = mapFille.get(keyPropertyFille);
				// parcourir la liste de ConditionalProperty fille
				for (ConditionalProperty conditionalPropertyFille : conditionalPropertiesFille) {
					if (!setValuesMere.contains(conditionalPropertyFille.getValue())){
						conditionalPropertiesMere.add(conditionalPropertyFille);
					}
				}
			}
		}
		
		return mapMere;
	}
	
	/**
	 * A partir de la liste ordonnée des modules d'une compilation (mainModule en dernier)
	 * construit la map des key - value pour les setProperties
	 * Les dernieres valeurs sont prioritaires.
	 * @param importModuleList
	 * @return
	 */
	private Map<KeyProperty, Property> buildMapKeyProperty(final List<Module> compilationModuleList) {
		
		final Map<KeyProperty, Property> map = new HashMap<KeyProperty, Property>();
		
		// for each module : last setProperty is the good one
		for (Module compilationModule : compilationModuleList) {
			
			// overriding with module property
			if (compilationModule.getXmlGwtFile().hasSetProperties(false)) {
				for (Property property : compilationModule.getXmlGwtFile().getSetPropertyList(false)) {
					map.put(property.getKey(), property);
				}
			}
		}
		
		return map;
	}

	
	private ExtraSession getExtraSession(final String sessionId) {
		return StoreExtraSession.getExtraSession(sessionId);
	}
}
