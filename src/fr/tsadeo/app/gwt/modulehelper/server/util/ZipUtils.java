package fr.tsadeo.app.gwt.modulehelper.server.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

public class ZipUtils {
	
	private static final Log log = LogFactory.getLog(ZipUtils.class);
	
	private static final int BUFFER = 512;
	
	private static final ZipUtils instance = new ZipUtils();
	public static final ZipUtils getInstance() {
		return instance;
	}
	
	@Test
	public void test() {
		
		File file = new File("C:/data/sylvie/program/workspace/ModuleHelper/doc/src.zip");
		this.readEntries(file);
	}
	
	public void readEntries(final File zipFile) {
		try {
			
			ZipFile zipfile = new ZipFile(zipFile);
			ZipEntry zipEntry;
	        Enumeration<? extends ZipEntry> e = zipfile.entries();
	         
	         while(e.hasMoreElements()) {
	        	 zipEntry = (ZipEntry) e.nextElement();
	            log.debug(">>Extracting: " +zipEntry.getName() + " " + ((zipEntry.isDirectory())?"directory":""));
	            
	           this.readEntry(zipfile, zipEntry);
	         }
			
		} catch (Exception e) {
			 e.printStackTrace();
		}
	}
	
	public List<String> readEntry (final ZipFile zipfile, final ZipEntry zipEntry) {
		
		
		final List<String> content = new ArrayList<String>();
		

		BufferedReader br = null;
		try {
			final InputStream in =  zipfile.getInputStream(zipEntry);
			 br = new BufferedReader(new InputStreamReader(in));
			 
			  String line;
	            int compteur = 0;
	            while ((line = br.readLine()) != null) {
					log.debug(compteur++ + " line: " + line);
					content.add(line);
				}
	            
			 return content;
			
		} catch (Exception e) {
			log.fatal("readEntry: " + zipEntry.getName() + " -> " + e.toString());
			throw new RuntimeException(e);
		}
		finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException ignored) {
					log.fatal(ignored.toString());
				}
			}
		}
	}
	


}
