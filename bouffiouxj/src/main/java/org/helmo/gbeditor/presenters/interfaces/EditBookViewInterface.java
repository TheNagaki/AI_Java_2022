package org.helmo.gbeditor.presenters.interfaces;

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

	/**
	 * Sets the book's information in the view
	 *
	 * @param bookToEdit the book to edit
	 */
	void setBookToEdit(Book bookToEdit);

	/**
	 * Sets the last number of the isbn in the view
	 *
	 * @param controlNumber the last number of the isbn
	 */
	void setIsbnControlNumber(String controlNumber);
}
