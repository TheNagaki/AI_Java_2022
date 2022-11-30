package org.helmo.gbeditor.presenters.interfaces;

public interface DisplayBookViewInterface {

	/**
	 * This method is used to set the summary of the book in the view
	 *
	 * @param metadata the summary of the book
	 */
	void setSummary(String metadata);

	/**
	 * This method is used to set the path to the image of the book in the view
	 *
	 * @param metadata the path to the image of the book
	 */
	void setImagePath(String metadata);

	/**
	 * This method is used to set the title of the book in the view
	 *
	 * @param metadata the title of the book
	 */
	void setTitle(String metadata);
}
