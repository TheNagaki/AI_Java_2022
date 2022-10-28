package org.helmo.gbeditor.presenters;

import org.helmo.gbeditor.models.Book;

import java.util.HashSet;
import java.util.Set;

public class MainPresenter implements Presenter {
	private final GBEInterface engine;
	private ViewInterface view;

	public MainPresenter(GBEInterface gbEditor) {
		this.engine = gbEditor;
	}

	private String getAuthorName() {
		return engine.getAuthorName() != null ? getEngine().getAuthorName() : "Prénom Nom";
	}

	public Set<Book> getBooksFromAuthor() {
		return engine.getBooksFromCurrentAuthor() != null ? engine.getBooksFromCurrentAuthor() : new HashSet<>();
	}

	public void askAuthorName() {
		view.setAuthorName(engine.getAuthorName());
	}

	public void setView(ViewInterface view) {
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

	public boolean deleteBook(Book b) {
		return engine.deleteBook(b);
	}
}
