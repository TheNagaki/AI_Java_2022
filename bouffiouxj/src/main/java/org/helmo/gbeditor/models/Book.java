package org.helmo.gbeditor.models;

import java.util.Objects;

public class Book {

	private String title;
	private final Author author;
	private final ISBN isbn;
	private String summary;
	private final String imagePath;

	public Book(String title, Author author, String summary) {
		this(title, author, summary, "", "");
	}

	public Book(String title, Author author, String summary, String isbn) {
		this(title, author, summary, isbn, "");
	}

	public Book(String title, Author author, String summary, String isbn, String imagePath) {
		if (title.length() > 150 || title.length() < 1) {
			throw new IllegalBookTitleException("Title length should be between 1 and 150");
		}
		if (summary.length() > 500 || summary.length() < 1) {
			throw new IllegalBookSummaryException("Summary length should be between 1 and 500");
		}
		this.title = title;
		this.author = author;
		this.isbn = isbn.trim().isEmpty() ? ISBN.createNewISBN(2, author.getMatricule()) : new ISBN(isbn);
		this.summary = summary;
		this.imagePath = imagePath;
	}

	public void setTitle(String title) {
		if (title.length() > 150) {
			throw new IllegalBookTitleException("Title is too long");
		}
		this.title = title;
	}

	public void setSummary(String summary) {
		if (summary.length() > 500) {
			throw new IllegalBookSummaryException("Summary is too long");
		}
		this.summary = summary;
	}

	public String getTitle() {
		return title;
	}

	public Author getAuthor() {
		return author;
	}

	public String getIsbn() {
		return isbn.toString();
	}

	public String getSummary() {
		return summary;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Book book = (Book) o;
		return Objects.equals(isbn.toString(), book.isbn.toString());
	}

	@Override
	public int hashCode() {
		return Objects.hash(isbn);
	}

	public String getImage() {
		return imagePath;
	}
}