package org.helmo.gbeditor.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthorTest {
	private Author author;

	@BeforeEach
	void setUp() {
		author = new Author("Bouffioux", "Jules", 1);
	}

	@Test
	void canCreateAuthor() {
		assertEquals("Bouffioux", author.getName());
		assertEquals("Jules", author.getFirstName());
		assertEquals(1, author.getIdentifier());
	}

	@Test
	void canNotCreateAuthorWithNullName() {
		assertThrows(IllegalArgumentException.class, () -> new Author(null, "Jules", 1));
	}

	@Test
	void canNotCreateAuthorWithNullFirstName() {
		assertThrows(IllegalArgumentException.class, () -> new Author("Bouffioux", null, 1));
	}

	@Test
	void canNotCreateAuthorWithEmptyName() {
		assertThrows(IllegalArgumentException.class, () -> new Author("", "Jules", 1));
	}

	@Test
	void canNotCreateAuthorWithEmptyFirstName() {
		assertThrows(IllegalArgumentException.class, () -> new Author("Bouffioux", "", 1));
	}

	@Test
	void getFullName() {
		assertEquals("Jules Bouffioux", author.getFullName());
	}

	@Test
	void testEquals() {
		Author author2 = new Author("Bouffioux", "Jules", 1);
		assertEquals(author, author2);
	}

	@Test
	void testEqualsEvenIfDifferentIdentifier() {
		Author author2 = new Author("Bouffioux", "Jules", 2);
		assertEquals(author, author2);
	}

	@Test
	void testNotEquals() {
		Author author2 = new Author("B", "J", 2);
		assertNotEquals(author, author2);
	}

	@Test
	void testNotEqualsWithNull() {
		assertNotEquals(author, null);
	}

	@Test
	void getName() {
		assertEquals("Bouffioux", author.getName());
	}

	@Test
	void getIdentifier() {
		assertEquals(1, author.getIdentifier());
	}

	@Test
	void getFirstName() {
		assertEquals("Jules", author.getFirstName());
	}

	@Test
	void identifierCanNotBeNegative() {
		assertThrows(IllegalArgumentException.class, () -> new Author("Bouffioux", "Jules", -1));
	}

	@Test
	void identifierCanNotBeLongerThanAMillion() {
		assertThrows(IllegalArgumentException.class, () -> new Author("Bouffioux", "Jules", 1000001));
	}
}