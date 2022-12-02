package org.helmo.gbeditor.presenters;

import org.helmo.gbeditor.models.Author;
import org.helmo.gbeditor.presenters.interfaces.PresenterInterface;
import org.helmo.gbeditor.presenters.interfaces.ViewInterface;
import org.helmo.gbeditor.repositories.RepositoryInterface;
import org.helmo.gbeditor.views.ConnexionView;

/**
 * The ConnexionPresenter is the presenter for the ConnexionView.
 * It is used to connect the user to the application.
 * It is also used to create a new user.
 */
public class ConnexionPresenter implements PresenterInterface {

	private final RepositoryInterface repo;
	private ConnexionView view;

	/**
	 * Constructor of the presenter with the engine
	 *
	 * @param repo the repository of the application
	 */
	public ConnexionPresenter(RepositoryInterface repo) {
		this.repo = repo;
	}

	/**
	 * Sends the information to the engine to create a new user
	 *
	 * @param name      the name of the user
	 * @param firstName the first name of the user
	 */
	public void connect(String name, String firstName) {
		if (repoConnection(name, firstName)) {
			view.changeView(ViewsEnum.MAIN);
		} else {
			view.display("Erreur de connexion");
		}
	}

	private boolean repoConnection(String name, String firstName) {
		try {
			Author author = new Author(name, firstName);
			var authors = repo.getAuthors();
			if (!authors.contains(new Author(name, firstName))) {
				authors.add(author);
				repo.setCurrentAuthor(author);
			} else {
				for (Author a : authors) {
					if (a.equals(author)) {
						repo.setCurrentAuthor(a);
						break;
					}
				}
			}
			return true;
		} catch (IllegalArgumentException e) {
			return false;
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
