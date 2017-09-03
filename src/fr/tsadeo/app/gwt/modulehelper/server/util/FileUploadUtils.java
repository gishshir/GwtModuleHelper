package fr.tsadeo.app.gwt.modulehelper.server.util;

import java.io.File;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;


public class FileUploadUtils {
	
	private final static FileUploadUtils instance = new FileUploadUtils();
	public final static FileUploadUtils getInstance() {
		return instance;
	}
	
	private final String GWT_MODULE_NAME = "modulehelper";
	  
	 private final Map<String, String> _mapContentTypes = new Hashtable<String, String>();
	 private final Map<String, File> _mapFiles = new Hashtable<String, File>();
	 
	public final File getTemporaryDirectory (final ServletContext servletContext) {
			
			final String tempDir = servletContext.getRealPath("/" + GWT_MODULE_NAME + "/temp/");
			 return (new File(tempDir));
	}
		
	private final File getUploadDirectory (final ServletContext servletContext) {
			
			final String uploadDir = servletContext.getRealPath("/" + GWT_MODULE_NAME + "/upload/");
			 return (new File(uploadDir));
	}
		
	public final File getNewUploadFile(final ServletContext servletContext) {
			
			 final File uploadDir = this.getUploadDirectory(servletContext);
	        return new File(uploadDir, "upload-" + new Date().getTime() + ".zip");	 
	}
		
	public final void storeUploadedFile(final HttpSession session,
			 final File file, final String fileName, final String contentType) {
		 
		 this._mapContentTypes.put(fileName, contentType);
         this._mapFiles.put(fileName, file);
		 session.setAttribute(IConstants.SESSION_UPLOADED_FILENAME, fileName);
	 }
	 
	public final File getUploadedFile(final String filename) {
		 return this._mapFiles.get(filename);
	 }
	public final void removeUploadedFile(final String filename) {
		 final File file = this._mapFiles.get(filename);
		 if (file == null) return;
		 
		this._mapFiles.remove(filename);
		this._mapContentTypes.remove(filename);
		file.delete();
	 }

}
