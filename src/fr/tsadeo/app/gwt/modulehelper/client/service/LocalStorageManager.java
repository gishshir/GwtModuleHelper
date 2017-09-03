package fr.tsadeo.app.gwt.modulehelper.client.service;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.storage.client.Storage;

public class LocalStorageManager {
	
	public static final String KEY_PROJECT_DIRECTORY = "projet.directory.key";
	public static final String KEY_SOURCE_DIRECTORY = "source.directory.key";
	
	private static final LocalStorageManager impl = GWT.create(LocalStorageManager.class);
	public static final LocalStorageManager get() {
		return impl;
	}
	
	private Boolean localStorageSupported = null;
	private Storage storage;
	
	private Map<String, String> mapIfNotLocalStorageSupported;
		
	//--------------------------------------------- constructeur
	public LocalStorageManager() {
		this.initStorage();
	}

    public boolean isLocalStorageSupported() {
    	return this.localStorageSupported;
    }
	public void storeProjectDirectory(final String projectDirectory) {
		this.storeKeyValue(KEY_PROJECT_DIRECTORY, projectDirectory);
	}
	public void storeSourceDirectory(final String sourceDirectory) {
		this.storeKeyValue(KEY_SOURCE_DIRECTORY, sourceDirectory);
	}
	public String getProjectDirectory() {
		return this.getValueFromKey(KEY_PROJECT_DIRECTORY);
	}
	public String getSourceDirectory() {
		return this.getValueFromKey(KEY_SOURCE_DIRECTORY);
	}
	//---------------------------------------------- private methods
	private void initStorage()  {

		this.storage =  Storage.getLocalStorageIfSupported();
		if (storage == null) {
			this.localStorageSupported = false;
			this.mapIfNotLocalStorageSupported = new HashMap<String, String>();
		} else {
			this.localStorageSupported = true;
		}

}
	private void storeKeyValue(final String key, final String value) {
		if (key == null || value == null) return;
		if (this.localStorageSupported) {
			this.storage.setItem(key,value);
		}
		else {
			this.mapIfNotLocalStorageSupported.put(key, value);
		}
	}
	private String getValueFromKey(final String key) {
		if (key == null) return null;

		if (this.localStorageSupported) {
			return this.storage.getItem(key);
		}
		else {
			return this.mapIfNotLocalStorageSupported.get(key);
		}
	}

}
