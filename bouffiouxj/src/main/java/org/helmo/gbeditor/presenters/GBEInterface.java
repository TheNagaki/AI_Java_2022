package org.helmo.gbeditor.presenters;

import org.helmo.gbeditor.models.Book;

import java.util.Set;

public interface GBEInterface {
	boolean connect(String name, String firstName);

	boolean createBook(String title, String isbn, String summary, String imagePath);

	boolean deleteBook(Book book);

	boolean updateBook(Book book, String title, String summary);

	Set<Book> getAllBooks();

	String getAuthorName();

	Set<Book> getBooksFromCurrentAuthor();
}
