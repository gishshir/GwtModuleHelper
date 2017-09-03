package fr.tsadeo.app.gwt.modulehelper.client.widget;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class DialogApplet extends DialogBox {
	
	private static final String[] LAUNCH_APPLET = new String[] {
		"<script language=\"javascript\">",
		  "function dirChooser() {",
		    "var attributes = {",
		       "code:'fr.tsadeo.app.applet.modulehelper.ModuleHelperApplet',  width:350, height:400} ; ",
		    "var parameters = {jnlp_href: 'ModuleHelperApplet.jnlp'} ; ",
		    "deployJava.runApplet(attributes, parameters, '1.6'); ",
		  "}",
		"</script>"
	};
	
	public DialogApplet() {
		super(false, true);
		
		this.setText("Dir Chooser applet");
		this.setAnimationEnabled(true);
		
		this.buildContent();
	}
	
	private String buildHtml() {
		
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < LAUNCH_APPLET.length; i++) {
			final String line = LAUNCH_APPLET[i];
			sb.append(line);
			sb.append("\n");
		}
		return sb.toString();
	}
	private void buildContent() {
		
		final Button buttonClose = new Button("Close",new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                DialogApplet.this.hide();
            }
        });
		
		final VerticalPanel panel = new VerticalPanel();
		
		final Label script = new Label();
		script.getElement().setInnerHTML(this.buildHtml());
		
		buttonClose.setWidth("90px");
        panel.add(buttonClose);
        panel.setCellHorizontalAlignment(buttonClose, HasAlignment.ALIGN_RIGHT);
        
        this.setWidget(panel);

	}

}
