package org.helmo.gbeditor.presenters.interfaces;

/**
 * Interface for the CreateBookView
 */
public interface EditBookViewInterface extends ViewInterface, DisplayBookViewInterface {

	/**
	 * Sets the first caracters of the isbn TextField in the view
	 *
	 * @param isbn the first caracters of the isbn [linguisticGroup, author's id]
	 */
	void presetISBN(int[] isbn);

	/**
	 * Sets the view in edition mode
	 *
	 * @param b the boolean which indicates if the view is in edition mode
	 */
	void setEditionMode(boolean b);

	/**
	 * Sets the last number of the isbn in the view
	 *
	 * @param controlNumber the last number of the isbn
	 */
	void setIsbnControlNumber(String controlNumber);

	/**
	 * Set the name of the author in the view
	 *
	 * @param authorName the full name (firstName Name) of the author
	 */
	void setAuthorName(String authorName);

	/**
	 * Sets the base isbn in the view
	 *
	 * @param baseIsbn the base isbn
	 */
	void setBaseIsbn(String baseIsbn);

	/**
	 * Sets the id of the book in the view
	 *
	 * @param idBook the id of the book
	 */
	void setBookId(int idBook);
}
