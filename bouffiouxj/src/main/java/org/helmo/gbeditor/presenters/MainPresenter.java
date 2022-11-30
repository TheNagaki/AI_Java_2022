package org.helmo.gbeditor.presenters;

import org.helmo.gbeditor.models.Book;
import org.helmo.gbeditor.presenters.interfaces.GBEInterface;
import org.helmo.gbeditor.presenters.interfaces.MainViewInterface;
import org.helmo.gbeditor.presenters.interfaces.PresenterInterface;
import org.helmo.gbeditor.presenters.interfaces.ViewInterface;
import org.helmo.gbeditor.presenters.viewmodels.BookViewModel;

import java.util.HashSet;
import java.util.Set;

/**
 * The MainPresenter is the presenter of the main view
 * It is used to manage the books of one author
 */
public class MainPresenter implements PresenterInterface {
	private final GBEInterface engine;
	private MainViewInterface view;
	private Book bookShown = null;
	private final BookDetailsPresenter detailsPresenter;
	private boolean bookDetailsOpened = false;

	/**
	 * This constructor is used to create a new MainPresenter with the given engine
	 *
	 * @param gbEditor the logic of the application
	 */
	public MainPresenter(GBEInterface gbEditor, BookDetailsPresenter detailsPresenter) {
		this.engine = gbEditor;
		this.detailsPresenter = detailsPresenter;
		detailsPresenter.setMainPresenter(this);
	}

	/**
	 * This method is used to get all the books created by the author
	 */
	public void askBooksFromAuthor() {
		var booksFromAuthor = engine.getBooksFromCurrentAuthor();
		if (booksFromAuthor != null && !booksFromAuthor.isEmpty()) {
			Set<BookViewModel> books = new HashSet<>();
			for (Book b : booksFromAuthor) {
				books.add(new BookViewModel(b));
			}
			view.setBooksFromAuthor(books);
		}
	}

	/**
	 * This method is used by the view to get the current author name
	 */
	public void askAuthorName() {
		var name = engine.getAuthorName();
		if (name != null && !name.isBlank()) {
			view.setAuthorName(name);
		}
	}

	/**
	 * This method is used to set the view of the presenter
	 *
	 * @param view the view to set
	 */
	public void setView(MainViewInterface view) {
		this.view = view;
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
	public void bookClicked(BookViewModel book) {
		setBaseViewOfDetailsPresenter();
		if (!bookDetailsOpened || !bookShown.equals(book.toBook())) {
			bookShown = engine.getBook(book.getIsbn());
			if (bookShown != null) {
				detailsPresenter.displayBook(bookShown);
				bookDetailsOpened = true;
			}
		}
	}

	private void setBaseViewOfDetailsPresenter() {
		if (detailsPresenter.getBaseView() == null || detailsPresenter.getBaseView() != view) {
			detailsPresenter.setBaseView(view);
		}
	}

	/**
	 * This method is used to know when the book details view is closed
	 */
	public void bookDetailsClosed() {
		this.bookDetailsOpened = false;
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
