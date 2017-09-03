package fr.tsadeo.app.gwt.modulehelper.client.util;

import fr.tsadeo.app.gwt.modulehelper.client.util.Constantes.Action;
import fr.tsadeo.app.gwt.modulehelper.client.util.Constantes.ActionInfo;

public class ModuleHelperBridge {

	//--------------------------------------------------- JSNI
	public static native void exportStaticMethod() /*-{
	   top.buildApplication =
	          $entry(@fr.tsadeo.app.gwt.modulehelper.client.util.ModuleHelperBridge::buildApplication(Ljava/lang/String;));
	          
	   top.cancelDialog =
	          $entry(@fr.tsadeo.app.gwt.modulehelper.client.util.ModuleHelperBridge::cancelChangeProject());
	          
	   top.startNewProject =
	          $entry(@fr.tsadeo.app.gwt.modulehelper.client.util.ModuleHelperBridge::startChangeProjectDirectory());
	      
	   top.getSourceDirName =
	          $entry(@fr.tsadeo.app.gwt.modulehelper.client.util.ModuleHelperBridge::getSourceDirName());
	   top.setSourceDirName =
	          $entry(@fr.tsadeo.app.gwt.modulehelper.client.util.ModuleHelperBridge::setSourceDirName(Ljava/lang/String;));
		
	   top.getProjectDirectory =
	          $entry(@fr.tsadeo.app.gwt.modulehelper.client.util.ModuleHelperBridge::getProjectDirectory());
	
	}-*/;
	//--------------------------------------------------- public methods
	public static void startChangeProjectDirectory() {
		AppController.getInstance().inProgress(Action.uploadContent);
	}
	/**
	 * Lance la construction des modules de l'application
	 * @param dirPathname
	 */
	public static void buildApplication(final String dirPathname) {
		AppController.getInstance().done(Action.uploadContent, dirPathname);
	}
	/**
	 * Définit le nom du répertoire des sources
	 * @param sourceDirName
	 */
	public static void setSourceDirName(final String sourceDirName) {
		AppController.getInstance().setInfo(ActionInfo.sourceDir, sourceDirName);
	}
	/**
	 * Retourne le nom du repertoire des sources
	 * @return
	 */
	public static String getSourceDirName() {
        return  AppController.getInstance().getInfo(ActionInfo.sourceDir);
	}
	
	public static void cancelChangeProject() {
		AppController.getInstance().done(Action.clearStatus);
	}
	

	/**
	 * Retourne le chemin complet du projet si connu
	 * @return can be null
	 */
	public static String getProjectDirectory() {
		 return  AppController.getInstance().getInfo(ActionInfo.projectDir);
	}
}
