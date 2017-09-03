package fr.tsadeo.app.gwt.modulehelper.client.widget;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import fr.tsadeo.app.gwt.modulehelper.client.util.AppController;
import fr.tsadeo.app.gwt.modulehelper.client.util.AppController.ViewNameEnum;
import fr.tsadeo.app.gwt.modulehelper.client.util.Constantes;
import fr.tsadeo.app.gwt.modulehelper.client.view.ViewParams;
import fr.tsadeo.app.gwt.modulehelper.client.view.ViewParams.UpdateType;
import fr.tsadeo.app.gwt.modulehelper.client.widget.AbstractModelTable.TableFilter;

public abstract class AbstractTable extends Composite {

	public enum TypeTable {byModule, byInterface, byPermutation, byKeyProperty};
	
	private static final String STYLE_ROW_PAIRE = "row2";
	private static final String STYLE_ROW_IMPAIRE = "row1";
	
	protected TypeTable _type;
	private final boolean _filtrable;
	
	private Grid _grid;
	private List<TextBox> _textBoxFilter;
	//------------------------------------------------- constructor
	protected AbstractTable() {
		this(null);
	}
	protected AbstractTable( final boolean filtrable) {
		this(null, filtrable);
	}
	protected AbstractTable(final TypeTable type) {
		this(type, false);
	}
	protected AbstractTable(final TypeTable type, final boolean filtrable) {
		this._type = type;
		this._filtrable = filtrable;
		this.initWidget(this.buildTable(true));
	}
	//------------------------------------------------- public methods
	public void reinit() {
		this._grid.clear();
		this._grid.resizeRows(this.getStaticRowCount());

		this.buildTable(false);
	}
	//------------------------------------------------- protected methods
	protected abstract Header[] getHeaders();	
	protected abstract Logger getLog();
	
	protected void clearFilter() {
		if (this._textBoxFilter != null) {
		for (TextBox textBox : this._textBoxFilter) {
			textBox.setText(null);
		}
	}
	}
	protected boolean isFiltrable() {
		return this._filtrable;
	}
	protected void resizeRows (final int itemNumber) {
		final int totalRows = itemNumber + this.getStaticRowCount();
		this.getLog().config("resizeRows : " + totalRows);
		this._grid.resizeRows(totalRows);
	}
	/**
	 * 
	 * @param row : relative row
	 * @param col
	 * @return
	 */
	protected Widget getWidget(int row, int col) {
		return this._grid.getWidget(this.getAbsoluteRow(row), col);
	}
	protected void applyDataRowStyles() {
		    HTMLTable.RowFormatter rf = this._grid.getRowFormatter();
		    
		    for (int row = this.getStaticRowCount(); row < this._grid.getRowCount(); ++row) {
		      if ((row % 2) != 0) {
		        rf.addStyleName(row, STYLE_ROW_IMPAIRE);
		      }
		      else {
		        rf.addStyleName(row, STYLE_ROW_PAIRE);
		      }
		    }
	 }
	/**
	 * 
	 * @param row : relative row
	 * @param col
	 * @param text
	 * @param html
	 * @param styleName
	 */
	protected void setWidgetLabel(final int row, final int col, final String text, final boolean html,
			final String styleName) {
		
		this.buildWidgetLabel(row, col, text, html, styleName);
	}
	
	protected void setWidgetLabelLien (final int row, final int col, final String text, final boolean html,
			final String styleName, final ViewNameEnum viewName,  final int dtoId) {
		
		final Label label = this.buildWidgetLabel(row, col, text, html, styleName);
		label.addStyleName(Constantes.STYLE_CURSOR_LIEN);
		label.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				AppController.getInstance().displayView(viewName, new ViewParams(dtoId, UpdateType.ENTITY));
				
			}
		});
		
	}
	private Label buildWidgetLabel(final int row, final int col, final String text, final boolean html,
			final String styleName) {
		
		final Label label = this.buildLabel(text, html, styleName, this.getHeaders()[col].getWidthPx(), null);		
		this._grid.setWidget(this.getAbsoluteRow(row), col, label);
		return label;
	}
	
	
	/**
	 * 
	 * @param row : relative row
	 * @param col
	 * @param tabText
	 * @param tabTitles
	 * @param html
	 */
	protected void setWidgetMultiLabel(final int row, final int col, final  String[] tabText, final String[] tabTitles, final boolean html) {
		
		this.setWidget(row, col,
				this.buildVerticalLabels(tabText, tabTitles, null, html,
						this.getWidthByCol(col), null));
	}
	protected String getWidthByCol(final int col) {
		return this.getHeaders()[col].getWidthPx();
	}
	/***
	 * To override for filtrable table
	 * @param filter
	 */
	protected void applyFilter(final TableFilter filter) {
		// TODO
	}
	
