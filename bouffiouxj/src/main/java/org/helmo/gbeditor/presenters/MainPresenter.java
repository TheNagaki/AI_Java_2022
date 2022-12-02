package org.helmo.gbeditor.presenters;

import org.helmo.gbeditor.models.Author;
import org.helmo.gbeditor.models.Book;
import org.helmo.gbeditor.presenters.interfaces.MainViewInterface;
import org.helmo.gbeditor.presenters.interfaces.PresenterInterface;
import org.helmo.gbeditor.presenters.interfaces.ViewInterface;
import org.helmo.gbeditor.presenters.viewmodels.BookViewModel;
import org.helmo.gbeditor.repositories.RepositoryInterface;

import java.util.HashSet;
import java.util.Set;

/**
 * The MainPresenter is the presenter of the main view
 * It is used to manage the books of one author
 */
public class MainPresenter implements PresenterInterface {
	private final RepositoryInterface repo;
	private MainViewInterface view;
	private Book bookShown = null;
	private final BookDetailsPresenter detailsPresenter;
	private boolean bookDetailsOpened = false;
	private Author currentAuthor;

	/**
	 * This constructor is used to create a new MainPresenter with the given engine
	 *
	 * @param repo the repository of the application
	 */
	public MainPresenter(RepositoryInterface repo, BookDetailsPresenter detailsPresenter) {
		this.repo = repo;
		this.detailsPresenter = detailsPresenter;
		detailsPresenter.setMainPresenter(this);
	}

	/**
	 * This method is used to get all the books created by the author
	 */
	public void askBooksFromAuthor() {
		loadAuthor();
		Set<Book> books1 = repo.getBooksFromAuthor(currentAuthor);
		if (books1 != null && !books1.isEmpty()) {
			Set<BookViewModel> books = new HashSet<>();
			for (Book b : books1) {
				books.add(new BookViewModel(b));
			}
			view.setBooksFromAuthor(books);
		}
	}

	private void loadAuthor() {
		if (currentAuthor == null) {
			currentAuthor = repo.getCurrentAuthor();
		}
	}

	/**
	 * This method is used by the view to get the current author name
	 */
	public void askAuthorName() {
		loadAuthor();
		if (currentAuthor != null) {
			view.setAuthorName(currentAuthor.getFullName());
		}
	}

	/**
	 * This method is used to set the view of the presenter
	 *
	 * @param view the view to set
	 */
	public void setView(MainViewInterface view) {
		if (view != null) {
			this.view = view;
		}else {
			throw new IllegalArgumentException("The view cannot be null");
		}
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
			bookShown = repo.getBook(book.getIsbn());
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
		repo.setBookToEdit(book);
	}

	/**
	 * This method is used to handle the quit action from the view
	 */
	public void onQuit_Click() {
		view.close();
	}

}
