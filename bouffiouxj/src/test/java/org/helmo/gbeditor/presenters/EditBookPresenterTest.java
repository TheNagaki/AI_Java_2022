package org.helmo.gbeditor.presenters;

import org.helmo.gbeditor.models.Author;
import org.helmo.gbeditor.models.Book;
import org.helmo.gbeditor.models.BookMetadata;
import org.helmo.gbeditor.models.ISBN;
import org.helmo.gbeditor.presenters.interfaces.EditBookViewInterface;
import org.helmo.gbeditor.presenters.viewmodels.BookViewModel;
import org.helmo.gbeditor.repositories.RepositoryInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EditBookPresenterTest {
	private EditBookPresenter presenter;
	private EditBookViewInterface view;
	private RepositoryInterface repo;

	private Book book;

	@BeforeEach
	void setUp() {
		book = new Book("title", new Author("name", "firstName"), "summary");
		repo = mock(RepositoryInterface.class);
		presenter = new EditBookPresenter(repo);
		view = mock(EditBookViewInterface.class);
		presenter.setView(view);
	}

	@Test
	void createBookDisplaysTheResultOfTheEngineToTheView() {
		presenter.createBook("title", "isbn", "summary", "imagePath");
		verify(view, atMostOnce()).display("result");
	}

	@Test
	void createBookSendsTheInformationToTheEngine() {
		when(repo.getBooks()).thenReturn(new LinkedHashSet<>());
		var author = new Author("name", "surname");
		when(repo.getCurrentAuthor()).thenReturn(author);
		presenter.askAuthorName();
		presenter.createBook("title", "summary", ISBN.createNewISBN(BookMetadata.LINGUISTIC_GROUP, author.getIdentifier()).toString(), "imagePath");
		verify(repo).saveBooks(any());
	}

	@Test
	void askAuthorNameCallsTheEngineGetAuthorName() {
		when(repo.getCurrentAuthor()).thenReturn(new Author("name", "firstName"));
		presenter.askAuthorName();
		verify(repo, atMostOnce()).getCurrentAuthor();
	}

	@Test
	void askAuthorNameSetsTheAuthorNameInTheView() {
		var author = new Author("name", "firstName");
		when(repo.getCurrentAuthor()).thenReturn(author);
		presenter.askAuthorName();
		verify(view, atMostOnce()).setAuthorName(author.getFullName());
	}

	@Test
	void setView() {
		presenter.setView(view);
		assertEquals(view, presenter.getView());
	}

	@Test
	void askISBNCallsPresetISBNFromEngineAndSetsItsResultInTheView() {
		var author = new Author("name", "firstName");
		var arr = new int[]{BookMetadata.LINGUISTIC_GROUP, author.getIdentifier()};
		when(repo.getCurrentAuthor()).thenReturn(author);
		presenter.askISBN();
		verify(view, atMostOnce()).presetISBN(arr);
	}

	@Test
	void askBookToEditCallsEngineGetBookToEdit() {
		when(repo.getBookToEdit()).thenReturn(book);
		presenter.askBookToEdit();
		verify(repo, atMostOnce()).getBookToEdit();
	}

	@Test
	void askBookToEditSetsTheEditionModeInTheView() {
		when(repo.getBookToEdit()).thenReturn(book);
		presenter.askBookToEdit();
		verify(view, atMostOnce()).setEditionMode(true);
	}

	@Test
	void askBookToEditSetsTheEditionModeInTheViewToFalseIfNoBookToEdit() {
		when(repo.getBookToEdit()).thenReturn(null);
		presenter.askBookToEdit();
		verify(view, atMostOnce()).setEditionMode(false);
	}

	@Test
	void editBookCallsEngineUpdateBook() {
		presenter.editBook("title", "summary", "isbn", "imagePath");
		verify(repo, atMostOnce()).deleteBook(any(Book.class));
		verify(repo, atMostOnce()).saveBooks(any());
	}

	@Test
	void editBookCallsEngineUpdateBookWithTheBookToEdit() {
		var book = mock(Book.class);
		when(repo.getBookToEdit()).thenReturn(book);
		presenter.editBook("title", "summary", "isbn", "imagePath");
		verify(repo, atMostOnce()).deleteBook(book);
		verify(repo, atMostOnce()).saveBooks(any());
	}

	@Test
	void editBookCallsEngineUpdateBookWithTheBookToEditAndTheGivenInformation() {
		var book = mock(Book.class);
		when(repo.getBookToEdit()).thenReturn(book);
		presenter.editBook("title", "summary", "isbn", "imagePath");
		verify(repo, atMostOnce()).deleteBook(book);
		verify(repo, atMostOnce()).saveBooks(any());
	}

	@Test
	void editBookDisplaysTheResultOfTheEngineToTheView() {
		presenter.editBook("title", "summary", "isbn", "imagePath");
		verify(view, atMostOnce()).display("Votre livre a bien été mis à jour");
	}

	@Test
	void askIsbnControlNumberCallsEngine() {
		var author = new Author("name", "firstName");
		when(repo.getCurrentAuthor()).thenReturn(author);
		var isbn = ISBN.createNewISBN(BookMetadata.LINGUISTIC_GROUP, author.getIdentifier());
		presenter.askIsbnControlNumber(String.format("%d-%06d-%02d", isbn.getLinguisticGroup(), isbn.getIdAuthor(), isbn.getIdBook()));
		verify(repo, atMostOnce()).getCurrentAuthor();
	}

	@Test
	void askIsbnControlNumberSetsTheResultInTheView() {
		var author = new Author("name", "firstName");
		when(repo.getCurrentAuthor()).thenReturn(author);
		var isbn = ISBN.createNewISBN(BookMetadata.LINGUISTIC_GROUP, author.getIdentifier());
		presenter.askIsbnControlNumber(String.format("%d-%06d-%02d", isbn.getLinguisticGroup(), isbn.getIdAuthor(), isbn.getIdBook()));
		verify(view, atMostOnce()).setIsbnControlNumber(isbn.getCheckSum() + "");
	}

	@Test
	void onQuit_ClickedClosesView() {
		presenter.onQuit_Clicked();
		verify(view, atMostOnce()).close();
	}

	@Test
	void askBookToEditSetsTheBookToEditInTheView() {
		when(repo.getBookToEdit()).thenReturn(book);
		presenter.askBookToEdit();
		verify(view, atMostOnce()).setBookToDisplay(new BookViewModel(book));
	}

	@Test
	void askBaseIsbnSetsTheBookToEditSBaseIsbnInTheView() {
		var book = new Book("title", new Author("a", "a"), "summary");
		when(repo.getBookToEdit()).thenReturn(book);
		presenter.askBookToEdit();
		presenter.askBaseIsbn();
		var isbn = book.getIsbn();
		verify(view, atMostOnce()).setBaseIsbn(String.format("%s-%s-", isbn.getLinguisticGroup(), isbn.getIdAuthor()));
	}

	@Test
	void askBookId() {
		var book = new Book("title", new Author("a", "a"), "summary");
		when(repo.getBookToEdit()).thenReturn(book);
		presenter.askBookToEdit();
		presenter.askBookId();
		verify(view, atMostOnce()).setBookId(book.getIsbn().getIdBook());
	}
}