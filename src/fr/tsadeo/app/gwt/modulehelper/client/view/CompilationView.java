package fr.tsadeo.app.gwt.modulehelper.client.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import fr.tsadeo.app.gwt.modulehelper.client.panel.PanelPermutation;
import fr.tsadeo.app.gwt.modulehelper.client.service.ServiceCallback;
import fr.tsadeo.app.gwt.modulehelper.client.util.AppController;
import fr.tsadeo.app.gwt.modulehelper.client.util.AppController.ICallbackInfo;
import fr.tsadeo.app.gwt.modulehelper.client.util.AppController.ViewNameEnum;
import fr.tsadeo.app.gwt.modulehelper.client.util.Constantes;
import fr.tsadeo.app.gwt.modulehelper.client.util.Constantes.Action;
import fr.tsadeo.app.gwt.modulehelper.client.util.WidgetUtils;
import fr.tsadeo.app.gwt.modulehelper.client.widget.TableModules;
import fr.tsadeo.app.gwt.modulehelper.client.widget.TablePermutations;
import fr.tsadeo.app.gwt.modulehelper.client.widget.TableProperties;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.CompilationDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.CompilationLightDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.PermutationDto;

public class CompilationView extends AbstractPanelView {
	
	private final static Logger log = Logger.getLogger(CompilationView.class.getName());
	
	private final TableModules _tableModules = new TableModules();
	private final TableProperties _tableProperties  = new TableProperties();
	private TablePermutations _tablePermutations;
	
	private CompilationDto _currentCompilation;
	
	
	private final ListBox _lbCompilations = new ListBox();
	
	private final Panel _permutationContener = new VerticalPanel();	
	private final PanelPermutation _panelPermutation = new PanelPermutation();
	
	private final Map<String, CompilationLightDto> _mapCompilationById = new HashMap<String, CompilationLightDto>();

	public CompilationView() {
		super("Compilation", ViewNameEnum.compilation);
		this.initComposants();
		this.initHandlers();
		this.setContent(this.buildContent());
	}
	
	//---------------------------------- overriding AbstractPanelView
	@Override
	protected void protectedReinit() {
		this._lbCompilations.clear();
		this._mapCompilationById.clear();
		this._tableModules.reinit();
		this._tableProperties.reinit();
		this._tablePermutations.reinit();


	}
	@Override
	protected void protectedUpdate() {
		
		switch (this.getViewParams().getUpdateType()) {
		case NONE: return;
		case ALL : this.getDataFromServer(Action.loadCompilations);
		    break;
		case ENTITY :
			 this.selectCurrentItemInListBox(this._lbCompilations);
			this.getDataFromServer(Action.loadCompilation);
			break;
		}

	}
	@Override
	protected void getDataFromServer(final Action action) {
			
		switch (action) {
		
	
		case loadCompilations:	
			AppController.getService().loadCompilations(new ServiceCallback<List<CompilationLightDto>>(action) {

				@Override
				public void onSuccess(List<CompilationLightDto> result) {
					populateCompilationList(result);
					AppController.getInstance().done(action);
					if (result != null && !result.isEmpty()) {
						 selectCurrentItemInListBox(_lbCompilations);
				  	     getDataFromServer(Action.loadCompilation);
					}
					
				}
			});
			break;

		case loadCompilation: 
	        
              AppController.getService().loadCompilation(this.getCurrentCompilationName(),
            		  new ServiceCallback<CompilationDto>(action) {

				@Override
				public void onSuccess(CompilationDto compilationDto) {
					_currentCompilation = compilationDto;
					populateGrids(compilationDto);
					AppController.getInstance().done(action);
				}
			});
			break;
		}
	}
	//---------------------------------- private methods
	private String getCurrentCompilationName() {
		
	    final String compilationId =  this._lbCompilations.getValue(this._lbCompilations.getSelectedIndex());	        
	   return  this._mapCompilationById.get(compilationId).getName();
	}
	private void populateCompilationList(final List<CompilationLightDto> compilationList) {
		for (int i = 0; i < compilationList.size(); i++) {
			final CompilationLightDto compilation = compilationList.get(i);
			
			final String compilationId = Integer.toString(compilation.getId());
			this._lbCompilations.addItem(compilation.getName(),  compilationId);
			this._mapCompilationById.put(compilationId, compilation);		
		}
	}

