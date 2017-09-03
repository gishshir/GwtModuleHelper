package fr.tsadeo.app.gwt.modulehelper.client.panel;

import java.util.logging.Logger;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Widget;

import fr.tsadeo.app.gwt.modulehelper.client.widget.AbstractTable.TypeTable;
import fr.tsadeo.app.gwt.modulehelper.client.widget.TableInterfaceXImplementationOrGenerator;
import fr.tsadeo.app.gwt.modulehelper.client.widget.TableInterfaceXImplementationOrGenerator.SousType;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.IContainerInterface;

public class PanelInterface extends Composite {
	
	private final static Logger log = Logger.getLogger(PanelInterface.class.getName());
	
	private final DisclosurePanel _disclosurePanel = new DisclosurePanel("interface");
	private final TableInterfaceXImplementationOrGenerator _tableInterfaceXImplementationOrGenerator;
	
	//--------------------------------------------------- constructor
	public PanelInterface(final TypeTable typeTable, final SousType sousType) {
		this._tableInterfaceXImplementationOrGenerator = new TableInterfaceXImplementationOrGenerator(typeTable, sousType, false);
		log.config("create new PanelPermutation");
		this.initWidget(this.buildContent());
	}
	
	//------------------------------------------------------------ public methods
	private void reinit() {
			this._tableInterfaceXImplementationOrGenerator.reinit();
	}
	public void populateGrid(final IContainerInterface containerInterface, final String interfaceName) {
			
		log.config("populateGrid() - containerInterface " + ((containerInterface != null)?containerInterface.getName():"null"));
		this.reinit();
		if (containerInterface == null) return;
			
		this._disclosurePanel.getHeaderTextAccessor().setText(interfaceName);
		final SousType sousType = this._tableInterfaceXImplementationOrGenerator.getSousType();
		this._tableInterfaceXImplementationOrGenerator.populateGrid((sousType == SousType.implementation)?
					containerInterface.getInterfaceXImplementationList(interfaceName):
					containerInterface.getInterfaceXGeneratorList(interfaceName));
	}
		
		//------------------------------------------------------------ private methods
	private final Widget buildContent() {
			
		this._disclosurePanel.add(this._tableInterfaceXImplementationOrGenerator);
		return this._disclosurePanel;
			
	}

}
