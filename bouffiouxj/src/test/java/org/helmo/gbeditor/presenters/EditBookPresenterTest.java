package org.helmo.gbeditor.presenters;

import org.helmo.gbeditor.models.Author;
import org.helmo.gbeditor.models.Book;
import org.helmo.gbeditor.presenters.interfaces.EditBookViewInterface;
import org.helmo.gbeditor.presenters.interfaces.GBEInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EditBookPresenterTest {
	private EditBookPresenter presenter;
	private GBEInterface engine;
	private EditBookViewInterface view;

	@BeforeEach
	void setUp() {
		engine = mock(GBEInterface.class);
		presenter = new EditBookPresenter(engine);
		view = mock(EditBookViewInterface.class);
		presenter.setView(view);
	}

	@Test
	void createBookDisplaysTheResultOfTheEngineToTheView() {
		when(engine.createBook(anyString(), anyString(), anyString(), anyString())).thenReturn("result");
		presenter.createBook("title", "isbn", "summary", "imagePath");
		verify(view, atMostOnce()).display("result");
	}

	@Test
	void createBookSendsTheInformationToTheEngine() {
		presenter.createBook("title", "isbn", "summary", "imagePath");
		verify(engine).createBook("title", "isbn", "summary", "imagePath");
	}

	@Test
	void askAuthorNameCallsTheEngineGetAuthorName() {
		presenter.askAuthorName();
		verify(engine, atMostOnce()).getAuthorName();
	}

	@Test
	void askAuthorNameSetsTheAuthorNameInTheView() {
		when(engine.getAuthorName()).thenReturn("authorName");
		presenter.askAuthorName();
		verify(view, atMostOnce()).setAuthorName("authorName");
	}

	@Test
	void setView() {
		presenter.setView(view);
		assertEquals(view, presenter.getView());
	}

	@Test
	void askISBNCallsPresetISBNFromEngine() {
		presenter.askISBN();
		verify(engine, atMostOnce()).presetISBN();
	}

	@Test
	void askISBNCallsPresetISBNFromEngineAndSetsItsResultInTheView() {
		var arr = new int[2];
		when(engine.presetISBN()).thenReturn(arr);
		presenter.askISBN();
		verify(view, atMostOnce()).presetISBN(arr);
	}

	@Test
	void askBookToEditCallsEngineGetBookToEdit() {
		presenter.askBookToEdit();
		verify(engine, atMostOnce()).getBookToEdit();
	}

	@Test
	void askBookToEditSetsTheEditionModeInTheView() {
		var book = mock(Book.class);
		when(engine.getBookToEdit()).thenReturn(book);
		presenter.askBookToEdit();
		verify(view, atMostOnce()).setEditionMode(true);
	}

	@Test
	void askBookToEditSetsTheEditionModeInTheViewToFalseIfNoBookToEdit() {
		when(engine.getBookToEdit()).thenReturn(null);
		presenter.askBookToEdit();
		verify(view, atMostOnce()).setEditionMode(false);
	}

	@Test
	void editBookCallsEngineUpdateBook() {
		presenter.editBook("title", "summary", "isbn", "imagePath");
		verify(engine, atMostOnce()).updateBook(any(Book.class), eq("title"), eq("summary"), eq("isbn"), eq("imagePath"));
	}

	@Test
	void editBookCallsEngineUpdateBookWithTheBookToEdit() {
		var book = mock(Book.class);
		when(engine.getBookToEdit()).thenReturn(book);
		presenter.editBook("title", "summary", "isbn", "imagePath");
		verify(engine, atMostOnce()).updateBook(eq(book), anyString(), anyString(), anyString(), anyString());
	}

	@Test
	void editBookCallsEngineUpdateBookWithTheBookToEditAndTheGivenInformation() {
		var book = mock(Book.class);
		when(engine.getBookToEdit()).thenReturn(book);
		presenter.editBook("title", "summary", "isbn", "imagePath");
		verify(engine, atMostOnce()).updateBook(eq(book), eq("title"), eq("summary"), eq("isbn"), eq("imagePath"));
	}

	@Test
	void editBookDisplaysTheResultOfTheEngineToTheView() {
		when(engine.updateBook(any(Book.class), anyString(), anyString(), anyString(), anyString())).thenReturn("result");
		presenter.editBook("title", "summary", "isbn", "imagePath");
		verify(view, atMostOnce()).display("result");
	}

	@Test
	void askIsbnControlNumberCallsEngine() {
		presenter.askIsbnControlNumber("isbn");
		verify(engine, atMostOnce()).getIsbnControlNum(anyString());
	}

	@Test
	void askIsbnControlNumberCallsEngineWithTheGivenIsbn() {
		presenter.askIsbnControlNumber("isbn");
		verify(engine, atMostOnce()).getIsbnControlNum(eq("isbn"));
	}

	@Test
	void askIsbnControlNumberSetsTheResultInTheView() {
		when(engine.getIsbnControlNum(anyString())).thenReturn("result");
		presenter.askIsbnControlNumber("isbn");
		verify(view, atMostOnce()).setIsbnControlNumber("result");
	}

	@Test
	void onQuit_ClickedClosesView() {
		presenter.onQuit_Clicked();
		verify(view, atMostOnce()).close();
	}

	@Test
	void askTitleSetsTheBookToEditSTitleInTheView() {
		when(engine.getBookToEdit()).thenReturn(new Book("title", new Author("a", "a"), "summary"));
		presenter.askBookToEdit();
		presenter.askTitle();
		verify(view, atMostOnce()).setTitle("title");
	}

	@Test
	void askImagePathSetsTheBookToEditSImagePathInTheView() {
		when(engine.getBookToEdit()).thenReturn(new Book("title", new Author("a", "a"), "summary"));
		presenter.askBookToEdit();
		presenter.askImagePath();
		verify(view, atMostOnce()).setImagePath("");
	}

	@Test
	void askSummarySetsTheBookToEditSSummaryInTheView() {
		when(engine.getBookToEdit()).thenReturn(new Book("title", new Author("a", "a"), "summary"));
		presenter.askBookToEdit();
		presenter.askSummary();
		verify(view, atMostOnce()).setSummary("summary");
	}

	@Test
	void askBaseIsbnSetsTheBookToEditSBaseIsbnInTheView() {
		var book = new Book("title", new Author("a", "a"), "summary");
		when(engine.getBookToEdit()).thenReturn(book);
		presenter.askBookToEdit();
		presenter.askBaseIsbn();
		var isbn = book.getIsbn();
		verify(view, atMostOnce()).setBaseIsbn(String.format("%s-%s-", isbn.getLinguisticGroup(), isbn.getIdAuthor()));
	}

	@Test
	void askBookId() {
		var book = new Book("title", new Author("a", "a"), "summary");
		when(engine.getBookToEdit()).thenReturn(book);
		presenter.askBookToEdit();
		presenter.askBookId();
		verify(view, atMostOnce()).setBookId(book.getIsbn().getIdBook());
	}
}