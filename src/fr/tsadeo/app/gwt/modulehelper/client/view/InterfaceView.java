package fr.tsadeo.app.gwt.modulehelper.client.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.gwt.advanced.client.datamodel.ListCallbackHandler;
import org.gwt.advanced.client.datamodel.ListDataModel;
import org.gwt.advanced.client.datamodel.SuggestionBoxDataModel;
import org.gwt.advanced.client.ui.widget.SuggestionBox;
import org.gwt.advanced.client.ui.widget.combo.DropDownPosition;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

import fr.tsadeo.app.gwt.modulehelper.client.service.ServiceCallback;
import fr.tsadeo.app.gwt.modulehelper.client.util.AppController;
import fr.tsadeo.app.gwt.modulehelper.client.util.AppController.ViewNameEnum;
import fr.tsadeo.app.gwt.modulehelper.client.util.Constantes;
import fr.tsadeo.app.gwt.modulehelper.client.util.Constantes.Action;
import fr.tsadeo.app.gwt.modulehelper.client.util.WidgetUtils;
import fr.tsadeo.app.gwt.modulehelper.client.widget.AbstractTable.TypeTable;
import fr.tsadeo.app.gwt.modulehelper.client.widget.TableInterfaceXImplementationOrGenerator;
import fr.tsadeo.app.gwt.modulehelper.client.widget.TableInterfaceXImplementationOrGenerator.SousType;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.InterfaceXGeneratorDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.InterfaceXImplementationDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.MyInterfaceDto;

public class InterfaceView extends AbstractPanelView {
	
	 private final static Logger log = Logger.getLogger(InterfaceView.class.getName());
	
	private final SuggestionBox _suggestionBoxInterfaces = new SuggestionBox();
	
	private Map<String, MyInterfaceDto> _mapInterfaceById = new HashMap<String, MyInterfaceDto>();
	 
	private final TableInterfaceXImplementationOrGenerator _tableImplementations = 
			new TableInterfaceXImplementationOrGenerator(TypeTable.byInterface, SousType.implementation, false);
	private final TableInterfaceXImplementationOrGenerator _tableGenerators = 
			new TableInterfaceXImplementationOrGenerator(TypeTable.byInterface, SousType.generator, false);

	public InterfaceView() {
		super("Interfaces", ViewNameEnum.interfaces);
		this.initComposants();
		this.initHandlers();
		this.setContent(this.buildContent());
	}
	//---------------------------------- overriding AbstractPanelView
	@Override
	protected void protectedReinit() {
		log.config("protectedReinit");
		this._tableImplementations.reinit();
		this._tableGenerators.reinit();
		this._mapInterfaceById.clear();
		
	}
	@Override
	protected void protectedUpdate() {
		switch (this.getViewParams().getUpdateType()) {
		case NONE: return;
		case ALL : this.getDataFromServer(Action.loadInterfaces);
		    break;
		case ENTITY : 
			this.selectCurrentItemInSuggestionBox(this._suggestionBoxInterfaces);
			this.getDataFromServer(Action.loadImplementations);
			this.getDataFromServer(Action.loadGenerators);
			break;
		}
	}

	@Override
	protected void getDataFromServer(final Action action) {
		
		AppController.getInstance().inProgress(action);
		
		switch (action) {
		case loadInterfaces:
			AppController.getService().loadInterfaces(new ServiceCallback<List<MyInterfaceDto>>(action) {

				@Override
				public void onSuccess(List<MyInterfaceDto> result) {
					populateInterfaceList(result);
					AppController.getInstance().done(action);
				}
			});
		  break;

		case loadGenerators:  {
		  final String interfaceName = this.getCurrentInterfaceName();
		  if (interfaceName == null) {
			  return;
		  }
		  AppController.getService().loadInterfaceXGenerators(interfaceName, new ServiceCallback<List<InterfaceXGeneratorDto>>(action) {

			@Override
			public void onSuccess(List<InterfaceXGeneratorDto> result) {
				populateGridGenerators(result);
				AppController.getInstance().done(action);
			}
	    });
		}
		break; 
		  
		case loadImplementations: { 
			final String interfaceName = this.getCurrentInterfaceName();
			  if (interfaceName == null) {
				  return;
			  }
			AppController.getService().loadInterfaceXImplementations(interfaceName, new ServiceCallback<List<InterfaceXImplementationDto>>(action) {

				@Override
				public void onSuccess(List<InterfaceXImplementationDto> result) {
					populateGridImplementations(result);
					AppController.getInstance().done(action);
				}
		});
		}
			break;
		}
	}

