package fr.tsadeo.app.gwt.modulehelper.server.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.tsadeo.app.gwt.modulehelper.server.util.IConstants;
import fr.tsadeo.app.gwt.modulehelper.server.util.ServerStringHelper;
import fr.tsadeo.app.gwt.modulehelper.shared.util.StringHelper;

public abstract class AbstractXmlGwtFile  implements IXmlGwtFile, Serializable {

	
private static final long serialVersionUID = 1L;
	
	private static final Log log = LogFactory.getLog(AbstractXmlGwtFile.class);
	//------------------------------------------------------ instance

	private final String _sourceDirName;
	private String _renameTo;
	private String _entryPoint;

	
	private  List<String> _inheritList;
	
	private List<Property> _definePropertyList;
	// conditional and no conditional properties
	private List<Property> _setPropertyList;
	
	private List<IXmlGwtFile> _importXmlGwtFileList;
	private List<ReplaceWith> _replaceWithList;
	private List<GenerateWith> _generateWithList;
	private List<String> _content;
	
	// attributs calcules
	private String _completeName;
	private String _package;
	
	//-----------------------------------accessors
	@Override
	public List<String> getContent() {
		return this._content;
	}
	@Override
	public void setContent(final List<String> content) {
		this._content = content;
	}
	@Override
	public String getEntryPoint() {
		return _entryPoint;
	}
	@Override
	public void setEntryPoint(final String _entryPoint) {
		this._entryPoint = _entryPoint;
	}
	@Override
	public String getRenameTo() {
		return this._renameTo;
	}
	@Override
	public void setRenameTo(final String renameTo) {
		this._renameTo = (renameTo == null || renameTo.length() == 0)?null:renameTo;
	}
	@Override
	public List<String> getInheritList() {
		return this._inheritList;
	}
	@Override
	public void setInheritList(List<String> inheritList) {
		this._inheritList = inheritList;
	}
	@Override
	public List<Property> getDefinePropertyList() {
		return (this._definePropertyList==null)?null:Collections.unmodifiableList(this._definePropertyList);
	}
	@Override
	public List<Property> getSetPropertyList(final boolean withConditional) {
		
		if (this._setPropertyList == null) return null;
		if (withConditional) {
			return Collections.unmodifiableList(this._setPropertyList);
		}
		
		// no conditional
		final List<Property> listPropertyWithoutConditional =
				new ArrayList<Property>();
		for (Property setProperty : this._setPropertyList) {
			if (!setProperty.isConditional()) {
				listPropertyWithoutConditional.add(setProperty);
			}
		}
		return listPropertyWithoutConditional;
	}
	// ordre des tag inherit dans le fichier
	@Override
	public List<IXmlGwtFile> getImportXmlGwtFileList() {
	  return (this._importXmlGwtFileList == null)?null:Collections.unmodifiableList(this._importXmlGwtFileList);
	}
	//----------------------------------- public method
	@Override
	public abstract String getFileName();
	
	@Override
	public String getModuleName() {
		return this.getCompleteName();
	}
	
	protected abstract String getRelativePath();
	
	// package . fileName
	@Override
	public String getCompleteName() {

		if (this._completeName == null) {
			String path = this.getRelativePath();
			log.debug("relative path: " + path);
			path = ServerStringHelper.removeBeginSlashOrAntiSlash(path);
			//log.debug("path without slash: " + path);
			
			final int pos = path.indexOf(this._sourceDirName);
			if (pos != -1) {
				path = path.substring(pos + this._sourceDirName.length() + 1);
			} 
			//log.debug("path after source: " + path);
			
			path = path.substring(0,path.length() - IConstants.GWT_EXTENSION.length());
			//log.debug("path without ext: " + path);
			this._completeName = path.replace(IConstants.SEPARATOR_ANTI_SLASH,IConstants.SEPARATOR_POINT);
			this._completeName = _completeName.replace(IConstants.SEPARATOR_SLASH, IConstants.SEPARATOR_POINT);
			log.debug("_completeName: " + this._completeName);
		}
		return this._completeName;
	}
	@Override
	public boolean isVirtual() {
		return false;
	}
	@Override
	public String getPackage() {
		
       if (this._package == null) {
    	  return StringHelper.splitPackageClass( this.getCompleteName())[0];
       }
       
       return this._package;
	}
	
