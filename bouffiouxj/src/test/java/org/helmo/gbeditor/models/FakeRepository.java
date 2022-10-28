package org.helmo.gbeditor.models;

import org.helmo.gbeditor.repositories.Repository;

import java.util.LinkedHashSet;
import java.util.Set;

public class FakeRepository implements Repository {
	private final Set<Book> bookCollection;

	public FakeRepository(Set<Book> bookCollection) {
		this.bookCollection = bookCollection;
	}

	@Override
	public Set<Book> loadBooks() {
		return bookCollection;
	}

	@Override
	public boolean saveBooks(Set<Book> books) {
		return true;
	}

	@Override
	public boolean saveBook(Book book) {
		if (!bookCollection.contains(book)) {
			bookCollection.add(book);
			return true;
		}
		return false;
	}

	@Override
	public boolean deleteBook(Book book) {
		if (bookCollection.contains(book)) {
			bookCollection.remove(book);
			return true;
		}
		return false;
	}

	@Override
	public Set<Author> loadAuthors() {
		Set<Author> authors = new LinkedHashSet<>();
		for (Book book : bookCollection) {
			authors.add(book.getAuthor());
		}
		return authors;
	}

	@Override
	public String moveImage(String title) {
		return "image moved";
	}
}
