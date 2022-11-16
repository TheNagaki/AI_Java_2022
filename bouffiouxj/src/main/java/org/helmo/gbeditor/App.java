package org.helmo.gbeditor;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.helmo.gbeditor.models.GBEditor;
import org.helmo.gbeditor.presenters.*;
import org.helmo.gbeditor.repositories.JsonRepository;
import org.helmo.gbeditor.repositories.RepositoryInterface;
import org.helmo.gbeditor.views.ConnexionView;
import org.helmo.gbeditor.views.BaseView;
import org.helmo.gbeditor.views.EditBookView;
import org.helmo.gbeditor.views.MainView;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This class launches the application.
 */
public class App extends Application {

	/**
	 * Starts the application.
	 * @param args The arguments to pass to the application.
	 */
	public static void main(String[] args) {
		launch(args);
	}

	private Map<ViewsEnum, ViewInterface> initViews(GBEInterface editor) {
		ConnexionPresenter connexionPr = new ConnexionPresenter(editor);
		ConnexionView connexionVw = new ConnexionView(connexionPr);
		MainPresenter mainPr = new MainPresenter(editor);
		MainView mainVw = new MainView(mainPr);
		EditBookPresenter createBookPr = new EditBookPresenter(editor);
		EditBookView createBookVw = new EditBookView(createBookPr);
		return new HashMap<>() {
			{
				put(ViewsEnum.CONNEXION, connexionVw);
				put(ViewsEnum.MAIN, mainVw);
				put(ViewsEnum.EDIT_BOOK, createBookVw);
			}
		};
	}

	/**
	 * This method is called when the application is launched.
	 * It initializes the views and the presenter and displays the first view.
	 *
	 * @param primaryStage the stage of the application
	 */
	@Override
	public void start(Stage primaryStage) {
		Path bookPath = Path.of(System.getProperty("user.home") + "/ue36/e190740.json");
		Path imgDirPath = Path.of(System.getProperty("user.home") + "/ue36/images_e190740");
		RepositoryInterface repo = new JsonRepository(bookPath, imgDirPath);
		GBEInterface editor = new GBEditor(repo);
		ViewInterface mainView = new BaseView(initViews(editor));
		Parent root = mainView.getRoot();
		Scene scene = new Scene(root, 400, 500);
		scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm());
		primaryStage.setTitle("Gamebook Editor");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}