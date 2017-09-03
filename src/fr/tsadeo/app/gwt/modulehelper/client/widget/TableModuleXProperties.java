package fr.tsadeo.app.gwt.modulehelper.client.widget;

import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.user.client.ui.Panel;

import fr.tsadeo.app.gwt.modulehelper.client.util.Constantes;
import fr.tsadeo.app.gwt.modulehelper.client.util.AppController.ViewNameEnum;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.ModuleXPropertyDto;
import fr.tsadeo.app.gwt.modulehelper.shared.util.StringHelper;

public class TableModuleXProperties extends AbstractTablePropertiesDisplayRules {

	private final static Logger log = Logger
			.getLogger(TableModuleXProperties.class.getName());
	private static final Header[] headersByModule = new Header[] { new Header("clé"),
			new Header("define-property"), new Header("extends-property"), new Header("set-property") };
	private static final Header[] headersByKey = new Header[] {new Header( "module"),
		new Header("define-property"), new Header("extends-property"), new Header("set-property") };
	
	// --------------------------------------- overriding AbstractTable
	@Override
	protected Header[] getHeaders() {
		switch (this._type) {
		case byModule: return headersByModule;
		case byKeyProperty: return headersByKey;
		default : return headersByModule;
		}
	}

	// --------------------------------------- constructor
	public TableModuleXProperties(final TypeTable typeTable) {
		super(typeTable);
	}
	// --------------------------------------- public methods
	public void populateGrid(final List<ModuleXPropertyDto> modulePropertyList) {
		log.info("populateGrid()");
		this.reinit();
		if (modulePropertyList == null || modulePropertyList.size() == 0)
			return;

		this.resizeRows(modulePropertyList.size());

		for (int i = 0; i < modulePropertyList.size(); i++) {
			int row = i;
			int col = 0;
			final ModuleXPropertyDto modulePropertyDto = modulePropertyList
					.get(i);
			log.config("module: " + modulePropertyDto.toString());
			
			if (this._type != TypeTable.byModule) {
				this.setWidgetLabelLien(row, col++, 
						StringHelper.splitPackageClassToHtml(modulePropertyDto.getModule().getName(),  Constantes.TABLE_CELL_MAX_LENGTH), true,
						null, ViewNameEnum.module, modulePropertyDto.getModule().getId());
			} else {
				this.setWidgetLabelLien(row, col++, 
			          modulePropertyDto.getKeyProperty().getName(), true,
			          null, ViewNameEnum.properties, modulePropertyDto.getKeyProperty().getId());
			}

			this.setWidgetLabel(row, col++, StringHelper.splitMultivalueToHtml(
					modulePropertyDto.getDefineValues()), true);
			this.setWidgetLabel(row, col++, StringHelper.splitMultivalueToHtml(
                 modulePropertyDto.getExtendValues()), true);
			
			// no conditional properties
			if (!modulePropertyDto.hasConditionalProperties()) {
				log.config("without conditional property");
			   this.setWidgetLabel(row, col, StringHelper.splitMultivalueToHtml(
					modulePropertyDto.getSetValues()), true);
			}
			else { //with conditional properties
						
				final Panel panelLabels = this.buildSetAndConditionalPropertiesPanel(modulePropertyDto, row, col, false);
				this.setWidget(row, col, panelLabels);
				
			}
			
		}

		this.applyDataRowStyles();
	}
	//------------------------------------------------------ overriding AbstractTablePropertiesDisplayRules
	@Override
	protected Logger getLog() {
		return log;
	}
	
	



}