/**
 * 
 * @param row : relative row
 * @param col
 * @param widget
 */
	protected void setWidget(final int row, final int col, final Widget widget) {
		this._grid.setWidget(this.getAbsoluteRow(row), col, widget);
	}
	protected Panel buildVerticalLabels(final  String[] tabText, final String[] tabTitles, 
			                            final String[] tabStyleNames,
			                            final boolean html, final String width,
			                            final ClickHandler clickHandler) {
		
		final boolean hasSpecifiedTitle = (tabTitles != null && tabTitles.length == tabText.length);
		final boolean hasSpecifiedClassname = (tabStyleNames != null && tabStyleNames.length == tabText.length);
		
		final VerticalPanel vPanel = new VerticalPanel();
		for (int i = 0; i < tabText.length; i++) {
			final String text = tabText[i];
			final String title = (hasSpecifiedTitle)?tabTitles[i]:text;
			final String classname = (hasSpecifiedClassname)?tabStyleNames[i]:null;
			final Label label = this.buildLabel(text, html, classname, width, title);
			if (clickHandler != null) {
				label.addClickHandler(clickHandler);
			}
			vPanel.add(label);
		}
		return vPanel;
	}
	/**
	 * 
	 * @param row : relative row
	 * @param col
	 * @param text
	 * @param html
	 */
	protected void setWidgetLabel(final int row, final int col, final String text, final boolean html) {
		
		this.setWidgetLabel(row, col, text, html, null);
	}

	//--------------------------------------------- private methods
	private void applyFilter() {
		this.getLog().config("applyFilter()");
		final int headerSize = this.getHeaders().length;
		
		// build the filter
		TableFilter tableFilter = new TableFilter(headerSize);
		for (int i = 0; i <headerSize; i++) {
			final TextBox textBox = this._textBoxFilter.get(i);
			tableFilter.putValue(this.getHeaders()[i]._title, textBox.getText().trim());
		}
		
		// apply the filter
		this.getLog().config("filters : " + tableFilter.toString());
		this.applyFilter(tableFilter);
	}
	/**
	 * Numéro absolu de la ligne en tenant compte des lignes statiques
	 */
	private int getAbsoluteRow (int relativeRow) {
		return relativeRow  + this.getStaticRowCount();
	}
	/**
	 * Nombre de lignes statques
	 * - 1 ligne de header
	 * - 1 ligne de filter (facultatif)
	 * @return
	 */
	private int getStaticRowCount() {
		return 1 + (this.isFiltrable()?1:0);
	}
    private Label buildLabel (final String text, final boolean html,
			final String styleName, final String width, final String title) {
    	
    	final Label label = (html)? new HTML(text):new Label(text);
		label.addStyleName(Constantes.STYLE_TABLE_TEXT);
		if (title != null) {
	     	label.setTitle(title);
		}
		if (styleName != null) {
			label.addStyleName(styleName);
		}
		if (width != null) {
			label.setWidth(width);
		}
		return label;
    }

	private Widget buildTable(boolean create) {

		this.getLog().config("buildTable() - create: " + create);
		if (create) {
			this._grid = new Grid(this.getStaticRowCount(), this.getHeaders().length);
			this._grid.setStyleName(Constantes.STYLE_TABLE);
		}
        this.buildHeader();
        this.buidFilter(create);

		return this._grid;
	}

	private void buildHeader() {
		
		this.getLog().config("buildHeader()");
		final Header[] myHeaders = this.getHeaders();
		for (int i = 0; i < myHeaders.length; i++) {
			final Label header = new Label(myHeaders[i]._title);
			header.addStyleName(Constantes.STYLE_HEADER);
			final String width = myHeaders[i].getWidthPx();
			if (width != null) {
				this._grid.getColumnFormatter().setWidth(i, width);
			}
			this._grid.setWidget(0, i, header);
		}
	}
	private void buidFilter(boolean create) {
		if (!this.isFiltrable()) {
			return;
		}
		final int headerSize = this.getHeaders().length;
		this.getLog().config("buidFilter() - create: " + create + " headerSize: " + headerSize);

		if (create) {
			
		  final ChangeHandler handler = new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				applyFilter();
			}
		};
			
		  this._textBoxFilter = new ArrayList<TextBox>(headerSize);
		  for (int i = 0; i < headerSize; i++) {
			final TextBox textBox = new TextBox();
			textBox.addStyleName(Constantes.STYLE_TABLE_FILTERBOX);
			this._textBoxFilter.add(textBox);
			
			textBox.addChangeHandler(handler);
			
		  }
		}
		
		for (int i = 0; i < headerSize; i++) {
			final TextBox textBox = this._textBoxFilter.get(i);
			this._grid.setWidget(1, i, textBox);
		}
	}
	
	//=========================================== INNER CLASS
	protected  static final class Header {
		
		private final String _title;
		private final int _width;
		
		String getWidthPx() {
			return (this._width == 0)?null:this._width + "px";
		}
		
		public Header(final String title) {
			this(title, 0);
		}
		public Header(final String title, final int width) {
			this._title = title;
			this._width = width;
		}
	}
}
