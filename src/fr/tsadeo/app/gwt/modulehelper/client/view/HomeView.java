package fr.tsadeo.app.gwt.modulehelper.client.view;

import fr.tsadeo.app.gwt.modulehelper.client.service.ServiceCallback;
import fr.tsadeo.app.gwt.modulehelper.client.util.AppController;
import fr.tsadeo.app.gwt.modulehelper.client.util.AppController.ViewNameEnum;
import fr.tsadeo.app.gwt.modulehelper.client.util.Constantes;
import fr.tsadeo.app.gwt.modulehelper.client.util.Constantes.Action;
import fr.tsadeo.app.gwt.modulehelper.client.util.Constantes.ActionInfo;
import fr.tsadeo.app.gwt.modulehelper.client.util.Constantes.ActionState;
import fr.tsadeo.app.gwt.modulehelper.client.util.IAppListener;
import fr.tsadeo.app.gwt.modulehelper.client.util.WidgetUtils;
import fr.tsadeo.app.gwt.modulehelper.client.widget.IActionCallback;
import fr.tsadeo.app.gwt.modulehelper.client.widget.TableModules;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.ModuleLightDto;
import gwtupload.client.IUploadStatus.Status;
import gwtupload.client.IUploader;
import gwtupload.client.IUploader.UploadedInfo;
import gwtupload.client.SingleUploader;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class HomeView extends AbstractPanelView implements IAppListener {
	
	 private final static Logger log = Logger.getLogger(HomeView.class.getName());
	 
	 private final static String GROUP_CHOICE_SOURCE = "homeChoiceSource";
	 
	 
	private static final String[] LAUNCH_APPLET = new String[] {
		"<applet id=\"mhapplet\" mayscript=\"MAYSCRIPT\" ",
		"style=\"width:80px;height:40px;\" ",
		"code=\"fr.tsadeo.app.applet.modulehelper.ModuleHelperApplet.class\" ",
		"codebase=\"./applet\" ",
		"archive=\"ModuleHelperApplet.jar\"></applet>"
	};
			
				 
	 private final SingleUploader _fileUploader = new SingleUploader();
	 private Widget _appletButton;
	 
	 private final HorizontalPanel _panelBrowse = new HorizontalPanel();
     private final HorizontalPanel _panelProject =  new HorizontalPanel();
     
     private final RadioButton _rbChoisirArchive = new RadioButton(GROUP_CHOICE_SOURCE, "Choisir l'archive du projet");
     private final RadioButton _rbChoisirRepSource = new RadioButton(GROUP_CHOICE_SOURCE, "Choisir le répertoire du projet");
    
     private final Label _labelProject = WidgetUtils.buildWhiteLabel("");
     private Label _labelProjectName;

     
	 private final TableModules _tableModules = new TableModules();
	 
	 private String _projectName;
		
	private final Button _btDemo = new Button();
	private final Button _btReinit = new Button("Nouveau projet");
    private final Button _btGoInfo = new Button();
    
    private DialogBox _dialogInfo;
    private DialogBox _dialogDemo;
    
    private final TextBox _tbSourceDirName = new TextBox();

    // ------------------------------------------ constructor
	public HomeView() {
			super("Projet", ViewNameEnum.home);
		
			this.initComposants();
			this.initHandlers();
			
			this.setContent(this.buildContent());
			this.protectedReinit();
			this.displayPanels();
			AppController.getInstance().addAppListener(this);
	}
		
	//---------------------------------- overriding AbstractPanelView
	@Override
	protected void protectedReinit() {
		this._tableModules.reinit();

	}

	@Override
	protected void protectedUpdate() {
		this.getDataFromServer(Action.loadModules);
	}

	@Override
	protected void getDataFromServer(final Action action) {
		this.getDataFromServer(action, Constantes.ActionState.start, null);
	}
	
	//---------------------------------- implementing IAppListener

	@Override
	public void reinitApp() {
		this.enabledButton(true);
	}

	@Override
	public void displayView(ViewNameEnum viewName, ViewParams viewParams) {
		// Nothing	
	}

	@Override
	public void inProgress(Action action, String message) {
		this.enabledButton(false);
	}

	@Override
	public void done(Action action, String message) {
		this.enabledButton(true);
	}

	@Override
	public void error(Action action, String errorMessage) {
		this.enabledButton(true);
	}

	@Override
	public void updateInfo(ActionInfo actionInfo, String info) {
		// Nothing	
	}

	//------------------------------------------------------- protected methods
	protected void buildApplicationAfterUploadContent(final String dirPathname) {
		this.getDataFromServer(Action.uploadContent, ActionState.done, dirPathname);
	}
    protected void updateSourceDirName(final String sourceDirName) {
    	this._tbSourceDirName.setText(sourceDirName);
    }
	//------------------------------------------------------- private methods	
    private void enabledButton (final boolean enabled) {
    	
    	this._rbChoisirArchive.setEnabled(enabled);
    	this._rbChoisirRepSource.setEnabled(enabled);
    	this._btDemo.setEnabled(enabled);
    	this._btGoInfo.setEnabled(enabled);
    	this._btReinit.setEnabled(enabled);
    }
	private void saveSourceDir() {
        AppController.getInstance().setInfo(ActionInfo.sourceDir, this.getSourceDir());
	}
	private String getSourceDir() {
		final String tbValue = this._tbSourceDirName.getValue();
		return (tbValue != null && tbValue.trim()
				.length() > 0) ? tbValue : AppController.getInstance().getInfo(ActionInfo.sourceDir);
	}
	private  void getDataFromServer(final Action action, final ActionState state, final Object object) {
		
		if (log.isLoggable(Level.INFO)) {
		  log.info("getDataFromServer: action " + action + " - state: " + state + ((object==null)?"": " - object: " + object.toString()));
		}
		AppController.getInstance().done(Action.clearStatus);
			
			switch (action) {
			
			case uploadContent:
				   switch(state) {
				   case done: 
					    AppController.getInstance().reinit(); 
					    getDataFromServer(Action.buildApplicationFromContent);
						this._projectName = object.toString();
						this.displayPanels();
					   break;	  
				   }
				break;
			
						
			case uploadZip:
				 
				 // controle
				   IUploader uploader = null;
				   if (state != ActionState.error && object != null && object instanceof IUploader) {
					   uploader = (IUploader)object;
					   final StringBuilder errorMessage = new StringBuilder();
					   if (!controleUpload(uploader, errorMessage)) {
						   getDataFromServer(Action.uploadZip, ActionState.error, errorMessage.toString());
						   break;
						}
					}
				   
				   switch(state) {
				   case start: 		
					
					_projectName = null;
					 AppController.getInstance().reinit(); 
					 AppController.getInstance().inProgress(action);
					  break;

				   case done: 			    	
					  
				      if (uploader.getStatus() == Status.SUCCESS) {
				    	  
				       AppController.getInstance().done(action);
					    _projectName = uploader.getServerInfo().name;
					    displayPanels();  	  

				        // You can send any customized message and parse it 
				       log.config("Server message " +  uploader.getServerInfo().message);
				       
				       getDataFromServer(Action.buildApplication);
				      
				      }
					  break;
					   
				   case cancel:  AppController.getInstance().done(Action.uploadZip, "Interruption par l'utilisateur");
					 _projectName = null;
					  displayPanels();  
					  break;
					   
				   case error: AppController.getInstance().error(Action.uploadZip, (object==null)?"erreur inconnue!":object.toString());
					   break;
				   }
				   
				   
				   break;
			
			case loadModules:
				 AppController.getInstance().inProgress(action);
				AppController.getService().loadModules(new ServiceCallback<List<ModuleLightDto>>(action) {

					@Override
					public void onSuccess(List<ModuleLightDto> result) {
						_tableModules.populateGrid(result);
						AppController.getInstance().done(action);
						
					}
				});
				break;
				
		case buildApplication: {
			AppController.getInstance().inProgress(action);
			AppController.getService().buildApplication(this.getSourceDir(),
					new ServiceCallback<Boolean>(action) {

						@Override
						public void onSuccess(Boolean result) {
							AppController.getInstance().done(action);
							getDataFromServer(Action.loadModules);
						}
					});
			break;
		  }

			case buildApplicationFromContent:  {AppController.getInstance().inProgress(action);
			    AppController.getInstance().inProgress(action);
				AppController.getService().buildApplicationFromMapContent(this.getSourceDir(), new ServiceCallback<Boolean>(action) {

				@Override
				public void onSuccess(Boolean result) {
					AppController.getInstance().done(action);
					getDataFromServer(Action.loadModules);
				}
			});
				break;}
				
			case buildDemo: _projectName = "démo";

			AppController.getInstance().reinit();	
			AppController.getInstance().inProgress(action);
			AppController.getService().buildDemo(new ServiceCallback<Boolean>(action) {
				
				@Override
				public void onSuccess(Boolean result) {
					AppController.getInstance().done(action);
					displayPanels();
					getDataFromServer(Action.loadModules);
				}

			});
			break;
			}
		}
		//---------------------------------- private methods
		private Panel buildContent() {
			
			log.info("buildContent()");
			final VerticalPanel content = new VerticalPanel();
					
			content.add(this.buildPanelBrowse());
            content.add(this.buildPanelProject());
            
			content.add(this._tableModules);
			content.setCellHeight(this._tableModules, Constantes.MAX_SIZE);
			return content;
		}
		
		private Panel buildPanelBrowse () {
			
			this._panelBrowse.setSpacing(Constantes.SPACING_MIN);
			this._panelBrowse.setHeight(Constantes.Dim50PX);
			this._panelBrowse.setWidth(Constantes.MAX_SIZE);
			this._panelBrowse.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
			this._panelBrowse.setHorizontalAlignment( HasHorizontalAlignment.ALIGN_LEFT);
			
			// radio button
			final VerticalPanel vPanelLabel = new VerticalPanel();
			vPanelLabel.add(this._rbChoisirArchive);
			vPanelLabel.add(this._rbChoisirRepSource);
			this._panelBrowse.add(vPanelLabel);
			this._panelBrowse.setCellWidth(vPanelLabel, "300px");
			 
			// action panel
			final FlowPanel actionPanel = new FlowPanel();	
			actionPanel.add(this._fileUploader);
			actionPanel.add(this.buildAppletButton());
			this._panelBrowse.add(actionPanel);
			this._panelBrowse.setCellWidth(actionPanel, "350px");
			
			// buttons
			this._panelBrowse.add(this._btGoInfo);
			this._panelBrowse.setCellWidth(this._btGoInfo, "50px");
			
			this._panelBrowse.add(this._btDemo);
			
			return this._panelBrowse;
		}
		
		private Panel buildPanelProject() {
			
			this._panelProject.setSpacing(Constantes.SPACING_MIN);
			this._panelProject.setHeight(Constantes.Dim50PX);
			this._panelProject.setWidth(Constantes.MAX_SIZE);
			this._panelProject.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
			
			this._panelProject.add(this._labelProject);
			this._panelProject.setCellWidth(this._labelProject, "200px");
			this._panelBrowse.setCellHorizontalAlignment(this._labelProject, HasHorizontalAlignment.ALIGN_LEFT);
			
			this._labelProjectName = WidgetUtils.buildWhiteLabel("");
			this._panelProject.add(this._labelProjectName);
			this._panelProject.setCellWidth(this._labelProjectName, "300px");
			this._panelBrowse.setCellHorizontalAlignment(this._labelProjectName, HasHorizontalAlignment.ALIGN_LEFT);
			
			this._panelProject.add(this._btReinit);
			return this._panelProject;
		}
		private void initComposants() {
			
			this._rbChoisirArchive.setValue(true);		
			this._rbChoisirArchive.addStyleName(Constantes.STYLE_WHITE_LABEL);
			this._rbChoisirRepSource.addStyleName(Constantes.STYLE_WHITE_LABEL);

			this._panelProject.setVisible(false);
			
			this._btGoInfo.setTitle("information");
			this._btGoInfo.addStyleName(Constantes.STYLE_BUTTON);
			this._btGoInfo.addStyleName(Constantes.STYLE_BUTTON_HELP);
			
			this._btDemo.setTitle("données de démonstration");
			this._btDemo.addStyleName(Constantes.STYLE_BUTTON);
			this._btDemo.addStyleName(Constantes.STYLE_BUTTON_DEMO);
			
       }

		
	   private void displayPanels() {
		   
		   // show source archive / repertoire
		   final boolean showArchiveSouce = this._rbChoisirArchive.getValue();
		   this._fileUploader.setVisible(showArchiveSouce);
		   this._appletButton.setVisible(!showArchiveSouce);
		   
		   // show panel browse / projet
		   final boolean showPanelProject = this._projectName != null;
		   
		   if (showPanelProject) {
			   final String text = (showArchiveSouce)?
					   "Archive du projet:":"Répertoire du projet:";
			   this._labelProject.setText(text);
			   this._labelProjectName.setText(this._projectName);
		   }
		   
		   this._panelBrowse.setVisible(!showPanelProject);
		   this._panelProject.setVisible(showPanelProject);
		   


	   }
		private void initHandlers() {
			
			//----------- change source Dir ----------
			this._tbSourceDirName.addChangeHandler(new ChangeHandler() {
				
				@Override
				public void onChange(ChangeEvent event) {
					saveSourceDir();
				}
			});
			
			//---------- radio button ----------------
			final ClickHandler rbClickHandler = new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					displayPanels();
				}
			};
			this._rbChoisirArchive.addClickHandler(rbClickHandler);
			this._rbChoisirRepSource.addClickHandler(rbClickHandler);
			
			
            // ----------file upload --------------------	
		this._fileUploader.addOnStartUploadHandler( new IUploader.OnStartUploaderHandler(){
			
			@Override
			public void onStart(final IUploader uploader) {		
	           getDataFromServer(Action.uploadZip, ActionState.start, uploader);
			}
		});
		
		this._fileUploader.addOnCancelUploadHandler( new IUploader.OnCancelUploaderHandler() {
			
			@Override
			public void onCancel(IUploader uploader) {
				getDataFromServer(Action.uploadZip, ActionState.cancel, uploader);	
		
			}
		});

		 // Load the image in the document and in the case of success attach it to the viewer  
		this._fileUploader.addOnFinishUploadHandler( new IUploader.OnFinishUploaderHandler() {
			
			   public void onFinish(IUploader uploader) {
			    	getDataFromServer(Action.uploadZip, ActionState.done, uploader);			   
			    }
		 });
			
		// -----------demo
		this._btDemo.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
				   showDialogDemo();
				}
			});
			
			//------------ reinit
			this._btReinit.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					_projectName = null;
					final Action action = Action.reinit;
					AppController.getService().reinit(new ServiceCallback<Void>(action) {

						@Override
						public void onSuccess(Void result) {
							AppController.getInstance().reinit();
							AppController.getInstance().done(action);
							displayPanels();							
						}
					});

				}
			});
			
			//------------- help

			this._btGoInfo.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					showDialogInfo();				
				}
			});

		}

		private boolean controleUpload(IUploader uploader, final StringBuilder messageToReturn) {
			
			// controle
			String baseName = uploader.getBasename();		   
		   baseName = (baseName == null)?null:baseName.trim().toLowerCase();
		   log.config("baseName: " + baseName);
		   if (baseName == null || baseName.length() == 0 || 
				   !(baseName.endsWith(Constantes.EXTENSION_ZIP) || baseName.endsWith(Constantes.EXTENSION_JAR))) {
				log.severe("annulation du téléchargement!");
				uploader.cancel();
				messageToReturn.append("Le fichier choisi n'existe pas ou bien n'est pas une archive zip ou jar!");
			   return false;
			}
		   
		   
		      log.config("File url " + uploader.fileUrl());
		      
		        // The server sends useful information to the client by default
		        UploadedInfo info = uploader.getServerInfo();
		      if (info != null) {
		       log.config("File name " + info.name);
		       log.config("File content-type " + info.ctype);
		       log.config("File size " + info.size);
		      }
		   return true;
		}
		

		private Widget buildAppletButton() {
			
	
			final StringBuilder sb = new StringBuilder();
			for (int i = 0; i < LAUNCH_APPLET.length; i++) {
				final String line = LAUNCH_APPLET[i];
				sb.append(line);
				sb.append("\n");
			}
			this._appletButton = new HTML(sb.toString());			
			
			return this._appletButton;
		}
		
			
	
		
		private void buildDialogDemo() {
			
			if (this._dialogDemo == null) {
				
				final String[] messages = new String[] 
						{"Si vous cliquez sur OK le projet de démonstration va être chargé.",
						"La démonstration remplacera le projet en cours."};				
			
				this._dialogDemo = 
						WidgetUtils.buildDialogBox("Projet de démonstration", messages, null, true, new IActionCallback() {
					
					@Override
					public void onOk() {						
                       getDataFromServer(Action.buildDemo);
					}
					
					@Override
					public void onCancel() {}
				});
				
			}
		}
		
		private void buildDialogInfo() {
			
			if (this._dialogInfo == null) {
				
				final String[] messages = new String[] 
						{"Construire une archive zip ou jar contenant le projet GWT.",
						"Afin de réduire le temps de chargement, n'inclure que les fichiers *.gwt.xml.",
						"La taille du fichier archive ne doit pas dépasse 3Mo."};				

	            final HorizontalPanel hPanelSourceDir = new HorizontalPanel();
	            hPanelSourceDir.setSpacing(Constantes.SPACING_MIN);
	            hPanelSourceDir.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
	            
	            
	            this._tbSourceDirName.setWidth("100px");
	            this._tbSourceDirName.setValue(this.getSourceDir());
	            final Label label = new Label("répertoire racine des sources: ");
	            hPanelSourceDir.add(label);
	            hPanelSourceDir.add(this._tbSourceDirName);
				
				this._dialogInfo =
						WidgetUtils.buildDialogBox("Information", messages, hPanelSourceDir, false, null);
				
			}
		}
		

		
		private void showDialogInfo () {

			this.buildDialogInfo();
			this._dialogInfo.showRelativeTo(this._btGoInfo);            
		}
		private void showDialogDemo() {
			this.buildDialogDemo();
			this._dialogDemo.showRelativeTo(this._btDemo);
		}



}
