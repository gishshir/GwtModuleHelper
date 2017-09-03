package fr.tsadeo.app.gwt.modulehelper.client.widget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;

import fr.tsadeo.app.gwt.modulehelper.client.util.AppController;
import fr.tsadeo.app.gwt.modulehelper.client.util.AppController.ViewNameEnum;
import fr.tsadeo.app.gwt.modulehelper.client.util.Constantes;
import fr.tsadeo.app.gwt.modulehelper.client.view.ViewParams;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.IDto;

public class ButtonMenu extends Composite {
	
	
	private static final String STYLE_MAIN = "panelButton";
	private static final String STYLE_BUTTON = "button";
	
	
	private final ViewNameEnum _viewName;
	private Button _btButton;
	private final Panel _panelImage = new SimplePanel();
	
	public ButtonMenu(final String name, final ViewNameEnum viewName) {

		this._viewName = viewName;
		this.initWidget(this.buildContent(name));

	}	
	
	public void setEnabled(final boolean enabled) {
		this._btButton.setEnabled(enabled);
		if (!enabled) {
			this._btButton.addStyleName(Constantes.STYLE_CURSOR_IN_PROGRESS);
		}
		else {
			this._btButton.removeStyleName(Constantes.STYLE_CURSOR_IN_PROGRESS);
		}
	}
	//------------------------------------------ private methods

	private Panel buildContent(final String name) {
		
		final Panel panel = new FlowPanel();
		panel.addStyleName(STYLE_MAIN);
		
		this._panelImage.addStyleName(Constantes.STYLE_IMAGE);
		this._panelImage.addStyleName(Constantes.getIconStyleNameByView(this._viewName));
		panel.add(this._panelImage);
		
		this._btButton = new Button(name);
		this._btButton.setStyleName(STYLE_BUTTON);
		
		this._btButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				final ViewParams viewParams = new ViewParams(IDto.ID_UNDEFINED);
				AppController.getInstance().displayView(_viewName, viewParams);
			}
		});
		panel.add(_btButton);
		
		return panel;
	}
	
	

}
