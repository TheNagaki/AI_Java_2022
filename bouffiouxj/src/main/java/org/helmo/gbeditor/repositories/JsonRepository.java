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

	private static final byte[] JPG_BYTES = new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF};
	private static final byte[] PNG_BYTES = new byte[]{(byte) 0x89, (byte) 0x50, (byte) 0x4E, (byte) 0x47,
			(byte) 0x0D, (byte) 0x0A, (byte) 0x1A, (byte) 0x0A}; // Avec l'autorisation de M. Hendrikx pour le cast

	private final Gson gson = new Gson();
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
				Set<Book> bookSet = new LinkedHashSet<>();
				gson.newJsonReader(reader);
				Arrays.asList(gson.fromJson(reader, BookDto[].class)).forEach(bookDto -> {
					Book book;
					try {
						book = bookDto.toBook();
					} catch (IllegalArgumentException e) {
						book = new Book(bookDto.title, bookDto.author, bookDto.summary);
					}
					bookSet.add(book);
				});
				return bookSet;
			} catch (IOException e) {
				return new LinkedHashSet<>();
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
				return false;
			}
		}
		try (BufferedWriter writer = Files.newBufferedWriter(bookPath, StandardCharsets.UTF_8, StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)) {
			Set<BookDto> bookDtos = new LinkedHashSet<>();
			books.forEach(book -> bookDtos.add(new BookDto(book)));
			gson.toJson(bookDtos, writer);
			return true;
		} catch (IOException e) {
			return false;
		}
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
		for (Book b : books) {
			if (b.equals(book)) {
				books.remove(b);
				return saveBooks(new HashSet<>(books));
			}
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

	/**
	 * Déplace l'image du livre dans le dossier d'images
	 *
	 * @param imagePath chemin de l'image
	 * @return le chemin de l'image dans le dossier d'images, null si l'image n'a pas pu être déplacée
	 */
	@Override
	public String moveImage(String imagePath) {
		Path imageStored = Path.of(imagePath);
		Path imageDestination = imageDirectoryPath.resolve(imageStored.getFileName());
		try (InputStream inputStream = new FileInputStream(imageStored.toFile());
		     OutputStream outputStream = new FileOutputStream(imageDestination.toFile())) {
			if (!imageTrueExtension(imagePath)) {
				throw new IllegalImageExtensionException("Image extension does not correspond to the image content");
			}
			byte[] buf = new byte[1024];
			int length;
			while ((length = inputStream.read(buf)) > 0) {
				outputStream.write(buf, 0, length);
			}
//			inputStream.close();
//			outputStream.close();
			return imageDestination.toString();
		} catch (IOException e) {
			return "";
		}
	}

	/**
	 * Vérifie si l'extension de l'image correspond bien aux premiers octets de l'image (jpg ou png), peut évoluer
	 * facilement dans le futur
	 *
	 * @param imagePath le chemin de l'image
	 * @return true si l'extension correspond au contenu, false sinon
	 */
	private static boolean imageTrueExtension(String imagePath) throws IOException {
		switch (imagePath.substring(imagePath.lastIndexOf('.')).toLowerCase()) {
			case ".jpg":
				try (InputStream inputStream = new FileInputStream(imagePath)) {
					byte[] buf = new byte[3];
					inputStream.read(buf);
					return Arrays.equals(buf, JPG_BYTES);
				} catch (IOException e) {
					return false;
				}
			case ".png":
				try (InputStream inputStream = new FileInputStream(imagePath)) {
					byte[] buf = new byte[8];
					inputStream.read(buf);
					return Arrays.equals(buf, PNG_BYTES);
				} catch (IOException e) {
					return false;
				}
		}
		return false;
	}
}