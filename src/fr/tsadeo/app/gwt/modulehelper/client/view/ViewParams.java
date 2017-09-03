package fr.tsadeo.app.gwt.modulehelper.client.view;



/**
 * Paramètres de la vue
 * @author sylvie
 *
 */
public class ViewParams {
	
	public enum UpdateType {ALL, ENTITY, NONE}

	private int _currentId;
	private UpdateType _updadeType = UpdateType.NONE;
	
	public ViewParams(final int id) {
		this._currentId = id;
	} 
	public ViewParams(final int id, UpdateType updateType) {
		this(id);
		this._updadeType = (updateType == null)?UpdateType.NONE:updateType;
	}
	public void setUpdateType(UpdateType updateType){
		this._updadeType = updateType;
	}
	public void setCurrentId(int id) {
		this._currentId = id;
	}
	
	public int getCurrentId() {
		return this._currentId;
	}
	public UpdateType getUpdateType() {
		return this._updadeType;
	}
	
	public boolean isToUpdate() {
		return this._updadeType != null && this._updadeType != UpdateType.NONE;
	}
}
