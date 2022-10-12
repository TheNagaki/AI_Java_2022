package org.helmo.gbeditor.presenters;

public abstract class Presenter {
	private final GBEInterface engine;
	private ViewInterface view;

	public Presenter(GBEInterface engine) {
		this.engine = engine;
	}

	public void setView(ViewInterface view) {
		this.view = view;
	}

	public GBEInterface getEngine() {
		return engine;
	}

	public ViewInterface getView() {
		return view;
	}
}
