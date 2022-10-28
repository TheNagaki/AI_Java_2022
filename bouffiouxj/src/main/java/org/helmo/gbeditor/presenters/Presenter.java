package org.helmo.gbeditor.presenters;

/**
 * This interface is used to define the methods that a presenter should have
 */
public interface Presenter {
	/**
	 * This method is used to get the Logic of the presenter
	 */
	GBEInterface getEngine();

	/**
	 * This method is used to get the view corresponding to the presenter
	 */
	ViewInterface getView();
}
