package org.helmo.gbeditor.repositories;

import org.helmo.gbeditor.models.Author;
import org.helmo.gbeditor.models.Book;

import java.util.Set;

/**
 * Repository interface is used to define the methods that will be used to save and load data from any kind of storage.
 * It is used by the GBEInterface.
 */
public interface Repository {

	/**
	 * Load all the books from the storage.
	 *
	 * @return a Set of Book
	 */
	Set<Book> loadBooks();

	/**
	 * Save a set of books in the storage.
	 *
	 * @param books The set of books to save.
	 * @return true if the operation was successful, false otherwise.
	 */
	boolean saveBooks(Set<Book> books);

	/**
	 * Save a book in the repository
	 *
	 * @param book the book to save
	 * @return true if the book has been saved, false otherwise
	 */
	boolean saveBook(Book book);

	/**
	 * Delete a book from the repository
	 *
	 * @param book the book to delete
	 * @return true if the book has been deleted, false otherwise
	 */
	boolean deleteBook(Book book);

	/**
	 * Load all the authors from the storage.
	 *
	 * @return a set of authors
	 */
	Set<Author> loadAuthors();

	/**
	 * Copies the image from the temporary folder to the image folder.
	 *
	 * @param imagePath The imagePath of the book.
	 * @return The path of the image.
	 */
	String copyImage(String imagePath);
}
