package fr.tsadeo.app.gwt.modulehelper.shared.dto;

import java.util.ArrayList;
import java.util.List;

public class MyInterfaceDto extends AbstractDto {

	
    private static final long serialVersionUID = 1L;
	
	private String _name;
	private List<String> _implementationList;
	private List<String> _generatorList;
	
	//--------------------------------------------- accessors
	public String getName () {
		return this._name;
	}
    public boolean hasImplementations() {
    	return this._implementationList != null && !this._implementationList.isEmpty();
    }
	public List<String> getImplementationList() {
		return (this._implementationList==null)?new ArrayList<String>(0):this._implementationList;
	}
	public boolean hasGenerators() {
		return this._generatorList != null && !this._generatorList.isEmpty();
	}
	public List<String> getGeneratorList() {
		return (this._generatorList==null)?new ArrayList<String>(0):this._generatorList;
	}
	//--------------------------------------------- constructor
	public MyInterfaceDto() {
		this(ID_UNDEFINED, null, null, null);
	}
	public MyInterfaceDto(final int id, final String name, final List<String> implementationList,
			final List<String> generatorList) {
		super(id);
		this._name = name;
		this._implementationList = implementationList;
		this._generatorList = generatorList;
	}
}
