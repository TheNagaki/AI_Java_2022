package org.helmo.gbeditor.views;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.helmo.gbeditor.presenters.CreateBookPresenter;
import org.helmo.gbeditor.presenters.CreateBookViewInterface;
import org.helmo.gbeditor.presenters.ViewInterface;
import org.helmo.gbeditor.presenters.ViewsEnum;

import java.io.File;
import java.util.Objects;

/**
 * View for the creation of a book
 * It displays a form to fill in order to create a new book (title, summary, ISBN, image)
 * It also allows to cancel the creation of the book and to go back to the main view
 */
public class CreateBookView implements ViewInterface, CreateBookViewInterface {

	private static final int MAX_TITLE = 150;
	private static final int MAX_ISBN = 13;
	private static final int MAX_SUMMARY = 500;
	private final CreateBookPresenter presenter;
	private ViewInterface baseView;
	private final BorderPane mainPane = new BorderPane();
	private Button createBookButton;
	private File imageChosen;
	private final BorderPane topPane = new BorderPane();
	private final FileChooser fileChooser = new FileChooser();

	private final GridPane centerGrid = new GridPane();
	private final TextArea inputTitle = new TextArea();
	private final TextField inputIsbn = new TextField();
	private final TextArea inputSummary = new TextArea();
	private final Label authorName = new Label("");
	private String baseIsbn;

	/**
	 * Constructor of the CreateBookView class
	 * @param createBookPresenter the presenter of the view
	 */
	public CreateBookView(CreateBookPresenter createBookPresenter) {
		this.presenter = createBookPresenter;
		this.presenter.setView(this);
		initView();
	}

