package fr.tsadeo.app.gwt.modulehelper.client.widget;

import static fr.tsadeo.app.gwt.modulehelper.client.widget.ModelTableModules.HEADER_ENTRYPOINT;
import static fr.tsadeo.app.gwt.modulehelper.client.widget.ModelTableModules.HEADER_FILE;
import static fr.tsadeo.app.gwt.modulehelper.client.widget.ModelTableModules.HEADER_MODULE;
import static fr.tsadeo.app.gwt.modulehelper.client.widget.ModelTableModules.HEADER_PACKAGE;

import java.util.List;
import java.util.logging.Logger;

import fr.tsadeo.app.gwt.modulehelper.client.util.AppController.ViewNameEnum;
import fr.tsadeo.app.gwt.modulehelper.client.util.Constantes;
import fr.tsadeo.app.gwt.modulehelper.client.widget.AbstractModelTable.TableFilter;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.ModuleLightDto;
import fr.tsadeo.app.gwt.modulehelper.shared.util.StringHelper;

public class TableModules extends AbstractTable {
	

    private final static Logger log = Logger.getLogger(TableModules.class.getName());
	private static final Header[] headers = new Header[]  {new Header(HEADER_MODULE), new Header(HEADER_FILE),
		         new Header(HEADER_PACKAGE), new Header(HEADER_ENTRYPOINT)};
	
	private ModelTableModules _model;
	
	//---------------------------------------- constructor
	public TableModules () {
		super(true);
	}
	
	// --------------------------------------- overriding AbstractTable
	@Override
	protected Logger getLog() {
		return log;
	}
	@Override
	protected Header[] getHeaders() {
		return TableModules.headers;
	}
	@Override
	protected void applyFilter(final TableFilter filter) {
		this.buildTableLines(this._model.getFilteredDatas(filter));
	}
    //--------------------------------------- public methods
	public void populateGrid(final List<ModuleLightDto> moduleList) {
		log.info("populateGrid()");

		this._model = new ModelTableModules(moduleList);
		// get all datas - no filter
		this.clearFilter();
		this.buildTableLines(moduleList);
	}

    private void buildTableLines(final List<ModuleLightDto> moduleList) {
    	
		this.reinit();
		if (moduleList == null || moduleList.size() == 0) return;
		log.info("buildTableLines() : " + moduleList.size() + " items!");
		
		this.resizeRows(moduleList.size());
		
		for (int i = 0; i < moduleList.size(); i++) {
			int row = i;
			final ModuleLightDto moduleDto = moduleList.get(i);		
			//log.config("module: [" + moduleDto.toString() + "]");
			
			final String styleName = Constantes.getStyleModuleName(moduleDto.isVirtual());
			
			this.setWidgetLabelLien(row, 0, StringHelper.splitPackageClassToHtml(
					moduleDto.getName(), Constantes.TABLE_CELL_MAX_LENGTH), true, styleName, ViewNameEnum.module, moduleDto.getId());
			this.setWidgetLabel(row, 1, moduleDto.getFileName(), false, styleName);
			this.setWidgetLabel(row, 2, StringHelper.splitPackageToHtml(moduleDto.getPackageName(), Constantes.TABLE_CELL_MAX_LENGTH)
					, true, styleName);
			this.setWidgetLabel(row, 3, StringHelper.splitPackageClassToHtml(
					moduleDto.getEntryPoint(), Constantes.TABLE_CELL_MAX_LENGTH), true, styleName);
		
		}
		this.applyDataRowStyles();
    }

}
