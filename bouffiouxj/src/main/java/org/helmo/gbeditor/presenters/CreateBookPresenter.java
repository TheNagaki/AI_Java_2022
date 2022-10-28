package org.helmo.gbeditor.presenters;

/**
 * The CreateBookPresenter is the presenter for the CreateBookView.
 * It is used to create a new book in the model.
 * It is also used to edit an existing book. //TODO: implement the edit feature
 */
public class CreateBookPresenter implements Presenter {
	private final GBEInterface engine;
	private CreateBookViewInterface view;

	/**
	 * Constructor of the presenter with the engine
	 * @param editor the logic of the application
	 */
	public CreateBookPresenter(GBEInterface editor) {
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
	public void setView(CreateBookViewInterface view) {
		this.view = view;
	}

	@Override
	public GBEInterface getEngine() {
		return engine;
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
}
