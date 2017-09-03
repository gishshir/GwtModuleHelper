package fr.tsadeo.app.gwt.modulehelper.client.widget;

import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import fr.tsadeo.app.gwt.modulehelper.client.util.Constantes;
import fr.tsadeo.app.gwt.modulehelper.client.util.WidgetUtils;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.IContainerPropertyRules;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.PropertyRulesDto;
import fr.tsadeo.app.gwt.modulehelper.shared.util.StringHelper;

/**
 * Table de properties contentant des PropertyRules et pouvant afficher une sous-table des rules
 * @author sylvie
 *
 */
public abstract class AbstractTablePropertiesDisplayRules extends AbstractTable {
	
	protected TablePropertyValuesAndRules _tablePropertyValuesAndRules;
	protected final Label _labelPropery = new Label();
	protected DialogBox _dialogPropertyValuesAndRules;
	
	protected abstract Logger getLog();
	
	//---------------------------------------------- constructor
	public AbstractTablePropertiesDisplayRules() {
		super(null);
	}
	public AbstractTablePropertiesDisplayRules(final boolean filtrable) {
		super(filtrable);
	}
	public AbstractTablePropertiesDisplayRules(final TypeTable type) {
		super(type);
	}
	
	//---------------------------------------------- private methods
	private void buildDialogPropertyValuesAndRules() {
		
		if (this._dialogPropertyValuesAndRules == null) {			

			final VerticalPanel vPanel = new VerticalPanel();
			vPanel.setSpacing(Constantes.SPACING_MIN);
			vPanel.add(this._labelPropery);
			vPanel.add(this._tablePropertyValuesAndRules);
			
			this._dialogPropertyValuesAndRules = 
					WidgetUtils.buildDialogBox("Conditional property",null, 
					vPanel, false, null);
		}
	}
	
	//---------------------------------------------- protected methods
	protected Panel buildSetAndConditionalPropertiesPanel(final IContainerPropertyRules containerPropertyRules, 
			final int row, final int col, final boolean onlyConditionnal) {
		
		getLog().config("with conditional property");
		if (this._tablePropertyValuesAndRules == null)
		{
			this._tablePropertyValuesAndRules = new TablePropertyValuesAndRules();
		}
		
	     String setValues = null;	    		
	     if (!onlyConditionnal) {
	       setValues = StringHelper.splitMultivalueToHtml(containerPropertyRules.getSetValues());
	       setValues = (setValues.length() == 0)?null:setValues;
	     }
	     
		final List<PropertyRulesDto> listPropertyRules = containerPropertyRules.getListPropertyRules();
		
		final int countElements = ((setValues==null)?0:1) + listPropertyRules.size();
		getLog().config("count elements: " + countElements);
		if (countElements == 0) return new SimplePanel();
	     
		
		final String[] tabTexts = new String[countElements];
		final String[] tabTitles = new String[countElements];
		final String[] tabStyleNames  = new String[countElements];
		
		int compteur = 0;
		if (setValues != null) {
			tabTexts[0] = setValues;
			tabTitles[0] = setValues;
			tabStyleNames[0] = null;
			compteur++;
		}
		
		// for each conditionnal property
		for (PropertyRulesDto propertyRule : listPropertyRules) {
			
			tabTexts[compteur] = propertyRule.getValue();
			tabTitles[compteur] = "conditional";
			tabStyleNames[compteur] = Constantes.STYLE_TABLE_CONDITIONAL_PROPERTY;
			compteur++;
		}
		
        final ClickHandler valuesClickHandler = new ClickHandler() {
        	

			@Override
			public void onClick(ClickEvent event) {
					_tablePropertyValuesAndRules.populateGrid(listPropertyRules);
					_labelPropery.setText("Propriété: " + containerPropertyRules.getKeyPropertyName());					
					buildDialogPropertyValuesAndRules();
				
				_dialogPropertyValuesAndRules.showRelativeTo(getWidget(row, col-1));
				
			}
		};
		final Panel panelLabels = 
				this.buildVerticalLabels(tabTexts, tabTitles, tabStyleNames, true, 
						this.getWidthByCol(col), valuesClickHandler);
		return panelLabels;
	}

}
