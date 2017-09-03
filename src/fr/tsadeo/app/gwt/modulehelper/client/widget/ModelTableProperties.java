package fr.tsadeo.app.gwt.modulehelper.client.widget;

import java.util.List;
import java.util.logging.Logger;

import fr.tsadeo.app.gwt.modulehelper.shared.dto.CompilationPropertyDto;

public class ModelTableProperties extends AbstractModelTable<CompilationPropertyDto> {
	
	private final static Logger log = Logger.getLogger(ModelTableProperties.class.getName());

	final static String HEADER_KEY = "key";
	final static String HEADER_VALUES = "values";
	final static String HEADER_CONDITIONNAL = "conditionnal";
	
	//---------------------------------------- constructor
	public ModelTableProperties(List<CompilationPropertyDto> itemList) {
		super(itemList);
	}

	//------------------------------------------ overriding AbstractModelTable
	@Override
	protected boolean filter(CompilationPropertyDto property, AbstractModelTable.TableFilter filter) {
		
		if (filter.containsKey(HEADER_KEY)) {
    		if(!this.dofilter(property.getKeyPropertyName(), filter.getValue(HEADER_KEY))) {
    			return false;
    		}
    	}
    	
    	if (filter.containsKey(HEADER_VALUES)) {
    		if(!this.dofilter(property.getSetValues(), filter.getValue(HEADER_VALUES))) {
    			return false;
    		}
    	}
    	
    	if (filter.containsKey(HEADER_CONDITIONNAL)) {
    		
    		
    		if(!this.dofilter(property.getConditionalValues(), filter.getValue(HEADER_CONDITIONNAL))) {
    			return false;
    		}
    	}
    	
 
    	log.config("OK filter() - property: " +property.getKeyPropertyName());
    	return true;

	}

}
