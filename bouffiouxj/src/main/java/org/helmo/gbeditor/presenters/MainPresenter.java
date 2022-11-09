package org.helmo.gbeditor.presenters;

import org.helmo.gbeditor.models.Book;
import org.helmo.gbeditor.views.BookDetailsView;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * The MainPresenter is the presenter of the main view
 * It is used to manage the books of one author
 */
public class MainPresenter implements Presenter {
	private final GBEInterface engine;
	private MainViewInterface view;

	private final Set<Book> booksShown = new LinkedHashSet<>();

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
	 * @param b the book to display
	 */
	public void bookClicked(Book b) {
		if (!booksShown.contains(b)) {
			var presenter = new BookDetailsPresenter(engine, this);
			var detailsView = new BookDetailsView(presenter);
			detailsView.setBaseView(view);
			presenter.displayBook(b);
			booksShown.add(b);
		}
	}

	/**
	 * This method is used to know when the book details view is closed
	 *
	 * @param b the book to remove from the list of books shown
	 */
	public void BookDetailsClosed(Book b) {
		booksShown.remove(b);
	}

	/**
	 * This method is used to create a new book or to edit an existing one
	 *
	 * @param book the book to edit, null if the user wants to create a new book
	 */
	public void setBookToEdit(Book book) {
		engine.setBookToEdit(book);
	}

	public void quitBtnClicked() {
		view.close();
	}
}
