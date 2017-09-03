package fr.tsadeo.app.gwt.modulehelper.client;

import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

import fr.tsadeo.app.gwt.modulehelper.client.util.ModuleHelperBridge;
import fr.tsadeo.app.gwt.modulehelper.client.view.ModuleHelperView;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ModuleHelperEntryPoint implements EntryPoint {
	
    private final static Logger log = Logger.getLogger("ModuleHelperEntryPoint");


	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		log.info("onModuleLoad");
		ModuleHelperBridge.exportStaticMethod();
		RootPanel.get("moduleHelper").add(new ModuleHelperView());
		
	}
}
