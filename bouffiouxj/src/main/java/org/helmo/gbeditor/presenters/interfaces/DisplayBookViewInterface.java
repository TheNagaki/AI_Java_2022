package org.helmo.gbeditor.presenters.interfaces;

import org.helmo.gbeditor.presenters.viewmodels.BookViewModel;

public interface DisplayBookViewInterface {

	/**
	 * This method is used to set the book to display in the view
	 *
	 * @param bookToDisplay the book to display
	 */
	void setBookToDisplay(BookViewModel bookToDisplay);
}
