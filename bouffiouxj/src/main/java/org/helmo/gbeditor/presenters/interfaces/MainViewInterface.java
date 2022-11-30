package org.helmo.gbeditor.presenters.interfaces;

import org.helmo.gbeditor.presenters.viewmodels.BookViewModel;

import java.util.Set;

/**
 * This interface is used to define the methods that the view must implement to interact with the presenter.
 * It is used to display the books of one author.
 */
public interface MainViewInterface extends ViewInterface {

	/**
	 * This method is used to display the list of books of the current author
	 */
	void setBooksFromAuthor(Set<BookViewModel> books);
	/**
	 * Set the name of the author in the view
	 *
	 * @param authorName the full name (firstName Name) of the author
	 */
	void setAuthorName(String authorName);
}
