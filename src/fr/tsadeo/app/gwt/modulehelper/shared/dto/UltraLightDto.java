package fr.tsadeo.app.gwt.modulehelper.shared.dto;


public class UltraLightDto extends AbstractDto {


	private static final long serialVersionUID = 1L;
		//------------------------------------------------ instance
		private String _name;
		
	//-------------------------------------- accessors
	public String getName() {
		return (this._name == null)?"":this._name;
	}
	//----------------------------------------- constructor
	public UltraLightDto() {
			super(ID_UNDEFINED);
	}
	public UltraLightDto(final int id, final String name) {
			super(id);
			this._name = name;
	}
}
