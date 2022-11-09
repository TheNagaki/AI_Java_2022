package org.helmo.gbeditor.presenters;

import org.helmo.gbeditor.models.Book;

import java.util.Set;

public interface MainViewInterface extends ViewInterface {

	/**
	 * This method is used to display the list of books of the current author
	 */
	void setBooksFromAuthor(Set<Book> books);
}
