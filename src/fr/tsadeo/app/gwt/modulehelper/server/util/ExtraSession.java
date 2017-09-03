package fr.tsadeo.app.gwt.modulehelper.server.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.tsadeo.app.gwt.modulehelper.server.model.Compilation;
import fr.tsadeo.app.gwt.modulehelper.server.model.IBusinessObject;
import fr.tsadeo.app.gwt.modulehelper.server.model.IXmlGwtFile;
import fr.tsadeo.app.gwt.modulehelper.server.model.KeyProperty;
import fr.tsadeo.app.gwt.modulehelper.server.model.Module;
import fr.tsadeo.app.gwt.modulehelper.server.model.MyInterface;

public class ExtraSession implements Serializable {
	

	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(ExtraSession.class);
	
	public enum ModeleEnum {compilation, key, module, myinterface}

	private final String _sessionId;
	//----------------------------------------- accessors
	public String getSessionId() {
		return _sessionId;
	}
	//----------------------------------------- constructors
	public ExtraSession() {
		this(null);
	}
	public ExtraSession(final String sessionId) {
		this._sessionId = sessionId;
	}
	//----------------------------------------- Compilation
	private int _indexCompilation = 0;
	private final  Map<String, Compilation> mapCompilationByName = new HashMap<String, Compilation>();
	public final  Collection<Compilation> getCompilations() {
			return this.mapCompilationByName.values();
	}
	public boolean hasCompilations() {
		return (this.mapCompilationByName != null && !this.mapCompilationByName.isEmpty());
	}
	public final void addCompilation(final Compilation compilation) {
		if (compilation == null) return;
		this.mapCompilationByName.put(compilation.getName(), compilation);
	}
	public Compilation getCompilationByName(final String name) {
		return this.mapCompilationByName.get(name);
	}
	
	//------------------------------------------------- KeyProperty
	private int _indexKeyProperty = 0;
    private final Map<String, KeyProperty> mapKeyPropertyByName = new HashMap<String, KeyProperty>();
    public final Collection<KeyProperty> getKeyProperties() {
    	return mapKeyPropertyByName.values();
    }
    public boolean hasKeyProperties() {
    	return !mapKeyPropertyByName.isEmpty();
    }
    public final KeyProperty geKeyPropertyByName(final String name) {
 		return this.mapKeyPropertyByName.get(name);
 	}
    public final void addKeyProperty(final KeyProperty key) {
    	this.mapKeyPropertyByName.put(key.getName(), key);
    }
    
	//---------------------------------------------- module
    private int _indexModule = 0;
	private final  Map<String, Module> mapModuleByName = new HashMap<String, Module>();
	public final  Collection<Module> getModules() {
		return this.mapModuleByName.values();
	}
	public boolean hasModules() {
		return (this.mapModuleByName != null && !this.mapModuleByName.isEmpty());
	}
	public final Module getModuleByName (final String name) {
		return mapModuleByName.get(name);
	}
	public final void addModule (final Module module) {
		this.mapModuleByName.put(module.getName(), module);
	}
	
	//------------------------------------------------------ XmlGwtFile
	private final Map<String, IXmlGwtFile> mapXmlGwtFileByCompleteName = new HashMap<String, IXmlGwtFile>();
	public final Collection<IXmlGwtFile> getXmlGwtFiles() {
		return this.mapXmlGwtFileByCompleteName.values();
	}
	public final IXmlGwtFile getXmlGwtFileByName (final String completeName) {
		return  this.mapXmlGwtFileByCompleteName.get(completeName);
	}
    public void addXmlGwtFile(final IXmlGwtFile xmlGwtFile) {
    log.debug("addXmlGwtFile: " + xmlGwtFile.getCompleteName());
    	this.mapXmlGwtFileByCompleteName.put(xmlGwtFile.getCompleteName(), xmlGwtFile);
    }

    //------------------------------------------------------ MyInterface
    private int _indexInterface = 0;
    private final Map<String, MyInterface> mapInterfaceByName = new HashMap<String, MyInterface>();
    public final Collection<MyInterface> getInterfaces() {
		return this.mapInterfaceByName.values();
	}
	public boolean hasInterfaces() {
		return (this.mapInterfaceByName != null && !this.mapInterfaceByName.isEmpty());
	}
	public final MyInterface getInterfaceByName (final String name) {
		return  this.mapInterfaceByName.get(name);
	}
    public void addInterface(final MyInterface myInterface) {
    	this.mapInterfaceByName.put(myInterface.getName(), myInterface);
    }
    //-------------------------------------------------------- general
    public void clearAll() {
    	this.mapCompilationByName.clear();
    	this.mapKeyPropertyByName.clear();
    	this.mapModuleByName.clear();
    	this.mapXmlGwtFileByCompleteName.clear();
    	this.mapInterfaceByName.clear();
    	this._indexCompilation = 0;
    	this._indexInterface = 0;
    	this._indexKeyProperty = 0;
    	this._indexModule = 0;
    }
    private Map<?, ?> getMap(final ModeleEnum modeleEnum) {
    	
    	
    	switch (modeleEnum) {
    	
		  case compilation: return  this.mapCompilationByName;
			
		  case key: return this.mapKeyPropertyByName;
		  
		  case module: return this.mapModuleByName;
		  
		  case myinterface: return this.mapInterfaceByName;

		}
    	return null;
    }
	public final boolean containByKey(final ModeleEnum modeleEnum, final String name) {
		return this.getMap(modeleEnum).containsKey(name);
	}
	public final int getNewIdentifier (final ModeleEnum modeleEnum) {
		
		switch (modeleEnum) {
    	
		  case compilation: return  this._indexCompilation++;
			
		  case key: return this._indexKeyProperty++;
		  
		  case module: return this._indexModule++;
		  
		  case myinterface: return this._indexInterface++;

		}
  	    return IBusinessObject.ID_UNDEFINED;
	}
 
	
	//TODO terminer
}
