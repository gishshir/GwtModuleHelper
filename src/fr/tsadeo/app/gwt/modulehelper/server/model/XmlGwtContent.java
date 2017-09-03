package fr.tsadeo.app.gwt.modulehelper.server.model;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class XmlGwtContent extends AbstractXmlGwtFile implements
		IXmlGwtFile {

	private static final long serialVersionUID = 1L;
	
	//------------------------------------------------------ instance
	private final String _pathname;

	//-------------------------------------------- constructor
	protected XmlGwtContent (final String pathname, 
			final String sourceDirName,
			final List<String> content) {
		super(sourceDirName);
		super.setContent(content);
		this._pathname = pathname;
	}
	
	//------------------------------------------- public method
	public InputStream getInputStream()  {
		
		ByteArrayInputStream in = null;

			final StringBuilder sb = new StringBuilder();
			for (String line : this.getContent()) {
				sb.append(line);
				sb.append("\n");
			}
			try {
				in = new ByteArrayInputStream(sb.toString().getBytes("UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return in;

	}
	//--------------------------------------------overriding IXmlGwtFile
	@Override
	public String getFileName() {
		return new File(this._pathname).getName();
	}

	@Override
	protected String getRelativePath() {
		return this._pathname;
	}

}
