package fr.tsadeo.app.gwt.modulehelper.client.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.gwt.core.client.GWT;

import fr.tsadeo.app.gwt.modulehelper.client.service.IModuleHelperService;
import fr.tsadeo.app.gwt.modulehelper.client.service.IModuleHelperServiceAsync;
import fr.tsadeo.app.gwt.modulehelper.client.service.LocalStorageManager;
import fr.tsadeo.app.gwt.modulehelper.client.util.Constantes.Action;
import fr.tsadeo.app.gwt.modulehelper.client.util.Constantes.ActionInfo;
import fr.tsadeo.app.gwt.modulehelper.client.view.ViewParams;


public class AppController {
	
	private static AppController instance = new AppController();
	public static AppController getInstance() {
		return instance;
	}
	
	private static final IModuleHelperServiceAsync service = GWT.create(IModuleHelperService.class);
	
	private final Set<IAppListener> _listeners = new HashSet<IAppListener>();
	
	private AppController() {}
	
	public enum ViewNameEnum{
		home, module, compilation, properties, interfaces;
	}
	
	public void displayView (final ViewNameEnum viewName, final ViewParams viewParams) {

         final Iterator<IAppListener> iter = this._listeners.iterator();
         while (iter.hasNext()) {
			iter.next().displayView(viewName, viewParams);
		}
	}
	
	public void addAppListener(final IAppListener listener) {
		this._listeners.add(listener);
	}
	public void removeAppListener(final IAppListener listener) {
		this._listeners.remove(listener);
	}

	public static IModuleHelperServiceAsync getService() {
		return AppController.service;
	}

	public void inProgress(final Action action) {
		this.inProgress(action, null);
	}
	public void inProgress(final Action action, final String message) {
		  final Iterator<IAppListener> iter = this._listeners.iterator();
	         while (iter.hasNext()) {
				iter.next().inProgress(action, message);
			}
	}
	public void done(final Action action) {
		this.done(action, null);
	}
	public void done(final Action action, final String message) {
		 final Iterator<IAppListener> iter = this._listeners.iterator();
         while (iter.hasNext()) {
			iter.next().done(action, message);
		}
	}
	public void error(final Action action, final String errorMessage) {
		 final Iterator<IAppListener> iter = this._listeners.iterator();
         while (iter.hasNext()) {
			iter.next().error(action, errorMessage);
		}
	}
	
	public void reinit() {
		 final Iterator<IAppListener> iter = this._listeners.iterator();
         while (iter.hasNext()) {
			iter.next().reinitApp();
		}
	}
	
	public String getInfo(final ActionInfo actionInfo) {
		
		// Recupération depuis LocalStorage
		switch (actionInfo) {
		   case projectDir: return LocalStorageManager.get().getProjectDirectory();
		   case sourceDir: return LocalStorageManager.get().getSourceDirectory();
		}
        return null;

	}
	
	public void setInfo(final ActionInfo actionInfo, final String info) {

		// sauvegarde dans LocalStorage
		switch (actionInfo) {
		   case projectDir: LocalStorageManager.get().storeProjectDirectory(info);
		        break;
		   case sourceDir: LocalStorageManager.get().storeSourceDirectory(info);
		       break;
		}
		// on propage à l'application
		 final Iterator<IAppListener> iter = this._listeners.iterator();
         while (iter.hasNext()) {
			iter.next().updateInfo(actionInfo, info);
		}
	}

	//------------------------------------------------ inner class
	public interface ICallbackInfo<T> {
		
		public void onHave(T info);
	}
}
