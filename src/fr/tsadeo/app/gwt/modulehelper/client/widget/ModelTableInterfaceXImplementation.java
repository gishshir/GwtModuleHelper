package fr.tsadeo.app.gwt.modulehelper.client.widget;

import java.util.List;
import java.util.logging.Logger;

import fr.tsadeo.app.gwt.modulehelper.shared.dto.InterfaceXImplementationDto;

public class ModelTableInterfaceXImplementation extends AbstractModelTable<InterfaceXImplementationDto> {
	
	private final static Logger log = Logger.getLogger(ModelTableInterfaceXImplementation.class.getName());
	
	static final String HEADER_IMPLEMENTATION = "implementation";
	static final String HEADER_MODULE = "module";
	static final String HEADER_ALL = "all";
	static final String HEADER_ANY = "any";
	static final String HEADER_NONE = "none";
	static final String HEADER_INTERFACE = "interface";
	static final String HEADER_GENERATOR = "generator";
	
	//---------------------------------------------- constructor
	ModelTableInterfaceXImplementation(final List<InterfaceXImplementationDto> interfaceXImplementationList) {
			super(interfaceXImplementationList);
	}
		   
	    //----------------------------------------- overriding AbstractTableModel
	protected boolean filter(InterfaceXImplementationDto interfaceXImplementation, AbstractModelTable.TableFilter filter) {

    	if (filter.containsKey(HEADER_IMPLEMENTATION)) {
    		if(!this.dofilter(interfaceXImplementation.getImplementation(), filter.getValue(HEADER_IMPLEMENTATION))) {
    			return false;
    		}
    	}
    	if (filter.containsKey(HEADER_INTERFACE)) {
    		if(!this.dofilter(interfaceXImplementation.getMyInterface().getName(), filter.getValue(HEADER_INTERFACE))) {
    			return false;
    		}
    	}
    	if (filter.containsKey(HEADER_MODULE)) {
    		if(!this.dofilter(interfaceXImplementation.getModuleName(), filter.getValue(HEADER_MODULE))) {
    			return false;
    		}
    	}
    	if (filter.containsKey(HEADER_ALL)) {
    		if(!this.dofilter(interfaceXImplementation.getRulesAll(), filter.getValue(HEADER_ALL))) {
    			return false;
    		}
    	}
    	if (filter.containsKey(HEADER_ANY)) {
    		if(!this.dofilter(interfaceXImplementation.getRulesAny(), filter.getValue(HEADER_ANY))) {
    			return false;
    		}
    	}
    	if (filter.containsKey(HEADER_NONE)) {
    		if(!this.dofilter(interfaceXImplementation.getRulesNone(), filter.getValue(HEADER_NONE))) {
    			return false;
    		}
    	}

    	
    	log.config("OK filter() - interfaceXImplementation: " + interfaceXImplementation.getMyInterface().getName());
    	return true;
	}


}
