package org.helmo.gbeditor.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ISBNTest {

	private ISBN isbn;

	@BeforeEach
	void setUp() {
		isbn = new ISBN(2, 123456, 1);
	}

	@Test
	void computeCheckSum() {
		assertEquals(6, ISBN.computeCheckSum(isbn.getLinguisticGroup(), isbn.getIdAuthor(), isbn.getIdBook()));
	}

	@Test
	void createNewISBN() {
		ISBN isbn = ISBN.createNewISBN(2, 123456);
		assertEquals(2, isbn.getLinguisticGroup());
		assertEquals(123456, isbn.getIdAuthor());
		assertNotNull(isbn);
	}

	@Test
	void createNewISBNWithNegativeLinguisticGroupThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> ISBN.createNewISBN(-1, 123456));
		assertThrows(IllegalArgumentException.class, () -> new ISBN(-1, 123456, 1));
	}

	@Test
	void createNewISBNWithNegativeIdAuthorThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> ISBN.createNewISBN(2, -1));
		assertThrows(IllegalArgumentException.class, () -> new ISBN(2, -1, 1));
	}

	@Test
	void createNewISBNWithNegativeIdBookThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> new ISBN(2, 123456, -1));
	}

	@Test
	void createNewIsbnFromStringWorks() {
		ISBN newIsbn = new ISBN(isbn.toString());
		assertEquals(2, newIsbn.getLinguisticGroup());
		assertEquals(123456, newIsbn.getIdAuthor());
		assertEquals(1, newIsbn.getIdBook());
	}

	@Test
	void createNewIsbnFromNullStringThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> new ISBN(null));
	}

	@Test
	void createNewIsbnFromEmptyStringThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> new ISBN(""));
	}

	@Test
	void createNewIsbnFromBlankStringThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> new ISBN(" "));
	}

	@Test
	void testComputeCheckSum() {
		assertEquals(6, ISBN.computeCheckSum(2, 123456, 1));
	}

	@Test
	void getCheckSum() {
		assertEquals(6, isbn.getCheckSum());
	}

	@Test
	void getLinguisticGroup() {
		assertEquals(2, isbn.getLinguisticGroup());
	}

	@Test
	void getIdAuthor() {
		assertEquals(123456, isbn.getIdAuthor());
	}

	@Test
	void getIdBook() {
		assertEquals(1, isbn.getIdBook());
	}

	@Test
	void testEquals() {
		ISBN isbn2 = new ISBN(2, 123456, 1);
		assertEquals(isbn, isbn2);
	}

	@Test
	void testComputeCheckSumWorksInNormalCase() {
		assertEquals(isbn.getCheckSum() + "", ISBN.computeCheckSum(String.format("%1d-%06d-%02d", isbn.getLinguisticGroup(), isbn.getIdAuthor(), isbn.getIdBook())));
	}

	@Test
	void testComputeCheckSumThrowsExceptionWhenBookIdIsTooShort() {
		assertThrows(IllegalArgumentException.class, () -> ISBN.computeCheckSum("2-123456-1"));
	}

	@Test
	void testComputeCheckSumThrowsExceptionWhenBookIdIsTooLong() {
		assertThrows(IllegalArgumentException.class, () -> ISBN.computeCheckSum("2-123456-123"));
	}

	@Test
	void testComputeCheckSumThrowsExceptionWhenAuthorIdIsTooShort() {
		assertThrows(IllegalArgumentException.class, () -> ISBN.computeCheckSum("2-12345-12"));
	}

	@Test
	void testComputeCheckSumThrowsExceptionWhenAuthorIdIsTooLong() {
		assertThrows(IllegalArgumentException.class, () -> ISBN.computeCheckSum("2-1234567-12"));
	}

	@Test
	void testComputeCheckSumThrowsExceptionWhenLinguisticGroupIsTooShort() {
		assertThrows(IllegalArgumentException.class, () -> ISBN.computeCheckSum("-123456-12"));
	}

	@Test
	void testComputeCheckSumThrowsExceptionWhenLinguisticGroupIsTooLong() {
		assertThrows(IllegalArgumentException.class, () -> ISBN.computeCheckSum("12-123456-12"));
	}
}