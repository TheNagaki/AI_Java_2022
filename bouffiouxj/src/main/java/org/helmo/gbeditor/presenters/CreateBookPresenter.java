package org.helmo.gbeditor.presenters;

public class CreateBookPresenter extends Presenter {
	public CreateBookPresenter(GBEInterface editor) {
		super(editor);
	}

	public void createBook(String title, String isbn, String summary, String imagePath) {
		if (getEngine().createBook(title, isbn, summary, imagePath)) {
			getView().display(String.format("Book %s created.", title));
		} else {
			getView().display("Book not created.");
		}
	}

	public String getAuthorName() {
		return getEngine().getAuthorName();
	}
}
