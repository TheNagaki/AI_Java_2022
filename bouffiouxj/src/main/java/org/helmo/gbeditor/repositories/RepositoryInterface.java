package org.helmo.gbeditor.repositories;

import org.helmo.gbeditor.models.Author;
import org.helmo.gbeditor.models.Book;

import java.util.Set;

/**
 * Repository interface is used to define the methods that will be used to save and load data from any kind of storage.
 * It is used by the GBEInterface.
 * <hr>
 * Je n'ai pas à présenter le cours de Structure de Données, c'est pourquoi mes méthodes sont assez concises.
 */
public interface RepositoryInterface {

	/**
	 * Load all the books from the storage.
	 *
	 * @return a Set of Book
	 */
	Set<Book> getBooks();

	/**
	 * Save a set of books in the storage.
	 *
	 * @param books The set of books to save.
	 * @return true if the operation was successful, false otherwise.
	 */
	boolean saveBooks(Set<Book> books);

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
	Set<Author> getAuthors();

	/**
	 * Copies the image from the temporary folder to the image folder.
	 *
	 * @param imagePath The imagePath of the book.
	 * @return The path of the image.
	 */
	String copyImage(String imagePath);

	/**
	 * Sets the current author in the repository.
	 *
	 * @param author The author to set.
	 */
	void setCurrentAuthor(Author author);

	/**
	 * Gets the current author in the repository.
	 *
	 * @return The current author.
	 */
	Author getCurrentAuthor();

	/**
	 * Gets the book to edit.
	 *
	 * @return The book to edit.
	 */
	Book getBookToEdit();

	/**
	 * Sets the book to edit.
	 *
	 * @param book The book to edit.
	 */
	void setBookToEdit(Book book);

	/**
	 * Gets all the books of the current author.
	 * @return A set of books.
	 */
	Set<Book> getBooksFromAuthor(Author currentAuthor);

	Book getBook(String isbn);
}
