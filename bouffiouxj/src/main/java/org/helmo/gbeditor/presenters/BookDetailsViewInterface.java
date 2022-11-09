package org.helmo.gbeditor.presenters;

import org.helmo.gbeditor.models.Book;

public interface BookDetailsViewInterface extends ViewInterface {
	/**
	 * This method is used to display the details of a book
	 */
	void displayBook(Book book);

	void close();
}
