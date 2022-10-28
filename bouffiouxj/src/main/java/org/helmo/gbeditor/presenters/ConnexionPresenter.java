package org.helmo.gbeditor.presenters;

import org.helmo.gbeditor.views.ConnexionView;

public class ConnexionPresenter implements Presenter {

	private final GBEInterface engine;
	private ConnexionView view;

	public ConnexionPresenter(GBEInterface engine) {
		this.engine = engine;
	}

	public void connect(String name, String firstName) {
		if (engine.connect(name, firstName)) {
			view.changeView(ViewsEnum.MAIN);
		} else {
			view.display("Erreur de connexion");
		}
	}

	public void setView(ConnexionView connexionView) {
		this.view = connexionView;
	}

	@Override
	public GBEInterface getEngine() {
		return engine;
	}

	@Override
	public ViewInterface getView() {
		return view;
	}
}
