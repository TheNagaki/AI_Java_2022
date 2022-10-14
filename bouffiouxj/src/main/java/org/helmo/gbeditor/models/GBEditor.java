package org.helmo.gbeditor.models;

import org.helmo.gbeditor.presenters.GBEInterface;
import org.helmo.gbeditor.repositories.Repository;

import java.util.HashSet;
import java.util.Set;

public class GBEditor implements GBEInterface {
	private final Set<Author> authors;
	private Author currentAuthor;
	private final Set<Book> books;
	private final Repository repository;

	public GBEditor(Repository repository) {
		this.repository = repository;
		this.authors = repository.loadAuthors();
		this.books = repository.loadBooks();
	}

	@Override
	public boolean connect(String name, String firstName) {
		Author author = new Author(name, firstName);
		if (!authors.contains(new Author(name, firstName))) {
			authors.add(author);
		}
		currentAuthor = author;
		return true;
	}

	@Override
	public boolean createBook(String title, String isbn, String summary, String imagePath) {
		Book book = new Book(title, currentAuthor, isbn, summary, ""); //check isbn in constructor
		if (!books.contains(book)) {
			String path2Image = repository.moveImage(imagePath);
			book = new Book(title, currentAuthor, isbn, summary, path2Image);
			books.add(book);
			repository.saveBook(book);
			return true;
		}
		return false;
	}

	@Override
	public boolean deleteBook(Book book) {
		return books.remove(book) && repository.deleteBook(book);
	}

	@Override
	public boolean updateBook(Book book, String title, String summary) {
		book.setTitle(title);
		book.setSummary(summary);
		return books.add(book) && repository.saveBook(book);
	}

	@Override
	public Set<Book> getAllBooks() {
		return books;
	}

	@Override
	public Set<Book> getBooksFromCurrentAuthor() {
		Set<Book> result = new HashSet<>();
		for (Book book : books) {
			if (book.getAuthor().equals(currentAuthor)) {
				result.add(book);
			}
		}
		return result;
	}

	@Override
	public String getAuthorName() {
		return currentAuthor != null ? currentAuthor.getFullName() : null;
	}
}
