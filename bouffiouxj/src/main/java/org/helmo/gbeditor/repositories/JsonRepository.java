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
//	public static void main(String[] args) {
//		final String pathRes = ("bouffiouxj/src/test/resources/repositories/resFiles");
//		final Path pathBooksOk = Path.of(pathRes + "/booksOk.json");
//		final Path pathSingleBook = Path.of(pathRes + "/singleBook.json");
//		Set<Book> cyrilBooks;
//		Author authorA = new Author("name A", "firstName A");
//		cyrilBooks = new HashSet<>();
//		cyrilBooks.add(new Book("title A1", authorA, 1, "summary A1"));
//		JsonRepository jsonRepository = new JsonRepository();
//
//		System.out.println(pathSingleBook.toAbsolutePath());
//		System.out.println(jsonRepository.saveBooks(cyrilBooks, pathSingleBook) + " ? ");
//	}

	@Override
	public Set<Book> loadBooks(Path path) {
		if (Files.exists(path)) {
			try (BufferedReader reader = Files.newBufferedReader(path)) {
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
	public boolean saveBooks(Set<Book> books, Path path) {
		if (!Files.exists(path)) {
			try {
				Files.createFile(path);
			} catch (IOException e) {
				System.err.println("IOException : " + e.getMessage());
				return false;
			}
		}
		try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)) {
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
	public boolean saveBook(Book book, Path path) {
		Set<Book> books = loadBooks(path);
		if (!books.contains(book)) {
			books.add(book);
			return saveBooks(new HashSet<>(books), path);
		}
		return true;
	}

	@Override
	public boolean deleteBook(Book book, Path path) {
		Set<Book> books = loadBooks(path);
		if (books.contains(book)) {
			books.remove(book);
			return saveBooks(new HashSet<>(books), path);
		}
		return false;
	}

	@Override
	public Set<Author> loadAuthors(Path path) {
		Set<Book> books = loadBooks(path);
		Set<Author> authors = new LinkedHashSet<>();
		for (Book book : books) {
			authors.add(book.getAuthor());
		}
		return authors;
	}

	@Override
	public String moveImage(String imagePath, String newPath) {
		try (InputStream in = new FileInputStream(Path.of(imagePath).toFile());
		     OutputStream out = new FileOutputStream(Path.of(newPath).toFile())) {

			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
			return newPath;
		} catch (IOException e) {
			System.err.println("IOException : " + e.getMessage());
		}
		return null;
	}
}