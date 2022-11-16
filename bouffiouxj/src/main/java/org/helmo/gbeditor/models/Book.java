package org.helmo.gbeditor.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.util.*;

/**
 * Book class which represents a book in the GBEditor
 */
public class Book {
	private String title;
	private final Author author;
	private final ISBN isbn;
	private String summary;
	private String imagePath;
	private final Set<Page> pages = new LinkedHashSet<>();
	private static final int MAX_SUMMARY = 500;
	private static final int MAX_TITLE = 150;
	public static final int LINGUISTIC_GROUP = 2;
	private ArrayList<Page> sortedPages;

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
		this.isbn = isbn.isBlank() ? ISBN.createNewISBN(LINGUISTIC_GROUP, author.getMatricule()) : new ISBN(isbn);
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

	/**
	 * Setter for the image path of the book
	 *
	 * @param path2Image the new image path of the book
	 */
	public void setImagePath(String path2Image) {
		this.imagePath = path2Image;
	}

	/**
	 * Adds a page to the book (if it is not already in the book)
	 *
	 * @param page the page to add
	 */
	public void addPage(Page page) {
		pages.add(page);
//		orderPages();
	}

	/**
	 * Removes a page from the book (if it is in the book)
	 */
	public void removePage(Page page) {
		pages.remove(page);
		removeChoicesToPage(page);
//		orderPages();
	}

	/**
	 * Getter for the pages of the book
	 *
	 * @return the pages of the book
	 */
	public Set<Page> getPages() {
		return pages;
	}

	/**
	 * This method is used to get a c representation of the book (as a string)
	 *
	 * @return the string representation of the book
	 */
	@Override
	public String toString() {
		return String.format("Book{title='%s',\n author=%s,\n isbn=%s,\n summary='%s',\n imagePath='%s',\n" + " pages=%s}",
				title, author, isbn, summary, imagePath, pages);
	}

	/**
	 * This method is used to set a new value for the page of the book
	 *
	 * @param page the page to set
	 */
	public void updatePage(Page page) {
		pages.remove(page);
		removeChoicesToPage(page);
		pages.add(page);
		orderPages();
	}

	private void removeChoicesToPage(Page page) {
		pages.forEach(p -> {
			if (p.getChoices().containsValue(page)) {
				p.getChoices().forEach((k, v) -> {
					if (v.equals(page)) {
						p.removeChoice(k);
					}
				});
			}
		});
	}

	/**
	 * This method is used to define how the pages of the book are ordered
	 *
	 * @return a lambda expression that defines how to get a page's number
	 */
	public Callback<TableColumn.CellDataFeatures<Page, String>, ObservableValue<String>> getPageNumberFactory() {
		return param -> new SimpleStringProperty(String.valueOf(getPageNumber(param.getValue())));
	}

	private void orderPages() {
		sortedPages = new ArrayList<>();
		sortedPages.addAll(pages);
		sortedPages.sort(Comparator.comparing(page -> page.toString().length()));
	}

	/**
	 * This method is used to get the number of a page in the book
	 *
	 * @param page the page to get the number of
	 * @return the number of the page
	 */
	public int getPageNumber(Page page) {
		orderPages();
		return sortedPages.indexOf(page);
	}
}