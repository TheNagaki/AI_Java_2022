package org.helmo.gbeditor.presenters;

import org.helmo.gbeditor.models.Author;
import org.helmo.gbeditor.models.Book;
import org.helmo.gbeditor.presenters.interfaces.GBEInterface;
import org.helmo.gbeditor.presenters.interfaces.MainViewInterface;
import org.helmo.gbeditor.presenters.viewmodels.BookViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MainPresenterTest {

	private MainViewInterface view;
	private MainPresenter presenter;
	private GBEInterface engine;
	private BookDetailsPresenter detailsPresenter;

	@BeforeEach
	void setUp() {
		view = mock(MainViewInterface.class);
		engine = mock(GBEInterface.class);
		detailsPresenter = mock(BookDetailsPresenter.class);
		presenter = new MainPresenter(engine, detailsPresenter);
		presenter.setView(view);
	}

	@Test
	void askBooksFromAuthorCallsSetBooksFromAuthorInTheView() {
		var books = new LinkedHashSet<Book>();
		var author = new Author("test", "test");
		var bookA = new Book("a", author, "a");
		var bookB = new Book("b", author, "b");
		var bookC = new Book("c", author, "c");
		books.add(bookA);
		books.add(bookB);
		books.add(bookC);
		when(engine.getBooksFromCurrentAuthor()).thenReturn(books);
		presenter.askBooksFromAuthor();
		verify(engine).getBooksFromCurrentAuthor();
		verify(view).setBooksFromAuthor(Set.of(new BookViewModel(bookA), new BookViewModel(bookB), new BookViewModel(bookC)));
	}

	@Test
	void askBooksFromAuthorDoesNotCallViewWhenNullSet() {
		when(engine.getBooksFromCurrentAuthor()).thenReturn(null);
		presenter.askBooksFromAuthor();
		verify(engine).getBooksFromCurrentAuthor();
		verifyNoInteractions(view);
	}

	@Test
	void askBooksFromAuthorDoesNotCallViewWhenEmptySet() {
		when(engine.getBooksFromCurrentAuthor()).thenReturn(new LinkedHashSet<>());
		presenter.askBooksFromAuthor();
		verify(engine).getBooksFromCurrentAuthor();
		verifyNoInteractions(view);
	}

	@Test
	void askAuthorNameCallsSetAuthorNameInTheView() {
		when(engine.getAuthorName()).thenReturn("author");
		presenter.askAuthorName();
		verify(engine).getAuthorName();
		verify(view).setAuthorName(anyString());
	}

	@Test
	void askAuthorNameDoesNotCallViewWhenNull() {
		when(engine.getAuthorName()).thenReturn(null);
		presenter.askAuthorName();
		verify(engine).getAuthorName();
		verifyNoInteractions(view);
	}

	@Test
	void askAuthorNameDoesNotCallViewWhenEmpty() {
		when(engine.getAuthorName()).thenReturn("");
		presenter.askAuthorName();
		verify(engine).getAuthorName();
		verifyNoInteractions(view);
	}

	@Test
	void setViewActuallySetsTheView() {
		var view = mock(MainViewInterface.class);
		presenter.setView(view);
		assertEquals(view, presenter.getView());
	}

	@Test
	void setNullViewThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> presenter.setView(null));
	}

	@Test
	void getViewActuallyReturnsTheView() {
		var view = mock(MainViewInterface.class);
		presenter.setView(view);
		assertEquals(view, presenter.getView());
	}

	@Test
	void getViewReturnsNullWhenNoViewSet() {
		var presenter = new MainPresenter(engine, detailsPresenter);
		assertNull(presenter.getView());
	}

	@Test
	void bookClickedSetsBaseViewOfDetailsPresenterOnFirstBookClicked() {
		presenter.bookClicked(new BookViewModel(new Book("a", new Author("a", "a"), "a")));
		verify(detailsPresenter).setBaseView(view);
	}

	@Test
	void bookClickedDoesNotDisplayBookIfBookNotInEngine() {
		var book = new Book("a", new Author("a", "a"), "a");
		presenter.bookClicked(new BookViewModel(book));
		verify(detailsPresenter, never()).displayBook(any());
	}

	@Test
	void bookClickedDisplaysBookIfBookInEngine() {
		var book = new Book("a", new Author("a", "a"), "a");
		when(engine.getBook(any())).thenReturn(book);
		presenter.bookClicked(new BookViewModel(book));
		verify(detailsPresenter).displayBook(book);
	}

	@Test
	void bookClickedSetsBaseViewOfDetailsPresenterOnlyIfItsBaseViewIsNull() {
		var book = new Book("a", new Author("a", "a"), "a");
		when(engine.getBook(any())).thenReturn(book);
		when(detailsPresenter.getBaseView()).thenReturn(null, view);
		presenter.bookClicked(new BookViewModel(book));
		presenter.bookClicked(new BookViewModel(book));
		verify(detailsPresenter, times(1)).setBaseView(view);
	}

	@Test
	void bookClickedDoesNotSetBaseViewOfDetailsPresenterIfItsBaseViewIsNotNull() {
		var book = new Book("a", new Author("a", "a"), "a");
		when(engine.getBook(any())).thenReturn(book);
		when(detailsPresenter.getBaseView()).thenReturn(view);
		presenter.bookClicked(new BookViewModel(book));
		verify(detailsPresenter, never()).setBaseView(view);
	}

	@Test
	void setBookToEditTellsEngineToSetBookToEdit() {
		var book = new Book("a", new Author("a", "a"), "a");
		presenter.setBookToEdit(book);
		verify(engine).setBookToEdit(book);
	}

	@Test
	void setBookToEditDoesNotThrowExceptionWhenNullBook() {
		assertDoesNotThrow(() -> presenter.setBookToEdit(null));
	}

	@Test
	void onQuit_ClickClosesView() {
		presenter.onQuit_Click();
		verify(view).close();
	}
}