package fr.tsadeo.app.gwt.modulehelper.server.util;

public class ServerStringHelper {

	
	public static String removeEndSlashOrAntiSlash(final String text) {
		
		 if (text != null && 
		    		(text.endsWith(IConstants.SEPARATOR_SLASH_STRING) || text.endsWith(IConstants.SEPARATOR_ANTI_SLASH_STRING))) {
		    	return  text.substring(0, text.length() - 1);	
		    }
		    else {
		      return text;
		    }
	}
	
	public static String removeBeginSlashOrAntiSlash(final String text) {
		
		 if (text != null && 
		    		(text.startsWith(IConstants.SEPARATOR_SLASH_STRING) || text.startsWith(IConstants.SEPARATOR_ANTI_SLASH_STRING))) {
		    	return  text.substring(1);	
		    }
		    else {
		      return text;
		    }
	}
}
