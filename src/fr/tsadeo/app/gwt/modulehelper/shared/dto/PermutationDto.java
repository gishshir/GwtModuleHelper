package fr.tsadeo.app.gwt.modulehelper.shared.dto;

import java.io.Serializable;
import java.util.List;

public class PermutationDto implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String _compilationName;
	private List<CompilationPropertyDto> _compilationPropertyList;
	private List<InterfaceXImplementationDto> _implementationList;
	private List<InterfaceXGeneratorDto> _generatorList;
	// liste des properties responsables de la permutation (sous ensemble de CompilationPropertyDto)
	// c'est à dire les properties multi-valuées de la compilaton
	private List<PropertyLightDto> _deltaProperties;
	
	//--------------------------------------- accessors
	public final String getCompilationName() {
		return this._compilationName;
	}
	
	//--- PropertyLightDto
	public final List<PropertyLightDto> getListDeltaProperties() {
		return this._deltaProperties;
	}
	public final void setListDeltaProperties (final List<PropertyLightDto> deltaProperties) {
		this._deltaProperties = deltaProperties;
	}
	public final boolean hasDeltaProperties() {
		return this._deltaProperties != null && !this._deltaProperties.isEmpty();
}
	//--- CompilationPropertyDto
	public final List<CompilationPropertyDto> getCompilationPropertyList() {
		return this._compilationPropertyList;
	}
	
	//--- implementations
	public final boolean hasImplementations() {
		return this._implementationList != null && !this._implementationList.isEmpty();
	}
	public final List<InterfaceXImplementationDto> getInterfaceXImplementationList() {
		return this._implementationList;
	}
	public final void setInterfaceXImplementationList(final List<InterfaceXImplementationDto> implementationList) {
		this._implementationList = implementationList;
	}
	
	//--- generators
	public final boolean hasGenerators() {
			return this._generatorList != null && !this._generatorList.isEmpty();
	}
	public final List<InterfaceXGeneratorDto> getInterfaceXGeneratorList() {
			return this._generatorList;
	}
	public final void setInterfaceXGeneratorList(final List<InterfaceXGeneratorDto> generatorList) {
			this._generatorList = generatorList;
	}
	//--------------------------------------- constructor
	public PermutationDto() {
		this(null, null);
	}
	public PermutationDto(final String compilationName, 
			final List<CompilationPropertyDto> compilationPropertyList) {
		this._compilationName = compilationName;
		this._compilationPropertyList = compilationPropertyList;
	}

}
