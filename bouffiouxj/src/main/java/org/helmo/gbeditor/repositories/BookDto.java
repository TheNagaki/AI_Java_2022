package org.helmo.gbeditor.repositories;

import org.helmo.gbeditor.models.*;

/**
 * BookDto is a data transfer object for the Book class
 * It is used to transfer data from the repository to the GBEInterface and vice versa
 */
public class BookDto {
	public String title;
	public Author author;
	public String isbn;
	public String summary;
	public String imagePath;

	/**
	 * BookDto constructor from a book
	 *
	 * @param book the book to convert
	 */
	public BookDto(Book book) {
		this.title = book.getTitle();
		this.author = book.getAuthor();
		this.isbn = book.getIsbn().toString();
		this.summary = book.getSummary();
		this.imagePath = book.getImagePath();
	}

	/**
	 * Create a book from the DTO
	 *
	 * @return the book
	 */
	public Book toBook() {
		if (author == null) {
			throw new IllegalArgumentException("Author is null");
		}
		if (imagePath == null) {
			imagePath = "";
		}
		return new Book(title, author, summary, isbn, imagePath);
	}
}
