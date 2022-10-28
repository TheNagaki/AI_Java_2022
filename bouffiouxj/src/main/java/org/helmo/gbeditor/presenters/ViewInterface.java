package org.helmo.gbeditor.presenters;

import javafx.scene.Parent;

/**
 * This interface is used to define the methods that a view must implement
 */
public interface ViewInterface {
	/**
	 * This method returns the root of the view (the main node which will be displayed)
	 *
	 * @return the root of the view
	 */
	Parent getRoot();

	/**
	 * Display a message to the user
	 *
	 * @param response the message to display
	 */
	void display(String response);

	/**
	 * Set the base view of the application
	 *
	 * @param baseView the base view of the application
	 */
	void setBaseView(ViewInterface baseView);

	/**
	 * Display the view corresponding to the viewName
	 *
	 * @param viewName the name of the view to display (see ViewsEnum)
	 */
	void changeView(ViewsEnum viewName);

	/**
	 * Refresh the view to its initial state
	 */
	void refresh();

	/**
	 * Set the name of the author in the view
	 *
	 * @param authorName the full name (firstName Name) of the author
	 */
	void setAuthorName(String authorName);
}
