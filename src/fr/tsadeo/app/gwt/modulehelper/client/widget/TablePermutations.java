package fr.tsadeo.app.gwt.modulehelper.client.widget;

import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.RadioButton;

import fr.tsadeo.app.gwt.modulehelper.client.util.AppController.ICallbackInfo;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.PermutationDto;
import fr.tsadeo.app.gwt.modulehelper.shared.dto.PropertyLightDto;

public class TablePermutations extends AbstractTable {
	
	private final static String GROUP_PERMUTATION = "rbPermutation";
	
    private final static Logger log = Logger.getLogger(TableModules.class.getName());
	private static final Header[] headers = new Header[]  {new Header("permutation"), new Header("differential properties")};

	private final ICallbackInfo<Integer> _callbackPermutation;
	// --------------------------------------- overriding AbstractTable
	@Override
	protected Logger getLog() {
		return log;
	}
	@Override
	protected Header[] getHeaders() {
		return TablePermutations.headers;
	}
	//--------------------------------------- constructor
	public TablePermutations (final ICallbackInfo<Integer> callbackPermutation) {
		this._callbackPermutation = callbackPermutation;
	}

	 //--------------------------------------- public methods
	public void populateGrid(final List<PermutationDto> permutationList) {
			
		log.info("populateGrid()");
		this.reinit();
		if (permutationList == null || permutationList.size() == 0) return;
		
		this.resizeRows(permutationList.size());
		
		for (int i = 0; i < permutationList.size(); i++) {
			int row = i;
			final PermutationDto permutationDto = permutationList.get(i);		
			log.fine("permutation: [" + permutationDto.toString() + "]");
				
			int col = 0;
			this.setWidget(row, col++, this.buildRadioButtonPermutation(row));
			final String[] tab = this.buildDeltaPropertiesToTab(permutationDto);
			if (tab == null) {
				this.setWidgetLabel(row, col++, "", false);
			}
			else {
			  this.setWidgetMultiLabel(row, col++, tab, tab, true);
			}
		
		}
		this.applyDataRowStyles();
	}
	private RadioButton buildRadioButtonPermutation(int permutationNumber) {
		final RadioButtonPermutation radioButton = new RadioButtonPermutation(permutationNumber);
		radioButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if (_callbackPermutation != null) {
					_callbackPermutation.onHave(radioButton._permutationNumber);
				}
			}
		});
		return radioButton;
	}
	private String[] buildDeltaPropertiesToTab(final PermutationDto permutation) {
		if (!permutation.hasDeltaProperties()) return null;
		
		final String[] tabProperties = new String[permutation.getListDeltaProperties().size()];
		for (int i = 0; i < tabProperties.length; i++) {
			PropertyLightDto property = permutation.getListDeltaProperties().get(i);
			tabProperties[i] = property.getKey() + "=" + property.getValues();
		}
		return tabProperties;
	}
	
	private class RadioButtonPermutation extends RadioButton {

		private final int _permutationNumber;
		public RadioButtonPermutation(final int permutationNumber) {
			super(GROUP_PERMUTATION, "permutation " + permutationNumber);
			this._permutationNumber = permutationNumber;
			if (permutationNumber == 0) {
				this.setValue(true);
			}
		}
		
	}
}
