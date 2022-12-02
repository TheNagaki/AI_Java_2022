package org.helmo.gbeditor.presenters;

import org.helmo.gbeditor.models.Author;
import org.helmo.gbeditor.models.Book;
import org.helmo.gbeditor.models.BookDataFields;
import org.helmo.gbeditor.models.ISBN;
import org.helmo.gbeditor.models.exceptions.IllegalIsbnLinguisticIdException;
import org.helmo.gbeditor.presenters.interfaces.EditBookViewInterface;
import org.helmo.gbeditor.presenters.interfaces.PresenterInterface;
import org.helmo.gbeditor.presenters.interfaces.ViewInterface;
import org.helmo.gbeditor.repositories.RepositoryInterface;

import static org.helmo.gbeditor.models.BookMetadata.LINGUISTIC_GROUP;

/**
 * The CreateBookPresenter is the presenter for the CreateBookView.
 * It is used to create a new book in the model.
 * It is also used to edit an existing book.
 */
public class EditBookPresenter implements PresenterInterface {
	private final RepositoryInterface repo;
	private EditBookViewInterface view;
	private Book bookEdited = null;
	private Author currentAuthor;

	/**
	 * Constructor of the presenter with the engine
	 *
	 * @param repo the repository of the application
	 */
	public EditBookPresenter(RepositoryInterface repo) {
		this.repo = repo;
	}

	/**
	 * Sends the information to the engine to create a new book
	 *
	 * @param title     the title of the book
	 * @param isbn      the isbn of the book
	 * @param summary   the summary of the book
	 * @param imagePath the path to the image of the book
	 */
	public void createBook(String title, String summary, String isbn, String imagePath) {
		try {
			var books = repo.getBooks();
			var book = new Book(title, currentAuthor, summary, isbn, ""); //check isbn in constructor
			if (!books.contains(book)) {
				String path2Image = repo.copyImage(imagePath);
				book = new Book(title, currentAuthor, summary, isbn, path2Image);
				books.add(book);
				repo.saveBooks(books);
				view.display("Votre livre a bien été enregistré");
			} else {
				view.display("Votre livre a déjà été enregistré");
			}
		} catch (IllegalIsbnLinguisticIdException e) {
			view.display(String.format("L'identifiant linguistique de l'isbn est invalide (%d attendu)", LINGUISTIC_GROUP));
		} catch (IllegalArgumentException e) {
			view.display(e.getMessage());
		}
	}

	/**
	 * Answers to the view's request to display the author's name
	 */
	public void askAuthorName() {
		this.currentAuthor = repo.getCurrentAuthor();
		view.setAuthorName(currentAuthor.getFullName());
	}

	/**
	 * Sets the view of the presenter
	 *
	 * @param view the view to set
	 */
	public void setView(EditBookViewInterface view) {
		this.view = view;
	}

	@Override
	public ViewInterface getView() {
		return view;
	}

	/**
	 * Asks the engine to preset the isbn with the author's id
	 */
	public void askISBN() {
		currentAuthor = repo.getCurrentAuthor();
		if (currentAuthor != null) {
			view.presetISBN(new int[]{LINGUISTIC_GROUP, currentAuthor.getIdentifier()});
		}
	}

	/**
	 * Asks the engine if there is a book to edit
	 */
	public void askBookToEdit() {
		this.bookEdited = repo.getBookToEdit();
		view.setEditionMode(bookEdited != null);
	}

	/**
	 * Asks the engine to update the book with the new information
	 *
	 * @param title     the new title
	 * @param summary   the new summary
	 * @param isbn      the new isbn
	 * @param imagePath the new image path
	 */
	public void editBook(String title, String summary, String isbn, String imagePath) {
		if (bookEdited != null) {
			try {
				if (imagePath != null && !imagePath.isEmpty()) {
					String path2Image = repo.copyImage(imagePath);
					bookEdited.setMetadata(BookDataFields.IMAGE_PATH, path2Image);
				}
				if (repo.deleteBook(bookEdited)) {
					bookEdited.setMetadata(BookDataFields.TITLE, title);
					bookEdited.setMetadata(BookDataFields.SUMMARY, summary);
					bookEdited.setMetadata(BookDataFields.BOOK_ISBN, isbn);
					repo.updatesAddBook(bookEdited);
					view.display("Votre livre a bien été mis à jour");
					return;
				}
			} catch (IllegalIsbnLinguisticIdException e) {
				view.display(String.format("L'identifiant linguistique de l'isbn est invalide (%d attendu)", LINGUISTIC_GROUP));
				return;
			} catch (IllegalArgumentException e) {
				view.display(e.getMessage());
				return;
			}
		}
		view.display("Une erreur est survenue lors de la mise à jour de votre livre");
	}

	/**
	 * Asks the engine to set the last number of the isbn
	 *
	 * @param isbn the isbn to check
	 */
	public void askIsbnControlNumber(String isbn) {
		view.setIsbnControlNumber(ISBN.computeCheckSum(isbn));
	}

	/**
	 * Method called when the user wants to quit the application
	 */
	public void onQuit_Clicked() {
		view.close();
	}

	/**
	 * This method is used to ask the engine the title of the book
	 */
	public void askTitle() {
		view.setTitle(bookEdited.getMetadata(BookDataFields.TITLE));
	}

	/**
	 * This method is used to ask the engine the path to the image of the book
	 */
	public void askImagePath() {
		view.setImagePath(bookEdited.getMetadata(BookDataFields.IMAGE_PATH));
	}

	/**
	 * This method is used to communicate the Summary of the book in the view
	 */
	public void askSummary() {
		view.setSummary(bookEdited.getMetadata(BookDataFields.SUMMARY));
	}

	public void askBaseIsbn() {
		var isbn = bookEdited.getIsbn();
		view.setBaseIsbn(String.format("%d-%d-", isbn.getLinguisticGroup(), isbn.getIdAuthor()));
	}

	public void askBookId() {
		view.setBookId(bookEdited.getIsbn().getIdBook());
	}
}
