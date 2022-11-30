package org.helmo.gbeditor.presenters;

import org.helmo.gbeditor.models.Author;
import org.helmo.gbeditor.models.Book;
import org.helmo.gbeditor.models.BookDataFields;
import org.helmo.gbeditor.presenters.interfaces.BookDetailsViewInterface;
import org.helmo.gbeditor.presenters.interfaces.GBEInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BookDetailsPresenterTest {

	private BookDetailsViewInterface view;
	private BookDetailsPresenter presenter;
	private GBEInterface engine;
	private Book book;
	private MainPresenter mainPresenter;

	@BeforeEach
	void setUp() {
		engine = mock(GBEInterface.class);
		view = mock(BookDetailsViewInterface.class);
		mainPresenter = mock(MainPresenter.class);
		presenter = new BookDetailsPresenter(engine);
		presenter.setView(view);
		presenter.setMainPresenter(mainPresenter);
		book = new Book("title", new Author("name", "firstName"), "summary");
	}

	@Test
	void getView() {
		assertEquals(view, presenter.getView());
	}

	@Test
	void setView() {
		BookDetailsViewInterface view2 = mock(BookDetailsViewInterface.class);
		presenter.setView(view2);
		assertEquals(view2, presenter.getView());
	}

	@Test
	void displayBookSetsAllBookDetailsInTheView() {
		presenter.displayBook(book);
		verify(view, atMostOnce()).setTitle(book.getMetadata(BookDataFields.TITLE));
		verify(view, atMostOnce()).setSummary(book.getMetadata(BookDataFields.SUMMARY));
		verify(view, atMostOnce()).setIsbn(book.getMetadata(BookDataFields.BOOK_ISBN));
		verify(view, atMostOnce()).setImagePath(book.getAuthor().getFullName());
		verify(view, atMostOnce()).setBookPages(any());
		verify(view, atMostOnce()).displayBook();
	}

	@Test
	void displayBookSetsAllBookDetailsInTheViewWithNullBookOrDoesNotSetThem() {
		presenter.displayBook(null);
		verify(view, atMostOnce()).setTitle("");
		verify(view, atMostOnce()).setSummary("");
		verify(view, atMostOnce()).setIsbn("");
		verify(view, atMostOnce()).setImagePath("");
		verify(view, atMostOnce()).setBookPages(any());
		verify(view, atMostOnce()).displayBook();
	}

	@Test
	void closeViewClosesView() {
		presenter.closeView();
		verify(view, atMostOnce()).close();
	}

	@Test
	void closeViewClosesViewAndCallsMainPresenter() {
		presenter.closeView();
		verify(view, times(1)).close();
		verify(mainPresenter, times(1)).bookDetailsClosed();
	}

	@Test
	void deleteBookWorksWithNormalBook() {
		presenter.displayBook(book);
		presenter.deleteBook();
		verify(engine, times(1)).deleteBook(book);
		verify(view, times(1)).close();
	}

	@Test
	void deleteBookDoesNotWorkWithNullBook() {
		presenter.displayBook(null);
		presenter.deleteBook();
		verify(engine, never()).deleteBook(book);
		verify(view, never()).close();
	}

	@Test
	void editBook() {
	}

	@Test
	void addPage() {
	}

	@Test
	void removePage() {
	}

	@Test
	void editPage() {
	}

	@Test
	void updatePage() {
	}

	@Test
	void getPageNumber() {
	}

	@Test
	void askTitle() {
	}

	@Test
	void askImagePath() {
	}

	@Test
	void askIsbn() {
	}

	@Test
	void askSummary() {
	}

	@Test
	void confirmPageDeletion() {
	}

	@Test
	void askPages() {
	}

	@Test
	void getPageById() {
	}

	@Test
	void setMainPresenter() {
	}

	@Test
	void setBaseView() {
	}

	@Test
	void getBaseView() {
	}
}