	//---- replaceWith
	@Override
	public boolean hasReplaceWith() {
		return (this._replaceWithList != null && !this._replaceWithList.isEmpty());
	}
	@Override
	public void addReplaceWithList (final List<ReplaceWith> replaceWithList) {
		if (replaceWithList == null) return;
		if (this._replaceWithList == null) {
			this._replaceWithList = new ArrayList<ReplaceWith>();
		}
		this._replaceWithList.addAll(replaceWithList);
	}
	@Override
	public List<ReplaceWith> getReplaceWithList() {
		return this._replaceWithList;
	}
	
	//---- generateWith
	@Override
	public boolean hasGenerateWith() {
			return (this._generateWithList != null && !this._generateWithList.isEmpty());
	}
	@Override
	public void addGenerateWithList (final List<GenerateWith> generateWithList) {
			if (generateWithList == null) return;
			if (this._generateWithList == null) {
				this._generateWithList = new ArrayList<GenerateWith>();
			}
			this._generateWithList.addAll(generateWithList);
	}
	@Override
	public List<GenerateWith> getGenerateWithList() {
			return this._generateWithList;
	}
		
	
	//---- set property
	@Override
	public boolean hasSetProperties(final boolean withConditional) {
		if (this._setPropertyList == null || this._setPropertyList.isEmpty()) {
			return false;
		}
		
		if (withConditional) return true;
		
		for (Property setProperty : this._setPropertyList) {
			if(!setProperty.isConditional()) return true;
		}
		return false;
	}
	// non conditional property. only one per key
	@Override
	public Property getStandardSetProperty(final KeyProperty keyProperty) {
		if (!this.hasSetProperties(false)) return null;
		
		for (Property property : this._setPropertyList) {
			if (!property.isConditional() && property.getKey() == keyProperty) return property;
		}
		return null;
	}
	@Override
	public Map<KeyProperty, List<ConditionalProperty>> getMapConditionalSetProperties() {
		if (!this.hasSetProperties(true)) return null;
		
		Map<KeyProperty, List<ConditionalProperty>> map = null;
		for (Property property : this._setPropertyList) {
			if (property.isConditional()) {
				if (map == null) {
					map  = new HashMap<KeyProperty, List<ConditionalProperty>>();
				}
				List<ConditionalProperty> list = map.get(property.getKey());
				if (list == null) {
					list = new ArrayList<ConditionalProperty>();
					map.put(property.getKey(), list);
				}
				list.add((ConditionalProperty)property);
			}
		}
		
		return map;
	}
	
	@Override
	public List<ConditionalProperty> getConditionalSetProperties(final KeyProperty keyProperty) {
		if (!this.hasSetProperties(true)) return null;
		
	    List<ConditionalProperty> list = null;
		for (Property property : this._setPropertyList) {
			if (property.isConditional() && property.getKey() == keyProperty) {
				if (list == null) {
					list  = new ArrayList<ConditionalProperty>();
				}
				list.add((ConditionalProperty)property);
			}
		}
		
		return list;
		
	}
	
	
	
	@Override
	public void addSetProperties (final List<Property> propertyList) {
		if (this._setPropertyList == null) {
			this._setPropertyList = new ArrayList<Property>();
		}
		this._setPropertyList.addAll(propertyList);
	}
	
	//---- define property
	@Override
	public boolean hasDefineProperties() {
		return (this._definePropertyList != null && !this._definePropertyList.isEmpty());
	}
	@Override
	public Property getDefineProperties(final KeyProperty keyProperty) {
		if (!this.hasDefineProperties()) return null;
		
		for (Property property : this._definePropertyList) {
			if (property.getKey() == keyProperty) return property;
		}
		return null;
	}
	@Override
	public void addDefineProperties (final List<Property> definePropertyList) {
		if (this._definePropertyList == null) {
			this._definePropertyList = new ArrayList<Property>();
		}
		this._definePropertyList.addAll(definePropertyList);
	}

    //---- import 
	@Override
	public boolean hasImports() {
		return (this._importXmlGwtFileList != null && !this._importXmlGwtFileList.isEmpty());
	}
	@Override
	public void addImport (final IXmlGwtFile importXmlGwtFile) {
		if (this._importXmlGwtFileList == null) {
			this._importXmlGwtFileList = new ArrayList<IXmlGwtFile>();
		}
		this._importXmlGwtFileList.add(importXmlGwtFile);
	}



	//---------------------------------- constructor
		public AbstractXmlGwtFile() {
			this(null);
		}
		protected AbstractXmlGwtFile (final String sourceDirName) {
		    this._sourceDirName = sourceDirName;
		    
		}


}
