package org.helmo.gbeditor.views;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import org.helmo.gbeditor.presenters.ConnexionPresenter;
import org.helmo.gbeditor.presenters.ViewsEnum;
import org.helmo.gbeditor.presenters.interfaces.ConnexionViewInterface;
import org.helmo.gbeditor.presenters.interfaces.ViewInterface;

import java.util.Objects;

/**
 * View for the connection of an author
 * It displays a form to fill in order to connect to the application (name, first name)
 */
public class ConnexionView implements ConnexionViewInterface {
	private static final int MAX_NAME = 20;
	private final ConnexionPresenter presenter;
	private ViewInterface baseView;
	private final ImageView refreshImage = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/refresh-page.png")).toExternalForm()));

	{
		refreshImage.setFitWidth(20);
		refreshImage.setFitHeight(20);
	}

	private final VBox titlePane = new VBox();

	{
		titlePane.setAlignment(Pos.CENTER);
		Label title = new Label("Connexion");
		title.getStyleClass().add("title");
		titlePane.getChildren().add(title);
	}

	private final ImageView iv = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/couverture-app.jpg")).toExternalForm()));

	{
		iv.setPreserveRatio(true);
		iv.setFitWidth(200);
	}

	private final GridPane centerGrid = new GridPane();
	private final TextField inputName = new TextField();
	private final TextField inputFirstName = new TextField();
	private final VBox mainPane = new VBox();
	private final Button validerButton = new Button("Valider");

	{
		inputName.textProperty().addListener((observable, oldValue, newValue) -> handleNewValue(newValue, inputName));
		inputFirstName.textProperty().addListener((observable, oldValue, newValue) -> handleNewValue(newValue, inputFirstName));
	}

	private void handleNewValue(String newValue, TextField textField) {
		String textEntered = newValue.replaceAll("[^a-zA-Z- ]", "");
		if (textEntered.length() >= MAX_NAME) {
			textEntered = textEntered.substring(0, MAX_NAME);
		}
		textField.setText(textEntered);
		textField.positionCaret(textField.getText().length());
		validerButton.setDisable(checkDisableButton());
	}

	{
		centerGrid.setAlignment(Pos.CENTER);
		centerGrid.setHgap(10);
		centerGrid.setVgap(12);
		centerGrid.add(iv, 0, 0, 2, 1);
		Label lName = new Label("Nom :");
		Label lFirstName = new Label("Prénom");

		validerButton.setOnAction(action -> clickOnValidate());
		centerGrid.add(lName, 0, 1);
		centerGrid.add(inputName, 1, 1);
		centerGrid.add(lFirstName, 0, 2);
		centerGrid.add(inputFirstName, 1, 2);
		centerGrid.add(validerButton, 0, 3);

		inputName.setOnAction(action -> handleEnter());
		inputFirstName.setOnAction(action -> handleEnter());
	}

	private boolean checkDisableButton() {
		return (inputName.getText().isBlank() || inputFirstName.getText().isBlank() ||
				inputName.getText().length() > 20 || inputFirstName.getText().length() > 20);
	}

	private void clickOnValidate() {
		presenter.connect(inputName.getText().strip(), inputFirstName.getText().strip());
	}

	{
		mainPane.setAlignment(Pos.CENTER);
		mainPane.getChildren().addAll(titlePane, centerGrid);
	}

	/**
	 * Constructor of the ConnexionView
	 *
	 * @param presenter the presenter of the view
	 */
	public ConnexionView(ConnexionPresenter presenter) {
		this.presenter = presenter;
		presenter.setView(this);

		var quitButton = new Button("Quitter");
		quitButton.setOnAction(action -> presenter.OnQuit_Click());
		centerGrid.add(quitButton, 1, 3);
	}

	@Override
	public javafx.scene.Parent getRoot() {
		return mainPane;
	}

	@Override
	public void display(String response) {
		baseView.display(response);
	}

	@Override
	public void setBaseView(ViewInterface baseView) {
		this.baseView = baseView;
	}

	@Override
	public void changeView(ViewsEnum viewName) {
		baseView.changeView(viewName);
	}

	@Override
	public void refresh() {
		inputName.setText("");
		inputName.setPromptText("Votre nom");
		inputFirstName.setText("");
		inputFirstName.setPromptText("Votre prénom");
		validerButton.setDisable(checkDisableButton());
	}

	@Override
	public Window getStage() {
		return baseView.getStage();
	}

	@Override
	public void close() {
		baseView.close();
	}

	private void handleEnter() {
		if (!checkDisableButton()) {
			clickOnValidate();
		} else {
			if (inputName.getText().isEmpty() || (!inputFirstName.getText().isEmpty() && inputName.getText().length() >= 20)) {
				inputName.requestFocus();
			} else {
				inputFirstName.requestFocus();
			}
		}
	}
}
