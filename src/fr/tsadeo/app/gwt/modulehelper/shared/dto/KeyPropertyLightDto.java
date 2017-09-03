package fr.tsadeo.app.gwt.modulehelper.shared.dto;

import java.util.Set;

import fr.tsadeo.app.gwt.modulehelper.shared.util.ISharedConstants;
import fr.tsadeo.app.gwt.modulehelper.shared.util.StringHelper;

public class KeyPropertyLightDto extends UltraLightDto {

	private static final long serialVersionUID = 1L;
	
	 //------------------------------------------------ instance
		private Set<String> _values;

		//-------------------------------------- accessors
		public Set<String> getValues() {
			return _values;
		}
		//----------------------------------------- constructor
		public KeyPropertyLightDto() {
			this(ID_UNDEFINED, null, null);
		}
		public KeyPropertyLightDto(final int id, final String name, final Set<String> values) {
			super(id, name);
			this._values = values;
		}
		
		//-------------------------------------- overriding Object
		public String toString() {
			
			final StringBuilder sb = new StringBuilder();
			sb.append("[");
			sb.append(this.getId());
			sb.append("/");
			sb.append(this.getName());
			sb.append(": ");

			sb.append(StringHelper.valuesToString(this.getValues(), ISharedConstants.SEPARATOR_VALUE));
			
			sb.append("]");

			return sb.toString();
		}
}