	{
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg"));

		inputTitle.setPrefColumnCount(25);
		inputTitle.setPrefRowCount(4);

		inputSummary.setPrefColumnCount(20);
		inputSummary.setPrefRowCount(10);

		inputTitle.setWrapText(true);
		inputSummary.setWrapText(true);

		// You can not enter more than 150 in the text area for the title
		inputTitle.addEventHandler(KeyEvent.KEY_TYPED, event -> {
			inputTitle.setText(inputTitle.getText().replaceAll("[^0-9a-zA-Zéèàâêûùîçäïüë.,;!?() \\-/]", ""));
			if (inputTitle.getText().length() > MAX_TITLE) {
				event.consume();
				display(String.format("Vous avez atteint la limite de %d caractères pour le titre", MAX_TITLE));
			}
			inputTitle.positionCaret(inputTitle.getText().length());
			checkToEnableButton();
		});

//		 You can not enter more than 13 in the text field for the isbn
		inputIsbn.addEventHandler(KeyEvent.KEY_TYPED, keyEvent -> {
			if (inputIsbn.getText().length() >= MAX_ISBN) {
				keyEvent.consume();
				display(String.format("L'ISBN ne peut pas dépasser %d caractères", MAX_ISBN));
			} else {
				if (!keyEvent.getCharacter().matches("[0-9X]")) {
					keyEvent.consume();
				}
				String text = inputIsbn.getText().replaceAll("[^0-9]", "");
				String content = baseIsbn;
				if (text.length() >= 9) {
					content += text.substring(7, 9) + "-" + (text.length() == 10 ? text.charAt(9) : "");
				} else if (text.length() > 7) {
					content += text.substring(7);
				}
				inputIsbn.setText(content);
			}
			inputIsbn.positionCaret(inputIsbn.getText().length());
			checkToEnableButton();
		});

		inputIsbn.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
			if (keyEvent.getCode().equals(KeyCode.DELETE) || keyEvent.getCode().equals(KeyCode.BACK_SPACE)) {
				inputIsbn.setText(baseIsbn);
				inputIsbn.positionCaret(inputIsbn.getText().length());
			}
			keyEvent.consume();
		});

		// You can not enter more than 500 characters in the text area for the summary
		inputSummary.addEventHandler(KeyEvent.KEY_TYPED, event -> {
			inputSummary.setText(inputSummary.getText().replaceAll("[^0-9a-zA-Zéèàâêûùîçäïüë.,;!?() \\-/]", ""));
			if (inputSummary.getText().length() > MAX_SUMMARY) {
				event.consume();
				display(String.format("Vous avez atteint la limite de %d caractères pour le résumé", MAX_SUMMARY));
			}
			inputSummary.positionCaret(inputSummary.getText().length());
			checkToEnableButton();
		});
	}

	/**
	 * Enables or disables the create book button depending on the state of the form (if all fields are filled and under the limit)
	 */
	private void checkToEnableButton() {
		createBookButton.setDisable(inputTitle.getText().length() == 0 || inputIsbn.getText().length() == 0 ||
				inputSummary.getText().length() == 0 || inputTitle.getText().length() > MAX_TITLE ||
				inputIsbn.getText().length() > MAX_ISBN || inputSummary.getText().length() > MAX_SUMMARY);
	}

	private void initView() {
		Label viewTitle = new Label("Créez votre livre");
		viewTitle.getStyleClass().add("title");
		topPane.setCenter(viewTitle);
		authorName.getStyleClass().add("author-name");
		topPane.setRight(authorName);

		centerGrid.setAlignment(Pos.CENTER);
		centerGrid.setHgap(10);
		centerGrid.setVgap(10);
		Label imageLabel = new Label("Image :");
		BorderPane imageBox = new BorderPane();
		imageBox.setMaxWidth(60);
		imageBox.setMaxHeight(60);
		ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/placeholder.png")).toExternalForm()));
		imageView.setFitWidth(60);
		imageView.setFitHeight(60);
		imageView.setPreserveRatio(true);
		imageBox.setCenter(imageView);
		Button fileChooserButton = new Button("Choisir une image");
		fileChooserButton.setOnAction(action -> {
			baseView.getRoot().setDisable(true);
			File file = fileChooser.showOpenDialog(this.baseView.getRoot().getScene().getWindow());
			if (file != null) {
				ImageView newImageView = new ImageView(new Image(file.toURI().toString()));
				newImageView.setFitWidth(80);
				newImageView.setFitHeight(80);
				newImageView.setPreserveRatio(true);
				imageBox.setCenter(newImageView);
				this.imageChosen = file;
			}
			baseView.getRoot().setDisable(false);
		});
		imageBox.setCenter(imageView);
		VBox buttonBox = new VBox(fileChooserButton);
		buttonBox.setAlignment(Pos.CENTER);
		imageBox.setRight(buttonBox);
		Label title = new Label("Titre :");
		Label isbn = new Label("ISBN :");
		Label summary = new Label("Résumé :");
		Button cancelCreationButton = new Button("Annuler");
		cancelCreationButton.setOnAction(action -> changeView(ViewsEnum.MAIN));
		centerGrid.setAlignment(Pos.CENTER);
		createBookButton = new Button("Valider");
		createBookButton.setOnAction(action -> createBook(inputTitle.getText().strip(), inputIsbn.getText().strip(), inputSummary.getText().strip()));
		centerGrid.add(title, 0, 0);
		centerGrid.add(inputTitle, 1, 0);
		centerGrid.add(isbn, 0, 1);
		centerGrid.add(inputIsbn, 1, 1);
		centerGrid.add(summary, 0, 2);
		centerGrid.add(inputSummary, 1, 2);
		centerGrid.add(imageLabel, 0, 3, 2, 1);
		centerGrid.add(imageBox, 1, 3, 2, 2);
		centerGrid.add(createBookButton, 0, 8);
		centerGrid.add(cancelCreationButton, 1, 8);
		centerGrid.setHgap(10);
		centerGrid.setVgap(10);
		mainPane.setCenter(centerGrid);
		mainPane.setTop(topPane);
	}

	@Override
	public Parent getRoot() {
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
		presenter.askISBN();
		presenter.askAuthorName();
		inputTitle.setText("");
		inputSummary.setText("");
		checkToEnableButton();
	}

	@Override
	public void setAuthorName(String authorName) {
		this.authorName.setText(authorName);
	}

	@Override
	public void presetISBN(int[] baseIsbn) {
		if (baseIsbn.length == 2) {
			this.baseIsbn = String.format("%d-%d-", baseIsbn[0], baseIsbn[1]);
			inputIsbn.setText(this.baseIsbn);
		}
	}

	private void createBook(String title, String isbn, String summary) {
		String answer = "";
		if (title.isBlank() || isbn.isBlank() || summary.isBlank()) {
			answer += "Veuillez remplir tous les champs.\n";
		}
		if (title.length() > 150) {
			answer += "Le titre ne doit pas dépasser 150 caractères.\n";
		}
		if (!isbn.isBlank() && isbn.length() != 13) {
			answer += "L'ISBN doit contenir 13 caractères.\n";
		}
		if (summary.length() > 500) {
			answer += "Le résumé ne doit pas dépasser 500 caractères.\n";
		}
		if (answer.isBlank()) {
			presenter.createBook(title, isbn, summary, imageChosen == null ? "" : imageChosen.getAbsolutePath());
		} else {
			display(answer);
		}
	}
}