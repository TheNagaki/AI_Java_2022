package org.helmo.gbeditor.presenters;

import org.helmo.gbeditor.presenters.interfaces.GBEInterface;
import org.helmo.gbeditor.presenters.interfaces.PresenterInterface;
import org.helmo.gbeditor.presenters.interfaces.ViewInterface;
import org.helmo.gbeditor.views.ConnexionView;

/**
 * The ConnexionPresenter is the presenter for the ConnexionView.
 * It is used to connect the user to the application.
 * It is also used to create a new user.
 */
public class ConnexionPresenter implements PresenterInterface {

	private final GBEInterface engine;
	private ConnexionView view;

	/**
	 * Constructor of the presenter with the engine
	 *
	 * @param engine the logic of the application
	 */
	public ConnexionPresenter(GBEInterface engine) {
		this.engine = engine;
	}

	/**
	 * Sends the information to the engine to create a new user
	 *
	 * @param name      the name of the user
	 * @param firstName the first name of the user
	 */
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
	public ViewInterface getView() {
		return view;
	}

	/**
	 * This method is used to handle the quit action from the view
	 */
	public void OnQuit_Click() {
		view.close();
	}
}
