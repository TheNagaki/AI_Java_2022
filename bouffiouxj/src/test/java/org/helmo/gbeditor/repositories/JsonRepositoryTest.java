package org.helmo.gbeditor.repositories;

import com.google.gson.Gson;
import org.helmo.gbeditor.models.Author;
import org.helmo.gbeditor.models.Book;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class is a test class for the JsonRepository class
 */
class JsonRepositoryTest {
	private final String pathRes = ("src/test/resources/repositories/resFiles");
	private final String pathTest = ("src/test/resources/repositories/testedFiles");
	private final Path pathBooksOk = Path.of(pathRes + "/booksOk.json");
	private final Path pathSingleBook = Path.of(pathRes + "/singleBook.json");
	private final Path pathSaveBooks = Path.of(pathTest + "/saveBooks.json");
	private final Path pathAddBook = Path.of(pathTest + "/addBook.json");
	private List<Book> bookSet;
	private Set<Author> authorSet;
	private Book bookA;

	@BeforeEach
	void setUp() {
		final Author authorA = new Author("name A", "firstName A", 961380);
		final Author authorB = new Author("name B", "firstName B");
		bookSet = new ArrayList<>();
		authorSet = new HashSet<>();
		authorSet.add(authorA);
		authorSet.add(authorB);
		bookA = new Book("title A1", authorA, "summary A1", "2-961380-61-X");
		bookSet.add(bookA);
		bookSet.add(new Book("title A2", authorA, "summary A2", "2-961380-59-8"));
		bookSet.add(new Book("title B1", authorB, "summary B1", "2-123456-12-6"));
		bookSet.add(new Book("title B2", authorB, "summary B2", "2-123456-13-7"));
	}

	@AfterEach
	void tearDown() {
		Path path = Path.of(pathTest);
		File testDirectory = path.toFile();
		if (testDirectory.exists()) {
			File[] files = testDirectory.listFiles();
			if (files != null) {
				for (File file : files) {
					file.delete();
				}
			}
		}
	}

	@Test
	void loadBooks() {
		try {
			JsonRepository jsonRepository = new JsonRepository(pathBooksOk, null);
			Set<Book> booksLoaded = jsonRepository.loadBooks();
			for (Book book : booksLoaded) {
				assertTrue(bookSet.remove(book), "Book " + book.getTitle() + " was not contained");
			}
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	void saveBooks() {
		try {
			JsonRepository jsonRepository = new JsonRepository(pathSaveBooks, null);
			jsonRepository.saveBooks(new HashSet<>(bookSet));
			try (Reader reader = Files.newBufferedReader(pathSaveBooks)) {
				Gson gson = new Gson();
				Book[] books = Arrays.stream(gson.fromJson(reader, BookDto[].class)).map(BookDto::toBook).toArray(Book[]::new);
				for (Book book : books) {
					assertTrue(bookSet.contains(book), "Book " + book.getTitle() + " was not contained");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	void saveBook() {
		try {
			JsonRepository jsonRepository = new JsonRepository(pathAddBook, null);
			jsonRepository.saveBook(bookA);
			assertEquals(Files.readString(pathSingleBook), Files.readString(pathAddBook));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	void deleteBook() {
		try {
			JsonRepository jsonRepository = new JsonRepository(pathAddBook, null);
			Files.copy(pathSingleBook, pathAddBook);
			jsonRepository.deleteBook(bookA);
			assertEquals("[]", Files.readString(pathAddBook));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	void loadAuthors() {
		try {
			JsonRepository jsonRepository = new JsonRepository(pathBooksOk, null);
			Set<Author> authorsLoaded = jsonRepository.loadAuthors();
			assertIterableEquals(authorSet, authorsLoaded);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	void moveImage() {
		try {
			JsonRepository jsonRepository = new JsonRepository(null, Path.of(pathTest));
			jsonRepository.copyImage(pathRes + "/testImage.png");
			Path imageDestination = Path.of(pathTest + "/testImage.png");
			assertTrue(Files.exists(imageDestination));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	void copyFakeImageThrowsException() {
		try {
			JsonRepository jsonRepository = new JsonRepository(null, Path.of(pathTest));
			assertThrows(IllegalArgumentException.class, () -> jsonRepository.copyImage(pathRes + "/fakeImage.jpg"));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	void copyNonExistingImageReturnsFalse() {
		try {
			JsonRepository jsonRepository = new JsonRepository(null, Path.of(pathTest));
			jsonRepository.copyImage(pathRes + "/nonExistingImage.jpg");
			Path imageDestination = Path.of(pathTest + "/nonExistingImage.jpg");
			assertFalse(Files.exists(imageDestination));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
}