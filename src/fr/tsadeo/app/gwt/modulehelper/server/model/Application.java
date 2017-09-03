package fr.tsadeo.app.gwt.modulehelper.server.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.tsadeo.app.gwt.modulehelper.server.util.ExtraSession;
import fr.tsadeo.app.gwt.modulehelper.server.util.StoreExtraSession;


public class Application implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	private final String _sessionId;

	//---------------------------------------------- accessors
	public String getSessionId () {
		return this._sessionId;
	}


	
	//----------------------------------------------- constructors
	public Application() {
		this(null);
	}
	public Application(final String sessionId) {
		this._sessionId = sessionId;
	}
	
	//----------------------------------------------- public methods
	
	// ---compilations
	public boolean hasCompilations() {
		return  this.getExtraSession().hasCompilations();
	}
	public List<Compilation> getCompilationList() {
		final List<Compilation> compilationList = new ArrayList<Compilation>(this.getExtraSession().getCompilations());
		Collections.sort(compilationList);
		return compilationList;
	}
    public Compilation getCompilationByName(final String compilationName) {
    	return this.getExtraSession().getCompilationByName(compilationName);
    }
    
    // ---modules
	public boolean hasModules() {
		return this.getExtraSession().hasModules();
	}
	public List<Module> getModuleList() {
		
		final List<Module> moduleList = new ArrayList<Module>(this.getExtraSession().getModules());
		Collections.sort(moduleList);
		return moduleList;
	}
	public final Module getModuleByName (final String name) {
		return this.getExtraSession().getModuleByName(name);
	}

    // ---interfaces
	public boolean hasInterfaces() {
		return this.getExtraSession().hasInterfaces();
	}
	public List<MyInterface> getInterfaceList() {
		final List<MyInterface> interfaceList = new ArrayList<MyInterface>(this.getExtraSession().getInterfaces());
		Collections.sort(interfaceList);
		return interfaceList;
	}
	public final MyInterface getInterfaceByName (final String name) {
		return  this.getExtraSession().getInterfaceByName(name);
	}
	
	// ---keyPropertie
	public boolean hasKeyProperties() {
		return this.getExtraSession().hasKeyProperties();
	}
	 public final KeyProperty geKeyPropertyByName(final String name) {
		 return this.getExtraSession().geKeyPropertyByName(name);
	 }
	public List<KeyProperty> getKeyPropertyList () {
		final List<KeyProperty> keyPropertyList = new ArrayList<KeyProperty>(this.getExtraSession().getKeyProperties());
		Collections.sort(keyPropertyList);
		return keyPropertyList;
	}
    //------------------------------------------- private methods
    private ExtraSession getExtraSession() {
    	return StoreExtraSession.getExtraSession(this._sessionId);
    }
}
