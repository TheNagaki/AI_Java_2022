package org.helmo.gbeditor.repositories;

import com.google.gson.Gson;
import org.helmo.gbeditor.models.Author;
import org.helmo.gbeditor.models.Book;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class JsonRepository implements Repository {


	private final Path bookPath;
	private final Path imageDirectoryPath;

	public JsonRepository(Path bookPath, Path imageDirectoryPath) {
		this.bookPath = bookPath;
		this.imageDirectoryPath = imageDirectoryPath;
	}

	@Override
	public Set<Book> loadBooks() {
		if (Files.exists(bookPath)) {
			try (BufferedReader reader = Files.newBufferedReader(bookPath)) {
				Gson gson = new Gson();
				gson.newJsonReader(reader);
				return new LinkedHashSet<>(Arrays.asList(gson.fromJson(reader, Book[].class)));
			} catch (FileNotFoundException e) {
				System.err.println("FileNotFoundException : " + e.getMessage());
			} catch (IOException e) {
				System.err.println("IOException : " + e.getMessage());
			}
		}
		return new LinkedHashSet<>();
	}

	@Override
	public boolean saveBooks(Set<Book> books) {
		if (!Files.exists(bookPath)) {
			try {
				Files.createFile(bookPath);
			} catch (IOException e) {
				System.err.println("IOException : " + e.getMessage());
				return false;
			}
		}
		try (BufferedWriter writer = Files.newBufferedWriter(bookPath, StandardCharsets.UTF_8, StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)) {
			Gson gson = new Gson();
			gson.toJson(books, writer);
			return true;
		} catch (IOException e) {
			System.err.println("IOException : " + e.getMessage());
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean saveBook(Book book) {
		Set<Book> books = loadBooks();
		if (!books.contains(book)) {
			books.add(book);
			return saveBooks(new HashSet<>(books));
		}
		return true;
	}

	@Override
	public boolean deleteBook(Book book) {
		Set<Book> books = loadBooks();
		if (books.contains(book)) {
			books.remove(book);
			return saveBooks(new HashSet<>(books));
		}
		return false;
	}

	@Override
	public Set<Author> loadAuthors() {
		Set<Book> books = loadBooks();
		Set<Author> authors = new LinkedHashSet<>();
		for (Book book : books) {
			authors.add(book.getAuthor());
		}
		return authors;
	}

	@Override
	public String moveImage(String imagePath) {
		Path imageStored = Path.of(imagePath);
		Path imageDestination = imageDirectoryPath.resolve(imageStored.getFileName());
		try (InputStream inputStream = new FileInputStream(imageStored.toFile());
		     OutputStream outputStream = new FileOutputStream(imageDestination.toFile())) {

			byte[] buf = new byte[1024];
			int length;
			while ((length = inputStream.read(buf)) > 0) {
				outputStream.write(buf, 0, length);
			}
			inputStream.close();
			outputStream.close();
			return imageDestination.toString();
		} catch (IOException e) {
			System.err.println("IOException : " + e.getMessage());
		}
		return null;
	}
}