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
import javafx.stage.Window;
import org.helmo.gbeditor.models.Book;
import org.helmo.gbeditor.presenters.EditBookPresenter;
import org.helmo.gbeditor.presenters.EditBookViewInterface;
import org.helmo.gbeditor.presenters.ViewInterface;
import org.helmo.gbeditor.presenters.ViewsEnum;

import java.io.File;
import java.util.Objects;

/**
 * View for the creation of a book
 * It displays a form to fill in order to create a new book (title, summary, ISBN, image)
 * It also allows to cancel the creation of the book and to go back to the main view
 */
public class EditBookView implements ViewInterface, EditBookViewInterface {

	private static final int MAX_TITLE = 150;
	private static final int MAX_ISBN = 13;
	private static final int MAX_SUMMARY = 500;
	private final EditBookPresenter presenter;
	private boolean bookCreation;
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
	private Book bookEdited;

	/**
	 * Constructor of the CreateBookView class
	 *
	 * @param crBkPresenter the presenter of the view
	 */
	public EditBookView(EditBookPresenter crBkPresenter) {
		this.presenter = crBkPresenter;
		this.presenter.setView(this);
		this.bookCreation = true;
		initView();
	}

	public void setBookToEdit(Book book) {
		if (book != null) {
			this.bookCreation = false;
			this.bookEdited = book;
		}
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

		final int ISBN_SECOND_DASH = 7;
		final int ISBN_LAST_DASH = 9;
//		 You can not enter more than 13 in the text field for the isbn
		inputIsbn.addEventHandler(KeyEvent.KEY_TYPED, keyEvent -> {
			if (inputIsbn.getText().length() >= MAX_ISBN) {
				keyEvent.consume();
				display(String.format("L'ISBN ne peut pas dépasser %d caractères", MAX_ISBN));
			} else {
				if (!keyEvent.getCharacter().matches("[0-9X]")) {
					keyEvent.consume();
				}
				var text = inputIsbn.getText().replaceAll("[^0-9]", "");
				var content = baseIsbn;
				if (text.length() >= ISBN_LAST_DASH) {
					content += text.substring(ISBN_SECOND_DASH, ISBN_LAST_DASH) + "-" + (text.length() == 10 ? text.charAt(9) : "");
				} else if (text.length() > ISBN_SECOND_DASH) {
					content += text.substring(ISBN_SECOND_DASH);
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
		var viewTitle = new Label("Créez votre livre");
		viewTitle.getStyleClass().add("title");
		topPane.setCenter(viewTitle);
		authorName.getStyleClass().add("author-name");
		topPane.setRight(authorName);

		centerGrid.setAlignment(Pos.CENTER);
		centerGrid.setHgap(10);
		centerGrid.setVgap(10);
		var imageLabel = new Label("Image :");
		var imageBox = new BorderPane();
		imageBox.setMaxWidth(60);
		imageBox.setMaxHeight(60);
		var imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/placeholder.png")).toExternalForm()));
		imageView.setFitWidth(60);
		imageView.setFitHeight(60);
		imageView.setPreserveRatio(true);
		imageBox.setCenter(imageView);
		var fileChooserButton = new Button("Choisir une image");
		fileChooserButton.setOnAction(action -> {
			baseView.getRoot().setDisable(true);
			var file = fileChooser.showOpenDialog(this.baseView.getRoot().getScene().getWindow());
			if (file != null) {
				var newImageView = new ImageView(new Image(file.toURI().toString()));
				newImageView.setFitWidth(80);
				newImageView.setFitHeight(80);
				newImageView.setPreserveRatio(true);
				imageBox.setCenter(newImageView);
				this.imageChosen = file;
			}
			baseView.getRoot().setDisable(false);
		});
		imageBox.setCenter(imageView);
		var buttonBox = new VBox(fileChooserButton);
		buttonBox.setAlignment(Pos.CENTER);
		imageBox.setRight(buttonBox);
		var title = new Label("Titre :");
		var isbn = new Label("ISBN :");
		var summary = new Label("Résumé :");
		var cancelButton = new Button("Annuler");
		cancelButton.setOnAction(action -> changeView(ViewsEnum.MAIN));
		var quitButton = new Button("Quitter");
		quitButton.setOnAction(action -> presenter.onQuit_Clicked());
		centerGrid.setAlignment(Pos.CENTER);
		createBookButton = new Button("Valider");
		createBookButton.setOnAction(action -> createBook(inputTitle.getText().strip(), inputIsbn.getText().strip(), inputSummary.getText().strip()));
		centerGrid.getChildren().clear();
		centerGrid.add(title, 0, 0);
		centerGrid.add(inputTitle, 1, 0);
		centerGrid.add(isbn, 0, 1);
		centerGrid.add(inputIsbn, 1, 1);
		centerGrid.add(summary, 0, 2);
		centerGrid.add(inputSummary, 1, 2);
		centerGrid.add(imageLabel, 0, 3, 2, 1);
		centerGrid.add(imageBox, 1, 3, 2, 2);
		centerGrid.add(createBookButton, 0, 8);
		centerGrid.add(cancelButton, 1, 8);
		centerGrid.add(quitButton, 2, 8);
		centerGrid.setHgap(10);
		centerGrid.setVgap(10);
		mainPane.setCenter(centerGrid);
		mainPane.setTop(topPane);
		if (!bookCreation) {
			viewTitle = new Label("Modifiez votre livre");
			topPane.setCenter(viewTitle);
			var image = bookEdited.getImage();
			if (image != null && !image.isEmpty()) {
				imageView = new ImageView(new Image(image));
				imageView.setFitWidth(80);
				imageView.setFitHeight(80);
				imageView.setPreserveRatio(true);
				imageBox.setCenter(imageView);
			}
		}
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
		bookCreation = true;
		baseView.changeView(viewName);
	}

	@Override
	public void refresh() {
		presenter.askBookToEdit();
		if (bookCreation) {
			inputTitle.setText("");
			presenter.askISBN();
			inputSummary.setText("");
		} else {
			inputTitle.setText(bookEdited.getTitle());
			inputIsbn.setText(bookEdited.getIsbn());
			inputSummary.setText(bookEdited.getSummary());
		}
		presenter.askAuthorName();
		checkToEnableButton();
		display("");
	}

	@Override
	public void setAuthorName(String authorName) {
		this.authorName.setText(authorName);
	}

	@Override
	public Window getStage() {
		return baseView.getStage();
	}

	@Override
	public void close() {
		baseView.close();
	}

	@Override
	public void presetISBN(int[] baseIsbn) {
		final int NUMBER_OF_PARAMS = 2;
		if (baseIsbn.length == NUMBER_OF_PARAMS) {
			this.baseIsbn = String.format("%d-%d-", baseIsbn[0], baseIsbn[1]);
			inputIsbn.setText(this.baseIsbn);
		}
	}

	private void createBook(String title, String isbn, String summary) {
		if (bookCreation) {
			presenter.createBook(title, isbn, summary, imageChosen == null ? "" : imageChosen.getAbsolutePath());
		} else {
			presenter.editBook(bookEdited, title, summary, imageChosen == null ? "" : imageChosen.getAbsolutePath());
		}
	}
}