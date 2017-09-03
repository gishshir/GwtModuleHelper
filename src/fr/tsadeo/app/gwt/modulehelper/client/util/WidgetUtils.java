package fr.tsadeo.app.gwt.modulehelper.client.util;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import fr.tsadeo.app.gwt.modulehelper.client.widget.IActionCallback;

public class WidgetUtils {

	public final static Label buildWhiteLabel (final String text) {
		return buildLabel(text, Constantes.STYLE_WHITE_LABEL);
	}
	public final static  Label buildLabel (final String text, final String styleName) {
		final Label label = new Label(text);
		if (styleName != null) {
			label.addStyleName(styleName);
		}
		return label;
	}
	
	public final static DialogBox buildDialogBox(final String title, final String[] messages, 
			final Widget widget, final boolean withCancel,
			final IActionCallback actionCallback) {

		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText(title);
		dialogBox.setAnimationEnabled(true);

		final VerticalPanel vPanelInfo = new VerticalPanel();
		vPanelInfo.setSpacing(Constantes.SPACING_MIN);
		
		if (messages != null) {			
			for (int i = 0; i < messages.length; i++) {
				vPanelInfo.add(new Label(messages[i]));
			}
		}
   
		if (widget != null) {
		  vPanelInfo.add(widget);
		}
		
		
		final HorizontalPanel panelButton = new HorizontalPanel();
		panelButton.setSpacing(Constantes.SPACING_MIN);
		panelButton.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		
		final Button closeButton = new Button("ok");
		closeButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();			
				if (actionCallback != null) {
					actionCallback.onOk();
				}
			}
		});
		panelButton.add(closeButton);
		
		if (withCancel) {
		final Button cancelButton = new Button("annuler");
		cancelButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();	
				if (actionCallback != null) {
					actionCallback.onCancel();
				}
		
			}
		});
		panelButton.add(cancelButton);
		}


		
		vPanelInfo.add(panelButton);
		vPanelInfo.setCellHorizontalAlignment(panelButton,HasHorizontalAlignment.ALIGN_RIGHT);


		dialogBox.setWidget(vPanelInfo);
		return dialogBox;
	}
}
