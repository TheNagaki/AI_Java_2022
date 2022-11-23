package org.helmo.gbeditor.models;

import org.helmo.gbeditor.presenters.interfaces.GBEInterface;
import org.helmo.gbeditor.repositories.RepositoryInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.helmo.gbeditor.models.BookDataFields.SUMMARY;
import static org.helmo.gbeditor.models.BookDataFields.TITLE;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class is a test class for the GBEditor class
 */
class GBEditorTest {
	private Author authorA;
	private Set<Book> booksFromAuthorA;
	private GBEInterface gbe;
	private RepositoryInterface repo;

	@BeforeEach
	void setUp() {
		authorA = new Author("name A", "firstName A", 961380);
		booksFromAuthorA = new LinkedHashSet<>();
		Book book1 = new Book("title A1", authorA, "summary A1");
		Book book2 = new Book("title A2", authorA, "summary A2");
		booksFromAuthorA.add(book1);
		booksFromAuthorA.add(book2);
		final Author authorB = new Author("name B", "firstName B", 961381);
		var bookCollection = new LinkedHashSet<Book>() {
			{
				add(book1);
				add(book2);
				add(new Book("title3", authorB, "summary B1"));
			}
		};
		repo = new FakeRepository(bookCollection);
		gbe = new GBEditor(repo);
	}

	@Test
	void connectWorksWithNormalBehavior() {
		gbe.connect(authorA.getName(), authorA.getFirstName());
		assertEquals(authorA.getFullName(), gbe.getAuthorName());
		assertEquals(booksFromAuthorA, gbe.getBooksFromCurrentAuthor());
	}

	@Test
	void connectReturnsFalseOnEmptyString() {
		assertFalse(gbe.connect("", ""));
	}

	@Test
	void connectReturnsFalseOnNull() {
		assertFalse(gbe.connect(null, null));
	}

	@Test
	void createBookWorksWithNormalBehavior() {
		gbe.connect(authorA.getName(), authorA.getFirstName());
		gbe.createBook("title", "summary", ISBN.createNewISBN(2, authorA.getMatricule()).toString(), "");
		assertEquals(3, gbe.getBooksFromCurrentAuthor().size());
	}

	@Test
	void createBookReturnsExceptionMessageOnEmptyTitle() {
		gbe.connect(authorA.getName(), authorA.getFirstName());
		assertEquals("Le titre du livre doit avoir une taille comprise entre 1 et 150 caractères", gbe.createBook("", "aaaa", "", ""));
	}

	@Test
	void createBookReturnsExceptionMessageOnEmptySummary() {
		gbe.connect(authorA.getName(), authorA.getFirstName());
		assertEquals("Le résumé du livre doit avoir une taille comprise entre 1 et 500 caractères", gbe.createBook("aaaa", "", "", ""));
	}

	@Test
	void deleteBook() {
		gbe.connect(authorA.getName(), authorA.getFirstName());
		gbe.deleteBook(booksFromAuthorA.iterator().next());
		assertEquals(1, gbe.getBooksFromCurrentAuthor().size());
	}

	@Test
	void deleteBookReturnsFalseOnNull() {
		assertFalse(gbe.deleteBook(null));
	}

	@Test
	void deleteBookReturnsFalseOnEmpty() {
		repo = new FakeRepository(new LinkedHashSet<>());
		gbe = new GBEditor(repo);
		assertFalse(gbe.deleteBook(new Book("new book", authorA, "summary")));
	}

	@Test
	void deleteBookReturnsFalseOnNotExistingBook() {
		assertFalse(gbe.deleteBook(new Book("new book", authorA, "summary")));
	}

	@Test
	void updateBook() {
		gbe.connect(authorA.getName(), authorA.getFirstName());
		Book book = booksFromAuthorA.iterator().next();
		gbe.updateBook(book, "new title", "new summary", "", "");
		assertEquals("new title", book.getMetadata(TITLE));
		assertEquals("new summary", book.getMetadata(SUMMARY));
	}

	@Test
	void updateBookReturnsExceptionMessageOnEmptyTitle() {
		gbe.connect(authorA.getName(), authorA.getFirstName());
		Book book = booksFromAuthorA.iterator().next();
		assertThrows(IllegalArgumentException.class, () -> gbe.updateBook(book, "", "new summary", "", ""));
	}

	@Test
	void updateBookReturnsExceptionMessageOnEmptySummary() {
		gbe.connect(authorA.getName(), authorA.getFirstName());
		Book book = booksFromAuthorA.iterator().next();
		assertThrows(IllegalArgumentException.class, () -> gbe.updateBook(book, "new title", "", "", ""));
	}

	@Test
	void getAllBooks() {
		assertEquals(3, gbe.getAllBooks().size());
	}

	@Test
	void getAllBooksReturnsEmptySetOnEmptyRepository() {
		repo = new FakeRepository(new LinkedHashSet<>());
		gbe = new GBEditor(repo);
		assertEquals(0, gbe.getAllBooks().size());
	}

	@Test
	void getBooksFromCurrentAuthor() {
		gbe.connect(authorA.getName(), authorA.getFirstName());
		assertEquals(booksFromAuthorA, gbe.getBooksFromCurrentAuthor());
	}

	@Test
	void getBooksFromCurrentAuthorReturnsEmptySetOnNull() {
		gbe.connect(null, null);
		assertEquals(new LinkedHashSet<Book>(), gbe.getBooksFromCurrentAuthor());
	}

	@Test
	void getAuthorName() {
		gbe.connect(authorA.getName(), authorA.getFirstName());
		assertEquals(authorA.getFullName(), gbe.getAuthorName());
	}

	@Test
	void getAuthorNameReturnsNullOnNull() {
		assertNull(gbe.getAuthorName());
	}

	@Test
	void presetISBN() {
		gbe.connect(authorA.getName(), authorA.getFirstName());
		assertEquals(String.format("2-%d-", authorA.getMatricule()), String.format("2-%d-", gbe.presetISBN()[1]));
	}

	@Test
	void presetISBNReturnsNullOnNull() {
		assertNull(gbe.presetISBN());
	}
}