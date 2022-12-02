package org.helmo.gbeditor.presenters.viewmodels;

import org.helmo.gbeditor.models.Book;
import org.helmo.gbeditor.models.BookDataFields;
import org.helmo.gbeditor.models.Page;

import java.util.*;

import static org.helmo.gbeditor.models.BookDataFields.*;

public class BookViewModel {
	private final boolean isPublished;
	private AuthorViewModel author;
	private final Set<PageViewModel> pages = new LinkedHashSet<>();

	private final Map<BookDataFields, String> metadata = new LinkedHashMap<>();

	public BookViewModel(Book b) {
		metadata.put(TITLE, b.getMetadata(TITLE));
		this.author = new AuthorViewModel(b.getAuthor());
		metadata.put(SUMMARY, b.getMetadata(SUMMARY));
		metadata.put(BOOK_ISBN, b.getMetadata(BOOK_ISBN));
		metadata.put(IMAGE_PATH, b.getMetadata(IMAGE_PATH));
		this.isPublished = b.isPublished();
		for (Page p : b.getPages()) {
			pages.add(new PageViewModel(p));
		}
	}

	public String getTitle() {
		return metadata.get(TITLE);
	}

	public void setTitle(String title) {
		this.metadata.put(TITLE, title);
	}

	public AuthorViewModel getAuthor() {
		return author;
	}

	public void setAuthor(AuthorViewModel author) {
		this.author = author;
	}

	public String getSummary() {
		return metadata.get(SUMMARY);
	}

	public void setSummary(String summary) {
		this.metadata.put(SUMMARY, summary);
	}

	public String getIsbn() {
		return metadata.get(BOOK_ISBN);
	}

	public void setIsbn(String isbn) {
		this.metadata.put(BOOK_ISBN, isbn);
	}

	public String getImagePath() {
		return metadata.get(IMAGE_PATH);
	}

	public void setImagePath(String imagePath) {
		this.metadata.put(IMAGE_PATH, imagePath);
	}

	public boolean isPublished() {
		return isPublished;
	}

	public Set<PageViewModel> getPages() {
		return pages;
	}

	public Book toBook() {
		Book b = new Book(metadata.get(TITLE), author.toAuthor(), metadata.get(SUMMARY), metadata.get(BOOK_ISBN), metadata.get(IMAGE_PATH));
		for (PageViewModel p : pages) {
			b.addPage(p.toPage());
		}
		if (isPublished) {
			b.publish();
		}
		return b;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		BookViewModel that = (BookViewModel) o;
		return metadata.get(BOOK_ISBN).equals(that.metadata.get(BOOK_ISBN));
	}

	@Override
	public int hashCode() {
		return Objects.hash(metadata.get(BOOK_ISBN));
	}
}
