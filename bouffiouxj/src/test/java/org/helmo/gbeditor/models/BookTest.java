package org.helmo.gbeditor.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.helmo.gbeditor.models.BookDataFields.*;
import static org.helmo.gbeditor.models.BookMetadata.MAX_SUMMARY;
import static org.helmo.gbeditor.models.BookMetadata.MAX_TITLE;
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
		var title = "a".repeat(MAX_TITLE + 1);
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
		var summary = "a".repeat(MAX_SUMMARY + 1);
		assertThrows(IllegalArgumentException.class, () -> new Book(title, author, summary, "", imagePath));
	}

	@Test
	void setTitle() {
		book.setMetadata(TITLE, "newTitle");
		assertEquals("newTitle", book.getMetadata(TITLE));
	}

	@Test
	void setNullTitleThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> book.setMetadata(TITLE, null));
	}

	@Test
	void setEmptyTitleThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> book.setMetadata(TITLE, ""));
	}

	@Test
	void setTooBigTitleThrowsException() {
		var tooBigTitle = "a".repeat(MAX_TITLE + 1);
		assertThrows(IllegalArgumentException.class, () -> book.setMetadata(TITLE, tooBigTitle));
	}

	@Test
	void setSummary() {
		book.setMetadata(SUMMARY, "newSummary");
		assertEquals("newSummary", book.getMetadata(SUMMARY));
	}

	@Test
	void setNullSummaryThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> book.setMetadata(SUMMARY, null));
	}

	@Test
	void setEmptySummaryThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> book.setMetadata(SUMMARY, ""));
	}

	@Test
	void setTooBigSummaryThrowsException() {
		var tooBigSummary = "a".repeat(MAX_SUMMARY + 1);
		assertThrows(IllegalArgumentException.class, () -> book.setMetadata(SUMMARY, tooBigSummary));
	}

	@Test
	void getTitle() {
		assertEquals(title, book.getMetadata(TITLE));
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
		assertEquals(summary, book.getMetadata(SUMMARY));
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
		book.setMetadata(IMAGE_PATH, "newImagePath");
		assertEquals("newImagePath", book.getMetadata(IMAGE_PATH));
	}

	@Test
	void setNullImagePathDoesNotThrowException() {
		assertDoesNotThrow(() -> book.setMetadata(IMAGE_PATH, null));
		assertEquals("", book.getMetadata(IMAGE_PATH));
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