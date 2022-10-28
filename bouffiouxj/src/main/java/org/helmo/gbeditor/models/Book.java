package org.helmo.gbeditor.models;

import java.util.Objects;

/**
 * Book class which represents a book in the GBEditor
 */
public class Book {
	private String title;
	private final Author author;
	private final ISBN isbn;
	private String summary;
	private final String imagePath;

	private static final int MAX_SUMMARY = 500;
	private static final int MAX_TITLE = 150;

	/**
	 * Constructor of the Book class (computes the ISBN later)
	 *
	 * @param title   the title of the book
	 * @param author  the author of the book
	 * @param summary the summary of the book
	 */
	public Book(String title, Author author, String summary) {
		this(title, author, summary, "", "");
	}

	/**
	 * Constructor of the Book class with an ISBN
	 *
	 * @param title   the title of the book
	 * @param author  the author of the book
	 * @param summary the summary of the book
	 * @param isbn    the isbn of the book
	 */
	public Book(String title, Author author, String summary, String isbn) {
		this(title, author, summary, isbn, "");
	}

	/**
	 * Constructor of the Book class with an ISBN and an image path
	 *
	 * @param title     the title of the book
	 * @param author    the author of the book
	 * @param summary   the summary of the book
	 * @param isbn      the isbn of the book
	 * @param imagePath the path of the image of the book
	 */
	public Book(String title, Author author, String summary, String isbn, String imagePath) {
		checkTitle(title);
		checkSummary(summary);
		this.title = title;
		this.author = author;
		this.isbn = isbn.isBlank() ? ISBN.createNewISBN(2, author.getMatricule()) : new ISBN(isbn);
		this.summary = summary;
		this.imagePath = imagePath;
	}

	/**
	 * Setter for the title of the book
	 *
	 * @param title the new title of the book
	 */
	public void setTitle(String title) {
		checkTitle(title);
		this.title = title;
	}

	private static void checkTitle(String title) {
		if (title.length() > MAX_TITLE || title.length() < 1) {
			throw new IllegalBookTitleException();
		}
	}

	/**
	 * Setter for the summary of the book
	 *
	 * @param summary the new summary of the book
	 */
	public void setSummary(String summary) {
		checkSummary(summary);
		this.summary = summary;
	}

	private static void checkSummary(String summary) {
		if (summary.length() > MAX_SUMMARY || summary.length() < 1) {
			throw new IllegalBookSummaryException();
		}
	}

	/**
	 * Getter for the title of the book
	 *
	 * @return the title of the book
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Getter for the author of the book
	 *
	 * @return the author of the book
	 */
	public Author getAuthor() {
		return author;
	}

	/**
	 * Getter for the ISBN of the book
	 *
	 * @return the ISBN of the book
	 */
	public String getIsbn() {
		return isbn.toString();
	}

	/**
	 * Getter for the summary of the book
	 *
	 * @return the summary of the book
	 */
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

	/**
	 * Getter for the image path of the book
	 *
	 * @return the image path of the book, or an empty string if there is no image
	 */
	public String getImage() {
		return imagePath;
	}
}