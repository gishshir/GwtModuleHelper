package fr.tsadeo.app.gwt.modulehelper.server.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import fr.tsadeo.app.gwt.modulehelper.client.service.IModuleHelperService;
import fr.tsadeo.app.gwt.modulehelper.server.model.Application;
import fr.tsadeo.app.gwt.modulehelper.server.util.ExtraSession;
import fr.tsadeo.app.gwt.modulehelper.server.util.FileUploadUtils;
import fr.tsadeo.app.gwt.modulehelper.server.util.IConstants;
import fr.tsadeo.app.gwt.modulehelper.server.util.StoreExtraSession;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.CompilationDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.CompilationLightDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.InterfaceXGeneratorDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.InterfaceXImplementationDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.KeyPropertyDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.KeyPropertyLightDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.ModuleDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.ModuleLightDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.ModuleNodeDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.MyInterfaceDto;


public class ModuleHelperService extends RemoteServiceServlet implements
		IModuleHelperService {

	private static final long serialVersionUID = 1L;
	
	private static final Log log = LogFactory.getLog(ModuleHelperService.class);
	

	//-------------------------------------- overriding IModuleHelperService


	@Override
	public boolean buildDemo() throws Exception{

		log.info("buildDemo() start...");
		
		try {
			this.reinit();
			final String demoPath = this.getServletContext().getRealPath("/modulehelper/demo/demo2.zip");

			log.info("servlet real path: " + demoPath);
			final boolean result = this.buildApplication(new File(demoPath), "java");
			
			log.info("...buildDemo() end.");
			return result;
		} catch (Exception e) {
			log.fatal(e.toString());
			throw e;
		}

	}

	@Override
	public void reinit() throws Exception {
		ModuleHelperUtils.getInstance().reinit(this.getCurrentSession());	
	}

	@Override
	public boolean buildApplication(final String sourceDirName) throws Exception {
		log.info("buildApplication() start...");
		this.reinit();
		
		try {
			
			final String mySourceDirName = (sourceDirName==null || sourceDirName.trim().length() == 0)?IConstants.SOURCE_DIRNAME_DEFAULT:sourceDirName.trim();
			log.debug("mySourceDirName: " + mySourceDirName);
			final String uploadedFilename = (String)this.getCurrentSession().getAttribute(IConstants.SESSION_UPLOADED_FILENAME);

			final File uploadedFile = (uploadedFilename == null)?null:FileUploadUtils.getInstance().getUploadedFile(uploadedFilename);
			if (uploadedFilename == null) {
				log.fatal("aucun fichier téléchargé!");
				throw new Exception("aucun fichier téléchargé!!");
			}
			
			final boolean result = this.buildApplication(uploadedFile,mySourceDirName);
			FileUploadUtils.getInstance().removeUploadedFile(uploadedFilename);
			this.getCurrentSession().removeAttribute(IConstants.SESSION_UPLOADED_FILENAME);
					
			log.info("...buildApplication() end.");
			return result;
		} catch (Exception e) {
			log.fatal(e.toString());
			throw e;
		}
		
		
	}
	@Override
	public boolean buildApplicationFromMapContent(String sourceDirName)  throws Exception {
		
		log.info("buildApplicationFromMapContent() start...");
		this.reinit();
		
		try {
			
			final String mySourceDirName = (sourceDirName==null || sourceDirName.trim().length() == 0)?IConstants.SOURCE_DIRNAME_DEFAULT:sourceDirName.trim();
			log.debug("mySourceDirName: " + mySourceDirName);
			@SuppressWarnings("unchecked")
			final Map<String, List<String>> uploadedContent = (Map<String, List<String>>)
					this.getCurrentSession().getAttribute(IConstants.SESSION_UPLOADED_CONTENT);
			if (uploadedContent == null) {
				log.fatal("aucun fichier téléchargé!");
				throw new Exception("aucun fichier téléchargé!!");
			}
			
			final HttpSession session = this.getCurrentSession();
			final Application application =
					ModuleHelperUtils.getInstance().buildApplication(uploadedContent, sourceDirName, session.getId());
					
			session.setAttribute(IConstants.SESSION_APPLICATION, application);
			session.setAttribute(IConstants.SESSION_EXTRA_SESSION, StoreExtraSession.getExtraSession(session.getId()));

			this.getCurrentSession().removeAttribute(IConstants.SESSION_UPLOADED_CONTENT);
					
			log.info("...buildApplicationFromMapContent() end.");
			return true;
		} catch (Exception e) {
			log.fatal(e.toString());
			throw e;
		}
	}
	
	@Override
	public ModuleNodeDto loadTreeModules(final String compilationName) throws Exception {
		log.info("loadTreeModules() start...");
		
		try {
			final ModuleNodeDto moduleNodeDto = 
					ModuleHelperDtoUtils.getInstance().loadTreeModules(compilationName, this.getCurrentApplication());
			log.info("...loadTreeModules() end.");
			return moduleNodeDto;
		} catch (Exception e) {
			log.fatal(e.toString());
			throw e;
		}
	}
	@Override
	public List<InterfaceXImplementationDto> loadInterfaceXImplementations(
			final String interfaceName) throws Exception {
		log.info("loadInterfaceXImplementations() start...");
		
		try {
			final List<InterfaceXImplementationDto> interfaceXImplementationList =
					ModuleHelperDtoUtils.getInstance().loadInterfaceXImplementations(interfaceName, this.getCurrentApplication());
			log.info("...loadInterfaceXImplementations() end.");
			return interfaceXImplementationList;
		} catch (Exception e) {
			log.fatal(e.toString());
			throw e;
		}
	}

	@Override
	public List<InterfaceXGeneratorDto> loadInterfaceXGenerators(
			String interfaceName) throws Exception {
		log.info("loadInterfaceXGenerators() start...");
		
		try {
			final List<InterfaceXGeneratorDto> interfaceXGeneratorList =
					ModuleHelperDtoUtils.getInstance().loadInterfaceXGenerators(interfaceName, this.getCurrentApplication());
			log.info("...loadInterfaceXGenerators() end.");
			return interfaceXGeneratorList;
		} catch (Exception e) {
			log.fatal(e.toString());
			throw e;
		}
	}
	@Override
	public List<ModuleLightDto> loadModules() throws Exception {
		log.info("loadModules() start...");
		
		try {
			final List<ModuleLightDto> moduleDtoList =
					ModuleHelperDtoUtils.getInstance().loadModules( this.getCurrentApplication());
			log.info("...loadModules() end.");
			return moduleDtoList;
		} catch (Exception e) {
			log.fatal(e.toString());
			throw e;
		}
		
	}
	@Override
	public ModuleDto loadModule(final String moduleName) throws Exception {
		log.info("loadModule() start...");
		
		try {
			final ModuleDto moduleDto =
					ModuleHelperDtoUtils.getInstance().loadModule(moduleName, this.getCurrentApplication());
			log.info("...loadModule() end.");
			return moduleDto;
		} catch (Exception e) {
			log.fatal(e.toString());
			throw e;
		}
		
	}
	@Override
	public CompilationDto loadCompilation(final String compilationName) throws Exception {
        log.info("loadCompilation() start...");
		
		try {
			final CompilationDto compilationDto =
					ModuleHelperDtoUtils.getInstance().loadCompilation(this.getCurrentApplication(), compilationName);
			log.info("...loadCompilation() end.");
			return compilationDto;
		} catch (Exception e) {
			log.fatal(e.toString());
			throw e;
		}
	}
	@Override
	public List<CompilationLightDto> loadCompilations()  throws Exception{
		log.info("loadCompilations() start...");
		
		try {
			final List<CompilationLightDto> moduleDtoList =
					ModuleHelperDtoUtils.getInstance().loadCompilations(this.getCurrentApplication());
			log.info("...loadCompilations() end.");
			return moduleDtoList;
		} catch (Exception e) {
			log.fatal(e.toString());
			throw e;
		}
		
	}

	@Override
	public List<MyInterfaceDto> loadInterfaces()  throws Exception{
		log.info("loadInterfaces() start...");
		
		try {
			final List<MyInterfaceDto> interfaceDtoList =
					ModuleHelperDtoUtils.getInstance().loadInterfaces(this.getCurrentApplication());
			log.info("...loadInterfaces() end.");
			return interfaceDtoList;
		} catch (Exception e) {
			log.fatal(e.toString());
			throw e;
		}
	}

	@Override
	public List<KeyPropertyLightDto> loadKeyProperties() throws Exception {
		log.info("loadKeyProperties() start...");
		
		try {
			final List<KeyPropertyLightDto> keyPropertyDtoList =
					ModuleHelperDtoUtils.getInstance().loadKeyProperties(this.getCurrentApplication());
			log.info("...loadKeyProperties() end.");
			return keyPropertyDtoList;
		} catch (Exception e) {
			log.fatal(e.toString());
			throw e;
		}
	}
	@Override
	public KeyPropertyDto loadKeyProperty(final KeyPropertyLightDto keyProperty)
			throws Exception {
		log.info("loadKeyProperty() start...");
		
		try {
			final KeyPropertyDto keyPropertyDto =
					ModuleHelperDtoUtils.getInstance().loadKeyProperty(keyProperty, this.getCurrentApplication());
			log.info("...loadKeyProperties() end.");
			return keyPropertyDto;
		} catch (Exception e) {
			log.fatal(e.toString());
			throw e;
		}
	}


   //------------------------------------- private methods
	private boolean buildApplication(final File zipFile, final String sourceDirName) throws Exception {
		
		try {
			final HttpSession session = this.getCurrentSession();
			final Application application =
					ModuleHelperUtils.getInstance().buildApplication(zipFile, sourceDirName, session.getId());
					
			session.setAttribute(IConstants.SESSION_APPLICATION, application);
			session.setAttribute(IConstants.SESSION_EXTRA_SESSION, StoreExtraSession.getExtraSession(session.getId()));
			
			
			return true;
		} catch (Exception e) {
			log.fatal(e.toString());
			throw e;
		}
	}
	private HttpSession getCurrentSession() {
		final HttpSession session = this.getThreadLocalRequest().getSession(true);
		log.info("session id: " + session.getId() + " - " + session.getCreationTime());
		return session;
	}
	
    private Application getCurrentApplication() throws Exception {
    	final HttpSession session = this.getCurrentSession();
    	
    	// restaurer eventuellement l'extra session
    	ExtraSession extraSession = StoreExtraSession.getExtraSession(session.getId(), false);
    	if (extraSession == null) {
    		// on tente de la restaurer depuis la session
    		extraSession = (ExtraSession) session.getAttribute(IConstants.SESSION_EXTRA_SESSION);
    		if (extraSession != null) {
    			StoreExtraSession.addExtraSession(extraSession, session.getId());
    		}
    	}
    	
    	final Application application = (Application) session.getAttribute(IConstants.SESSION_APPLICATION);
    	if (application == null) {
			log.fatal("application cannot be null!");
			throw new Exception("La session a expiré. Prière d'ouvrir un nouveau projet.");
		}
    	return application;
    }




}
