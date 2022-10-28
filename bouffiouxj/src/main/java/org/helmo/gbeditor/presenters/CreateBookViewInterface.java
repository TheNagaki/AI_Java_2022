package org.helmo.gbeditor.presenters;

/**
 * Interface for the CreateBookView
 */
public interface CreateBookViewInterface extends ViewInterface {

	/**
	 * Sets the first caracters of the isbn TextField in the view
	 *
	 * @param isbn the first caracters of the isbn [linguisticGroup, author's id]
	 */
	void presetISBN(int[] isbn);
}
