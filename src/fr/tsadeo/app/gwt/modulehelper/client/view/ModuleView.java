package fr.tsadeo.app.gwt.modulehelper.client.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

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
import fr.tsadeo.app.gwt.modulehelper.client.widget.TableModules;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.ModuleDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.ModuleLightDto;
import fr.tsadeo.app.gwt.modulehelper.shared.util.ISharedConstants;
import fr.tsadeo.app.gwt.modulehelper.shared.util.StringHelper;

public class ModuleView extends AbstractPanelView {


	private final static Logger log = Logger.getLogger(ModuleView.class.getName());
	
	final VerticalPanel _interfacesContener = new VerticalPanel();
	
	private final TableModuleXProperties _tableProperty = new TableModuleXProperties(TypeTable.byModule);
	private final TableModules _tableImportModules = new TableModules();
	

	private final TextArea _taContent = new TextArea();
	private final TabPanel _tabPanel = new TabPanel();
	
	final ListBox _lbModules = new ListBox();
	
	private final TextBox _tbModuleName = new TextBox();
	private final TextBox _tbRenameTo = new TextBox();
	private final TextBox _tbEntryPoint = new TextBox();
	
	private final List<PanelInterface> _panelInterfaceXImplementationList = new ArrayList<PanelInterface>();
	private final List<PanelInterface> _panelInterfaceXGeneratorList = new ArrayList<PanelInterface>();
	
	private final Map<String, ModuleLightDto> _mapModuleById = new HashMap<String, ModuleLightDto>();

	
	 public ModuleView() {
		super("Modules", ViewNameEnum.module);
		this.initComposants();
		this.initHandlers();
		this.setContent(this.buildContent());
	}
	//----------------------------------- overriding AbstractPanelView
	@Override
	protected void protectedReinit() {
		this._lbModules.clear();
		this._mapModuleById.clear();
		this._tableImportModules.reinit();
		this._tableProperty.reinit();		
		this._interfacesContener.clear();
	}


	@Override
	protected void protectedUpdate() {
		
		switch (this.getViewParams().getUpdateType()) {
		case NONE: return;
		case ALL : this.getDataFromServer(Action.loadModules);
		    break;
		case ENTITY : 
			 this.selectCurrentItemInListBox(this._lbModules);
			this.getDataFromServer(Action.loadModule);
			break;
		}

	}
	@Override
	protected void getDataFromServer(final Action action) {
		
		switch (action) {
		
		case loadModules: AppController.getService().loadModules(new ServiceCallback<List<ModuleLightDto>>(action) {

			@Override
			public void onSuccess(List<ModuleLightDto> result) {
				populateModuleList(result);
				AppController.getInstance().done(action);
				if (result != null && !result.isEmpty()) {
					selectCurrentItemInListBox(_lbModules);
			  	    getDataFromServer(Action.loadModule);
				}
			}
		});
			break;
			
		case loadModule:
			    AppController.getService().loadModule(this.getCurrentModuleName(), 
			    		        new ServiceCallback<ModuleDto>(action) {

				@Override
				public void onSuccess(ModuleDto result) {
					populateDatas(result);
					AppController.getInstance().done(action);
				}
			});

			
			break;
		
		}
	}
	//---------------------------------------- private methods
	private String getCurrentModuleName() {
		
        final String moduleId =  this._lbModules.getValue(this._lbModules.getSelectedIndex());	        
        return this._mapModuleById.get(moduleId).getName();
	}
	private void populateModuleList(final List<ModuleLightDto> moduleList) {
		if (moduleList == null) return;
		
		log.info("populateModuleList()");
		Collections.sort(moduleList, ModuleLightDto.COMPARATOR_FILENAME);
		
		final int sizeList = moduleList.size();
		
		int maxLength = 0;
		
		for (int i = 0; i <sizeList; i++) {
			final int fileNameLength = moduleList.get(i).getFileName().length();
			if (maxLength < fileNameLength) {
				maxLength = fileNameLength;
			}
		}
		
        log.config("maxLength: " + maxLength);
		for (int i = 0; i <sizeList; i++) {
			
			final ModuleLightDto module = moduleList.get(i);

			//final String diplayName = StringHelper.completeStringWithSpaceJava(module.getFileName(), maxLength) + " [" + module.getPackageName() + "]";
			
			final String displayName = module.getFileName(); 
			final String moduleId = Integer.toString(module.getId());
			this._lbModules.addItem(displayName,  moduleId);
			this._mapModuleById.put(moduleId, module);
		}
		
	}
	
	
	private void populateDatas(final ModuleDto moduleDto) {
		
		// TabPanel
		final boolean virtualModule = moduleDto.isVirtual();
		this._tabPanel.setVisible(!virtualModule);
		
		// content				
		if (!virtualModule) {
			this._taContent.setText((moduleDto.getContent() == null)?"":
					StringHelper.valuesToString(moduleDto.getContent(), ISharedConstants.JAVA_SAUT_LIGNE)  );
		}
	
		// info module
		this._tbModuleName.setText(moduleDto.getName());
		this._tbEntryPoint.setText(moduleDto.getEntryPoint());
		this._tbRenameTo.setText(moduleDto.getRenameTo());
				
		// tables		
		this._tableImportModules.populateGrid(moduleDto.getImportModuleList());
        this._tableProperty.populateGrid(moduleDto.getModuleXPropertyList());

        // ---------------        interfaces
        this._interfacesContener.clear();
        
        // ---- interface X implementation
        List<String> interfaceNameList = moduleDto.getInterfaceWithImplementationList();
        if (interfaceNameList != null && !interfaceNameList.isEmpty()) {
        	
        	InterfaceContainerHelper.setDataInterfaces(TypeTable.byModule, this._interfacesContener, moduleDto,
        			interfaceNameList, this._panelInterfaceXImplementationList, SousType.implementation);
        }
        
        // ---- interface X Generator
        interfaceNameList = moduleDto.getInterfaceWithGeneratorList();
        if (interfaceNameList != null && !interfaceNameList.isEmpty()) {
        	
        	InterfaceContainerHelper.setDataInterfaces(TypeTable.byModule, this._interfacesContener, moduleDto,
        			interfaceNameList, this._panelInterfaceXGeneratorList, SousType.generator);
        }
        	
	}
	private Panel buildContent() {
		
		log.info("buildContent()");
		final VerticalPanel content = new VerticalPanel();

		final HorizontalPanel pModule = new HorizontalPanel();
		pModule.setSpacing(Constantes.SPACING_MIN);
		pModule.setHeight(Constantes.Dim50PX);
		pModule.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);;
			
