package org.helmo.gbeditor.presenters;

import org.helmo.gbeditor.models.Book;

import java.util.Set;

public interface GBEInterface {
	boolean connect(String name, String firstName);

	String createBook(String title, String summary, String isbn, String imagePath);

	boolean deleteBook(Book book);

	boolean updateBook(Book book, String title, String summary);

	Set<Book> getAllBooks();

	String getAuthorName();

	Set<Book> getBooksFromCurrentAuthor();

	int[] presetISBN();
}
