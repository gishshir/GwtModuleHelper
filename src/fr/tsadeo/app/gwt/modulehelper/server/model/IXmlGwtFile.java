package fr.tsadeo.app.gwt.modulehelper.server.model;

import java.util.List;
import java.util.Map;


/**
 * ----------/---------/src/-----------------/MyModule.gwt.xml
 * <  root  ><      - relativePath -                          >
 *                    <   > = sourceName
 *                         <      completeName with .         >
 *                                           <     fileName   >
 * @author sylvie
 *
 */
public interface IXmlGwtFile {

	//-----------------------------------accessors
	public abstract boolean isVirtual();
	
	public abstract String getEntryPoint();

	public abstract void setEntryPoint(final String _entryPoint);

	public abstract String getRenameTo();

	public abstract void setRenameTo(final String renameTo);

	public abstract List<String> getInheritList();

	public abstract void setInheritList(List<String> inheritList);

	public abstract List<Property> getDefinePropertyList();

	public abstract List<Property> getSetPropertyList(final boolean withConditional);
	
	public abstract List<String> getContent();
	
	public abstract void setContent(final List<String> content);

	// ordre des tag inherit dans le fichier
	public abstract List<IXmlGwtFile> getImportXmlGwtFileList();

	//----------------------------------- public method
	public abstract String getFileName();

	public abstract String getModuleName();

	public abstract String getCompleteName();

	public abstract String getPackage();

	//---- replaceWith
	public abstract boolean hasReplaceWith();

	public abstract void addReplaceWithList(
			final List<ReplaceWith> replaceWithList);

	public abstract List<ReplaceWith> getReplaceWithList();
	
	//---- generateWith
	public boolean hasGenerateWith();
	
	public void addGenerateWithList (final List<GenerateWith> generateWithList);
	
	public List<GenerateWith> getGenerateWithList();

	//---- set property
	public abstract boolean hasSetProperties(final boolean withConditional);

	public abstract Property getStandardSetProperty(final KeyProperty keyProperty);
	
	public abstract Map<KeyProperty, List<ConditionalProperty>> getMapConditionalSetProperties();
	public abstract List<ConditionalProperty> getConditionalSetProperties(final KeyProperty keyProperty);

	public abstract void addSetProperties(final List<Property> propertyList);

	//---- define property
	public abstract boolean hasDefineProperties();

	public abstract Property getDefineProperties(final KeyProperty keyProperty);

	public abstract void addDefineProperties(
			final List<Property> definePropertyList);

	//---- import 
	public abstract boolean hasImports();

	public abstract void addImport(final IXmlGwtFile importXmlGwtFile);

}