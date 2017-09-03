package fr.tsadeo.app.gwt.modulehelper.server.model;

import fr.tsadeo.app.gwt.modulehelper.server.util.IConstants;
import fr.tsadeo.app.gwt.modulehelper.shared.util.StringHelper;

/**
 * encapsule un fichier de module absent physiquement mais référencé par une importation (inherit)
 * @author sylvie
 *
 */
public class XmlGwtFileVirtual extends AbstractXmlGwtFile implements
		IXmlGwtFile {


	private static final long serialVersionUID = 1L;
	
	private final String _moduleName;
	
	// champs calcules
	private String _filename;
	private String relativePath;

	//---------------------------------- constructor
	public XmlGwtFileVirtual() {
			this(null);
	}
	protected XmlGwtFileVirtual (final String moduleName) {
		super(null);
	     this._moduleName = moduleName;
	     super.setRenameTo(moduleName);
		    
	}
	//------------------------------------------ overriding AbstractXmlGwtFile
	@Override
	public String getFileName() {
		if (this._filename == null) {
			this._filename = StringHelper.splitPackageClass(this._moduleName)[1] + IConstants.GWT_EXTENSION;
		}
		return this._filename;
	}

	@Override
	protected String getRelativePath() {
		if (this.relativePath == null) {
			final String packageName = StringHelper.splitPackageClass(this._moduleName)[0];
		    this.relativePath  =packageName.replace(IConstants.SEPARATOR_POINT, IConstants.SEPARATOR_SLASH);
		}
		return this.relativePath;
	}
	
	@Override
	public String getCompleteName() {
		return this._moduleName;
	}
	@Override
	public boolean isVirtual() {
		return true;
	}

}
