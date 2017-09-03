package fr.tsadeo.app.gwt.modulehelper.client.panel;




import java.util.logging.Logger;

import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import fr.tsadeo.app.gwt.modulehelper.client.util.AppController;
import fr.tsadeo.app.gwt.modulehelper.client.util.AppController.ViewNameEnum;
import fr.tsadeo.app.gwt.modulehelper.client.util.Constantes;
import fr.tsadeo.app.gwt.modulehelper.client.util.Constantes.Action;
import fr.tsadeo.app.gwt.modulehelper.client.util.Constantes.ActionInfo;
import fr.tsadeo.app.gwt.modulehelper.client.util.IAppListener;
import fr.tsadeo.app.gwt.modulehelper.client.view.ViewParams;
import fr.tsadeo.app.gwt.modulehelper.client.widget.ButtonMenu;

public class PanelMenu extends SimplePanel implements IAppListener {
	
	 private final static Logger log = Logger.getLogger("PanelMenu");
	
	private static final String STYLE_TITLE = "title";
	private static final String STYLE_CONTENT = "contentMenu";
	
    private final ButtonMenu _btHome = new ButtonMenu("Projet", AppController.ViewNameEnum.home);
	private final ButtonMenu _btModule = new ButtonMenu("Modules", AppController.ViewNameEnum.module);
	private final ButtonMenu _btCompilation = new ButtonMenu("Compilations", AppController.ViewNameEnum.compilation);
	private final ButtonMenu _btProperties = new ButtonMenu("Propriétés", AppController.ViewNameEnum.properties);
	private final ButtonMenu _btInterfaces = new ButtonMenu("Interfaces", AppController.ViewNameEnum.interfaces);
	
	private boolean _reinitialized = false;
	
	
	public PanelMenu() {
		this.setStyleName(Constantes.STYLE_PANEL_MENU);
		this.setWidget(this.buildMainPanel());
		AppController.getInstance().addAppListener(this);
	}
	
	//---------------------------------------- private methode
	private Panel buildMainPanel () {
		
		final VerticalPanel main = new VerticalPanel();
        main.setWidth(Constantes.MAX_SIZE);
        main.setHeight(Constantes.MAX_SIZE);
		
		final SimplePanel pTitle = new SimplePanel();
		pTitle.setStyleName(STYLE_TITLE);
		
		final VerticalPanel pMenu = new VerticalPanel();
		pMenu.setStyleName(STYLE_CONTENT);
		pMenu.setSpacing(Constantes.SPACING_MIN);
		pMenu.add(this._btHome);
		pMenu.add(this._btModule);
		pMenu.add(this._btCompilation);
		pMenu.add(this._btProperties);
		pMenu.add(this._btInterfaces);
		
		main.add(pTitle);
		main.add(pMenu);
		return main;
	}

	private void enabledButton (final boolean enabled) {
		this._btCompilation.setEnabled(enabled);
		this._btHome.setEnabled(enabled);
		this._btInterfaces.setEnabled(enabled);
		this._btModule.setEnabled(enabled);
		this._btProperties.setEnabled(enabled);
	}
	//------------------------------------ overriding IAppListener
	@Override
	public void reinitApp() {
		log.config("reinitApp()");
		this.enabledButton(false);
		this._reinitialized = true;
	}

	@Override
	public void displayView(ViewNameEnum viewName, final ViewParams viewParams) {
	}

	@Override
	public void inProgress(Action action, String message) {
		this.enabledButton(false);
	}

	@Override
	public void done(Action action, String message) {
		if (action == Action.buildApplication || action == Action.buildApplicationFromContent
				|| action == Action.buildDemo) {
			this._reinitialized = false;
		}
		this.enabledButton(!this._reinitialized);	
	}

	@Override
	public void error(Action action, String errorMessage) {
		this.enabledButton(!this._reinitialized);
	}

	@Override
	public void updateInfo(ActionInfo actionInfo, String info) {
	}

	
	

}
