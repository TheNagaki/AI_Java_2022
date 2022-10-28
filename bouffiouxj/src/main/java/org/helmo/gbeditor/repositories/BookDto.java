package org.helmo.gbeditor.repositories;

import org.helmo.gbeditor.models.*;

public class BookDto {
	public String title;
	public Author author;
	public String isbn;
	public String summary;
	public String imagePath;

	public BookDto(Book book) {
		this.title = book.getTitle();
		this.author = book.getAuthor();
		this.isbn = book.getIsbn();
		this.summary = book.getSummary();
		this.imagePath = book.getImage();
	}

	public BookDto(String title, Author author, String isbn, String summary, String imagePath) {
		this.title = title;
		this.author = author;
		this.isbn = isbn;
		this.summary = summary;
		this.imagePath = imagePath;
	}

	public Book toBook() {
		if (title.length() > 150) {
			throw new IllegalBookTitleException("Title is too long");
		}
		if (summary.length() > 500) {
			throw new IllegalBookSummaryException("Summary is too long");
		}
		if (isbn.length() != 13 && isbn.length() != 10) {
			throw new IllegalIsbnFormatException(String.format("ISBN %s not valid", isbn));
		}
		if (author == null) {
			throw new IllegalArgumentException("Author is null");
		}
		if (imagePath == null) {
			imagePath = "";
		}
		return new Book(title, author, summary, isbn, imagePath);
	}
}
