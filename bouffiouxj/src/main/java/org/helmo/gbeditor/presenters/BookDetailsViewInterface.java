package org.helmo.gbeditor.presenters;

import org.helmo.gbeditor.models.Book;

/**
 * This interface is used to define the methods that the view must implement to interact with the presenter
 */
public interface BookDetailsViewInterface extends ViewInterface {
	/**
	 * This method is used to display the details of a book
	 */
	void displayBook(Book book);
}
