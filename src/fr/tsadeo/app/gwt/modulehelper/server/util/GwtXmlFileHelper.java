package fr.tsadeo.app.gwt.modulehelper.server.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import fr.tsadeo.app.gwt.modulehelper.server.model.AbstractReplaceOrGenerateWith;
import fr.tsadeo.app.gwt.modulehelper.server.model.AbstractReplaceOrGenerateWith.Rules;
import fr.tsadeo.app.gwt.modulehelper.server.model.ConditionalProperty;
import fr.tsadeo.app.gwt.modulehelper.server.model.GenerateWith;
import fr.tsadeo.app.gwt.modulehelper.server.model.KeyProperty;
import fr.tsadeo.app.gwt.modulehelper.server.model.ModeleObjectFactory;
import fr.tsadeo.app.gwt.modulehelper.server.model.MyInterface;
import fr.tsadeo.app.gwt.modulehelper.server.model.Property;
import fr.tsadeo.app.gwt.modulehelper.server.model.Property.Qualifier;
import fr.tsadeo.app.gwt.modulehelper.server.model.ReplaceWith;
import fr.tsadeo.app.gwt.modulehelper.server.util.ExtraSession.ModeleEnum;
import fr.tsadeo.app.gwt.modulehelper.shared.util.ISharedConstants;
import fr.tsadeo.app.gwt.modulehelper.shared.util.ISharedConstants.TypeRule;


public class GwtXmlFileHelper {
	
	private static final Log log = LogFactory.getLog(GwtXmlFileHelper.class);
	
	private static final String ERROR_PARSE_PATTERN = "Echec de la lecture du fichier :%s - detail: %s ";
	
	private static final String ELEMENT_INHERIT = "inherits";
	private static final String ELEMENT_DEFINE_PROPERTY = "define-property";
	private static final String ELEMENT_EXTEND_PROPERTY = "extend-property";
	private static final String ELEMENT_SET_PROPERTY = "set-property";
	private static final String ELEMENT_ENTRY_POINT = "entry-point";
	private static final String ELEMENT_REPLACE_WITH = "replace-with";
	private static final String ELEMENT_GENERATE_WITH = "generate-with";
	private static final String ELEMENT_WHEN_TYPE_IS = "when-type-is";
	private static final String ELEMENT_WHEN_TYPE_ASSIGNABLE = "when-type-assignable";
	
	private static final String ELEMENT_RULE_ALL = "all";
	private static final String ELEMENT_RULE_ANY = "any";
	private static final String ELEMENT_RULE_NONE = "none";
	private static final String ELEMENT_WHEN_PROPERTY_IS = "when-property-is";
	
	private static final String ATTRIBUT_RENAME_TO = "rename-to";
	private static final String ATTRIBUT_NAME = "name";
	private static final String ATTRIBUT_VALUE = "value";
	private static final String ATTRIBUT_VALUES = "values";
	private static final String ATTRIBUT_CLASS = "class";
	
