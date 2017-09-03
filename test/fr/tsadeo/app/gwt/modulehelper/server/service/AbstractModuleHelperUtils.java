package fr.tsadeo.app.gwt.modulehelper.server.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Before;

import fr.tsadeo.app.gwt.modulehelper.server.model.Application;

public abstract class AbstractModuleHelperUtils {
	
	private static final Log log = LogFactory.getLog(AbstractModuleHelperUtils.class);

	private static final ModuleHelperUtils moduleHelperUtils = ModuleHelperUtils.getInstance();
	
	protected static Application application;
	private static final String sessionId = "mySession";

	// Construire l'Application à partir d'une map de contenu de fichiers
	@Before
	public void loadApplication() {
		
		if (application != null) return;
		this.buildApplicationWithContentList();
			
	}
	
//	// Construire Application à partir d'un répertoire de projet (root)
//	@Before
//	public void loadApplication() {
//		
//		if (application != null) return;
//		final String rootDirectory = "C:\\data\\sylvie\\tempo\\test\\source";
//				//"C:/data/sylvie/tempo/doc";			
//		application = moduleHelperUtils.buildApplication(rootDirectory, "src", sessionId);		
//
//	}
	
//	// construire Application à partir d'un zip
//	@Before
//	public void loadApplication() {
//		
//		if (application != null) return;	
//		final File file = new File("C:/data/sylvie/program/workspace/ModuleHelper/doc/sourceNOKparseException.zip");
//		application = moduleHelperUtils.buildApplication(file, "java", sessionId);
//	}
	//---------------------------------------------------------
	protected void title(final String title) {
		log.info("");
		log.info(title + "()");
	}
	
   private void buildApplicationWithContentList() {
		
		// ------initialisation
		final Map<String, List<String>> mapContentFiles = new HashMap<String, List<String>>();
		final String rootDirectory = 
				"C:/data/sylvie/tempo/test/source";
		
		final List<File> gwtFiles = this.buildFileList(new File(rootDirectory), "java");
		
		for (File file : gwtFiles) {
			
			final String relativePath = file.getAbsolutePath().substring(rootDirectory.length());
			final List<String> content = this.readContentFile(file);
			mapContentFiles.put(relativePath, content);
		}
		Assert.assertTrue("mapContentFiles cannot be empty", !mapContentFiles.isEmpty());
		
		//----------tests
		application = moduleHelperUtils.buildApplication(mapContentFiles, "demo", sessionId);
		Assert.assertNotNull("application cannot be null!", application);
		
	}
   


	private List<File> buildFileList(final File dir, final String sourceDirName) {

			final List<File> gwtFiles = new ArrayList<File>();
			if (dir == null)
				return gwtFiles;
			
			if (!dir.exists() || !dir.isDirectory())
				return gwtFiles;
			
		   MyFileFilter myFileFilter = new MyFileFilter(sourceDirName);
			
			// retenir soit repertoire soit fichier gwt
			final File[] files = dir.listFiles(myFileFilter);

			if (files == null) {
				return gwtFiles;
			}

			// for each file
			// - if directory >> find files
			// - if file >> add to list
			for (File file : files) {
				if (file.isDirectory()) {
					gwtFiles.addAll(this.buildFileList(file, sourceDirName));
				} else {
					gwtFiles.add(file);
				}
			}

			return gwtFiles;
		}
	
private List<String> readContentFile(final File file) {
		 
		 final List<String> content = new ArrayList<String>();
		 
		 FileInputStream fin = null;
		 BufferedReader reader = null;
		 try {
			 
			 fin = new FileInputStream(file);
			 reader = new BufferedReader(new InputStreamReader(fin, "UTF-8"));
			 
			 String line;
	            while ((line = reader.readLine()) != null) {
					content.add(line);
				}
	            
			 return content;
			
		} catch (Exception e) {
			log.error(e.toString());
		}
		 finally {
				if (fin != null) {
					try {
						fin.close();
					} catch (IOException ignored) {
						log.error(ignored.toString());
					}
				}
			}
		 
		 return null;
	 }


class MyFileFilter  implements FileFilter {
	
	public static final String GWT_EXTENSION = ".gwt.xml";
		
		private final String sourceDirName;
		public MyFileFilter(final String sourceDirName) {
			this.sourceDirName = sourceDirName;
		}

		@Override
		public boolean accept(File file) {
			if (file.isDirectory())
				return true;
			if (file.isFile()
					&& file.getName().endsWith(GWT_EXTENSION)
					&& file.getAbsolutePath().contains(sourceDirName))
				return true;
			return false;
		}
}
}
