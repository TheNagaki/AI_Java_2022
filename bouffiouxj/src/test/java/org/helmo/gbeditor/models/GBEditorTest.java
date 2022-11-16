package org.helmo.gbeditor.models;

import org.helmo.gbeditor.presenters.GBEInterface;
import org.helmo.gbeditor.repositories.RepositoryInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class is a test class for the GBEditor class
 */
class GBEditorTest {
	private Author authorA;
	private Set<Book> booksFromAuthorA;

	private GBEInterface gbe;

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
		RepositoryInterface repo = new FakeRepository(bookCollection);
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
	}

	@Test
	void updateBook() {
	}

	@Test
	void getAllBooks() {
	}

	@Test
	void getBooksFromCurrentAuthor() {
	}

	@Test
	void getAuthorName() {
	}

	@Test
	void presetISBN() {
	}
}