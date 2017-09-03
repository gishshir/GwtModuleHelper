package fr.tsadeo.app.gwt.modulehelper.shared.dto;

import java.util.Comparator;

public class ModuleLightDto extends UltraLightDto {

	private static final long serialVersionUID = 1L;
	
	public static final Comparator<ModuleLightDto> COMPARATOR_FILENAME = new Comparator<ModuleLightDto>() {

		@Override
		public int compare(ModuleLightDto o1, ModuleLightDto o2) {
			return o1.getFileName().compareTo(o2.getFileName());
		}
	};
	
	private String _renameTo;
	private String _packageName;
	private String _fileName;
	private String _entryPoint;
	private boolean _virtual;
	
	//-------------------------------------- accessors
	public String getRenameTo() {
		return (this._renameTo == null)?"":this._renameTo;
	}
	public String getEntryPoint() {
		return (this._entryPoint == null)?"":this._entryPoint;
	}

	public String getPackageName() {
		return (this._packageName == null)?"":this._packageName;
	}

	public String getFileName() {
		return (this._fileName == null)?"":this._fileName;
	}
	public boolean isVirtual() {
		return this._virtual;
	}
	//-------------------------------------- constructor
	public ModuleLightDto() {
		super();
	}
	public ModuleLightDto(final int id, final String name, final String packageName, final String renameTo,
			final String fileName, final String entryPoint, final boolean virtual) {
		super(id, name);
		this._fileName = fileName;
		this._packageName = packageName;
		this._renameTo = renameTo;
		this._entryPoint = entryPoint;
		this._virtual = virtual;
	}
	
	public String toString() {
		return "[id: " + this.getId() + " - name: " + this.getName() + " - file: " + this._fileName + 
				" - package: " + this._packageName +
                 " - virtual: " + this._virtual + "]";
	}
	
}
