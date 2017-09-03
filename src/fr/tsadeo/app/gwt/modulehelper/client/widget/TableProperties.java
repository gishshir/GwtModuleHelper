package fr.tsadeo.app.gwt.modulehelper.client.widget;

import static fr.tsadeo.app.gwt.modulehelper.client.widget.ModelTableProperties.HEADER_CONDITIONNAL;
import static fr.tsadeo.app.gwt.modulehelper.client.widget.ModelTableProperties.HEADER_KEY;
import static fr.tsadeo.app.gwt.modulehelper.client.widget.ModelTableProperties.HEADER_VALUES;

import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.user.client.ui.Panel;

import fr.tsadeo.app.gwt.modulehelper.client.util.AppController.ViewNameEnum;
import fr.tsadeo.app.gwt.modulehelper.client.widget.AbstractModelTable.TableFilter;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.CompilationPropertyDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.PropertyLightDto;
import fr.tsadeo.app.gwt.modulehelper.shared.util.StringHelper;

/**
 * TODO a incorporer dans TableModuleXProperties comme un cas particulier
 * @author sylvie
 *
 */
public class TableProperties extends AbstractTablePropertiesDisplayRules {
	
	private final static Logger log = Logger.getLogger(TableProperties.class.getName());
	private static final Header[] headers = new Header[]  {new Header(HEADER_KEY),
		new Header(HEADER_VALUES), new Header(HEADER_CONDITIONNAL)};
	
	private  ModelTableProperties _model;
	// --------------------------------------- constructor
	public TableProperties() {
		super(true);
	}
	// --------------------------------------- overriding AbstractTable
	@Override
	protected Header[] getHeaders() {
		return TableProperties.headers;
	}
	@Override
	protected void applyFilter(final TableFilter filter) {
		this.buildTableLines(this._model.getFilteredDatas(filter));
	}
   //--------------------------------------- public methods

	public void populateGrid(final List<CompilationPropertyDto> compilationPropertiesList) {
		log.info("populateGrid()");
		this._model = new ModelTableProperties(compilationPropertiesList);
		// get all datas - no filter
		this.clearFilter();
		this.buildTableLines(compilationPropertiesList);
	}

	private void buildTableLines(final List<CompilationPropertyDto> compilationPropertiesList) {
		this.reinit();
		if (compilationPropertiesList == null
				|| compilationPropertiesList.size() == 0)
			return;

		this.resizeRows(compilationPropertiesList.size());

		for (int i = 0; i < compilationPropertiesList.size(); i++) {
			int row = i;
			final CompilationPropertyDto compilationPropertyDto = compilationPropertiesList
					.get(i);
			final PropertyLightDto propertyDto = compilationPropertyDto.getProperty();
			log.fine("CompilationPropertyDto: "
					+ compilationPropertyDto.toString());
			
			this.setWidgetLabelLien(row, 0, propertyDto.getKey(), false, null, ViewNameEnum.properties, propertyDto.getKeyPropertyId());

			this.setWidgetLabel(
					row,
					1,
					StringHelper.splitMultivalueToHtml(propertyDto.getValues()),true);

			// conditionnal properties
			if (compilationPropertyDto.hasConditionalProperties()) {
				final Panel panelLabels = this
						.buildSetAndConditionalPropertiesPanel(compilationPropertyDto, row, 2, true);
				this.setWidget(row, 2, panelLabels);
			}
		}
		this.applyDataRowStyles();
	}

	//----------------------------------------------------- overriding AbstractTablePropertiesDisplayRules
	@Override
	protected Logger getLog() {
		return log;
	}

	

}
