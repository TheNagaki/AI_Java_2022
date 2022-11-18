package org.helmo.gbeditor.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class is used to test the Book class
 */
class BookTest {

	private final String name = "name";
	private final String firstName = "firstName";
	private final String title = "title";
	private final String summary = "summary";
	private final String imagePath = "some/path";
	private Book book;
	private Author author;
	private Page page;


	@BeforeEach
	void setUp() {
		this.author = new Author(name, firstName);
		this.book = new Book(title, author, summary, "", imagePath);
		this.page = new Page("content");
	}

	@Test
	void createBookWithNullTitleThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> new Book(null, author, summary, "", imagePath));
	}

	@Test
	void createBookWithEmptyTitleThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> new Book("", author, summary, "", imagePath));
	}

	@Test
	void createBookWithTooBigTitleThrowsException() {
		var title = "a".repeat(Book.MAX_TITLE + 1);
		assertThrows(IllegalArgumentException.class, () -> new Book(title, author, summary, "", imagePath));
	}

	@Test
	void createBookWithNullAuthorThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> new Book(title, null, summary, "", imagePath));
	}

	@Test
	void createBookWithNullSummaryThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> new Book(title, author, null, "", imagePath));
	}

	@Test
	void createBookWithEmptySummaryThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> new Book(title, author, "", "", imagePath));
	}

	@Test
	void createBookWithTooBigSummaryThrowsException() {
		var summary = "a".repeat(Book.MAX_SUMMARY + 1);
		assertThrows(IllegalArgumentException.class, () -> new Book(title, author, summary, "", imagePath));
	}

	@Test
	void setTitle() {
		book.setTitle("newTitle");
		assertEquals("newTitle", book.getTitle());
	}

	@Test
	void setNullTitleThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> book.setTitle(null));
	}

	@Test
	void setEmptyTitleThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> book.setTitle(""));
	}

	@Test
	void setTooBigTitleThrowsException() {
		var tooBigTitle = "a".repeat(Book.MAX_TITLE + 1);
		assertThrows(IllegalArgumentException.class, () -> book.setTitle(tooBigTitle));
	}

	@Test
	void setSummary() {
		book.setSummary("newSummary");
		assertEquals("newSummary", book.getSummary());
	}

	@Test
	void setNullSummaryThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> book.setSummary(null));
	}

	@Test
	void setEmptySummaryThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> book.setSummary(""));
	}

	@Test
	void setTooBigSummaryThrowsException() {
		var tooBigSummary = "a".repeat(Book.MAX_SUMMARY + 1);
		assertThrows(IllegalArgumentException.class, () -> book.setSummary(tooBigSummary));
	}

	@Test
	void getTitle() {
		assertEquals(title, book.getTitle());
	}

	@Test
	void getAuthor() {
		assertEquals(name, book.getAuthor().getName());
		assertEquals(firstName, book.getAuthor().getFirstName());
	}

	@Test
	void getIsbn() {
		assertNotNull(book.getIsbn());
	}

	@Test
	void getSummary() {
		assertEquals(summary, book.getSummary());
	}

	@Test
	void testEquals() {
		var book2 = new Book(title, author, summary, "2-689305-96-0", "");
		var book3 = new Book(title, author, summary, "2-689305-96-0", "");
		assertEquals(book2, book3);
	}

	@Test
	void testBookEqualsItself() {
		assertEquals(book, book);
	}

	@Test
	void testSameBooksWithoutIsbnAreNotEquals() {
		var book2 = new Book(title, author, summary, "", "");
		assertNotEquals(book, book2);
	}

	@Test
	void setImagePath() {
		book.setImagePath("newImagePath");
		assertEquals("newImagePath", book.getImagePath());
	}

	@Test
	void setNullImagePathDoesNotThrowException() {
		assertDoesNotThrow(() -> book.setImagePath(null));
		assertEquals("", book.getImagePath());
	}

	@Test
	void addPage() {

		book.addPage(page);
		assertTrue(book.getPages().contains(page));
	}

	@Test
	void addNullPageThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> book.addPage(null));
	}

	@Test
	void addPageWithNullContentThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> book.addPage(new Page(null)));
	}

	@Test
	void removePage() {
		book.addPage(page);
		assertTrue(book.getPages().contains(page));
		book.removePage(page);
		assertFalse(book.getPages().contains(page));
	}

	@Test
	void getPages() {
		book.addPage(page);
		assertTrue(book.getPages().contains(page));
	}

	@Test
	void testToString() {
		var author = new Author(name, firstName);
		var book2 = new Book(title, author, summary, "2-689305-96-0", "");
		assertEquals("Book{title='title',\n" +
				" author=org.helmo.gbeditor.models.Author@e26c3f1,\n" +
				" isbn=2-689305-96-0,\n" +
				" summary='summary',\n" +
				" imagePath='',\n" +
				" pages=[]}", book2.toString());
	}

	@Test
	void updatePage() {
		book.addPage(page);
		assertEquals("content", book.getPages().iterator().next().getContent());
		page.setContent("newContent");
		book.updatePage(page);
		assertEquals("newContent", book.getPages().iterator().next().getContent());
	}

	@Test
	void updateAPageDoesNotAddIt() {
		book.addPage(page);
		assertEquals(1, book.getPages().size());
		page.setContent("newContent");
		book.updatePage(page);
		assertEquals(1, book.getPages().size());
	}

	@Test
	void getPageNumber() {
		book.addPage(page);
		assertEquals(1, book.getPageNumber(page));
	}

	@Test
	void getPageNumberWithNullPageThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> book.getPageNumber(null));
	}

	@Test
	void getPageNumberWithUnknownPageDoesNotThrowException() {
		assertThrows(IllegalArgumentException.class, () -> book.getPageNumber(page));
	}
}