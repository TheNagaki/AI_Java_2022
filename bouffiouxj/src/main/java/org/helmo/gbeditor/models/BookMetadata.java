package org.helmo.gbeditor.models;

import org.helmo.gbeditor.models.exceptions.FieldNotValidException;
import org.helmo.gbeditor.models.exceptions.IllegalAuthorException;
import org.helmo.gbeditor.models.exceptions.IllegalBookSummaryException;
import org.helmo.gbeditor.models.exceptions.IllegalBookTitleException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static org.helmo.gbeditor.models.BookDataFields.*;

/**
 * BookMetadata class which represents the metadata of a book
 */
public class BookMetadata {

	protected static final int MAX_SUMMARY = 500;
	protected static final int MAX_TITLE = 150;
	public static final int LINGUISTIC_GROUP = 2;
	private final Author author;
	private ISBN isbn;

	private final Map<BookDataFields, String> metadata = new LinkedHashMap<>();

	/**
	 * Constructor of the BookMetadata class
	 *
	 * @param title     the title of the book
	 * @param author    the author of the book
	 * @param isbn      the isbn of the book
	 * @param summary   the summary of the book
	 * @param imagePath the path of the image of the book
	 */
	public BookMetadata(String title, Author author, String isbn, String summary, String imagePath) {
		setTitle(title);
		checkAuthor(author);
		this.author = author;
		metadata.put(AUTHOR_MATRICULE, String.format("%06d", author.getIdentifier()));
		setIsbn(isbn);
		setSummary(summary);
		metadata.put(IMAGE_PATH, imagePath == null ? "" : imagePath);
	}

	/**
	 * Gets a metadata field
	 *
	 * @param fieldName the field to get
	 * @return the value of the field
	 */
	public String getField(BookDataFields fieldName) {
		return metadata.get(fieldName);
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
				setTitle(value);
				break;
			case SUMMARY:
				setSummary(value);
				break;
			case BOOK_ISBN:
				setIsbn(value);
				break;
			case IMAGE_PATH:
				metadata.put(fieldName, value == null ? "" : value);
				break;
			default:
				throw new FieldNotValidException(fieldName);
		}
	}

	private void setIsbn(String value) {
		isbn = value == null || value.isBlank() ? ISBN.createNewISBN(LINGUISTIC_GROUP, author.getIdentifier()) : new ISBN(value);
		metadata.put(BOOK_ISBN, isbn.toString());
	}

	private void setSummary(String value) {
		checkSummary(value);
		metadata.put(SUMMARY, value);
	}

	private void setTitle(String value) {
		checkTitle(value);
		metadata.put(TITLE, value);
	}

	private static void checkTitle(String title) {
		if (title == null || title.length() > MAX_TITLE || title.length() < 1 || title.isBlank()) {
			throw new IllegalBookTitleException();
		}
	}

	private static void checkSummary(String summary) {
		if (summary == null || summary.length() > MAX_SUMMARY || summary.length() < 1 || summary.isBlank()) {
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
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
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
				"title='" + metadata.get(TITLE) + '\'' +
				", author=" + author +
				", isbn=" + isbn +
				", summary='" + metadata.get(SUMMARY) + '\'' +
				", imagePath='" + metadata.get(IMAGE_PATH) + '\'' +
				'}';
	}
}
