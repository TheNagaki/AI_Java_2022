package org.helmo.gbeditor.presenters;

import org.helmo.gbeditor.models.Book;

public interface MainViewInterface extends ViewInterface {
	/**
	 * This method is used to display the details of a book
	 */
	void displayBookDetails(Book b);
}
