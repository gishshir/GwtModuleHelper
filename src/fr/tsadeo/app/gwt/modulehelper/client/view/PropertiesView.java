package fr.tsadeo.app.gwt.modulehelper.client.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import fr.tsadeo.app.gwt.modulehelper.client.panel.PanelInterface;
import fr.tsadeo.app.gwt.modulehelper.client.service.InterfaceContainerHelper;
import fr.tsadeo.app.gwt.modulehelper.client.service.ServiceCallback;
import fr.tsadeo.app.gwt.modulehelper.client.util.AppController;
import fr.tsadeo.app.gwt.modulehelper.client.util.AppController.ViewNameEnum;
import fr.tsadeo.app.gwt.modulehelper.client.util.Constantes;
import fr.tsadeo.app.gwt.modulehelper.client.util.Constantes.Action;
import fr.tsadeo.app.gwt.modulehelper.client.util.WidgetUtils;
import fr.tsadeo.app.gwt.modulehelper.client.widget.AbstractTable.TypeTable;
import fr.tsadeo.app.gwt.modulehelper.client.widget.TableInterfaceXImplementationOrGenerator.SousType;
import fr.tsadeo.app.gwt.modulehelper.client.widget.TableModuleXProperties;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.KeyPropertyDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.KeyPropertyLightDto;

public class PropertiesView extends AbstractPanelView {
	
	 private final static Logger log = Logger.getLogger(PropertiesView.class.getName());
	
	private final ListBox _lbKeyProperties = new ListBox();
	private final TableModuleXProperties _tableModuleXProperty = new TableModuleXProperties(TypeTable.byKeyProperty);
	
	private final TabPanel _tabPanel = new TabPanel();
	final VerticalPanel _interfacesContener = new VerticalPanel();
	 
	private final List<PanelInterface> _panelInterfaceXImplementationList = new ArrayList<PanelInterface>();
	private final List<PanelInterface> _panelInterfaceXGeneratorList = new ArrayList<PanelInterface>();
	
	private final Map<String, KeyPropertyLightDto> _mapKeyPropertyById = new HashMap<String, KeyPropertyLightDto>();

	public PropertiesView() {
		super("Properties", ViewNameEnum.properties);
		this.initComposants();
		this.initHandlers();
		this.setContent(this.buildContent());
	}
	//---------------------------------- overriding AbstractPanelView
	@Override
	protected void protectedReinit() {
		this._mapKeyPropertyById.clear();
		this._lbKeyProperties.clear();
		this._tableModuleXProperty.reinit();
		
	}
	@Override
	protected void protectedUpdate() {
		switch (this.getViewParams().getUpdateType()) {
		case NONE: return;
		case ALL : this.getDataFromServer(Action.loadKeyProperties);
		    break;
		case ENTITY : 
	    	 this.selectCurrentItemInListBox(this._lbKeyProperties);
			this.getDataFromServer(Action.loadKeyProperty);
			break;
		}
	}

