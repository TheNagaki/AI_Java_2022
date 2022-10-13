package org.helmo.gbeditor.repositories;

import org.helmo.gbeditor.models.Author;
import org.helmo.gbeditor.models.Book;

import java.util.Set;

public interface Repository {

	Set<Book> loadBooks();

	boolean saveBooks(Set<Book> books);

	boolean saveBook(Book book);

	boolean deleteBook(Book book);

	Set<Author> loadAuthors();

	String moveImage(String title);
}
