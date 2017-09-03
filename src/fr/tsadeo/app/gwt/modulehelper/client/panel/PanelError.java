package fr.tsadeo.app.gwt.modulehelper.client.panel;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;

import fr.tsadeo.app.gwt.modulehelper.client.util.Constantes;

public class PanelError extends Composite {

	private final HTML _labelText = new HTML();
	
	public PanelError() {
		this.initWidget(this.buildContent());
	}
	
	//----------------------------------- public method
	public void setText(final String text, final boolean html) {
		if (html) {
		    this._labelText.setHTML(text);
		}
		else {
			this._labelText.setText(text);
		}
	}
	//----------------------------------- private method
	private Panel buildContent () {
		
		final Panel panel = new SimplePanel();
		panel.addStyleName(Constantes.STYLE_PANEL_ERROR);
		panel.add(this._labelText);
		return panel;
	}
}
