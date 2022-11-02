package org.helmo.gbeditor.presenters;

import org.helmo.gbeditor.models.Book;

import java.util.Set;

/**
 * Interface for the logic of the application.
 * It knows the repository and the model.
 * It is used by the presenters to communicate with the model.
 */
public interface GBEInterface {
	/**
	 * Sets the author connected to the application based on the given name and first name.
	 * Its id is recovered from the repository if it exists, or created if it doesn't.
	 *
	 * @param name      The name of the author
	 * @param firstName The first name of the author
	 * @return true if the connection was successful, false otherwise
	 */
	boolean connect(String name, String firstName);

	/**
	 * Creates a new book with the given parameters and adds it to the database
	 *
	 * @param title     The title of the book
	 * @param summary   The summary of the book
	 * @param isbn      The ISBN of the book (can be empty)
	 * @param imagePath The path to the image of the book (can be empty)
	 * @return A message indicating the result of the operation
	 */
	String createBook(String title, String summary, String isbn, String imagePath);

	/**
	 * Deletes a book from the repository
	 *
	 * @param book the book to delete
	 * @return true if the book has been deleted, false otherwise
	 */
	boolean deleteBook(Book book);

	/**
	 * Edits a book with the given parameters and saves it in the repository
	 *
	 * @param book    the book to edit
	 * @param title   the new title
	 * @param summary the new summary
	 * @param imagePath the new image path
	 *                  (if it is empty, the image is not changed)
	 */
	void updateBook(Book book, String title, String summary, String imagePath);

	/**
	 * Gets all the books from the repository
	 *
	 * @return a set of books
	 */
	Set<Book> getAllBooks();

	/**
	 * Gets the name and firstName of the author
	 *
	 * @return the name and firstName of the author
	 */
	String getAuthorName();

	/**
	 * Gets all the books of the author connected
	 *
	 * @return a set of books
	 */
	Set<Book> getBooksFromCurrentAuthor();

	/**
	 * Presets the new book form with the author's data
	 *
	 * @return an int array [linguisticGroup, author's id]
	 */
	int[] presetISBN();

	void setBookToEdit(Book book);

	Book getBookToEdit();
}
