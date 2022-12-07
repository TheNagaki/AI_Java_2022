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
	void canCreateBook() {
		assertNotNull(book);
	}

	@Test
	void canCreateBookWithNullIsbn() {
		assertDoesNotThrow(() -> new Book(title, author, summary, null));
	}

	@Test
	void canCreateBookWithEmptyIsbn() {
		assertDoesNotThrow(() -> new Book(title, author, summary, ""));
	}

	@Test
	void canCreateBookWithBlankIsbn() {
		assertDoesNotThrow(() -> new Book(title, author, summary, " "));
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
	void testBookDoesNotEqualsNull() {
		assertNotEquals(book, null);
	}

	@Test
	void setImagePath() {
		book.setMetadata(IMAGE_PATH, "newImagePath");
		assertEquals("newImagePath", book.getMetadata(IMAGE_PATH));
	}

	@Test
	void setNullImagePathDoesNotThrowException() {
		assertDoesNotThrow(() -> book.setMetadata(IMAGE_PATH, null), "Setting null image path should not throw exception");
		assertEquals("", book.getMetadata(IMAGE_PATH), "Setting null image path should set empty string");
	}

	@Test
	void addPage() {
		book.addPage(page, 0);
		assertTrue(book.getPages().contains(page));
	}

	@Test
	void addNullPageThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> book.addPage(null, 0));
	}

	@Test
	void addPageWithNullContentThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> book.addPage(new Page(null), 0));
	}

	@Test
	void removePage() {
		book.addPage(page, 0);
		assertTrue(book.getPages().contains(page));
		book.removePage(page);
		assertFalse(book.getPages().contains(page));
	}

	@Test
	void getPages() {
		book.addPage(page, 0);
		assertTrue(book.getPages().contains(page));
	}

	@Test
	void testToString() {
		var author = new Author(name, firstName);
		var book2 = new Book(title, author, summary, "2-689305-96-0", "");
		assertEquals("Book{BookMetadata{title='title', author=" + author + ", isbn=2-689305-96-0, summary='summary', imagePath=''} pages=[]}",
				book2.toString());
	}

	@Test
	void updatePage() {
		book.addPage(page, 0);
		assertEquals("content", book.getPages().iterator().next().getContent());
		page.setContent("newContent");
		book.updatePage(page);
		assertEquals("newContent", book.getPages().iterator().next().getContent());
	}

	@Test
	void updateAPageDoesNotAddIt() {
		book.addPage(page, 0);
		assertEquals(1, book.getPages().size());
		page.setContent("newContent");
		book.updatePage(page);
		assertEquals(1, book.getPages().size());
	}

	@Test
	void updateAPageRedirectsChoicesToTheNewPage() {
		var page2 = new Page("content2");
		book.addPage(page, 0);
		book.addPage(page2, 1);
		page.addChoice("choice", page2);
		page2.setContent("newContent");
		book.updatePage(page2);
		assertEquals(page2, page.getChoices().get("choice"));
	}

	@Test
	void removingAPageDeletesAllChoicesToThisPage() {
		var page2 = new Page("content2");
		book.addPage(page, 0);
		book.addPage(page2, 1);
		page.addChoice("choice", page2);
		book.removePage(page2);
		assertEquals(0, page.getChoices().size());
	}

	@Test
	void aBookContainsChoicesToAPage() {
		var page2 = new Page("content2");
		book.addPage(page, 0);
		book.addPage(page2, 1);
		page.addChoice("choice", page2);
		assertTrue(book.hasChoicesTo(page2));
	}

	@Test
	void aBookDoesNotContainChoicesToANullPage() {
		book.addPage(page, 0);
		assertFalse(book.hasChoicesTo(null));
	}

	@Test
	void aBookDoesNotContainChoicesToAPageNotInThisBook() {
		var page2 = new Page("content2");
		book.addPage(page, 0);
		assertFalse(book.hasChoicesTo(page2));
	}

	@Test
	void aBookDoesNotContainChoicesToAPageIfThereSNoChoices() {
		var page2 = new Page("content2");
		book.addPage(page, 0);
		book.addPage(page2, 1);
		assertFalse(book.hasChoicesTo(page2));
	}

	@Test
	void aBookDoesNotContainChoicesToAPageIfThereSNoChoicesToIt() {
		var page2 = new Page("content2");
		book.addPage(page, 0);
		page2.addChoice("choice", page);
		book.addPage(page2, 1);
		assertFalse(book.hasChoicesTo(page2));
	}

	@Test
	void getPageNumber() {
		book.addPage(page, 0);
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

	@Test
	void getPagesWithNullIdThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> book.getPageById(null));
	}

	@Test
	void getPagesWithUnknownPageDoesNotThrowExceptionAndReturnsNull() {
		assertDoesNotThrow(() -> book.getPageById("unknownId"));
	}

	@Test
	void getPageById() {
		book.addPage(page, 0);
		assertEquals(page, book.getPageById(page.getId()));
	}

	@Test
	void getPagesWithEmptyIdThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> book.getPageById(""));
	}

	@Test
	void getPagesWithBlankIdThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> book.getPageById(" "));
	}
}