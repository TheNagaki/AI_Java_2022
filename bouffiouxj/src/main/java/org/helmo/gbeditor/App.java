package org.helmo.gbeditor;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.helmo.gbeditor.models.GBEditor;
import org.helmo.gbeditor.presenters.*;
import org.helmo.gbeditor.repositories.JsonRepository;
import org.helmo.gbeditor.repositories.Repository;
import org.helmo.gbeditor.views.ConnexionView;
import org.helmo.gbeditor.views.BaseView;
import org.helmo.gbeditor.views.CreateBookView;
import org.helmo.gbeditor.views.MainView;

import java.util.HashMap;
import java.util.Map;


public class App extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	private Map<ViewsEnum, ViewInterface> initViews(GBEInterface editor) {
		ConnexionPresenter connexionPresenter = new ConnexionPresenter(editor);
		ConnexionView connexionView = new ConnexionView(connexionPresenter);
		MainPresenter mainPresenter = new MainPresenter(editor);
		MainView mainView = new MainView(mainPresenter);
		CreateBookPresenter createBookPresenter = new CreateBookPresenter(editor);
		CreateBookView createBookView = new CreateBookView(createBookPresenter);
		return new HashMap<>() {
			{
				put(ViewsEnum.CONNEXION, connexionView);
				put(ViewsEnum.MAIN, mainView);
				put(ViewsEnum.CREATE_BOOK, createBookView);
			}
		};
	}

	@Override
	public void start(Stage primaryStage) {
		Repository repo = new JsonRepository();
		GBEInterface editor = new GBEditor(repo);
		ViewInterface mainView = new BaseView(initViews(editor));
		Parent root = mainView.getRoot();
		Scene scene = new Scene(root, 400, 500);
		scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
		primaryStage.setTitle("Gamebook Editor");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}