		pModule.add(WidgetUtils.buildWhiteLabel("choisir le fichier : "));
		pModule.add(_lbModules);
		
		
		this._tabPanel.setStyleName(Constantes.STYLE_TAB_PANEL);
		this._tabPanel.add(this.buildPropertiesPanel(), "propriétés");
		this._tabPanel.add(this.buildInterfacesPanel(), "interfaces");
		this._tabPanel.add(this.buildModulesPanel(), "dependances");
		this._tabPanel.add(this.buildContentPanel(), "contenu");
		this._tabPanel.selectTab(0);
		
		content.add(pModule);
		content.setCellHeight(pModule, Constantes.Dim50PX);
		
        content.add(this.buildModuleInfoPanel());
        
		content.add(this._tabPanel);
		content.setCellHeight(this._tabPanel, Constantes.MAX_SIZE);
		
		return content;
	}
	
	private Widget buildModuleInfoPanel() {
		
		final Grid grid = new Grid(3, 2);
		grid.setWidth("85%");
		grid.setStyleName(Constantes.STYLE_TABLE);
		
		grid.setWidget(0, 0, new Label("nom du module: "));
		grid.setWidget(0, 1, this._tbModuleName);
		
		grid.setWidget(1, 0, new Label("rename to: "));
		grid.setWidget(1, 1, this._tbRenameTo);
		
		grid.setWidget(2, 0, new Label("entry point: "));
		grid.setWidget(2, 1, this._tbEntryPoint);
		
		grid.getColumnFormatter().setWidth(0, "20%");
		grid.getColumnFormatter().setWidth(1, "80%");

		return grid;
	}
	private Panel buildModulesPanel() {
		
		final VerticalPanel modulePanel = new VerticalPanel();
		modulePanel.setSpacing(Constantes.SPACING_MIN);
		
		modulePanel.setHeight(Constantes.MAX_SIZE);
		modulePanel.setWidth(Constantes.MAX_SIZE);
		modulePanel.add(this._tableImportModules);
		

		return modulePanel;
	}
	private Panel buildPropertiesPanel() {
		
		final Panel propertiesPanel = new SimplePanel();
		propertiesPanel.setHeight(Constantes.MAX_SIZE);
		propertiesPanel.setWidth(Constantes.MAX_SIZE);
		propertiesPanel.add(this._tableProperty);
		return propertiesPanel;
	}

	private Panel buildInterfacesPanel() {
			
		this._interfacesContener.setSpacing(Constantes.SPACING_MIN);
		this._interfacesContener.addStyleName(Constantes.STYLE_PANEL);
		return _interfacesContener;
	}
	
	private Panel buildContentPanel () {
		
	 final Panel panelContent  = new SimplePanel();
	 panelContent.add(this._taContent);
	 return panelContent;
	}

     private void initHandlers() {
    	 
    	 this._lbModules.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				updateCurrentIdWithSelectedItem(_lbModules);
				getDataFromServer(Action.loadModule);
			}
		});

     }
     private void initComposants()  {
    	     	 
 		this._lbModules.setVisibleItemCount(1);
 		this._lbModules.setWidth("670px");
 		
 		final String tbWidth = "600px";
 
 		this._tbEntryPoint.setWidth(tbWidth);
 		this._tbModuleName.setWidth(tbWidth);
 		this._tbRenameTo.setWidth(tbWidth);
 		
 		this._tbEntryPoint.addStyleName(Constantes.STYLE_TABLE_TEXTBOX);
 		this._tbModuleName.addStyleName(Constantes.STYLE_TABLE_TEXTBOX);
 		this._tbRenameTo.addStyleName(Constantes.STYLE_TABLE_TEXTBOX);
 		
 		this._tbEntryPoint.setEnabled(false);
 		this._tbModuleName.setEnabled(false);
 		this._tbRenameTo.setEnabled(false);
 		
 		
 		this._taContent.addStyleName(Constantes.STYLE_MODULE_CONTENT);
 		
     }
	
}
