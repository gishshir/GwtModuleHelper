package fr.tsadeo.app.gwt.modulehelper.server.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public class MyInterface extends AbstractBusinessObject implements  Comparable<MyInterface> {

	private static final long serialVersionUID = 1L;
	
	private final String _name;
	private final Set<String> _implementationList = new HashSet<String>();
	private final Set<String> _generatorList = new HashSet<String>();
	
	//--------------------------------------------- accessors
	public String getName () {
		return this._name;
	}
	//--------------------------------------------- constructor
	public MyInterface() {
		this(ID_UNDEFINED, null);
	}
	protected MyInterface(final int id, final String name) {
		super(id);
		this._name = name;
	}
	//------------------------------------------------public methods
	
	//--- implementations
	public boolean hasImplementations() {
		return (this._implementationList != null &&!this._implementationList.isEmpty());
	}
	public Set<String> getImplementationList() {
		return Collections.unmodifiableSet(this._implementationList);
	}
	public void addImplementation (final String implementation) {
		if (implementation == null) return;
		this._implementationList.add(implementation);
	}
	
	//--- generators
	public boolean hasGenerators() {
		return (this._generatorList != null &&!this._generatorList.isEmpty());
	}
	public Set<String> getGeneratorList() {
		return Collections.unmodifiableSet(this._generatorList);
	}
	public void addGenerator (final String generator) {
		if (generator == null) return;
		this._generatorList.add(generator);
	}
	//-------------------------------------------- overriding Comparable
	@Override
	public int compareTo(MyInterface o) {
		return this.getName().compareTo(o.getName());
	}
}
