
package fr.tsadeo.app.gwt.modulehelper.shared.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CompilationDto extends CompilationLightDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<ModuleLightDto> _moduleList; 
	private List<CompilationPropertyDto> _compilationPropertyList;
	
	private List<PermutationDto> _permutationList;
	//------------------------------------------------ accessors
	public List<ModuleLightDto> getModuleList() {
		if (this._moduleList == null) {
			this._moduleList = new ArrayList<ModuleLightDto>();
		}
		return this._moduleList;
	}
	public List<CompilationPropertyDto> getCompilationPropertyList() {
		if (this._compilationPropertyList == null) {
			this._compilationPropertyList = new ArrayList<CompilationPropertyDto>();
		}
		return _compilationPropertyList;
	}

    public void setPermutationList(final List<PermutationDto> permutationList) {
    	this._permutationList = permutationList;
    }
    public List<PermutationDto> getPermutationList() {
    	return this._permutationList;
    }
	//-------------------------------------------------- constructor
	public CompilationDto() {}
	public CompilationDto(final int id, final String name) {
		super(id, name);
	}
	//-------------------------------------------------- public methods

	public boolean hasModules() {
		return this._moduleList != null && !this._moduleList.isEmpty();
	}
	public int getModulesCount() {
		return (this.hasModules())?this._moduleList.size():0;
	}
	
	public boolean hasCompilationProperties() {
		return this._compilationPropertyList != null && !this._compilationPropertyList.isEmpty();
	}
	public int getCompilationPropertiesCount() {
		return (this.hasCompilationProperties())?this._compilationPropertyList.size():0;
	}
	public boolean hasPermutations() {
		return this._permutationList != null && !this._permutationList.isEmpty();
	}
	public int getPermutationCount() {
		return (this.hasPermutations())?this._permutationList.size():0;
	}
	//------------------------------ overriding Object
	public String toString() {
		return "[" + super.toProtectedString() + " - modules: " + this.getModulesCount() + "]";
	}
}
