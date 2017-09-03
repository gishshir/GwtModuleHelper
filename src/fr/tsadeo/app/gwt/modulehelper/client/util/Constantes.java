package fr.tsadeo.app.gwt.modulehelper.client.util;

import fr.tsadeo.app.gwt.modulehelper.client.util.AppController.ViewNameEnum;

public class Constantes {
	
	public enum ActionState {none, start, inprogress, done, error, cancel};
	
	public enum ActionInfo {
		sourceDir, projectDir
	}
	
	public enum Action {clearStatus, reinit, buildDemo, uploadZip, uploadContent,
		buildApplication, buildApplicationFromContent,
		loadModules, loadModule, loadTreeModules,
		loadCompilations, loadCompilation,
		loadInterfaces, loadImplementations, loadGenerators,
		loadKeyProperties, loadKeyProperty}
	
	public static final int TABLE_CELL_MAX_LENGTH = 32;
	
	public static final String MAX_SIZE = "100%";
	public static final int SPACING_MIN = 5;
	public static final String Dim50PX = "50px";
	
	public static final String EXTENSION_ZIP = ".zip";
	public static final String EXTENSION_JAR = ".jar";
	
	public static final String STYLE_HEADER = "myheader";
	public static final String STYLE_TABLE = "mytable";
	public static final String STYLE_PANEL = "myPanel";
	public static final String STYLE_TABLE_TEXT = "text";
	public static final String STYLE_TABLE_TEXTBOX = "textbox";
	public static final String STYLE_TABLE_FILTERBOX = "filterbox";
	public static final String STYLE_TABLE_CONDITIONAL_PROPERTY = "conditional";
	public static final String STYLE_TAB_PANEL = "tabPanel";
	
	public static final String STYLE_NOT_VISIBLE = "notVisible";
	
	public static final String STYLE_WHITE_LABEL = "whiteLabel";

	public static final String STYLE_MODULE_HELPER_VIEW = "moduleHelperView";
	public static final String STYLE_PANEL_VIEW = "panelView";
	public static final String STYLE_PANEL_TITLE = "panelTitle";
	public static final String STYLE_PANEL_MENU = "panelMenu";
	public static final String STYLE_CONTENT = "contentView";
	public static final String STYLE_PANEL_ERROR = "panelError";
	public static final String STYLE_MODULE_CONTENT = "moduleContent";
	
	public static final String STYLE_IMAGE = "imageIcone";
	public static final String STYLE_IMG_PROJET = "imgProjet";
	public static final String STYLE_IMG_MODULE = "imgModule";
	public static final String STYLE_IMG_COMPILATION = "imgCompilation";
	public static final String STYLE_IMG_PROPERTY = "imgProperty";
	public static final String STYLE_IMG_INTERFACE = "imgInterface";
	
	public static final String STYLE_BUTTON = "imageButton";
	public static final String STYLE_BUTTON_HELP = "help";
	public static final String STYLE_BUTTON_DEMO = "demo";
	
	public static final String STYLE_MODULE = "module";
	public static final String STYLE_MODULE_VIRTUAL = "virtualModule";
	
	public static final String STYLE_CURSOR_IN_PROGRESS = "cursorInProgress";
	public static final String STYLE_CURSOR_HELP = "cursorHelp";
	public static final String STYLE_CURSOR_LIEN = "cursorLien";
	
	public static final String STYLE_TITLE = "title";
	
	public static String getStyleModuleName(final boolean virtual) {
		return (virtual)?STYLE_MODULE_VIRTUAL:STYLE_MODULE;
	}
	
	public static String getIconStyleNameByView(final ViewNameEnum viewName) {
		
		String styleName = null;
		
		switch (viewName) {

			case home: styleName = Constantes.STYLE_IMG_PROJET;
			break;
			case module: styleName = Constantes.STYLE_IMG_MODULE;
			break;
			case compilation: styleName = Constantes.STYLE_IMG_COMPILATION;
			break;
			case properties: styleName = Constantes.STYLE_IMG_PROPERTY;
			break;
			case interfaces: styleName = Constantes.STYLE_IMG_INTERFACE;
			break;
			
		}
		
		return styleName;
	}
	
	
}
