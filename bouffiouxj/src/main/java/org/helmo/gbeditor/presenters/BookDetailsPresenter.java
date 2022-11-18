package org.helmo.gbeditor.presenters;

import org.helmo.gbeditor.models.Book;
import org.helmo.gbeditor.models.Page;
import org.helmo.gbeditor.presenters.interfaces.BookDetailsViewInterface;
import org.helmo.gbeditor.presenters.interfaces.GBEInterface;
import org.helmo.gbeditor.presenters.interfaces.Presenter;
import org.helmo.gbeditor.presenters.interfaces.ViewInterface;

/**
 * BookDetailsPresenter is the presenter for the book details view.
 * It is used to display the details of a book.
 */
public class BookDetailsPresenter implements Presenter {
	private final MainPresenter mainPresenter;
	private BookDetailsViewInterface view;
	private final GBEInterface engine;
	private Book bookDisplayed;

	/**
	 * Constructor of the presenter with the engine and the main presenter
	 *
	 * @param engine        the logic of the application
	 * @param mainPresenter the main presenter of the application
	 */
	public BookDetailsPresenter(GBEInterface engine, MainPresenter mainPresenter) {
		this.engine = engine;
		this.mainPresenter = mainPresenter;
	}

	@Override
	public GBEInterface getEngine() {
		return this.engine;
	}

	@Override
	public ViewInterface getView() {
		return this.view;
	}

	/**
	 * This method is used to set the view of the presenter
	 *
	 * @param view the view to set
	 */
	public void setView(BookDetailsViewInterface view) {
		this.view = view;
		view.getStage().setOnCloseRequest(event -> closeView());
	}

	/**
	 * This method is used to display the details of a book
	 *
	 * @param book the book to display
	 */
	public void displayBook(Book book) {
		this.bookDisplayed = book;
		view.displayBook(book);
	}

	/**
	 * This method is used to close the view
	 */
	public void closeView() {
		mainPresenter.BookDetailsClosed();
		view.close();
	}

	/**
	 * This method is used to delete a book from the current author
	 *
	 * @return true if the book has been deleted, false otherwise
	 */
	public boolean deleteBook() {
		return engine.deleteBook(bookDisplayed);
	}


	/**
	 * This method is used to tell the MainPresenter to create a new book or edit an existing one
	 */
	public void editBook() {
		mainPresenter.setBookToEdit(bookDisplayed);
		view.changeView(ViewsEnum.EDIT_BOOK);
		closeView();
	}

	/**
	 * This method is used to tell the MainPresenter to create a new page or edit an existing one
	 *
	 * @param text the text of the page
	 */
	public void addPage(String text) {
		engine.addPage(bookDisplayed, text);
		view.refresh();
	}

	/**
	 * This method is used to delete a page from the current book
	 *
	 * @param selectedPage the page to delete
	 */
	public void removePage(Page selectedPage) {
		engine.removePage(bookDisplayed, selectedPage);
		view.refresh();
	}

//	public void movePageUp(Page selectedPage) {
//		engine.movePageUp(bookDisplayed, selectedPage);
//		view.refresh();
//	}
//
//	public void movePageDown(Page selectedPage) {
//		engine.movePageDown(bookDisplayed, selectedPage);
//		view.refresh();
//	}
}
