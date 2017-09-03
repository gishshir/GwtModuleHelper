package fr.tsadeo.app.gwt.modulehelper.client.view;

import java.util.logging.Logger;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import fr.tsadeo.app.gwt.modulehelper.client.util.AppController.ViewNameEnum;
import fr.tsadeo.app.gwt.modulehelper.client.util.Constantes;
import fr.tsadeo.app.gwt.modulehelper.client.util.Constantes.Action;
import fr.tsadeo.app.gwt.modulehelper.client.view.ViewParams.UpdateType;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.IDto;

public abstract class AbstractPanelView extends SimplePanel {
	
	private final static Logger log = Logger.getLogger(AbstractPanelView.class.getName());

	private Panel _panelTitle = new FlowPanel();
	private SimplePanel _panelContent = new SimplePanel();
	private final ViewNameEnum _viewName;
	private ViewParams _viewParams;
	private boolean _reinitialized = true;
	
	//---------------------------------------- abstract methods
	protected abstract void protectedReinit();
	protected abstract void protectedUpdate();
	protected abstract void getDataFromServer(final Action action);
	
	//--------------------------------------- constructor
	public AbstractPanelView(final String title,  final ViewNameEnum viewName) {
		
		this.setStyleName(Constantes.STYLE_PANEL_VIEW);
		this.initComposant();
		this._viewName = viewName;
		
		this.setWidget(this.buildMainPanel(title));
	}
	
	//---------------------------------------- protected methods
	protected ViewParams getViewParams() {
		return this._viewParams;
	}
	protected boolean reinit() {
		log.config("reinit()");
		this._viewParams = null;
		this._reinitialized = true;
		this.protectedReinit();
		return true;
	}
	protected void update(final ViewParams viewParams) {

		log.config("update() > viewParams.id: " +
		viewParams.getCurrentId()  + " - type: " +viewParams.getUpdateType() + " - reinitialized: " + this._reinitialized);

		if(this._viewParams == null){
			this._viewParams = viewParams;
		}
		
	    // après reinit on récupère toutes les données
		if (this._reinitialized){
			this._viewParams.setUpdateType(UpdateType.ALL);
		} else {
			this._viewParams.setUpdateType(viewParams.getUpdateType());
		}
	
		 if (viewParams.getCurrentId() != IDto.ID_UNDEFINED){
				
			  //same entity
			  if (this._viewParams.getCurrentId() == viewParams.getCurrentId()) {
					
					if (this._viewParams.getUpdateType() == UpdateType.ENTITY) {
					  this._viewParams.setUpdateType(UpdateType.NONE);
					}
					
			  } else {
					// on change d'entity (ALL or ENTITY)
					this._viewParams.setCurrentId(viewParams.getCurrentId());
			   }	
			}
		
		
		  log.config("   >>>   viewParams.id: " + this._viewParams.getCurrentId()  + 
					" - type: " + this._viewParams.getUpdateType());
		  if (this._viewParams.isToUpdate()){

		     this.protectedUpdate();
		     this._reinitialized = false;
		  }

	}
	protected void setContent(Panel content) {
		content.setHeight(Constantes.MAX_SIZE);
		content.setWidth(Constantes.MAX_SIZE);
		this._panelContent.clear();
		this._panelContent.setWidget(content);
	}
	
	/**
	 * Selectionne dans la listbox l'item correspond au current id.
	 * si id undefined ou id non trouvé alors premier de la liste
	 */
	protected void selectCurrentItemInListBox(final ListBox listBox) {
		
		log.config("selectCurrentItemInListBox()");
		final int currentId = this.getViewParams().getCurrentId();
		
		log.config("selectCurrentItemInListBox() > currentId: " + currentId);
		if (currentId == IDto.ID_UNDEFINED) {
			listBox.setSelectedIndex(0);
			return;
		}
		
		// selectionner l'item de meme id
		boolean result = false;
		for (int i = 0; i < listBox.getItemCount(); i++) {
			final String value = listBox.getValue(i);
			if (Integer.parseInt(value) == currentId) {
				result = true;
				listBox.setSelectedIndex(i);
				break;
			}
		}
		if (!result) {
			listBox.setSelectedIndex(0);
		}
	}
	protected void updateCurrentIdWithSelectedItem(final ListBox listBox) {
		final String currentValue = listBox.getValue(listBox.getSelectedIndex());
		this.getViewParams().setCurrentId(Integer.parseInt(currentValue));
	}
	//---------------------------------------- private methods
	private Panel buildMainPanel (final String title) {
		
		final VerticalPanel main = new VerticalPanel();
		main.setWidth(Constantes.MAX_SIZE);
		main.setHeight(Constantes.MAX_SIZE);
		main.add(this.buildPanelTitle(title));
		main.add(this._panelContent);
		
		main.setCellHeight(this._panelTitle, "50px");
		main.setCellHeight(this._panelContent, Constantes.MAX_SIZE);
		return main;
	}
	private Panel buildPanelTitle(final String title) {
		final Panel panelImage = new SimplePanel();
		panelImage.addStyleName(Constantes.STYLE_IMAGE);
		panelImage.addStyleName(Constantes.getIconStyleNameByView(this._viewName));
		
		this._panelTitle.add(panelImage);
		this._panelTitle.add(new Label(title));
		
		return this._panelTitle;
	}
	
	private void initComposant () {

		this._panelTitle.addStyleName(Constantes.STYLE_PANEL_TITLE);
		this._panelContent.setStyleName(Constantes.STYLE_CONTENT);
	}


}
