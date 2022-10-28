package org.helmo.gbeditor.presenters;

public class CreateBookPresenter implements Presenter {
	private final GBEInterface engine;
	private CreateBookViewInterface view;

	public CreateBookPresenter(GBEInterface editor) {
		this.engine = editor;
	}

	public void createBook(String title, String isbn, String summary, String imagePath) {
		view.display(engine.createBook(title, summary, isbn, imagePath));
	}

	public void askAuthorName() {
		view.setAuthorName(engine.getAuthorName());
	}

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

	public void askISBN() {
		view.presetISBN(engine.presetISBN());
	}
}
