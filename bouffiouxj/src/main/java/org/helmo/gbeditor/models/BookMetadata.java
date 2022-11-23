package org.helmo.gbeditor.models;

import org.helmo.gbeditor.models.exceptions.FieldNotValidException;
import org.helmo.gbeditor.models.exceptions.IllegalAuthorException;
import org.helmo.gbeditor.models.exceptions.IllegalBookSummaryException;
import org.helmo.gbeditor.models.exceptions.IllegalBookTitleException;

import java.util.Objects;

public class BookMetadata {

	protected static final int MAX_SUMMARY = 500;
	protected static final int MAX_TITLE = 150;

	public static final int LINGUISTIC_GROUP = 2;
	private String title;
	private final Author author;
	private ISBN isbn;
	private String summary;
	private String imagePath;

	public BookMetadata(String title, Author author, String isbn, String summary, String imagePath) {
		checkTitle(title);
		this.title = title;
		checkAuthor(author);
		this.author = author;
		this.isbn = isbn.isBlank() ? ISBN.createNewISBN(LINGUISTIC_GROUP, author.getMatricule()) : new ISBN(isbn);
		checkSummary(summary);
		this.summary = summary;
		this.imagePath = imagePath == null ? "" : imagePath;
	}

	/**
	 * Gets a metadata field
	 *
	 * @param fieldName the field to get
	 * @return the value of the field
	 */
	public String getField(BookDataFields fieldName) {
		switch (fieldName) {
			case TITLE:
				return title;
			case AUTHOR_MATRICULE:
				return author.getMatricule() + "";
			case SUMMARY:
				return summary;
			case IMAGE_PATH:
				return imagePath;
			case ISBN:
				return isbn.toString();
			default:
				throw new FieldNotValidException(fieldName);
		}
	}

	public Author getAuthor() {
		return author;
	}

	/**
	 * Sets a metadata field to the given value
	 *
	 * @param fieldName the field to set
	 * @param value     the value to set
	 */
	public void setField(BookDataFields fieldName, String value) {
		switch (fieldName) {
			case TITLE:
				checkTitle(value);
				title = value;
				break;
			case SUMMARY:
				checkSummary(value);
				summary = value;
				break;
			case ISBN:
				isbn = new ISBN(value);
				break;
			case IMAGE_PATH:
				imagePath = value;
				break;
			default:
				throw new FieldNotValidException(fieldName);
		}
	}

	private static void checkTitle(String title) {
		if (title == null || title.length() > MAX_TITLE || title.length() < 1 || title.isBlank()) {
			throw new IllegalBookTitleException();
		}
	}

	private static void checkSummary(String summary) {
		if (summary == null || summary.length() > MAX_SUMMARY || summary.length() < 1) {
			throw new IllegalBookSummaryException();
		}
	}

	private void checkAuthor(Author author) {
		if (author == null) {
			throw new IllegalAuthorException();
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		BookMetadata that = (BookMetadata) o;
		return isbn.equals(that.isbn);
	}

	@Override
	public int hashCode() {
		return Objects.hash(isbn);
	}

	public ISBN getIsbn() {
		return isbn;
	}

	@Override
	public String toString() {
		return "BookMetadata{" +
				"title='" + title + '\'' +
				", author=" + author +
				", isbn=" + isbn +
				", summary='" + summary + '\'' +
				", imagePath='" + imagePath + '\'' +
				'}';
	}
}
