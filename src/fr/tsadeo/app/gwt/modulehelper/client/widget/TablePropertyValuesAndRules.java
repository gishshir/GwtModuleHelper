package fr.tsadeo.app.gwt.modulehelper.client.widget;

import java.util.List;
import java.util.logging.Logger;

import fr.tsadeo.app.gwt.modulehelper.shared.dto.PropertyRulesDto;
import fr.tsadeo.app.gwt.modulehelper.shared.util.ISharedConstants;
import fr.tsadeo.app.gwt.modulehelper.shared.util.StringHelper;

/**
 * table montrant les valeurs d'une property conditionnelle en fonction des règles
 * @author sylvie
 *
 */
public class TablePropertyValuesAndRules extends AbstractTable {
	
	private final static Logger log = Logger.getLogger(TablePropertyValuesAndRules.class.getName());
		
	private static final Header[] headersByProperty = new Header[] { 
			new Header("value"), new Header("all", 150), new Header("any", 150), new Header("none", 150) };

	//----------------------------------------------- constructor
		public TablePropertyValuesAndRules() {
			super(TypeTable.byKeyProperty);
		}
		// --------------------------------------- overriding AbstractTable
		@Override
		protected Logger getLog() {
			return log;
		}
	@Override
	protected Header[] getHeaders() {
		return headersByProperty;
	}

	// --------------------------------------- public methods
	public void populateGrid(final List<PropertyRulesDto> listPropertyRules) {
			log.info("populateGrid()");
			this.reinit();
			
		if (listPropertyRules == null || listPropertyRules.isEmpty()) return;
		this.resizeRows(listPropertyRules.size());
		
		for (int i = 0; i < listPropertyRules.size(); i++) {
			int row = i;
			final PropertyRulesDto propertyRules = listPropertyRules.get(i);
			log.config("PropertyRulesDto: " + propertyRules.toString());
			
			int col = 0;
			this.setWidgetLabel(row, col++, propertyRules.getValue(), false);
			
			this.setWidgetMultiLabel(row, col++, StringHelper.splitMultivalueToTab(propertyRules.getRulesAll(),
					ISharedConstants.SEPARATOR_PROPERTY), null, true);
			this.setWidgetMultiLabel(row, col++,  StringHelper.splitMultivalueToTab(
					propertyRules.getRulesAny(), ISharedConstants.SEPARATOR_PROPERTY), null, true);
			this.setWidgetMultiLabel(row, col++, StringHelper.splitMultivalueToTab(
					propertyRules.getRulesNone(), ISharedConstants.SEPARATOR_PROPERTY), null, true);
			
		}
		this.applyDataRowStyles();
	}

}
