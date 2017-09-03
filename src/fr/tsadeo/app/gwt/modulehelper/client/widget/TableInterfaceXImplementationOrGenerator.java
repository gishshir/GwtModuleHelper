package fr.tsadeo.app.gwt.modulehelper.client.widget;

import static fr.tsadeo.app.gwt.modulehelper.client.widget.ModelTableInterfaceXImplementation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import fr.tsadeo.app.gwt.modulehelper.client.util.AppController.ViewNameEnum;
import fr.tsadeo.app.gwt.modulehelper.client.util.Constantes;
import fr.tsadeo.app.gwt.modulehelper.client.widget.AbstractModelTable.TableFilter;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.AbstractInterfaceXImplementationOrGeneratorDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.InterfaceXImplementationDto;
import fr.tsadeo.app.gwt.modulehelper.shared.util.ISharedConstants;
import fr.tsadeo.app.gwt.modulehelper.shared.util.StringHelper;

public class TableInterfaceXImplementationOrGenerator extends AbstractTable {

	private final static Logger log = Logger.getLogger(TableInterfaceXImplementationOrGenerator.class.getName());
	
	public enum SousType {implementation, generator}
	
	//--- sous-type implementation
	private static final Header[] headersByInterface1 = new Header[] { new Header(HEADER_IMPLEMENTATION),
			new Header(HEADER_MODULE), new Header(HEADER_ALL, 150), new Header(HEADER_ANY, 150), new Header(HEADER_NONE, 150) };
	
	private static final Header[] headersByModule1 = new Header[] { new Header(HEADER_IMPLEMENTATION),
		    new Header(HEADER_ALL, 200), new Header(HEADER_ANY, 200), new Header(HEADER_NONE, 200) };
	
	private static final Header[] headersByPermutation1 = new Header[] { new Header(HEADER_IMPLEMENTATION),
		new Header(HEADER_INTERFACE), new Header(HEADER_ALL, 150), new Header(HEADER_ANY, 150), new Header(HEADER_NONE, 150) };
	
	//--- sous-type generator
	private static final Header[] headersByInterface2 = new Header[] { new Header(HEADER_GENERATOR),
		new Header(HEADER_MODULE), new Header(HEADER_ALL, 150), new Header(HEADER_ANY, 150), new Header(HEADER_NONE, 150) };

    private static final Header[] headersByModule2 = new Header[] { new Header(HEADER_GENERATOR),
	    new Header(HEADER_ALL, 200), new Header(HEADER_ANY, 200), new Header(HEADER_NONE, 200) };

     private static final Header[] headersByPermutation2 = new Header[] { new Header(HEADER_GENERATOR),
	new Header(HEADER_INTERFACE), new Header(HEADER_ALL, 150), new Header(HEADER_ANY, 150), new Header(HEADER_NONE, 150) };
	
	
	private final SousType _sousType;

	
	private ModelTableInterfaceXImplementation _modelTableInterface;
	
	//----------------------------------------------- accessors
	public SousType getSousType() {
		return this._sousType;
	}

	//----------------------------------------------- constructor
	public TableInterfaceXImplementationOrGenerator(final TypeTable type, final SousType sousType, final boolean filtrable) {
		super(type, filtrable);
		this._sousType = sousType;
	}
	// --------------------------------------- overriding AbstractTable
	@Override
	protected Logger getLog() {
		return log;
	}
	@Override
	protected void applyFilter(final TableFilter filter) {
		if (this._modelTableInterface != null) {
		  this.buildTableLines(this._modelTableInterface.getFilteredDatas(filter));
		}
	}
	@Override
	protected Header[] getHeaders() {
		
		if (this._sousType == SousType.implementation) {

			switch (this._type) {
			case byPermutation:return headersByPermutation1;
			case byInterface:return headersByInterface1;
			case byModule:return headersByModule1;
			default:return headersByModule1;
			}

		}

		else {

			switch (this._type) {
			case byPermutation:return headersByPermutation2;
			case byInterface:return headersByInterface2;
			case byModule:return headersByModule2;
			default:return headersByModule2;
			}

		}
	}

	// --------------------------------------- public methods
	public void populateGrid(final List<? extends AbstractInterfaceXImplementationOrGeneratorDto> interfaceXImplementationOrGeneratorList) {
		log.info("populateGrid()");

		// only for implementation type
        if (this._sousType == SousType.implementation && this.isFiltrable()) {
        	final List<InterfaceXImplementationDto> listInterfaceXImplementations = new ArrayList<InterfaceXImplementationDto>();
        	for (AbstractInterfaceXImplementationOrGeneratorDto interfaceXImplementationOrGeneratorDto : interfaceXImplementationOrGeneratorList) {
				listInterfaceXImplementations.add((InterfaceXImplementationDto)interfaceXImplementationOrGeneratorDto);
			}
        	this._modelTableInterface = new ModelTableInterfaceXImplementation(listInterfaceXImplementations);
        }
        // to begin with, all items
        this.clearFilter();
        this.buildTableLines(interfaceXImplementationOrGeneratorList);
	}
	

	private void buildTableLines(final List<? extends AbstractInterfaceXImplementationOrGeneratorDto> interfaceXImplementationOrGeneratorList) {
		this.reinit();
		if (interfaceXImplementationOrGeneratorList == null || interfaceXImplementationOrGeneratorList.size() == 0)
			return;

		this.resizeRows(interfaceXImplementationOrGeneratorList.size());

		for (int i = 0; i < interfaceXImplementationOrGeneratorList.size(); i++) {
			int row = i;
			final AbstractInterfaceXImplementationOrGeneratorDto interfaceXImplementationOrGenerator= interfaceXImplementationOrGeneratorList
					.get(i);
			//log.config("AbstractInterfaceXImplementationOrGeneratorDto: " + interfaceXImplementationOrGenerator.toString());
			
			int col = 0;
			this.setWidgetLabel(row, col++, 
					StringHelper.splitPackageClassToHtml(
							interfaceXImplementationOrGenerator.getImplementationOrGenerator(), Constantes.TABLE_CELL_MAX_LENGTH), true);
			
			if (this._type == TypeTable.byInterface) {
			  this.setWidgetLabelLien(row, col++,
					  StringHelper.splitPackageClassToHtml(
					  interfaceXImplementationOrGenerator.getModuleName(), Constantes.TABLE_CELL_MAX_LENGTH), true,
					  null, ViewNameEnum.module, interfaceXImplementationOrGenerator.getModuleId());
			}
			else if (this._type == TypeTable.byPermutation) {
				this.setWidgetLabelLien(row, col++, 
						StringHelper.splitPackageClassToHtml(
								interfaceXImplementationOrGenerator.getMyInterface().getName(), Constantes.TABLE_CELL_MAX_LENGTH), true,
								null, ViewNameEnum.interfaces, interfaceXImplementationOrGenerator.getMyInterface().getId());
			}
			
			this.setWidgetMultiLabel(row, col++, StringHelper.splitMultivalueToTab(interfaceXImplementationOrGenerator.getRulesAll(),
					ISharedConstants.SEPARATOR_PROPERTY), null, true);
			this.setWidgetMultiLabel(row, col++,  StringHelper.splitMultivalueToTab(
					interfaceXImplementationOrGenerator.getRulesAny(), ISharedConstants.SEPARATOR_PROPERTY), null, true);
			this.setWidgetMultiLabel(row, col++, StringHelper.splitMultivalueToTab(
					interfaceXImplementationOrGenerator.getRulesNone(), ISharedConstants.SEPARATOR_PROPERTY), null, true);

		}

		this.applyDataRowStyles();
	}



}
