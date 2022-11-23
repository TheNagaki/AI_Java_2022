package org.helmo.gbeditor.models;

import org.helmo.gbeditor.models.exceptions.*;

import java.util.*;

/**
 * Book class which represents a book in the GBEditor
 */
public class Book {
	private final BookMetadata metadata;
	private final Set<Page> pages = new LinkedHashSet<>();

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
		this.metadata = new BookMetadata(title, author, isbn, summary, imagePath);
	}

	/**
	 * Sets a metadata field to the given value
	 *
	 * @param field the field to set
	 * @param value the value to set
	 */
	public void setMetadata(BookDataFields field, String value) {
		this.metadata.setField(field, value);
	}

	/**
	 * Gets the value of a metadata field
	 *
	 * @param field the field to get (see BookDataFields)
	 * @return the value of the field
	 */
	public String getMetadata(BookDataFields field) {
		return this.metadata.getField(field);
	}

	/**
	 * Gets the Author of the book
	 *
	 * @return the author of the book
	 */
	public Author getAuthor() {
		return this.metadata.getAuthor();
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
		return metadata.equals(book.metadata);
	}

	@Override
	public int hashCode() {
		return Objects.hash(metadata);
	}

	/**
	 * Adds a page to the book (if it is not already in the book)
	 *
	 * @param page the page to add
	 */
	public void addPage(Page page) {
		if (page != null) {
			pages.add(page);
		} else {
			throw new IllegalPageException();
		}
	}

	/**
	 * Removes a page from the book (if it is in the book)
	 */
	public void removePage(Page page) {
		pages.remove(page);
		removeChoicesToPage(page);
	}

	/**
	 * Getter for the pages of the book
	 *
	 * @return the pages of the book
	 */
	public Set<Page> getPages() {
		return new LinkedHashSet<>(pages);
	}

	/**
	 * This method is used to get a c representation of the book (as a string)
	 *
	 * @return the string representation of the book
	 */
	@Override
	public String toString() {
		return String.format("Book{%s" + " pages=%s}",
				metadata, pages);
	}

	/**
	 * This method is used to set a new value for the page of the book
	 *
	 * @param page the page to set
	 */
	public void updatePage(Page page) {
		var oldPages = new HashSet<>(pages);
		for (Page p : oldPages) {
			if (p.equals(page)) {
				pages.remove(p);
				pages.add(page);
				updateChoicesToPage(page);
				break;
			}
		}
	}

	private void removeChoicesToPage(Page page) {
		pages.stream().filter(p -> p.getChoices().containsValue(page))
				.forEach(p -> p.getChoices().forEach((k, v) -> {
					if (v.equals(page)) {
						p.removeChoice(k);
					}
				}));
	}

	private void updateChoicesToPage(Page page) {
		pages.stream().filter(p -> p.getChoices().containsValue(page))
				.forEach(p -> p.getChoices().forEach((k, v) -> {
					if (v.equals(page)) {
						p.updateChoice(k, page);
					}
				}));
	}

	private List<Page> orderPages() {
		var sortedPages = new ArrayList<>(pages);
		sortedPages.sort(Comparator.comparing(page -> page.toString().length())); //TODO: make a better comparator
		return sortedPages;
	}

	/**
	 * This method is used to get the number of a page in the book
	 *
	 * @param page the page to get the number of
	 * @return the number of the page corresponding to its position in the book (starting at 1)
	 */
	public int getPageNumber(Page page) {
		//Le contains fait planter mon app...
		for (Page p : pages) {
			if (p.equals(page)) {
				return orderPages().indexOf(p) + 1;
			}
		}
		throw new PageNotInBookException();
	}

	/**
	 * Gets a page by its id
	 *
	 * @param id the id of the page
	 * @return the page with the given id
	 */
	public Page getPageById(String id) {
		for (var p : pages) {
			if (Objects.equals(p.getId(), id)) {
				return p;
			}
		}
		return null;
	}

	/**
	 * Gets the isbn of the book
	 *
	 * @return the isbn of the book
	 */
	public ISBN getIsbn() {
		return this.metadata.getIsbn();
	}
}