	//---------------------------------- private methods
    private void populateInterfaceList(final List<MyInterfaceDto> interfaces) {
    	
    	if (interfaces == null) return;
    	
    	for (MyInterfaceDto interfaceDto : interfaces) {
    		final String interfaceId = Integer.toString(interfaceDto.getId());
    		this._mapInterfaceById.put(interfaceId, interfaceDto);
		}
    }
	private String getCurrentInterfaceName() {
		final String interfaceId = Integer.toString(this.getViewParams().getCurrentId());
		return this._mapInterfaceById.get(interfaceId).getName();
	}
	
	private void populateGridImplementations(final List<InterfaceXImplementationDto> interfaceXImplementationList) {
		if (interfaceXImplementationList == null || interfaceXImplementationList.isEmpty()) {
			this._tableImplementations.setVisible(false);
			return;
		}
		this._tableImplementations.populateGrid(interfaceXImplementationList);
		this._tableImplementations.setVisible(true);
	}
	private void populateGridGenerators(final List<InterfaceXGeneratorDto> interfaceXGeneratorList) {
		if (interfaceXGeneratorList == null || interfaceXGeneratorList.isEmpty()) {
			this._tableGenerators.setVisible(false);
			return;
		}
		this._tableGenerators.populateGrid(interfaceXGeneratorList);
		this._tableGenerators.setVisible(true);
	}
	private Panel buildContent() {
		
		log.info("buildContent");
		final VerticalPanel content = new VerticalPanel();

		final HorizontalPanel pInterface = new HorizontalPanel();
		pInterface.setSpacing(Constantes.SPACING_MIN);
		pInterface.setHeight(Constantes.Dim50PX);
		pInterface.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);;
				
		pInterface.add(WidgetUtils.buildWhiteLabel("choisir une interface : "));
		//pInterface.add(this._lbInterfaces);
		pInterface.add(this._suggestionBoxInterfaces);
		
		content.add(pInterface);
		content.setCellHeight(pInterface, "30px;");
		
		content.add(this._tableImplementations);
		content.add(this._tableGenerators);
		
		return content;
	}


	private void initHandlers() {
	    	 
	 
		 this._suggestionBoxInterfaces.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				log.config("suggestionBoxInterfaces onChange()");
				updateCurrentIdWithSelectedItem(_suggestionBoxInterfaces);
				getDataFromServer(Action.loadImplementations);
				getDataFromServer(Action.loadGenerators);
			}
		});

	 }
	
	  private void initComposants()  {
	    	 
		    this._tableGenerators.setVisible(false);
		    this._tableImplementations.setVisible(false);
	 		
	 		this._suggestionBoxInterfaces.setExpressionLength(1);
	 		this._suggestionBoxInterfaces.setDropDownPosition(DropDownPosition.UNDER);
	 		this._suggestionBoxInterfaces.setModel(new SuggestionBoxDataModel (new InterfaceListCallbackHandler()));
	 		this._suggestionBoxInterfaces.setWidth("600px");
	 		this._suggestionBoxInterfaces.showList(true);
	 		
	  }
	  private void updateCurrentIdWithSelectedItem(final SuggestionBox suggestionBox) {
			final String currentValue = suggestionBox.getSelectedId();
			this.getViewParams().setCurrentId(Integer.parseInt(currentValue));
		}
	  
		/**
		 * Selectionne dans la suggestionBox l'item correspond au current id.
		 * en creant une expression de recherche sur le nom de l'interface
		 */
	private void selectCurrentItemInSuggestionBox(final SuggestionBox suggestionBox) {
			
			log.config("selectCurrentItemInSuggestionBox()");
			final int currentId = this.getViewParams().getCurrentId();
			
			// chercher le nom de l'interface
			final String interfaceName = this._mapInterfaceById.get(currentId + "").getName();
			log.config("interfaceName: " + interfaceName);
			
			if (interfaceName != null) {
			  this._suggestionBoxInterfaces.setExpression(interfaceName);
			}

	}
	  
  //==================================================== inner class
  private class InterfaceListCallbackHandler implements ListCallbackHandler<ListDataModel> {

	  @Override
	  public void fill(ListDataModel model) {

		  log.config("InterfaceListCallbackHandler > fill()");
         // expression to search
		  final String expressionToSearch = ((SuggestionBoxDataModel) model).getExpression();
		  model.clear();
		  log.config("expressionToSearch: " + expressionToSearch);
		
		  if (_mapInterfaceById == null ||_mapInterfaceById.isEmpty()) {
			  return;
		  }
		  
		  for (MyInterfaceDto interfaceDto : _mapInterfaceById.values()) {
			if (this.matches(interfaceDto, expressionToSearch)) {
				model.add(interfaceDto.getId() + "", interfaceDto.getName());
			}
		  }
		  
	  }

	  private boolean matches(MyInterfaceDto myInterface, final String expression) {
		  if (expression == null || expression.equals("*")) {
			  return true;
		  }
		return (myInterface.getName().indexOf(expression)  != -1);
	  }
	  
  }

}
