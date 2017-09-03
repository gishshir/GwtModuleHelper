package fr.tsadeo.app.gwt.modulehelper.shared.dto;

import java.util.ArrayList;
import java.util.List;

import fr.tsadeo.app.gwt.modulehelper.shared.util.StringHelper;

public class ModuleNodeDto extends AbstractDto {

	private static final long serialVersionUID = 1L;
	
	private ModuleLightDto _mainModule;	
	private List<ModuleNodeDto> _importModules;
	private boolean _enabled = true;
	
	//------------------------------------- accessors
	public ModuleLightDto getModule() {
		return _mainModule;
	}
	public List<ModuleNodeDto> getImportModules() {
		return _importModules;
	}
	public boolean isEnabled() {
		return this._enabled;
	}
	//------------------------------------- public methods
	public void disabled() {
		this._enabled = false;
	}
	public boolean hasImportModules() {
		return (this._importModules != null && !this._importModules.isEmpty());
	}
	public void addImportModule(final ModuleNodeDto importModule) {
		if (this._importModules == null) {
			this._importModules = new ArrayList<ModuleNodeDto>();
		}
		this._importModules.add(importModule);
	}
	public String displayTree(int level) {
		
		final StringBuilder sb = new StringBuilder();
		sb.append(StringHelper.repeatcharacter('>', level));
		sb.append(this._mainModule.getName());
		sb.append(" - enabled: ");
		sb.append(this._enabled);
		sb.append("\n");
		if (this.hasImportModules()) {
			level++;
			for (int i = 0; i < this._importModules.size(); i++) {
				final ModuleNodeDto importModule = this._importModules.get(i);
				sb.append(importModule.displayTree(level));
			}
		}
		return sb.toString();
	}
	//------------------------------------- constructor
	public ModuleNodeDto() {
		this(null, true);
	}
	public ModuleNodeDto(final ModuleLightDto mainModule, final boolean enabled) {
		super(mainModule.getId());
		this._mainModule = mainModule;
		this._enabled = enabled;
	}
	
	
}
