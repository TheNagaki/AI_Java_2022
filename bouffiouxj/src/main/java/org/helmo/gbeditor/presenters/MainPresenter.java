package org.helmo.gbeditor.presenters;

import org.helmo.gbeditor.models.Book;
import org.helmo.gbeditor.views.BookDetailsView;

/**
 * The MainPresenter is the presenter of the main view
 * It is used to manage the books of one author
 */
public class MainPresenter implements Presenter {
	private final GBEInterface engine;
	private MainViewInterface view;

	private Book bookShown = null;

	private BookDetailsPresenter detailsPresenter = null;

	/**
	 * This constructor is used to create a new MainPresenter with the given engine
	 *
	 * @param gbEditor the logic of the application
	 */
	public MainPresenter(GBEInterface gbEditor) {
		this.engine = gbEditor;
	}

	/**
	 * This method is used to get all the books created by the author
	 */
	public void askBooksFromAuthor() {
		if (engine.getBooksFromCurrentAuthor() != null) {
			view.setBooksFromAuthor(engine.getBooksFromCurrentAuthor());
		}
	}

	/**
	 * This method is used by the view to get the current author name
	 */
	public void askAuthorName() {
		view.setAuthorName(engine.getAuthorName());
	}

	public void setView(MainViewInterface view) {
		this.view = view;
	}

	@Override
	public GBEInterface getEngine() {
		return engine;
	}

	@Override
	public ViewInterface getView() {
		return view;
	}

	/**
	 * This method is used to display the details of a book when the user clicks on its thumbnail
	 *
	 * @param book the book to display
	 */
	public void bookClicked(Book book) {
		if (bookShown == null || !bookShown.equals(book)) {
			if (detailsPresenter == null) {
				var presenter = new BookDetailsPresenter(engine, this);
				var detailsView = new BookDetailsView(presenter);
				detailsView.setBaseView(view);
				presenter.displayBook(book);
				detailsPresenter = presenter;
			} else {
				detailsPresenter.displayBook(book);
			}
			bookShown = book;
		}
	}

	/**
	 * This method is used to know when the book details view is closed
	 */
	public void BookDetailsClosed() {
		bookShown = null;
		detailsPresenter = null;
	}

	/**
	 * This method is used to create a new book or to edit an existing one
	 *
	 * @param book the book to edit, null if the user wants to create a new book
	 */
	public void setBookToEdit(Book book) {
		engine.setBookToEdit(book);
	}

	/**
	 * This method is used to handle the quit action from the view
	 */
	public void onQuit_Click() {
		view.close();
	}
}
