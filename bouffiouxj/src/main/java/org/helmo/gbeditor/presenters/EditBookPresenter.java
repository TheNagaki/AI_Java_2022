package org.helmo.gbeditor.presenters;

import org.helmo.gbeditor.models.Book;
import org.helmo.gbeditor.models.BookDataFields;
import org.helmo.gbeditor.models.ISBN;
import org.helmo.gbeditor.presenters.interfaces.EditBookViewInterface;
import org.helmo.gbeditor.presenters.interfaces.GBEInterface;
import org.helmo.gbeditor.presenters.interfaces.PresenterInterface;
import org.helmo.gbeditor.presenters.interfaces.ViewInterface;

/**
 * The CreateBookPresenter is the presenter for the CreateBookView.
 * It is used to create a new book in the model.
 * It is also used to edit an existing book.
 */
public class EditBookPresenter implements PresenterInterface {
	private final GBEInterface engine;
	private EditBookViewInterface view;
	private Book bookEdited = null;

	/**
	 * Constructor of the presenter with the engine
	 *
	 * @param editor the logic of the application
	 */
	public EditBookPresenter(GBEInterface editor) {
		this.engine = editor;
	}

	/**
	 * Sends the information to the engine to create a new book
	 *
	 * @param title     the title of the book
	 * @param isbn      the isbn of the book
	 * @param summary   the summary of the book
	 * @param imagePath the path to the image of the book
	 */
	public void createBook(String title, String isbn, String summary, String imagePath) {
		view.display(engine.createBook(title, summary, isbn, imagePath));
	}

	/**
	 * Answers to the view's request to display the author's name
	 */
	public void askAuthorName() {
		view.setAuthorName(engine.getAuthorName());
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
		view.presetISBN(engine.presetISBN());
	}

	/**
	 * Asks the engine if there is a book to edit
	 */
	public void askBookToEdit() {
		this.bookEdited = engine.getBookToEdit();
		view.setEditionMode(bookEdited != null);
	}

	/**
	 * Asks the engine to update the book with the new information
	 *
	 * @param title     the new title
	 * @param summary   the new summary
	 * @param imagePath the new image path
	 */
	public void editBook(String title, String summary, String isbn, String imagePath) {
		view.display(engine.updateBook(bookEdited, title, summary, isbn, imagePath));
	}

	/**
	 * Asks the engine to set the last number of the isbn
	 *
	 * @param isbn the isbn to check
	 */
	public void askIsbnControlNumber(String isbn) {
		view.setIsbnControlNumber(engine.getIsbnControlNum(isbn));
	}

	/**
	 * Method called when the user wants to quit the application
	 */
	public void onQuit_Clicked() {
		view.close();
	}

	public String getImagePath() {
		return bookEdited.getMetadata(BookDataFields.IMAGE_PATH);
	}

	public String getTitle() {
		return bookEdited.getMetadata(BookDataFields.TITLE);
	}

	public ISBN getIsbn() {
		return bookEdited.getIsbn();
	}

	public String getSummary() {
		return bookEdited.getMetadata(BookDataFields.SUMMARY);
	}
}
