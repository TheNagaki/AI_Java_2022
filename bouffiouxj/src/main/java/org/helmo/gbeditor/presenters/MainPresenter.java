package org.helmo.gbeditor.presenters;

import org.helmo.gbeditor.models.Book;

import java.util.HashSet;
import java.util.Set;

public class MainPresenter extends Presenter {
	public MainPresenter(GBEInterface gbEditor) {
		super(gbEditor);
	}

	private String getAuthorName() {
		return getEngine().getAuthorName() != null ? getEngine().getAuthorName() : "Prénom Nom";
	}

	public Set<Book> getBooksFromAuthor() {
		return getEngine().getBooksFromCurrentAuthor() != null ? getEngine().getBooksFromCurrentAuthor() : new HashSet<>();
	}

	public void askAuthorName() {
		getView().setAuthorName(getAuthorName());
	}
}
