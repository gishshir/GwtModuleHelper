package fr.tsadeo.app.gwt.modulehelper.shared.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class StringHelper {
	
	private static final String POINT_AND_SAUT_LIGNE_HTML = ISharedConstants.SEPARATOR_CLASS + ISharedConstants.HTML_SAUT_LIGNE;
	private static final String POINT_AND_SAUT_LIGNE_JAVA = ISharedConstants.SEPARATOR_CLASS + ISharedConstants.JAVA_SAUT_LIGNE;
	
	private static final String[] ESPACES_JAVA = new String[]{
		"",	
		" ",
		"  ",
		"   ",
		"    ",
		"     ",
		"      ",
		"       ",
		"        ",
		"         ",
		"          "
		
	};
	private static final String ESPACE_HTML = "&nbsp;";
	private static final String[] ESPACES_HTML = new String[10];
	static {
		
		final StringBuffer sb = new StringBuffer();
		for (int i = 0; i < 10; i++) {
			
			ESPACES_HTML[i] = sb.toString();
			sb.append(ESPACE_HTML);
		}
	}
	
	   public static boolean isNullOrEmpty(final String value) {
		   return (value == null || value.length() == 0);
	   }
	   public static final String valuesToString(Collection<String> values, final String separator ) {
		   if (values == null) return "";
	    	 final StringBuilder sb = new StringBuilder();

			final int l = values.size();
			if (l == 0) return "";
			int i = 0;
			for (String value : values) {
				sb.append(value);
				if (i < l - 1) {
					sb.append(separator);
				}
				i++;
			}

		    return sb.toString();
	     }
	   
		public static String splitMultivalueToHtml(final String multivalue) {
			return splitMultivalueToHtml(multivalue, ISharedConstants.SEPARATOR_VALUE);
		}
		public static String splitMultivalueToHtml(final String multivalue, final String separator) {
			if (multivalue.indexOf(separator) == -1) return multivalue;
            return multivalue.replaceAll(separator, ISharedConstants.HTML_SAUT_LIGNE).trim();
		}
		public static String[] splitMultivalueToTab(final String multivalue, final String separator) {
			
			return multivalue.split(separator);
		}
		public static String splitPackageToHtml(final String completePackageName, final int maxLength) {
			return internalSplitPackageClass(completePackageName, maxLength, true, false);
		}
		public static String splitPackageToJava(final String completePackageName, final int maxLength) {
			return internalSplitPackageClass(completePackageName, maxLength, false, false);
		}
		public static String splitPackageClassToHtml(final String completeClassName, final int maxLength) {
			return internalSplitPackageClass(completeClassName, maxLength, true, true);
		}
		public static String splitPackageClassToJava(final String completeClassName, final int maxLength) {
			return internalSplitPackageClass(completeClassName, maxLength, false, true);
		}
		
		private static String internalSplitPackageClass(final String completeClassName, final int maxLength, final boolean html, final boolean separateClass) {
			
			final String SEPARATOR_CHAINE = (html)?POINT_AND_SAUT_LIGNE_HTML:POINT_AND_SAUT_LIGNE_JAVA;
			final String[] split = (separateClass)?splitPackageClass(completeClassName):new String[]{completeClassName, ""};
			
			if (maxLength < 0 || split[0].length() < maxLength) {
			  return ((split[0].length() == 0)?"":split[0] + SEPARATOR_CHAINE) + split[1];
			}
			else {
				
				final String packageName = split[0];
				final String className = split[1];
				
				final List<String> decoupage = new ArrayList<String>();
				decoupePackage(packageName, maxLength, decoupage);
				
				final int countLine = decoupage.size();
				 final StringBuilder sb = new StringBuilder();
				 for (int i = 0; i < countLine; i++) {
					String chaine = decoupage.get(i);
					sb.append(chaine);
					if (!(!separateClass &&  i == countLine - 1)) {
					  sb.append(SEPARATOR_CHAINE);
					}
				}
				 sb.append(className);

				 return sb.toString();
			}
		}
		
	public static void main(String[] args) {

		final String chaine = "ma chaine";
		String result = completeStringWithSpaceJava(chaine, 10);
		System.out.println("result\n" + result  + "end.");
		
		result = completeStringWithSpaceJava(chaine, 15);
		System.out.println("result\n" + result  + "end.");
		
		result = completeStringWithSpaceJava(chaine, 20);
		System.out.println("result\n" + result  + "end.");
		
		result = completeStringWithSpaceJava(chaine, 25);
		System.out.println("result\n" + result  + "end.");
		
		result = completeStringWithSpaceJava(chaine, 30);
		System.out.println("result\n" + result  + "end.");
		}
		
		
		
		private static void decoupePackage(final String chaine, final int maxLength, final List<String> decoupage) {
			
			if (chaine.length() < maxLength) {
				decoupage.add(chaine);
				return;
			}
			
			String racine = chaine.substring(0, maxLength);
			final int pos = racine.lastIndexOf(ISharedConstants.SEPARATOR_CLASS);
			if (pos != -1) {
				decoupage.add( racine.substring(0, pos));
				final String reste = chaine.substring(pos+1);
				decoupePackage(reste, maxLength, decoupage);
			}
			else {
			decoupage.add(chaine);
			}

		}
		
		public static String[] splitPackageClass(final String completeClassName) {
			
			final int pos = completeClassName.lastIndexOf(ISharedConstants.SEPARATOR_CLASS);
			if (pos != -1) {
				final String packageName = completeClassName.substring(0,pos);
				final String className = completeClassName.substring(pos + 1);
				return new String[] {packageName, className};
			}
			return new String[] {"", completeClassName};
		}
		
		public static String repeatcharacter (final char character, final int count) {
			if (count < 0) return "";
			final StringBuilder sb = new StringBuilder();
			for (int i = 0; i < count; i++) {
				sb.append(character);				
			}
			return sb.toString();
		}
		
		public static String completeStringWithSpaceJava(final String chaine, final int length) {
			return completeStringWithSpaces(chaine, length, false);
		}
		public static String completeStringWithSpaceHtml(final String chaine, final int length) {
			return completeStringWithSpaces(chaine, length, true);
		}
		
		private static String completeStringWithSpaces(final String chaine, final int length, final boolean html) {
			
			final String[] ESPACES = (html)?ESPACES_HTML:ESPACES_JAVA;
			final int countToComplete = length - chaine.length();
			if (countToComplete <= 0) {
				return chaine;
			}
			
			if (countToComplete <= 10) {
				return chaine + ESPACES[countToComplete];
			}
			
			// n * 10 + m
			int n = countToComplete / 10;
			int m = countToComplete % 10;
			
			if (n == 1) {
				return chaine + ESPACES[10] + ESPACES[m];
			}
			
			final StringBuilder sb = new StringBuilder();
			for (int i = 0; i <n; i++) {
				sb.append(ESPACES[10]);	
			}
			return chaine + sb.toString() + ESPACES[m];
			
		}
}
