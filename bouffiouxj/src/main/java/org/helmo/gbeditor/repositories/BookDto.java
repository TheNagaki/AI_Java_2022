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
		this.isbn = book.getIsbn();
		this.summary = book.getSummary();
		this.imagePath = book.getImage();
	}

	/**
	 * BookDto constructor
	 *
	 * @param title     the title of the book
	 * @param author    the author of the book
	 * @param isbn      the isbn of the book
	 * @param summary   the summary of the book
	 * @param imagePath the path of the image of the book
	 */
	public BookDto(String title, Author author, String isbn, String summary, String imagePath) {
		this.title = title;
		this.author = author;
		this.isbn = isbn;
		this.summary = summary;
		this.imagePath = imagePath;
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
