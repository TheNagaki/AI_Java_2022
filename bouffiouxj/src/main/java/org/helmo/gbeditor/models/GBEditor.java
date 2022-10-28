package org.helmo.gbeditor.models;

import org.helmo.gbeditor.presenters.GBEInterface;
import org.helmo.gbeditor.repositories.IllegalImageExtensionException;
import org.helmo.gbeditor.repositories.Repository;

import java.util.HashSet;
import java.util.Set;

public class GBEditor implements GBEInterface {
	private final int LINGUISTIC_GROUP = 2;
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
				String path2Image = repository.moveImage(imagePath);
				book = new Book(title, currentAuthor, summary, isbn, path2Image);
				books.add(book);
				repository.saveBook(book);
				return "Votre livre a bien été enregistré";
			}
		} catch (IllegalBookSummaryException e) {
			return "Le résumé du livre doit avoir une taille comprise entre 1 et 500 caractères";
		} catch (IllegalBookTitleException e) {
			return "Le titre du livre doit avoir une taille comprise entre 1 et 150 caractères";
		} catch (IllegalIsbnBookIdException e) {
			return "L'isbn du livre est invalide ou déjà utilisée";
		} catch (IllegalIsbnFormatException e) {
			return "L'isbn du livre est invalide";
		} catch (IllegalIsbnChecksumException e) {
			return String.format("La valeur de contrôle de l'isbn est invalide (%d attendue)", e.getExpectedChecksum());
		} catch (IllegalIsbnLinguisticIdException e) {
			return String.format("L'identifiant linguistique de l'isbn est invalide (%d attendu)", LINGUISTIC_GROUP);
		} catch (IllegalImageExtensionException e) {
			return "L'extension de l'image choisie ne correspond pas à son contenu";
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

	@Override
	public int[] presetISBN() {
		return currentAuthor != null ? new int[]{LINGUISTIC_GROUP, currentAuthor.getMatricule()} : new int[0];
	}
}
