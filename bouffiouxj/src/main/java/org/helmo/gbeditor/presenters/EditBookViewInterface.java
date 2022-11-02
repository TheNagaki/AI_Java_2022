package org.helmo.gbeditor.presenters;

import org.helmo.gbeditor.models.Book;

/**
 * Interface for the CreateBookView
 */
public interface EditBookViewInterface extends ViewInterface {

	/**
	 * Sets the first caracters of the isbn TextField in the view
	 *
	 * @param isbn the first caracters of the isbn [linguisticGroup, author's id]
	 */
	void presetISBN(int[] isbn);

	void setBookToEdit(Book bookToEdit);
}
