package org.helmo.gbeditor.models;

import org.helmo.gbeditor.models.exceptions.IllegalIsbnLinguisticIdException;
import org.helmo.gbeditor.presenters.interfaces.GBEInterface;
import org.helmo.gbeditor.repositories.RepositoryInterface;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.helmo.gbeditor.models.BookMetadata.LINGUISTIC_GROUP;

/**
 * This class is the model of the application. It is the link between the presenter, the repository and the model.
 * It is the only class that can access the repository.
 */
public class GBEditor implements GBEInterface {
	private final Set<Author> authors;
	private Author currentAuthor;
	private final Set<Book> books;
	private final RepositoryInterface repository;
	private Book bookToEdit = null;

	/**
	 * Constructor of the model. It initializes the repository and the sets of authors and books from it.
	 *
	 * @param repository The repository to use.
	 */
	public GBEditor(RepositoryInterface repository) {
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
				repository.saveBooks(books);
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
	public String updateBook(Book book, String title, String summary, String isbn, String imagePath) {
		try {
			if (imagePath != null && !imagePath.isEmpty()) {
				String path2Image = repository.copyImage(imagePath);
				book.setMetadata(BookDataFields.IMAGE_PATH, path2Image);
			}
			if (books.remove(book)) {
				book.setMetadata(BookDataFields.TITLE, title);
				book.setMetadata(BookDataFields.SUMMARY, summary);
				book.setMetadata(BookDataFields.BOOK_ISBN, isbn);
				books.add(book);
				repository.saveBooks(books);
			}
		} catch (IllegalIsbnLinguisticIdException e) {
			return String.format("L'identifiant linguistique de l'isbn est invalide (%d attendu)", LINGUISTIC_GROUP);
		} catch (IllegalArgumentException e) {
			return e.getMessage();
		}
		return "Votre livre a bien été mis à jour";
	}

	@Override
	public Set<Book> getAllBooks() {
		return new LinkedHashSet<>(books);
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
		return new int[]{LINGUISTIC_GROUP, currentAuthor.getMatricule()};
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
	public void addPage(Book book, String content) {
		books.forEach(b -> {
			if (b.equals(book)) {
				b.addPage(new Page(content));
			}
		});
		repository.saveBooks(books);
	}

	@Override
	public void removePage(Book book, Page page) {
		books.forEach(b -> {
			if (b.equals(book)) {
				b.removePage(page);
			}
		});
		repository.saveBooks(books);
	}

	@Override
	public void updatePage(Book book, Page page) {
		books.forEach(b -> {
			if (b.equals(book)) {
				b.updatePage(page);
			}
		});
		repository.saveBooks(books);
	}

	@Override
	public Set<Page> getPages(Book book) {
		return book.getPages();
	}

	@Override
	public String getIsbnControlNum(String isbn) {
		return ISBN.computeCheckSum(isbn);
	}

	@Override
	public int getPageNumber(Book bookDisplayed, Page value) {
		for (Book book : books) {
			if (book.equals(bookDisplayed)) {
				return book.getPageNumber(value);
			}
		}
		return bookDisplayed.getPageNumber(value);
	}
}