	private static DocumentBuilder docBuilder;
	private static DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
		if (docBuilder == null) {
			final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			docBuilder = factory.newDocumentBuilder();

		}
		return  docBuilder;
	}
	
	private final Document _document;
	private final String _sessionId;
	

	
	public GwtXmlFileHelper(final String filename, final InputStream inputStream, final String sessionId) {
		try {
			this._document  = getDocumentBuilder().parse(inputStream);
			this._sessionId = sessionId;
		} catch (Exception e) {			
			String errorMessage = String.format(ERROR_PARSE_PATTERN, filename, e.toString());
			log.fatal(e.toString());
			throw new RuntimeException(errorMessage);
		} 
	}
	public GwtXmlFileHelper(final File gwtFile, final String sessionId) {
		
		
		
		final String filename = (gwtFile == null)?"null":gwtFile.getName();
		
		try {
			this._sessionId = sessionId;
			this._document  = getDocumentBuilder().parse(gwtFile);

		} catch (Exception e) {			
			String errorMessage = String.format(ERROR_PARSE_PATTERN, filename, e.toString());
			log.fatal(e.toString());
			throw new RuntimeException(errorMessage);
		} 
		
	}

	public List<String> getContent(final String name, final InputStream inputStream) {
		return this.readContent(name, inputStream);
	}
	public String getRenameTo() {
		
		// get the root element
		Element rootElement = this._document.getDocumentElement();
		return rootElement.getAttribute(ATTRIBUT_RENAME_TO);
	}
	
	public String getEntryPoint() {
		
		// get the root element
		Element rootElement = this._document.getDocumentElement();
		NodeList nodeList = rootElement
				.getElementsByTagName(ELEMENT_ENTRY_POINT);
		
		if (nodeList != null && nodeList.getLength() == 1) {
			Element element = (Element)nodeList.item(0);
			return element.getAttribute(ATTRIBUT_CLASS);
			}
		
		return null;
	}
	
	public List<ReplaceWith> getListReplaceWith() {
		
		final List<AbstractReplaceOrGenerateWith> list = new ArrayList<AbstractReplaceOrGenerateWith>();
		this.populateListReplaceOrGenerateWith(list, true);
		
		final List<ReplaceWith> listReplaceWith = new ArrayList<ReplaceWith>(list.size());
		for (AbstractReplaceOrGenerateWith generateWith : list) {
			listReplaceWith.add((ReplaceWith)generateWith);
		}
		return listReplaceWith;
	}
	
	public List<GenerateWith> getListGenerateWith() {

		final List<AbstractReplaceOrGenerateWith> list = new ArrayList<AbstractReplaceOrGenerateWith>();
		this.populateListReplaceOrGenerateWith(list, false);
		
		final List<GenerateWith> listGenerateWith = new ArrayList<GenerateWith>(list.size());
		for (AbstractReplaceOrGenerateWith generateWith : list) {
			listGenerateWith.add((GenerateWith)generateWith);
		}
		
		return (List<GenerateWith>) listGenerateWith;
	}
	private void populateListReplaceOrGenerateWith(final List<AbstractReplaceOrGenerateWith> list, boolean replace) {
		
		// get the root element
		final Element rootElement = this._document.getDocumentElement();
		final NodeList nodeImplementOrGenerate = rootElement
						.getElementsByTagName((replace)?ELEMENT_REPLACE_WITH:ELEMENT_GENERATE_WITH);
		if (nodeImplementOrGenerate != null && nodeImplementOrGenerate.getLength() > 0) {
			
			// for each implementation or generator
			for (int i = 0; i < nodeImplementOrGenerate.getLength(); i++) {
				final Element elementImplementOrGenerate = (Element) nodeImplementOrGenerate.item(i);
				
				
				//----- interface
				final NodeList nodeInterfaces = elementImplementOrGenerate.getElementsByTagName(
						(replace)?ELEMENT_WHEN_TYPE_IS:ELEMENT_WHEN_TYPE_ASSIGNABLE);
				if (nodeInterfaces != null && nodeInterfaces.getLength() == 1) {
					final Element elementInterface = (Element)nodeInterfaces.item(0);
					final String interfaceName = elementInterface.getAttribute(ATTRIBUT_CLASS);
					
					MyInterface myInterface;
					if (!this.getExtraSession().containByKey(ModeleEnum.myinterface, interfaceName)) {
						//create
						myInterface = ModeleObjectFactory.getInstance().createMyInterface(interfaceName, this._sessionId);
					}
					else {
						myInterface = this.getExtraSession().getInterfaceByName(interfaceName);
					}
					AbstractReplaceOrGenerateWith replaceOrGenerateWith;
					final String implementionOrGeneratorName = elementImplementOrGenerate.getAttribute(ATTRIBUT_CLASS);
					if (replace) {
					   myInterface.addImplementation(implementionOrGeneratorName);
					   replaceOrGenerateWith = new ReplaceWith(myInterface, implementionOrGeneratorName);
					}
					else {
						myInterface.addGenerator(implementionOrGeneratorName);
						replaceOrGenerateWith = new GenerateWith(myInterface, implementionOrGeneratorName);
					}
					 list.add(replaceOrGenerateWith);
					
                    
    				//------ rules
                    final Rules rulesWithoutType = this.buildPropertyRules(null, elementImplementOrGenerate);
                    if (rulesWithoutType != null) {
                    	replaceOrGenerateWith.addRule(rulesWithoutType);
                    }
     				for (TypeRule typeRule : TypeRule.values()) {
    					final Rules rules = this.buildRules(typeRule, elementImplementOrGenerate);
    					if(rules == null) continue; // next typeRule
    					replaceOrGenerateWith.addRule(rules); 					
    				}
				}			
	    	
			}			
		}
	}

	
	
	public List<Property> getListSetProperties() {
		final List<Property> listSetProperties = new ArrayList<Property>();

		// get the root element
		Element rootElement = this._document.getDocumentElement();
		NodeList nodeList = rootElement
				.getElementsByTagName(ELEMENT_SET_PROPERTY);
        this.populateProperties(listSetProperties, this.buildListElement(nodeList), Qualifier.set);

		return listSetProperties;
	}
	
    public List<Property> getListDefineProperties() {
    	
    	final List<Property> listDefineProperties = new ArrayList<Property>();

		// get the root element
		Element rootElement = this._document.getDocumentElement();
		
		NodeList nodeList = rootElement
				.getElementsByTagName(ELEMENT_DEFINE_PROPERTY);
		this.populateProperties(listDefineProperties, this.buildListElement(nodeList),  Property.Qualifier.define);
		
		nodeList = rootElement
				.getElementsByTagName(ELEMENT_EXTEND_PROPERTY);
		this.populateProperties(listDefineProperties, this.buildListElement(nodeList),  Property.Qualifier.extend);

		return listDefineProperties;
    	
    }
    
    private List<Element> buildListElement (final NodeList nodeList) {
    	
    	if (nodeList == null || nodeList.getLength() == 0) return null;
    	
    	final List<Element> elementList = new ArrayList<Element>(nodeList.getLength());
    	for (int i = 0; i < nodeList.getLength(); i++) {
    		elementList.add((Element) nodeList.item(i));
    	}
    	return elementList;
    }

  
	public List<String> getListInherits() {
		final List<String> listInherits = new ArrayList<String>();
		
		//get the root element
		Element rootElement = this._document.getDocumentElement();
		NodeList nodeList = rootElement.getElementsByTagName(ELEMENT_INHERIT);
		if (nodeList == null || nodeList.getLength() == 0) return listInherits;
		
		for (int i = 0; i < nodeList.getLength(); i++) {
			final Element element = (Element) nodeList.item(i);
			final String name = element.getAttribute(ATTRIBUT_NAME);
			if (name != null) {
				listInherits.add(name);
			}
		}
		
		return listInherits;
	}
	
	//------------------------------------------------------- private methods
	
	private List<String> readContent(final String name, final InputStream inputStream) {

		final List<String> content = new ArrayList<String>();
		

		BufferedReader br = null;
		try {
			 br = new BufferedReader(new InputStreamReader(inputStream));
			 
			  String line;
	            while ((line = br.readLine()) != null) {
					content.add(line);
				}
	            
			 return content;
			
		} catch (Exception e) {
			log.fatal("readContent: " + name + " -> " + e.toString());
			throw new RuntimeException(e);
		}
		finally {
			if (inputStream != null) {
				try {
					if (br != null) br.close();
					inputStream.close();
				} catch (IOException ignored) {
					log.fatal(ignored.toString());
				}
			}
		}
	}
	private ReplaceWith.Rules buildRules(final TypeRule typeRule, final Element elementImplementation) {
		
		String tagName = "";
		switch (typeRule) {
		case all: tagName = ELEMENT_RULE_ALL;
			break;
		case any : tagName = ELEMENT_RULE_ANY;
		    break;
		case none: tagName = ELEMENT_RULE_NONE;
		}	
		final NodeList nodeRules = elementImplementation.getElementsByTagName(tagName);
		if (nodeRules != null && nodeRules.getLength() == 1) {
			
			final Element elementRules = (Element) nodeRules.item(0);	
		    return this.buildPropertyRules(typeRule, elementRules);

		}	
		return null;
	}
	
	// recherche des childs directs
	private ReplaceWith.Rules buildPropertyRules(final TypeRule typeRule, final Element element) {
		
		final NodeList nodeProperties = element.getElementsByTagName(ELEMENT_WHEN_PROPERTY_IS);
		if (nodeProperties != null && nodeProperties.getLength() > 0) {
			
		    final List<Property> listProperties = new ArrayList<Property>(nodeProperties.getLength());
			
			// cas particulier
			// si typeRule null alors ne garder que child directs
			if (typeRule == null) {
			  
			  final List<Element> elementList = new ArrayList<Element>(nodeProperties.getLength());
			  for (int i = 0; i < nodeProperties.getLength(); i++) {
				final Element elementProperty = (Element) nodeProperties.item(i);
				if (elementProperty.getParentNode().getNodeName().equals(element.getNodeName())) {
					elementList.add(elementProperty);
				}
			  }
			  if(elementList.isEmpty()) return null;
			  this.populateProperties(listProperties, elementList, Qualifier.rules);
			}
			else { // cas nominal
				this.populateProperties(listProperties, this.buildListElement(nodeProperties), Qualifier.rules);
			}
					    
            return new Rules((typeRule == null)?TypeRule.all:typeRule, listProperties);
		}
		return null;
	}
	  private void populateProperties (final List<Property> listProperties,
	    		final List<Element> elementList, final Property.Qualifier qualifier) {
	    	
	    	if (elementList == null || elementList.isEmpty()) return;
	    	
	    	for (Element element : elementList) {

				final String name = element.getAttribute(ATTRIBUT_NAME);
				boolean isConditionalProperty = false;
				
				final String attribut;
				switch (qualifier) {
				case rules:  attribut = ATTRIBUT_VALUE;
				           break;
				case set: attribut = ATTRIBUT_VALUE;
                    // conditionnal ?
				    final NodeList nodeProperties = element.getElementsByTagName(ELEMENT_WHEN_PROPERTY_IS);
				    if (nodeProperties.getLength() > 0) {
				    	isConditionalProperty = true;
				    }
					break;
				case extend:
				case define: attribut = ATTRIBUT_VALUES;
					break;
				default: attribut = ATTRIBUT_VALUE;
					break;
				}
				final String multivalue = element.getAttribute(attribut);
				
				if (name != null && multivalue != null) {
					final KeyProperty key = this.getKeyByName(name);	
					final Property property = (!isConditionalProperty)?new Property(key, qualifier):new ConditionalProperty(key, qualifier);
					
					// values
					final String[] values = multivalue.split(ISharedConstants.SEPARATOR_VALUE);
					for (String singleValue : values) {
						singleValue = singleValue.trim();
						key.addValue(singleValue);
						property.addValue(singleValue);
					}
					
					// rules if conditionnal property
					if (isConditionalProperty) {
						final ConditionalProperty conditionalProperty = (ConditionalProperty)property;
						  final Rules rulesWithoutType = this.buildPropertyRules(null, element);
		                    if (rulesWithoutType != null) {
		                    	conditionalProperty.addRule(rulesWithoutType);
		                    }
		     				for (TypeRule typeRule : TypeRule.values()) {
		    					final Rules rules = this.buildRules(typeRule, element);
		    					if(rules == null) continue; // next typeRule
		    					conditionalProperty.addRule(rules); 					
		    				}
					}
					
					listProperties.add(property);
				}
			}
	    }
		
	private KeyProperty getKeyByName(final String name) {
		KeyProperty key = StoreExtraSession.getExtraSession(this._sessionId).geKeyPropertyByName(name);
		if (key == null) {
			key = ModeleObjectFactory.getInstance().createKey(name, this._sessionId);
		}	
		return key;
	}
	 private ExtraSession getExtraSession() {
	    	return StoreExtraSession.getExtraSession(this._sessionId);
	    }
}
