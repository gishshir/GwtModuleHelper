package fr.tsadeo.app.gwt.modulehelper.client.panel;

import java.util.logging.Logger;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import fr.tsadeo.app.gwt.modulehelper.client.util.Constantes;
import fr.tsadeo.app.gwt.modulehelper.client.widget.AbstractTable.TypeTable;
import fr.tsadeo.app.gwt.modulehelper.client.widget.TableInterfaceXImplementationOrGenerator;
import fr.tsadeo.app.gwt.modulehelper.client.widget.TableInterfaceXImplementationOrGenerator.SousType;
import fr.tsadeo.app.gwt.modulehelper.client.widget.TableProperties;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.PermutationDto;

public class PanelPermutation extends Composite {
	
	private final static Logger log = Logger.getLogger(PanelPermutation.class.getName());
	
	private final DisclosurePanel _disclosureProperties = new DisclosurePanel("properties...");
	private final DisclosurePanel _disclosurePanelImplementation = new DisclosurePanel("implementations...");
	private final DisclosurePanel _disclosurePanelGenerator = new DisclosurePanel("generators...");
	
	private final TableProperties _tableProperties = new TableProperties();
	private final TableInterfaceXImplementationOrGenerator _tableImplementations = 
			new TableInterfaceXImplementationOrGenerator(TypeTable.byPermutation, SousType.implementation, true);
	private final TableInterfaceXImplementationOrGenerator _tableGenerators = 
			new TableInterfaceXImplementationOrGenerator(TypeTable.byPermutation, SousType.generator, false);
	
	public PanelPermutation() {
		log.config("create new PanelPermutation");
		this.initWidget(this.buildContent());
	}
	
	//------------------------------------------------------------ public methods
	private void reinit() {
		this._tableImplementations.reinit();
		this._tableGenerators.reinit();
		this._tableProperties.reinit();
		
		this._disclosureProperties.setOpen(false);
		this._disclosurePanelImplementation.setOpen(false);
		this._disclosurePanelGenerator.setOpen(false);
	}
	public void populateGrid(final PermutationDto permutation) {
		log.info("populateGrid() - permutation ");
		this.reinit();
		if (permutation == null) return;
		
		this._tableProperties.populateGrid(permutation.getCompilationPropertyList());
		
		// --- implementations
		if (permutation.hasImplementations()) {
		  this._tableImplementations.populateGrid(permutation.getInterfaceXImplementationList());
		  this._tableImplementations.setVisible(true);
		}
		else {
			 this._tableImplementations.setVisible(false);
		}
		
		// --- generators
		if (permutation.hasGenerators()) {
		  this._tableGenerators.populateGrid(permutation.getInterfaceXGeneratorList());
		  this._tableGenerators.setVisible(true);
		}
		else {
			this._tableGenerators.setVisible(false);
		}
	}
	
	//------------------------------------------------------------ private methods
	private final Widget buildContent() {
		
		// properties
		
		_disclosureProperties.add(this._tableProperties);
		_disclosurePanelImplementation.add(this._tableImplementations);
		_disclosurePanelGenerator.add(this._tableGenerators);
		
		final VerticalPanel vpanel = new VerticalPanel();
		vpanel.setSpacing(Constantes.SPACING_MIN);
		vpanel.add(_disclosureProperties);
		vpanel.add(_disclosurePanelImplementation);
		vpanel.add(_disclosurePanelGenerator);
		
		return vpanel;
	}

}