	@Override
	protected void getDataFromServer(final Action action) {
		
		switch (action) {
		     case loadKeyProperties: AppController.getService().loadKeyProperties(new ServiceCallback<List<KeyPropertyLightDto>>(action) {
				
				@Override
				public void onSuccess(List<KeyPropertyLightDto> result) {
					populateKeyPropertyList(result);
					AppController.getInstance().done(action);
					if (result != null && !result.isEmpty()) {
						selectCurrentItemInListBox(_lbKeyProperties);
					   getDataFromServer(Action.loadKeyProperty);
					}
				}

			});
		     break;
		     
		     case loadKeyProperty:
		    	  AppController.getService().loadKeyProperty(this.getCurrentKeyProperty(), 
		    			  new ServiceCallback<KeyPropertyDto>(action) {

					@Override
					public void onSuccess(KeyPropertyDto result) {
						populateDatas(result);
						AppController.getInstance().done(action);
					}
				});

		    	 	    	 
		    	 break;
		}
		
	}
	//---------------------------------- private methods
    private KeyPropertyLightDto getCurrentKeyProperty() {
  	  final String keyPropertyId =  this._lbKeyProperties.getValue(this._lbKeyProperties.getSelectedIndex());	        
  	  return this._mapKeyPropertyById.get(keyPropertyId);
    }
	private void populateDatas(final KeyPropertyDto keyProperty) {
		
		// ----------------       module tab
		this._tableModuleXProperty.populateGrid(keyProperty.getModuleXPropertyList());
		
		 // ---------------        interfaces tab
        this._interfacesContener.clear();
        
		 // ---- interface X implementation
        List<String> interfaceNameList = keyProperty.getInterfaceWithImplementationList();
        if (interfaceNameList != null && !interfaceNameList.isEmpty()) {
        	
        	InterfaceContainerHelper.setDataInterfaces(TypeTable.byInterface, this._interfacesContener, keyProperty,
        			interfaceNameList, this._panelInterfaceXImplementationList, SousType.implementation);
        }
        
        // ---- interface X Generator
        interfaceNameList = keyProperty.getInterfaceWithGeneratorList();
        if (interfaceNameList != null && !interfaceNameList.isEmpty()) {
        	
        	InterfaceContainerHelper.setDataInterfaces(TypeTable.byInterface, this._interfacesContener, keyProperty,
        			interfaceNameList, this._panelInterfaceXGeneratorList, SousType.generator);
        }
		
	}
	private void populateKeyPropertyList(final List<KeyPropertyLightDto> keyPropertyList) {
		if (keyPropertyList == null) return;
		for (int i = 0; i < keyPropertyList.size(); i++) {
			final KeyPropertyLightDto keyProperty = keyPropertyList.get(i);
			
			final String keyPropertyId = Integer.toString(keyProperty.getId());
			this._mapKeyPropertyById.put(keyPropertyId, keyProperty);
			this._lbKeyProperties.addItem(keyProperty.getName(),keyPropertyId);			
		}
	}
	private Panel buildContent() {
		
		log.info("buildContent()");
		final VerticalPanel content = new VerticalPanel();

		final HorizontalPanel pKeyProperties = new HorizontalPanel();
		pKeyProperties.setSpacing(Constantes.SPACING_MIN);
		pKeyProperties.setHeight(Constantes.Dim50PX);
		pKeyProperties.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);;
			
		pKeyProperties.add(WidgetUtils.buildWhiteLabel("choisir la propriété : "));
		pKeyProperties.add(this._lbKeyProperties);
		content.add(pKeyProperties);		
		
		this._tabPanel.setStyleName(Constantes.STYLE_TAB_PANEL);
		this._tabPanel.add(this.buildModulesPanel(), "modules");
		this._tabPanel.add(this.buildInterfacesPanel(), "interfaces");
		this._tabPanel.selectTab(0);
		
		content.add(this._tabPanel);
		content.setCellHeight(this._tabPanel, Constantes.MAX_SIZE);

		return content;
	}
	
	private Panel buildModulesPanel() {
		
		final VerticalPanel modulePanel = new VerticalPanel();
		modulePanel.setSpacing(Constantes.SPACING_MIN);
		
		modulePanel.setHeight(Constantes.MAX_SIZE);
		modulePanel.setWidth(Constantes.MAX_SIZE);
		modulePanel.add(this._tableModuleXProperty);
		

		return modulePanel;
	}
	
	private Panel buildInterfacesPanel() {
		
		this._interfacesContener.setSpacing(Constantes.SPACING_MIN);
		this._interfacesContener.addStyleName(Constantes.STYLE_PANEL);
		return _interfacesContener;
	}
	
	private void initHandlers() {
		
		this._lbKeyProperties.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				updateCurrentIdWithSelectedItem(_lbKeyProperties);
				getDataFromServer(Action.loadKeyProperty);
			}
		});

	}
	private void initComposants() {
		
		this._lbKeyProperties.setVisibleItemCount(1);
		this._lbKeyProperties.setWidth("300px");
	}


}
