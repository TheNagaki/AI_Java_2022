package org.helmo.gbeditor.repositories;

import org.helmo.gbeditor.models.Author;
import org.helmo.gbeditor.models.Book;

import java.nio.file.Path;
import java.util.Set;

public interface Repository {

	Set<Book> loadBooks(Path path);

	boolean saveBooks(Set<Book> books, Path path);

	boolean saveBook(Book book, Path path);

	boolean deleteBook(Book book, Path path);

	Set<Author> loadAuthors(Path path);

	String moveImage(String imagePath, String title);
}
