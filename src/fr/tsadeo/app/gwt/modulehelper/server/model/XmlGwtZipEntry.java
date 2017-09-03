package fr.tsadeo.app.gwt.modulehelper.server.model;

import java.io.Serializable;

import fr.tsadeo.app.gwt.modulehelper.server.util.IConstants;

public class XmlGwtZipEntry extends AbstractXmlGwtFile implements Serializable, IXmlGwtFile {

	private static final long serialVersionUID = 1L;
	
	private final String _zipEntryName;

	//---------------------------------- constructor
	public XmlGwtZipEntry() {
		this(null, null);
	}
	protected XmlGwtZipEntry (final String zipEntryName, final String sourceDirName) {
		super(sourceDirName);
        this._zipEntryName = zipEntryName;
	    
	}
	//--------------------------------------------- overriding AbstractXmlGwtFile
	@Override
	public String getFileName() {
		if(this._zipEntryName == null) {
			return null;
		}

       int pos = this._zipEntryName.lastIndexOf(IConstants.SEPARATOR_ANTI_SLASH);
       if (pos == -1) {
    	   pos = this._zipEntryName.lastIndexOf(IConstants.SEPARATOR_SLASH);
       }
       if (pos != -1) {
    	   return this._zipEntryName.substring(pos + 1);
       }
       return this._zipEntryName;
	}

	@Override
	protected String getRelativePath() {
		return this._zipEntryName;
	}

}
