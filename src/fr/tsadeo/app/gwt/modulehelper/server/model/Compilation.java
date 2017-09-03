package fr.tsadeo.app.gwt.modulehelper.server.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Compilation extends AbstractBusinessObject implements  Comparable<Compilation> {
	
	private static final long serialVersionUID = 1L;
	

	private final  Module _module;
	// no conditional property - only one Property by Key, but the Property can be multi-valued
	private final List<Property> _setPropertyList = new ArrayList<Property>();
	// conditional properties : property with rules
	private Map<KeyProperty, List<ConditionalProperty>> _mapConditionalProperties;
	private final List<Permutation> _permutationList = new ArrayList<Permutation>();

	// Tous les modules de la compilation par ordre (+faible au +fort)
	// en incluant le module principal en dernier
	private List<Module> _compilationModuleList;
	// par défaut les permutations ne sont pas calculées
	private boolean _toComplete = true;
	
	
	//------------------------------------------ accessors
	public void setCompilationModuleList(final List<Module> compilationModuleList) {
	   this._compilationModuleList = compilationModuleList;	
	}
	public List<Module> getCompilationModuleList() {
		return this._compilationModuleList;
	}
	public boolean isToComplete() {
		return this._toComplete;
	}
	public void setHasBeenCompleted() {
		this._toComplete = false;
	}
	public void addAllConditionalSetProperties(final Map<KeyProperty, List<ConditionalProperty>> mapConditionalProperties) {
		this._mapConditionalProperties = mapConditionalProperties;
	}
	public Module getModule() {
		return this._module;
	}
	public List<Property> getSetPropertyList() {
		Collections.sort(this._setPropertyList);
		return Collections.unmodifiableList(this._setPropertyList);
	}
	public List<Permutation> getPermutationList() {
		return Collections.unmodifiableList(this._permutationList);
	}
	//-----------------------------------------  public methods
	public String getName() {
		return this._module.getName();
	}
	public void addAllSetProperties (final Collection<Property> setPropertyList) {
		if (setPropertyList == null) return;
		this._setPropertyList.addAll(setPropertyList);
	}
	public boolean hasSetProperties() {
		return !this._setPropertyList.isEmpty();
	}
	public boolean hasConditionalProperties() {
		return !this._mapConditionalProperties.isEmpty();
	}
	public int getPermutationCount() {
		return this._permutationList.size();
	}
	public boolean hasPermutations() {
		return this._permutationList != null && !this._permutationList.isEmpty();
	}
	public void addPermutations(final List<Permutation> permutationList) {
		if (permutationList == null) return;
		this._permutationList.addAll(permutationList);
	}
	public Set<KeyProperty> getKeyPropertiesForConditionalProperties() {
		return this._mapConditionalProperties.keySet();
	}
	public List<ConditionalProperty> getConditionalPropertiesByKey(final KeyProperty keyProperty) {
		return this._mapConditionalProperties.get(keyProperty);
	}
	//----------------------------------------- constructor
	public Compilation() {
		this(ID_UNDEFINED, null);
	}
	protected Compilation(final int id, final  Module module) {
		super(id);
		this._module = module;

	}
	//-------------------------------------- overriding Comparable
	@Override
	public int compareTo(Compilation o) {
		return this.getName().compareTo(o.getName());
	}
}
