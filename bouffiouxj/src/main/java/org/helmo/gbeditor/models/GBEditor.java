package org.helmo.gbeditor.models;

import org.helmo.gbeditor.presenters.GBEInterface;
import org.helmo.gbeditor.repositories.Repository;

import java.util.HashSet;
import java.util.Set;

/**
 * This class is the model of the application. It is the link between the presenter, the repository and the model.
 * It is the only class that can access the repository.
 */
public class GBEditor implements GBEInterface {
	private static final int LINGUISTIC_GROUP = 2;
	private final Set<Author> authors;
	private Author currentAuthor;
	private final Set<Book> books;
	private final Repository repository;
	private Book bookToEdit;

	/**
	 * Constructor of the model. It initializes the repository and the sets of authors and books from it.
	 *
	 * @param repository The repository to use.
	 */
	public GBEditor(Repository repository) {
		this.repository = repository;
		this.authors = repository.loadAuthors();
		this.books = repository.loadBooks();
	}

	@Override
	public boolean connect(String name, String firstName) {
		try {
			Author author = new Author(name, firstName);
			if (!authors.contains(new Author(name, firstName))) {
				authors.add(author);
				currentAuthor = author;
			} else {
				for (Author a : authors) {
					if (a.equals(author)) {
						currentAuthor = a;
						break;
					}
				}
			}
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	@Override
	public String createBook(String title, String summary, String isbn, String imagePath) {
		try {
			Book book = new Book(title, currentAuthor, summary, isbn, ""); //check isbn in constructor
			if (!books.contains(book)) {
				String path2Image = repository.copyImage(imagePath);
				book = new Book(title, currentAuthor, summary, isbn, path2Image);
				books.add(book);
				repository.saveBook(book);
				return "Votre livre a bien été enregistré";
			}
		} catch (IllegalIsbnLinguisticIdException e) {
			return String.format("L'identifiant linguistique de l'isbn est invalide (%d attendu)", LINGUISTIC_GROUP);
		} catch (IllegalArgumentException e) {
			return e.getMessage();
		}
		return "Votre livre a déjà été enregistré";
	}

	@Override
	public boolean deleteBook(Book book) {
		return books.remove(book) && repository.deleteBook(book);
	}

	@Override
	public void updateBook(Book book, String title, String summary, String imagePath) {
		book.setTitle(title);
		book.setSummary(summary);
		if (imagePath != null && !imagePath.isEmpty()) {
			String path2Image = repository.copyImage(imagePath);
			book.setImagePath(path2Image);
		}
		if (books.remove(book)) {
			books.add(book);
			repository.saveBook(book);
		}
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

	@Override
	public int[] presetISBN() {
		return currentAuthor != null ? new int[]{LINGUISTIC_GROUP, currentAuthor.getMatricule()} : new int[0];
	}

	@Override
	public void setBookToEdit(Book book) {
		this.bookToEdit = book;
	}

	@Override
	public Book getBookToEdit() {
		return bookToEdit;
	}

	@Override
	public void addPage(Book book, Page page) {
		book.addPage(page);
		repository.saveBook(book);
	}

	@Override
	public void removePage(Book book, Page page) {
		book.removePage(page);
		repository.saveBook(book);
	}

	@Override
	public void updatePage(Book book, Page page) {
		book.updatePage(page);
		repository.saveBook(book);
	}

	@Override
	public Set<Page> getPages(Book book) {
		return book.getPages();
	}
}
