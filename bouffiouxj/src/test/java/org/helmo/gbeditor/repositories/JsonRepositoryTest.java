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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JsonRepositoryTest {
	private final String pathRes = ("src/test/resources/repositories/resFiles");
	private final String pathTest = ("src/test/resources/repositories/testedFiles");
	private final Path pathBooksOk = Path.of(pathRes + "/booksOk.json");
	private final Path pathSingleBook = Path.of(pathRes + "/singleBook.json");
	private final Path pathSaveBooks = Path.of(pathTest + "/saveBooks.json");
	private final Path pathAddBook = Path.of(pathTest + "/addBook.json");
	private Set<Book> bookSet;
	private Set<Author> authorSet;

	@BeforeEach
	void setUp() {
		final Author authorA = new Author("name A", "firstName A");
		final Author authorB = new Author("name B", "firstName B");
		bookSet = new HashSet<>();
		authorSet = new HashSet<>();
		authorSet.add(authorA);
		authorSet.add(authorB);
		bookSet.add(new Book("title A1", authorA, "1", "summary A1"));
		bookSet.add(new Book("title A2", authorA, "2", "summary A2"));
		bookSet.add(new Book("title B1", authorB, "3", "summary B1"));
		bookSet.add(new Book("title B2", authorB, "4", "summary B2"));
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
				assertTrue(bookSet.contains(book));
			}
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	void saveBooks() {
		try {
			JsonRepository jsonRepository = new JsonRepository(pathSaveBooks, null);
			jsonRepository.saveBooks(bookSet);
			try (Reader reader = Files.newBufferedReader(pathSaveBooks)) {
				Gson gson = new Gson();
				Book[] books = gson.fromJson(reader, Book[].class);
				assertIterableEquals(this.bookSet, new HashSet<>(List.of(books)));
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
			Book book = new Book("title A1", new Author("name A", "firstName A"), "1", "summary A1");
			jsonRepository.saveBook(book);
			bookSet.add(book);
			assertEquals(Files.readString(pathSingleBook), Files.readString(pathAddBook));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	void deleteBook() {
		try {
			JsonRepository jsonRepository = new JsonRepository(pathAddBook, null);
			Book book = new Book("title A1", new Author("name A", "firstName A"), "1", "summary A1");
			Files.copy(pathSingleBook, pathAddBook);
			jsonRepository.deleteBook(book);
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
			jsonRepository.moveImage(pathRes + "/testImage.png");
			Path imageDestination = Path.of(pathTest + "/testImage.png");
			assertTrue(Files.exists(imageDestination));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
}