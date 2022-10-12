package org.helmo.gbeditor.models;

import java.util.Objects;

public class Book {

	private String title;
	private final Author author;
	private final String isbn;
	private String summary;
	private final String imagePath;

	public Book(String title, Author author, String isbn, String summary) {
		if (title.length() > 150) {
			throw new IllegalArgumentException("Title is too long");
		}
		if (summary.length() > 500) {
			throw new IllegalArgumentException("Summary is too long");
		}
		this.title = title;
		this.author = author;
		this.isbn = isbn;
		this.summary = summary;
		imagePath = "";
	}

	public Book(String title, Author author, String isbn, String summary, String imagePath) {
		if (title.length() > 150) {
			throw new IllegalArgumentException("Title is too long");
		}
		if (summary.length() > 500) {
			throw new IllegalArgumentException("Summary is too long");
		}
		this.title = title;
		this.author = author;
		this.isbn = isbn;
		this.summary = summary;
		this.imagePath = imagePath;
	}

	public void setTitle(String title) {
		if (title.length() > 150) {
			throw new IllegalArgumentException("Title is too long");
		}
		this.title = title;
	}

	public void setSummary(String summary) {
		if (summary.length() > 500) {
			throw new IllegalArgumentException("Summary is too long");
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
		return isbn;
	}

	public String getSummary() {
		return summary;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Book book = (Book) o;
		return Objects.equals(isbn, book.isbn);
	}

	@Override
	public int hashCode() {
		return Objects.hash(isbn);
	}

	public String getImage() {
		return imagePath;
	}
}
