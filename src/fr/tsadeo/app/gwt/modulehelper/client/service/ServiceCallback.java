package fr.tsadeo.app.gwt.modulehelper.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

import fr.tsadeo.app.gwt.modulehelper.client.util.AppController;
import fr.tsadeo.app.gwt.modulehelper.client.util.Constantes.Action;

public abstract class ServiceCallback<T> implements AsyncCallback<T> {
	
	private final Action _action;

	public ServiceCallback(final Action action) {
		this._action = action;
		AppController.getInstance().inProgress(action);
	}
	
	@Override
	public void onFailure(Throwable caught) {
		AppController.getInstance().error(this._action, caught.getMessage());	
	}
}
