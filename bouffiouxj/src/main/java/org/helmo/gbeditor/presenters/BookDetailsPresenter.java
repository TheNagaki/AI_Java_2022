package org.helmo.gbeditor.presenters;

import org.helmo.gbeditor.models.Book;
import org.helmo.gbeditor.models.BookDataFields;
import org.helmo.gbeditor.presenters.interfaces.BookDetailsViewInterface;
import org.helmo.gbeditor.presenters.interfaces.PresenterInterface;
import org.helmo.gbeditor.presenters.interfaces.ViewInterface;
import org.helmo.gbeditor.presenters.interfaces.MainViewInterface;
import org.helmo.gbeditor.presenters.viewmodels.BookViewModel;
import org.helmo.gbeditor.presenters.viewmodels.PageViewModel;
import org.helmo.gbeditor.repositories.RepositoryInterface;

import java.util.LinkedHashSet;

/**
 * BookDetailsPresenter is the presenter for the book details view.
 * It is used to display the details of a book.
 */
public class BookDetailsPresenter implements PresenterInterface {
	private final RepositoryInterface repo;
	private MainPresenter mainPresenter;
	private BookDetailsViewInterface view;
	private Book bookDisplayed;
	private MainViewInterface baseView;

	/**
	 * Constructor of the presenter with the engine and the main presenter
	 *
	 * @param repo the logic of the application
	 */
	public BookDetailsPresenter(RepositoryInterface repo) {
		this.repo = repo;
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
	}

	/**
	 * This method is used to display the details of a book
	 *
	 * @param book the book to display
	 */
	public void displayBook(Book book) {
		if (book != null) {
			this.bookDisplayed = book;
			askIsbn();
			askTitle();
			askSummary();
			askImagePath();
			askPages();
			view.displayBook();
		}
	}

	/**
	 * This method is used to close the view
	 */
	public void closeView() {
		mainPresenter.bookDetailsClosed();
		view.close();
	}

	/**
	 * This method is used to delete a book from the current author
	 */
	public void deleteBook() {
		if (bookDisplayed != null) {
			if (repo.deleteBook(bookDisplayed)) {
				view.changeView(ViewsEnum.MAIN);
			} else {
				view.display("Erreur: Impossible de supprimer le livre");
			}
			closeView();
		}
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
		mainPresenter.addPage(bookDisplayed, text);
		askPages();
		displayBook(bookDisplayed);
	}

	/**
	 * This method is used to delete a page from the current book
	 *
	 * @param selectedPage the page to delete
	 */
	public void removePage(PageViewModel selectedPage) {
		if (bookDisplayed.hasChoicesTo(selectedPage.toPage())) {
			view.confirmPageSuppression(selectedPage);
		} else {
			confirmPageDeletion(selectedPage);
		}
	}

	/**
	 * This method is used to tell the view to display the popup to edit a Page
	 *
	 * @param selectedItem the page to edit
	 */
	public void editPage(PageViewModel selectedItem) {
		view.editPage(selectedItem);
	}

	/**
	 * This method is used to save a Page's state after modification.
	 *
	 * @param selected the page to save
	 */
	public void updatePage(PageViewModel selected) {
		mainPresenter.updatePage(bookDisplayed, selected.toPage());
		view.refresh();
	}

	/**
	 * This method is used to ask the engine the number of a page
	 *
	 * @param selectedPage the page to get the number
	 * @return the number of the page
	 */
	public int getPageNumber(PageViewModel selectedPage) {
		return bookDisplayed.getPageNumber(selectedPage.toPage());
	}

	/**
	 * This method is used to ask the engine the title of the book
	 */
	public void askTitle() {
		view.setTitle(bookDisplayed.getMetadata(BookDataFields.TITLE));
	}

	/**
	 * This method is used to ask the engine the path to the image of the book
	 */
	public void askImagePath() {
		view.setImagePath(bookDisplayed.getMetadata(BookDataFields.IMAGE_PATH));
	}

	/**
	 * This method is used to ask the engine the isbn of the book
	 */
	public void askIsbn() {
		view.setIsbn(bookDisplayed.getMetadata(BookDataFields.BOOK_ISBN));
	}

	/**
	 * This method is used to communicate the Summary of the book in the view
	 */
	public void askSummary() {
		view.setSummary(bookDisplayed.getMetadata(BookDataFields.SUMMARY));
	}

	/**
	 * This method is used to confirm the deletion of a page
	 *
	 * @param selectedPage the page to delete
	 */
	public void confirmPageDeletion(PageViewModel selectedPage) {
		var books = repo.getBooks();
		for (Book book : books) {
			book.removePage(selectedPage.toPage());
		}
		mainPresenter.bookClicked(new BookViewModel(bookDisplayed));
		view.refresh();
	}

	/**
	 * This method is used to give the list of pages to the view
	 */
	public void askPages() {
		view.setBookPages(bookDisplayed.getPages().stream().map(PageViewModel::new).collect(LinkedHashSet::new, LinkedHashSet::add, LinkedHashSet::addAll));
	}

	/**
	 * This method is used to get a Page from the engine by its identifier
	 *
	 * @param id the identifier of the page
	 * @return the page
	 */
	public PageViewModel getPageById(String id) {
		return new PageViewModel(bookDisplayed.getPageById(id));
	}

	/**
	 * This method is used to set the main presenter of this presenter
	 *
	 * @param mainPresenter the main presenter
	 */
	public void setMainPresenter(MainPresenter mainPresenter) {
		this.mainPresenter = mainPresenter;
	}

	/**
	 * This method is used to set the Base view of this presenter's view
	 *
	 * @param view the view to set
	 */
	public void setBaseView(MainViewInterface view) {
		this.baseView = view;
		this.view.setBaseView(view);
	}

	/**
	 * Gets the base view associated to this presenter
	 *
	 * @return the base view
	 */
	public MainViewInterface getBaseView() {
		return baseView;
	}
}
