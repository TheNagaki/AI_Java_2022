package org.helmo.gbeditor.presenters.interfaces;

import org.helmo.gbeditor.models.Book;
import org.helmo.gbeditor.models.Page;

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
	 * @param book      the book to edit
	 * @param title     the new title
	 * @param summary   the new summary
	 * @param isbn      the new isbn
	 * @param imagePath the new image path
	 *                  (if it is empty, the image is not changed)
	 * @return A message indicating the result of the operation
	 */
	String updateBook(Book book, String title, String summary, String isbn, String imagePath);

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

	/**
	 * Sets the book to edit
	 *
	 * @param book the book to edit
	 */
	void setBookToEdit(Book book);

	/**
	 * Gets the book to edit
	 *
	 * @return the book to edit, null if there is no book to edit
	 */
	Book getBookToEdit();

	/**
	 * Creates a new page with the given parameters and adds it to the database and the book
	 *
	 * @param book the book to add the page to
	 * @param text the content of the new page
	 */
	void addPage(Book book, String text);

	/**
	 * Deletes a page from the repository and the book
	 *
	 * @param book the book to delete the page from
	 * @param page the page to delete
	 */
	void removePage(Book book, Page page);

	/**
	 * Updates a page with the given parameters and saves it in the repository and the book
	 *
	 * @param book the book to update the page in
	 * @param page the page to update
	 */
	void updatePage(Book book, Page page);

	/**
	 * Gets all the pages of a book
	 *
	 * @param book the book to get the pages from
	 * @return a set of pages
	 */
	Set<Page> getPages(Book book);

	/**
	 * Gets the control number of the ISBN
	 *
	 * @param isbn the first caracters of the isbn [linguisticGroup, author's id, book's id]
	 * @return the control number of the ISBN
	 */
	String getIsbnControlNum(String isbn);

	/**
	 *  This method is used to get the page number of a page in a book
	 *
	 * @param bookDisplayed the book in which the page is
	 * @param value the page to get the number from
	 * @return the page number of the page
	 */
	int getPageNumber(Book bookDisplayed, Page value);
}
