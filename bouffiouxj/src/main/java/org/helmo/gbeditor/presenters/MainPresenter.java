package org.helmo.gbeditor.presenters;

import org.helmo.gbeditor.models.Book;

import java.util.HashSet;
import java.util.Set;

/**
 * The MainPresenter is the presenter of the main view
 * It is used to manage the books of one author
 */
public class MainPresenter implements Presenter {
	private final GBEInterface engine;
	private ViewInterface view;

	/**
	 * This constructor is used to create a new MainPresenter with the given engine
	 * @param gbEditor the logic of the application
	 */
	public MainPresenter(GBEInterface gbEditor) {
		this.engine = gbEditor;
	}

	/**
	 * This method is used to get all the books created by the author
	 * @return a set of books
	 */
	public Set<Book> getBooksFromAuthor() {
		return engine.getBooksFromCurrentAuthor() != null ? engine.getBooksFromCurrentAuthor() : new HashSet<>();
	}

	/**
	 * This method is used by the view to get the current author name
	 */
	public void askAuthorName() {
		view.setAuthorName(engine.getAuthorName());
	}

	public void setView(ViewInterface view) {
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
	 * This method is used to delete a book from the current author
	 *
	 * @param b the book to delete
	 * @return true if the book has been deleted, false otherwise
	 */
	public boolean deleteBook(Book b) {
		return engine.deleteBook(b);
	}
}
