package org.helmo.gbeditor.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookMetadataTest {

	public static final String TITLE = "title";
	public static final String SUMMARY = "summary";
	public static final String COVER = "cover";
	private BookMetadata metadata;
	private Author author;
	private ISBN isbn;


	@BeforeEach
	void setUp() {
		author = new Author("Bouffioux", "Jules", 1);
		isbn = ISBN.createNewISBN(BookMetadata.LINGUISTIC_GROUP, author.getIdentifier());
		metadata = new BookMetadata(TITLE, author, isbn.toString(), SUMMARY, COVER);
	}

	@Test
	void canCreateBookMetadata() {
		assertNotNull(metadata);
	}

	@Test
	void canNotCreateBookMetadataWithNullAuthor() {
		assertThrows(IllegalArgumentException.class, () -> new BookMetadata(TITLE, null, isbn.toString(), SUMMARY, COVER));
	}

	@Test
	void canCreateBookMetadataWithNullISBN() {
		assertDoesNotThrow(() -> new BookMetadata(TITLE, author, null, SUMMARY, COVER));
		assertNotNull(new BookMetadata(TITLE, author, null, SUMMARY, COVER).getIsbn());
	}

	@Test
	void canNotCreateBookMetadataWithNullTitle() {
		assertThrows(IllegalArgumentException.class, () -> new BookMetadata(null, author, isbn.toString(), SUMMARY, COVER));
	}

	@Test
	void canNotCreateBookMetadataWithEmptyTitle() {
		assertThrows(IllegalArgumentException.class, () -> new BookMetadata("", author, isbn.toString(), SUMMARY, COVER));
	}

	@Test
	void canNotCreateBookMetadataWithBlankTitle() {
		assertThrows(IllegalArgumentException.class, () -> new BookMetadata(" ", author, isbn.toString(), SUMMARY, COVER));
	}

	@Test
	void canNotCreateBookMetadataWithNullSummary() {
		assertThrows(IllegalArgumentException.class, () -> new BookMetadata(TITLE, author, isbn.toString(), null, COVER));
	}

	@Test
	void canNotCreateBookMetadataWithEmptySummary() {
		assertThrows(IllegalArgumentException.class, () -> new BookMetadata(TITLE, author, isbn.toString(), "", COVER));
	}

	@Test
	void canNotCreateBookMetadataWithBlankSummary() {
		assertThrows(IllegalArgumentException.class, () -> new BookMetadata(TITLE, author, isbn.toString(), " ", COVER));
	}

	@Test
	void getFieldWorksInNormalContext() {
		assertEquals(TITLE, metadata.getField(BookDataFields.TITLE));
		assertEquals(SUMMARY, metadata.getField(BookDataFields.SUMMARY));
		assertEquals(COVER, metadata.getField(BookDataFields.IMAGE_PATH));
		assertEquals("000001", metadata.getField(BookDataFields.AUTHOR_MATRICULE));
		assertEquals(isbn.toString(), metadata.getField(BookDataFields.BOOK_ISBN));
	}

	@Test
	void getAuthor() {
		assertEquals(author, metadata.getAuthor());
	}

	@Test
	void setField() {
		metadata.setField(BookDataFields.TITLE, "newTitle");
		assertEquals("newTitle", metadata.getField(BookDataFields.TITLE));
	}

	@Test
	void setFieldWithNullValue() {
		assertThrows(IllegalArgumentException.class, () -> metadata.setField(BookDataFields.TITLE, null));
	}

	@Test
	void setFieldWithEmptyValue() {
		assertThrows(IllegalArgumentException.class, () -> metadata.setField(BookDataFields.TITLE, ""));
	}

	@Test
	void setFieldWithBlankValue() {
		assertThrows(IllegalArgumentException.class, () -> metadata.setField(BookDataFields.TITLE, " "));
	}

	@Test
	void testEqualsSameAttributesObject() {
		BookMetadata metadata2 = new BookMetadata(TITLE, author, isbn.toString(), SUMMARY, COVER);
		assertEquals(metadata, metadata2);
	}

	@Test
	void testEqualsSameObject() {
		assertEquals(metadata, metadata);
	}

	@Test
	void testEqualsDifferentObjectButSameISBN() {
		assertEquals(metadata, new BookMetadata("title2", new Author("a", "a"), isbn.toString(), SUMMARY, COVER));
	}

	@Test
	void testEqualsSameObjectButDifferentISBN() {
		assertNotEquals(metadata, new BookMetadata("title2", author, ISBN.createNewISBN(BookMetadata.LINGUISTIC_GROUP, author.getIdentifier()).toString(), SUMMARY, COVER));
	}

	@Test
	void getIsbn() {
		assertEquals(isbn, metadata.getIsbn());
	}
}