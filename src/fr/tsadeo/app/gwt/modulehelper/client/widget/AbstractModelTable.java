package fr.tsadeo.app.gwt.modulehelper.client.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.tsadeo.app.gwt.modulehelper.shared.util.StringHelper;

public abstract class AbstractModelTable<T> {
	
	

	private final List<T> _itemList;
	
	public AbstractModelTable(final List<T> itemList) {
		this._itemList = itemList;
	}
	
	 List<T> getFilteredDatas(final TableFilter filter) {
	    	if (filter == null || filter.isEmpty() || this._itemList == null) {
	    		return this._itemList;
	    	}
	    	
	    	// filtrage
	    	final List<T> filteredList = new ArrayList<T>();
	    	for (T item : this._itemList) {
				if (this.filter(item, filter)) {
					filteredList.add(item);
				}
			}
	    	
	    	return filteredList;
	    }
	 
	 protected abstract boolean filter(final T module, final TableFilter filter) ;
	 
	   protected boolean dofilter (final String valueFromItem, final String valueFilter) {
		   
		   final boolean isEmptyValueFromItem = StringHelper.isNullOrEmpty(valueFromItem);
		   final boolean isEmptyValueFilter = StringHelper.isNullOrEmpty(valueFilter);

		   if (isEmptyValueFilter) {
			   return true;
		   }
		   if (isEmptyValueFromItem) {
			   return false;
		   }
	    	
	    	return valueFromItem.indexOf(valueFilter) != -1;
	    }
	    
	   
	  //======================================= INNER class
	  /**
	   * Encapsule une map pour les données de filtrage
	   * @author sylvie
	   *
	   */
	   final static class TableFilter {
		  
		  final Map<String, String> _internalMap;
		  
		  TableFilter(final int size) {
			  this._internalMap = new HashMap<String, String>(size);
		  }
		  boolean isEmpty() {
			  return this._internalMap.isEmpty();
		  }
		  void clear() {
			  this._internalMap.clear();
		  }
		  String getValue(final String key) {
			  return this._internalMap.get(key);
		  }
		  boolean containsKey(final String key) {
			  return this._internalMap.containsKey(key);
		  }
		  void putValue(final String key, final String value) {
			  this._internalMap.put(key, value);
		  }
	  
		  public String toString() {
			  final StringBuilder sb = new StringBuilder();
			  for (String key : this._internalMap.keySet()) {
				sb.append("[");
				sb.append(key);
				sb.append(" - ");
				sb.append(this._internalMap.get(key));
				sb.append("] ");
			}
			  return sb.toString();
		  }
	  }
}
