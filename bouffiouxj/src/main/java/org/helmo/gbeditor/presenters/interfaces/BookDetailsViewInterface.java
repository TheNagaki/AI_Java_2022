package org.helmo.gbeditor.presenters.interfaces;

import org.helmo.gbeditor.presenters.viewmodels.PageViewModel;

/**
 * This interface is used to define the methods that the view must implement to interact with the presenter
 */
public interface BookDetailsViewInterface extends ViewInterface, DisplayBookViewInterface {
	/**
	 * This method is used to display the details of a book
	 */
	void displayBook();

	/**
	 * This method is used to display the menu to edit a Page
	 *
	 * @param selectedItem the page to edit
	 */
	void editPage(PageViewModel selectedItem);

	/**
	 * This method is used to display a popup to confirm the deletion of a page
	 *
	 * @param selectedPage the page to delete
	 */
	void confirmPageSuppression(PageViewModel selectedPage);
}
