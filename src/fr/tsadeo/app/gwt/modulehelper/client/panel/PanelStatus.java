package fr.tsadeo.app.gwt.modulehelper.client.panel;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;

import fr.tsadeo.app.gwt.modulehelper.client.util.Constantes;
import fr.tsadeo.app.gwt.modulehelper.client.util.Constantes.Action;

public class PanelStatus extends SimplePanel {

	
	
	private static final String STYLE_PANEL_STATUS = "panelStatus";
	private static final String STYLE_PANEL_INFO = "panelInfo";
	private static final String STYLE_IS_ERROR = "error";
	
	private static final String STYLE_FLAG = "flag";
	private static final String STYLE_FLAG_OK = "flagOK";
	private static final String STYLE_FLAG_NOK = "flagNok";
	private static final String STYLE_FLAG_IN_PROGRESS = "flagInProgress";
	
	
	private final Label _labelState = new Label("");
	private final SimplePanel _panelInfo = new SimplePanel();
	private final HTML _labelInfo = new HTML("");
	private final Panel _panelFlag = new SimplePanel();
	
	public PanelStatus() {
		
		this.initComposants();
		this.setWidget(this.buildMainPanel());
	}
	
	//---------------------------------------- public methods
	public void setStatus(final Constantes.ActionState state, final Action action,  String message) {
		
		message = (message == null)?"":message;		
		this.removeAllStyles();
		
		switch (state) {
		  case none:  this._labelState.setText("");
		  			  this._labelInfo.setText("");
					break;
		  case inprogress: this._panelFlag.addStyleName(STYLE_FLAG_IN_PROGRESS);
			  		this._labelState.setText(action + " in progress... ");
			  		this._labelInfo.setHTML(message);
			  		break;

		  case done: this._panelFlag.addStyleName(STYLE_FLAG_OK);
		  			this._labelState.setText(action + " done");
		  			this._labelInfo.setHTML(message);
		  			break;
		  
		  case error: this._panelFlag.addStyleName(STYLE_FLAG_NOK);
		  			this._labelState.setText(action + " in error!");
		            this._labelInfo.setHTML(message);
		            this._panelInfo.addStyleName(STYLE_IS_ERROR);
		            break;
		}
	}
	//---------------------------------------- private methods
	private void removeAllStyles() {
		this._panelFlag.removeStyleName(STYLE_FLAG_IN_PROGRESS);
		this._panelFlag.removeStyleName(STYLE_FLAG_OK);
		this._panelFlag.removeStyleName(STYLE_FLAG_NOK);
		this._panelInfo.removeStyleName(STYLE_IS_ERROR);
	}
	private Panel buildMainPanel () {
		
		final HorizontalPanel main = new HorizontalPanel();
		main.setWidth(Constantes.MAX_SIZE);
		main.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		
		main.setSpacing(Constantes.SPACING_MIN);
		main.add(this._labelState);
		main.setCellWidth(this._labelState, "17%");
		main.setCellHorizontalAlignment(this._panelFlag, HasHorizontalAlignment.ALIGN_LEFT);
		
		this._panelInfo.setWidget(this._labelInfo);	
		main.add(this._panelInfo);
		main.setCellWidth(this._panelInfo, "78%");
				
		main.add(this._panelFlag);
		main.setCellWidth(this._panelFlag, "5%");
		main.setCellHorizontalAlignment(this._panelFlag, HasHorizontalAlignment.ALIGN_RIGHT);
		return main;
	}
	private void initComposants() {
		
		this.setStyleName(STYLE_PANEL_STATUS);		
		this._panelInfo.setStyleName(STYLE_PANEL_INFO);		
		this._panelFlag.addStyleName(STYLE_FLAG);
	}

}
