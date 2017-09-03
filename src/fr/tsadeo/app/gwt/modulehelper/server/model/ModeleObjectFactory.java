package fr.tsadeo.app.gwt.modulehelper.server.model;

import java.io.File;
import java.util.List;
import java.util.zip.ZipEntry;

import fr.tsadeo.app.gwt.modulehelper.server.util.ExtraSession;
import fr.tsadeo.app.gwt.modulehelper.server.util.StoreExtraSession;

public class ModeleObjectFactory {
	
	private final static ModeleObjectFactory instance = new ModeleObjectFactory();
	public final static ModeleObjectFactory getInstance() {
		return instance;
	}
	
	public MyInterface createMyInterface(final String name, final String sessionId) {
		if (name == null) return null;
		
		final MyInterface myInterface = new MyInterface(this.getExtraSession(sessionId).getNewIdentifier(ExtraSession.ModeleEnum.myinterface), name);
		if (this.getExtraSession(sessionId).containByKey(ExtraSession.ModeleEnum.myinterface, name)) {
			throw new IllegalArgumentException("duplicate name for MyInterface! : " + name);
		}	

		this.getExtraSession(sessionId).addInterface(myInterface);	
		return myInterface;
	}
	public Compilation createCompilation(final Module module, final String sessionId) {
		
	   if (module == null) return null;
		
		final Compilation compilation = new Compilation(this.getExtraSession(sessionId).getNewIdentifier(ExtraSession.ModeleEnum.compilation), module);	
		
		if (this.getExtraSession(sessionId).containByKey(ExtraSession.ModeleEnum.compilation, compilation.getName())) {
			throw new IllegalArgumentException("duplicate name for the compilation! : " + compilation.getName());
		}	
		this.getExtraSession(sessionId).addCompilation(compilation);
		return compilation;
	}
	
	public KeyProperty createKey(final String name, final String sessionId) {
		
		if (name == null) return null;
		
		if (this.getExtraSession(sessionId).containByKey(ExtraSession.ModeleEnum.key, name)) {
			throw new IllegalArgumentException("duplicate name for the key! : " + name);
		}
		final KeyProperty key = new KeyProperty(this.getExtraSession(sessionId).getNewIdentifier(ExtraSession.ModeleEnum.key), name);
		this.getExtraSession(sessionId).addKeyProperty(key);
		return key;
	}
	
	private ExtraSession getExtraSession(final String sessionId) {
		return StoreExtraSession.getExtraSession(sessionId);
	}
	
	public Module createModule(final IXmlGwtFile xmlGwtFile, final String sessionId) {
		
		if (xmlGwtFile == null) return null;
		
		final Module module = new Module(this.getExtraSession(sessionId).getNewIdentifier(ExtraSession.ModeleEnum.module), xmlGwtFile);
		if (this.getExtraSession(sessionId).containByKey(ExtraSession.ModeleEnum.module, module.getName())) {
			throw new IllegalArgumentException("Il n'est pas possible d'avoir deux modules de même nom: : " + module.getName());
		}	
		this.getExtraSession(sessionId).addModule(module);
		return module;
	}
	public IXmlGwtFile createXmlGwtFile(final String moduleName, final String sessionId) {
		
		if (moduleName == null) return null;
		final IXmlGwtFile xmlGwtFile = new XmlGwtFileVirtual(moduleName);
		this.getExtraSession(sessionId).addXmlGwtFile(xmlGwtFile);
		return xmlGwtFile;
		
	}
   public IXmlGwtFile createXmlGwtFile(final String pathname, 
			final String sourceDirName,
			final List<String> content, final String sessionId) {
			
			if (pathname == null ||  sourceDirName == null) return null;
			
			final IXmlGwtFile xmlGwtFile = new XmlGwtContent(pathname, sourceDirName, content);
			this.getExtraSession(sessionId).addXmlGwtFile(xmlGwtFile);
			return xmlGwtFile;
    }
    public IXmlGwtFile createXmlGwtFile(final ZipEntry zipEntry, final String sourceDirName, final String sessionId) {
		
		if (zipEntry == null ||  sourceDirName == null) return null;
		
		final IXmlGwtFile xmlGwtFile = new XmlGwtZipEntry(zipEntry.getName(), sourceDirName);
		this.getExtraSession(sessionId).addXmlGwtFile(xmlGwtFile);
		return xmlGwtFile;
	}
	public IXmlGwtFile createXmlGwtFile(final File gwtFile, final String rootPath, final String sourceDirName, final String sessionId) {
		
		if (gwtFile == null || rootPath == null || sourceDirName == null) return null;
		
		final IXmlGwtFile xmlGwtFile = new XmlGwtFile(gwtFile, rootPath, sourceDirName);
		this.getExtraSession(sessionId).addXmlGwtFile(xmlGwtFile);
		return xmlGwtFile;
	}

}
