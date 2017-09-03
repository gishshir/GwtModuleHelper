package fr.tsadeo.app.gwt.modulehelper.client.widget;

import java.util.List;
import java.util.logging.Logger;

import fr.tsadeo.app.gwt.modulehelper.shared.dto.ModuleLightDto;

class ModelTableModules extends AbstractModelTable<ModuleLightDto> {
	
	private final static Logger log = Logger.getLogger(ModelTableModules.class.getName());
	
	final static String HEADER_MODULE = "module";
	final static String HEADER_FILE = "file";
	final static String HEADER_PACKAGE = "package";
	final static String HEADER_ENTRYPOINT = "entryPoint";

	
	//---------------------------------------------- constructor
	ModelTableModules(final List<ModuleLightDto> moduleList) {
		super(moduleList);
	}
	   
    //----------------------------------------- overriding AbstractTableModel
    protected boolean filter(final ModuleLightDto module, final TableFilter filter) {
    	
    	if (filter.containsKey(HEADER_MODULE)) {
    		if(!this.dofilter(module.getName(), filter.getValue(HEADER_MODULE))) {
    			return false;
    		}
    	}
    	
    	if (filter.containsKey(HEADER_FILE)) {
    		if(!this.dofilter(module.getFileName(), filter.getValue(HEADER_FILE))) {
    			return false;
    		}
    	}
    	
    	if (filter.containsKey(HEADER_PACKAGE)) {
    		if(!this.dofilter(module.getPackageName(), filter.getValue(HEADER_PACKAGE))) {
    			return false;
    		}
    	}
    	
    	if (filter.containsKey(HEADER_ENTRYPOINT)) {
    		if(!this.dofilter(module.getEntryPoint(), filter.getValue(HEADER_ENTRYPOINT))) {
    			return false;
    		}
    	}
    	
    	log.config("OK filter() - module: " + module.getName());
    	return true;
    }
 
}
