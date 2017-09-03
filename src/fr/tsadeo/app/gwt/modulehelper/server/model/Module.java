package fr.tsadeo.app.gwt.modulehelper.server.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class Module  extends AbstractBusinessObject implements  Comparable<Module> {
	
	private static final long serialVersionUID = 1L;
	
	//--------------------------------------------- instance
	private final String _name;
	private final IXmlGwtFile _xmlGwtFile;
	// ordre des tags inherit dans le fichier de module
	private Map<String, ImportModule> _mapImportModules = new LinkedHashMap<String, ImportModule>();
	private List<Module> _sonModulesList = new ArrayList<Module>();
	
    //---------------------------------------- constructor
	public Module() {
		this(ID_UNDEFINED, null);
	}
	protected Module(final int id, final IXmlGwtFile xmlGwtFile) {
		super(id);
		this._xmlGwtFile = xmlGwtFile;
		this._name = this._xmlGwtFile.getModuleName();
	}
	//----------------------------------------accessors
	public String getName() {
		return this._name;
	}
	public boolean isVirtual() {
		return (this._xmlGwtFile == null)?true:this._xmlGwtFile.isVirtual();
	}
	public String getPackage() {
		return this._xmlGwtFile.getPackage();
	}
	/**
	 * List des import dans l'ordre d'écriture
	 * @param onlyEnabled
	 * @return
	 */
	public  List<Module> getImportModulesList(boolean onlyEnabled) {
		return this.getImportModulesList(false, onlyEnabled);
     }
	private List<Module> getImportModulesList(final boolean sortByModuleName, boolean onlyEnabled)  {
		
		final List<Module> moduleList = new ArrayList<Module>();
        for (ImportModule importModule : this._mapImportModules.values()) {
		   if (onlyEnabled) {
			   if (importModule.isEnabled())
			   moduleList.add(importModule.getModule());
		   }
		   else {
			   moduleList.add(importModule.getModule());
		   }
	    }

		if (sortByModuleName) {
			Collections.sort(moduleList);
		}
		return Collections.unmodifiableList(moduleList);
	}
	public List<Module> getSonModuleList() {
		Collections.sort(this._sonModulesList);
		return Collections.unmodifiableList(this._sonModulesList);
	}
    public IXmlGwtFile getXmlGwtFile() {
    	return this._xmlGwtFile;
    }
    
    //------------------------------------ public methods
    public boolean isCompilation() {
    	return this._xmlGwtFile.getRenameTo() != null && !this.isVirtual();
    }
    public boolean hasImports() {
    	return !this._mapImportModules.isEmpty();
    }
    public boolean hasSons() {
    	return !this._sonModulesList.isEmpty();
    }
    public void addImportModule(final Module module) {
    	this._mapImportModules.put(module.getName(),  new ImportModule(module));
    	module._sonModulesList.add(this);
    }
    public boolean isImportModuleEnabled(final Module module) {
    	if (!this._mapImportModules.containsKey(module.getName())) return true;
    	return this._mapImportModules.get(module.getName()).isEnabled();
    }
    public void disabledImportModule(final Module module) {
    	
    	if (!this._mapImportModules.containsKey(module.getName())) return;
    	final ImportModule importModule = this._mapImportModules.get(module.getName());
    	importModule._enabled = false;
    }
    
    //-------------------------------------- overriding comparable
	@Override
	public int compareTo(Module o) {
		return this.getName().toLowerCase().compareTo(o.getName().toLowerCase());
	}

	//========================================= INNER CLASS
	class ImportModule implements Comparable<ImportModule>, Serializable{
		
		private static final long serialVersionUID = 1L;
		
		private final Module _module;
		private  boolean _enabled = true;
		
		Module getModule() {
			return this._module;
		}
		boolean isEnabled() {
			return this._enabled;
		}
		public ImportModule(final Module module) {
			this._module = module;
		}
		
		@Override
		public int compareTo(ImportModule o) {
			return this.getModule().compareTo(o.getModule());
		}
	}
}
