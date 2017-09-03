package fr.tsadeo.app.gwt.modulehelper.client.view;

import java.util.logging.Logger;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;

import fr.tsadeo.app.gwt.modulehelper.client.panel.PanelMenu;
import fr.tsadeo.app.gwt.modulehelper.client.panel.PanelStatus;
import fr.tsadeo.app.gwt.modulehelper.client.service.LocalStorageManager;
import fr.tsadeo.app.gwt.modulehelper.client.util.AppController;
import fr.tsadeo.app.gwt.modulehelper.client.util.AppController.ViewNameEnum;
import fr.tsadeo.app.gwt.modulehelper.client.util.Constantes;
import fr.tsadeo.app.gwt.modulehelper.client.util.Constantes.Action;
import fr.tsadeo.app.gwt.modulehelper.client.util.Constantes.ActionInfo;
import fr.tsadeo.app.gwt.modulehelper.client.util.Constantes.ActionState;
import fr.tsadeo.app.gwt.modulehelper.client.util.IAppListener;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.IDto;

public class ModuleHelperView extends SimplePanel implements IAppListener {
	
	 private final static Logger log = Logger.getLogger("ModuleHelperView");
	 private final static String SOURCE_DIRNAME_DEFAULT = "java";
	
	
	private final DockLayoutPanel _mainPanel = new DockLayoutPanel(Unit.PX);
	private final PanelMenu _panelMenu = new PanelMenu();
	private final PanelStatus _panelStatus = new PanelStatus();
	private final SimplePanel _panelContent  = new SimplePanel();
	
	private HomeView _homeView;
	private ModuleView _moduleView;
	private CompilationView _compilationView;
	private InterfaceView _implementationView;
	private PropertiesView _propertiesView;
	
	public ModuleHelperView() {
		this.initComposants();
		this.setWidget(this.buildMainPanel());
		AppController.getInstance().displayView(ViewNameEnum.home, new ViewParams(IDto.ID_UNDEFINED));
	}
	//----------------------------------------- public methods

	
	//----------------------------------------- private methods
	private Panel buildMainPanel() {
		
		this._mainPanel.addSouth(this._panelStatus, 50);
		
		final Panel panel = new SimplePanel();
		panel.add(this._panelMenu);
		this._mainPanel.addWest(panel, 200);

		this._mainPanel.add(this._panelContent);
		this._mainPanel.addStyleName(Constantes.STYLE_MODULE_HELPER_VIEW);
		
		return this._mainPanel;
	}
	private void initComposants() {
		// init default source dir if not exist
		if (LocalStorageManager.get().getSourceDirectory() == null) {
		  LocalStorageManager.get().storeSourceDirectory(SOURCE_DIRNAME_DEFAULT);
		}
		this._panelContent.setHeight(Constantes.MAX_SIZE);
		AppController.getInstance().addAppListener(this);
	}
	
	//-------------------------------------- overriding IAppListener
	@Override
	public void displayView (final ViewNameEnum viewName, final ViewParams viewParams) {
		if (viewName == null) return;
		
		log.info("displayView: " + viewName.name());
		this._panelStatus.setStatus(ActionState.none, null, null);
		
		this._panelContent.clear();
		final AbstractPanelView panelView = this.updateView(viewName, viewParams);
		this._panelContent.setWidget(panelView);	
	}
	
	private AbstractPanelView updateView (final ViewNameEnum viewName, final ViewParams viewParams) {
		if (viewName == null) return null;
		
		log.info("getView: " + viewName.name());
		AbstractPanelView panelView = null;
		boolean reinit = false;
		switch (viewName) {
			case module:  
				if (this._moduleView == null) {
				  this._moduleView = new ModuleView();
				  reinit = true;
				}
				panelView = this._moduleView;
				break;
			case compilation: 
				if (this._compilationView == null) {
				  this._compilationView = new CompilationView();
				  reinit = true;
			    }
				panelView = this._compilationView;
				break;
			
			case properties: 
				if (this._propertiesView == null) {
				  this._propertiesView = new PropertiesView();
				  reinit = true;
			    }
				panelView = this._propertiesView;
				break;

			case interfaces:  
				if (this._implementationView == null) {
				  this._implementationView = new InterfaceView();
				  reinit = true;

			    }
				panelView = this._implementationView;
				break;
			
			case home:  
				if (this._homeView == null) {
					reinit = true;
				  this._homeView = new HomeView();
			    }
			    panelView = this._homeView;
			   break;
			
		}
		
		this.reinitAndUpdate(panelView, viewParams, reinit);
		return panelView;
		
	}


	@Override
	public void inProgress(final Action action, final String message) {
		log.info(action + " inProgress");
		this._panelStatus.setStatus(ActionState.inprogress, action, message);	
		this._mainPanel.addStyleName(Constantes.STYLE_CURSOR_IN_PROGRESS);
		
	}

	@Override
	public void done(final Action action, final String message) {
		log.info(action + " done " + ((message != null)?message:""));
		
		if (action == Action.uploadContent) {
			final String dirPathname = message;
			AppController.getInstance().setInfo(ActionInfo.projectDir, dirPathname);
			this._homeView.buildApplicationAfterUploadContent(dirPathname);
		} 
		this._panelStatus.setStatus((action == Action.clearStatus)?ActionState.none:
				ActionState.done, action, message);
		this._mainPanel.removeStyleName(Constantes.STYLE_CURSOR_IN_PROGRESS);

		
	}
	@Override
	public void error(final Action action, final String errorMessage) {
		log.info(action + " error");
		this._mainPanel.removeStyleName(Constantes.STYLE_CURSOR_IN_PROGRESS);
		this._panelStatus.setStatus(ActionState.error, action, errorMessage);
	}

	@Override
	public void reinitApp() {
		
		log.info("reinitApp()");
		@SuppressWarnings("unused")
		boolean result = (_homeView == null)?false: _homeView.reinit();
		result = (_compilationView == null)?false: _compilationView.reinit();
		result = (_implementationView == null)?false:_implementationView.reinit();
		result = (_moduleView == null)?false:_moduleView.reinit();
		result = (_propertiesView == null)?false:_propertiesView.reinit();
	}

	@Override
	public void updateInfo(final ActionInfo actionInfo, final String info) {
		switch (actionInfo) {
		  case sourceDir: this._homeView.updateSourceDirName(info);
			break;
		}
	}
	//-------------------------------------- private methods
	private void reinitAndUpdate(final AbstractPanelView panelView, final ViewParams viewParams, final boolean reinit) {
		if (panelView == null) return;
		if ( reinit) {
		   panelView.reinit();
		}        
		panelView.update(viewParams);
	}


	
}
