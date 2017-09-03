package fr.tsadeo.app.gwt.modulehelper.shared.dto;


public class CompilationLightDto extends AbstractDto implements IDto {

	private static final long serialVersionUID = 1L;
	
	private  String _name;
	private ModuleLightDto _mainModule;
	
	
	//------------------------------------------------ accessors
	public String getName() {
		return this._name;
	}

	public ModuleLightDto getMainModule() {
		return _mainModule;
	}
	public void setMainModule(ModuleLightDto mainModule) {
		this._mainModule = mainModule;
	}


	//-------------------------------------------------- constructor
	public CompilationLightDto() {
		this(ID_UNDEFINED, null);
	}
	public CompilationLightDto(final int id, final String name) {
		super(id);
		this._name = name;
	}
	
	//-------------------------------------------------- public methods
	public String toString() {
		return "[" + this.toProtectedString()+ "]";
	}
	
	//------------------------------------------------- protected methods
	public String toProtectedString() {
		return "id: " + this.getId() + " - name: " + this._name;
	}
}
