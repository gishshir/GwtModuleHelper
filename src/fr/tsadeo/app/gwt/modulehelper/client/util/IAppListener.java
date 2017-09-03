package fr.tsadeo.app.gwt.modulehelper.client.util;

import fr.tsadeo.app.gwt.modulehelper.client.util.AppController.ViewNameEnum;
import fr.tsadeo.app.gwt.modulehelper.client.util.Constantes.Action;
import fr.tsadeo.app.gwt.modulehelper.client.util.Constantes.ActionInfo;
import fr.tsadeo.app.gwt.modulehelper.client.view.ViewParams;

public interface IAppListener {
	
	
	public void reinitApp();
	
	public void displayView (final ViewNameEnum viewName, final ViewParams viewParams);
	
	public void inProgress(final Action action, final String message);
	
	public void done(final Action action, final String message);
	
	public void error(final Action action, final String errorMessage);
	

	public void updateInfo(final ActionInfo actionInfo, final String info);
	
}
