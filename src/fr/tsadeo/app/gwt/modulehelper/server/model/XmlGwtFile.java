package fr.tsadeo.app.gwt.modulehelper.server.model;

import java.io.File;
import java.io.Serializable;

import fr.tsadeo.app.gwt.modulehelper.server.util.ServerStringHelper;

public class XmlGwtFile extends AbstractXmlGwtFile implements Serializable, IXmlGwtFile {
	
	private static final long serialVersionUID = 1L;
	
	//------------------------------------------------------ instance
	private final String _rootPath;
	private final File _gwtFile;

	//---------------------------------- constructor
	public XmlGwtFile() {
		this(null, null, null);
	}
	protected XmlGwtFile (final File gwtFile, final String rootPath, final String sourceDirName) {
		super(sourceDirName);
	    this._gwtFile = gwtFile;
	    this._rootPath = ServerStringHelper.removeEndSlashOrAntiSlash(rootPath);
	    
	    
	}
	//----------------------------------------- overriding AbstractXmlGwtFile
	@Override
	public String getFileName() {
		return (this._gwtFile == null)?null:this._gwtFile.getName();
	}
	@Override
	protected String getRelativePath() {
		return (this._gwtFile == null)?null:this._gwtFile.getAbsolutePath().substring(this._rootPath.length());
	}


}
