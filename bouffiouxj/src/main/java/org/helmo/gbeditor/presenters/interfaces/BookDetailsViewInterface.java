package org.helmo.gbeditor.presenters.interfaces;

import org.helmo.gbeditor.models.Book;
import org.helmo.gbeditor.models.Page;

/**
 * This interface is used to define the methods that the view must implement to interact with the presenter
 */
public interface BookDetailsViewInterface extends ViewInterface {
	/**
	 * This method is used to display the details of a book
	 */
	void displayBook(Book book);

	/**
	 * This method is used to display the menu to edit a Page
	 *
	 * @param selectedItem the page to edit
	 */
	void editPage(Page selectedItem);
}
