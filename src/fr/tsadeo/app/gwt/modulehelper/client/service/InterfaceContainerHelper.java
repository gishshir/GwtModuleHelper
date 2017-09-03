package fr.tsadeo.app.gwt.modulehelper.client.service;

import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.user.client.ui.Panel;

import fr.tsadeo.app.gwt.modulehelper.client.panel.PanelInterface;
import fr.tsadeo.app.gwt.modulehelper.client.widget.AbstractTable.TypeTable;
import fr.tsadeo.app.gwt.modulehelper.client.widget.TableInterfaceXImplementationOrGenerator.SousType;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.IContainerInterface;

public class InterfaceContainerHelper {
	
	private final static Logger log = Logger.getLogger(InterfaceContainerHelper.class.getName());

	private static final void completerPanelInterfaceList (final TypeTable typeTable, final List<PanelInterface> panelInterfaceList, 
			final int countInterfaces, final SousType sousType) {
		
        // creation de PanelInterfaceXimplementation or PanelInterfaceXGenerator si necessaire
        int size = panelInterfaceList.size();
  		log.config("count: " + countInterfaces + " - size: " + size);
  		if (size < countInterfaces) {
  			for (int i = size; i < countInterfaces; i++) {
  				panelInterfaceList.add(new PanelInterface(typeTable, sousType));				
  			}
  		}
	}
	
	public static final void setDataInterfaces(final TypeTable typeTable, final Panel panel, final IContainerInterface containerInterface,
			final List<String> interfaceNameList, final List<PanelInterface> panelInterfaceList,
			final SousType sousType) {
		
	     // ---------------        interfaces
        if (interfaceNameList != null && !interfaceNameList.isEmpty()) {
        	
          // creation de PanelInterfaceXxxxxxxxxx si necessaire
         int countInterfaces = interfaceNameList.size();	
    	 InterfaceContainerHelper.completerPanelInterfaceList(typeTable, panelInterfaceList,
    				countInterfaces,sousType);
    	 // remplir avec les listes de InterfaceXImplementation ou InterfaceXGenerator
    	 InterfaceContainerHelper.populateGrids(interfaceNameList,
    			 countInterfaces, panelInterfaceList, containerInterface, panel);
        }
        
	}
	
	private static final void populateGrids(final List<String> interfaceNameList, final int countInterfaces,
			final List<PanelInterface> panelInterfaceList, final IContainerInterface containerInterface,
			final Panel panel) {
		
        // for each interface
  	  for (int j = 0; j < countInterfaces; j++) {
			final String interfaceName = interfaceNameList.get(j);
           log.config("interfaceName: " + interfaceName);
           final PanelInterface panelInterface = panelInterfaceList.get(j);
           panelInterface.populateGrid(containerInterface, interfaceName);
           panel.add(panelInterface);
        
		  }	
      
	}
}
