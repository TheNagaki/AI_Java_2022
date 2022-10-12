package org.helmo.gbeditor.presenters;

public class ConnexionPresenter extends Presenter {

	public ConnexionPresenter(GBEInterface engine) {
		super(engine);
	}

	public void connect(String name, String firstName) {
		if (getEngine().connect(name, firstName)) {
			getView().changeView(ViewsEnum.MAIN);
		} else {
			getView().display("Erreur de connexion");
		}
	}
}