	/**
	 * Permutation à mettre à jour en fonction du choix de l'utilisateur
	 * dans le PanelPermutation
	 *
	 * @param permutationNumber : 0 to n-1
	 */
	private void updatePermutation(final int permutationNumber) {
		
		Scheduler.get().scheduleDeferred(new Command() {
			
			@Override
			public void execute() {
				if (_currentCompilation != null && _currentCompilation.hasPermutations()) {
					int countPermutation = _currentCompilation.getPermutationList().size();
					if (permutationNumber < 0 || permutationNumber > countPermutation-1) {
						return;
					}
					PermutationDto permutationDto = _currentCompilation.getPermutationList().get(permutationNumber);
					_panelPermutation.populateGrid(permutationDto);
				}
			}
		});

	}
	private void populateGrids(final CompilationDto compilationDto) {
		
		log.config("populateGrids() : " + compilationDto.getName());

		// ------ tables
		this._tableModules.populateGrid(compilationDto.getModuleList());
		this._tableProperties.populateGrid(compilationDto.getCompilationPropertyList());
		this._tablePermutations.populateGrid(compilationDto.getPermutationList());
		
		//--------- permutations A TERMINER
		this._panelPermutation.populateGrid(compilationDto.getPermutationList().get(0));
		
	}
	private Panel buildContent() {
		
		log.info("buildContent()");
		final VerticalPanel content = new VerticalPanel();

		final HorizontalPanel pModule = new HorizontalPanel();
		pModule.setSpacing(Constantes.SPACING_MIN);
		pModule.setHeight(Constantes.Dim50PX);
		pModule.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);;
			
		pModule.add(WidgetUtils.buildWhiteLabel("choisir la compilation : "));
		pModule.add(_lbCompilations);
		
		final TabPanel tabPanel = new TabPanel();
		tabPanel.setStyleName(Constantes.STYLE_TAB_PANEL);
		tabPanel.add(this.buildModulesPanel(), "modules");
		tabPanel.add(this.buildPropertiesPanel(), "propriétés");
		tabPanel.add(this.buildPermutationsContener(), "permutations");
		tabPanel.selectTab(0);
		
		content.add(pModule);
		content.setCellHeight(pModule, Constantes.Dim50PX);
		content.add(tabPanel);
		content.setCellHeight(tabPanel, Constantes.MAX_SIZE);
		
		return content;
	}
	private Panel buildModulesPanel() {
		
		final VerticalPanel modulePanel = new VerticalPanel();
		modulePanel.setSpacing(5);
		modulePanel.setHeight(Constantes.MAX_SIZE);
		modulePanel.setWidth(Constantes.MAX_SIZE);
		modulePanel.add(this._tableModules);

		
		return modulePanel;
	}
	private Panel buildPropertiesPanel() {
		
		final VerticalPanel propertiesPanel = new VerticalPanel();
		propertiesPanel.setSpacing(Constantes.SPACING_MIN);
		propertiesPanel.setHeight(Constantes.MAX_SIZE);
		propertiesPanel.setWidth(Constantes.MAX_SIZE);
		propertiesPanel.add(this._tableProperties);
		return propertiesPanel;
	}
	private Panel buildPermutationsContener() {
				
		this._permutationContener.add(this._tablePermutations);
        this._permutationContener.add(this._panelPermutation);
		return _permutationContener;
	}

     private void initHandlers() {
    	 this._lbCompilations.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				updateCurrentIdWithSelectedItem(_lbCompilations);
				getDataFromServer(Action.loadCompilation);
			}
		});
    	 
     }
     private void initComposants()  {
    	 
    	this._tablePermutations = new TablePermutations(new ICallbackInfo<Integer>() {
			
			@Override
			public void onHave(Integer permutationNumber) {
				updatePermutation(permutationNumber);
			}
		});
 		this._lbCompilations.setVisibleItemCount(1);
 		this._lbCompilations.setWidth("550px");
     }


}
