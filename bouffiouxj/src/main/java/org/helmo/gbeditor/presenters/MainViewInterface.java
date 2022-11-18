package org.helmo.gbeditor.presenters;

import org.helmo.gbeditor.models.Book;

import java.util.Set;

/**
 * This interface is used to define the methods that the view must implement to interact with the presenter.
 * It is used to display the books of one author.
 */
public interface MainViewInterface extends ViewInterface {

	/**
	 * This method is used to display the list of books of the current author
	 */
	void setBooksFromAuthor(Set<Book> books);
}
