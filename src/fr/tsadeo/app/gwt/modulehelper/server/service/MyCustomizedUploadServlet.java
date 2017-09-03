package fr.tsadeo.app.gwt.modulehelper.server.service;

import fr.tsadeo.app.gwt.modulehelper.server.util.FileUploadUtils;
import gwtupload.server.UploadAction;
import gwtupload.server.exceptions.UploadActionException;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MyCustomizedUploadServlet extends UploadAction {

	 private static final long serialVersionUID = 1L;
	 
	 private Log log = LogFactory.getLog(MyCustomizedUploadServlet.class);

	  /**
	   * Override this method if you want to implement a different ItemFactory.
	   * 
	   * @return FileItemFactory
	   */
	 @Override
	  protected FileItemFactory getFileItemFactory(int requestSize) {
		 
		// Create a factory for disk-based file items
		 DiskFileItemFactory factory = new DiskFileItemFactory();
		 //factory.setSizeThreshold(yourMaxMemorySize);
		 factory.setRepository(FileUploadUtils.getInstance().getTemporaryDirectory(this.getServletContext()));
	     return factory;
	  }

	  /**
	   * Override executeAction to save the received files in a custom place
	   * and delete this items from session.  
	   */
	  @Override
	  public String executeAction(HttpServletRequest request, List<FileItem> sessionFiles) throws UploadActionException {
		  		
	    String response = "";
        log.debug("executeAction()");
        log.debug("sessionFiles: " + sessionFiles.size());
	    for (FileItem item : sessionFiles) {
	      if (false == item.isFormField()) {

	        try {
	          ModuleHelperUtils.getInstance().reinit(this.getCurrentSession());	
	          
	          final File file = FileUploadUtils.getInstance().getNewUploadFile(this.getServletContext());	        	  
	          item.write(file);
	          
	          /// Save a list with the received files
	          log.debug("filename: " + item.getFieldName());
	          
	          FileUploadUtils.getInstance().storeUploadedFile(this.getCurrentSession(),
	        		  file, item.getFieldName(), item.getContentType());
	          
	          response += "file stored in : " + file.getAbsolutePath();

	        } catch (Exception e) {
	          throw new UploadActionException(e);
	        }
	      }
	    }
	    
	    // Remove files from session because we have a copy of them
	    removeSessionFileItems(request);
	    
	    /// Send your customized message to the client.
	    return response;
	  }
	  
	  /**
	   * Get the content of an uploaded file.
	   */
	  @Override
	  public void getUploadedFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
		  log.debug("getUploadedFile()");
		  super.getUploadedFile(request, response);
	  }
	  
	  /**
	   * Remove a file when the user sends a delete request.
	   */
	  @Override
	  public void removeItem(HttpServletRequest request, String fieldName)  throws UploadActionException {
		  log.debug("removeItem()");
		  super.removeItem(request, fieldName);
	  }
	  
	  private HttpSession getCurrentSession() {
			final HttpSession session =getThreadLocalRequest().getSession(true);
			log.info("session id: " + session.getId() + " - " + session.getCreationTime());
			return session;
		}

}
