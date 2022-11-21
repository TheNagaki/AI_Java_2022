package org.helmo.gbeditor.views;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.*;
import org.helmo.gbeditor.models.Book;
import org.helmo.gbeditor.models.Page;
import org.helmo.gbeditor.presenters.BookDetailsPresenter;
import org.helmo.gbeditor.presenters.interfaces.BookDetailsViewInterface;
import org.helmo.gbeditor.presenters.interfaces.ViewInterface;
import org.helmo.gbeditor.presenters.ViewsEnum;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class BookDetailsView implements BookDetailsViewInterface {
	private final BookDetailsPresenter presenter;
	private static final int GLIMPSE_SIZE = 10;
	private ViewInterface baseView;

	private final Stage stage = new Stage();
	private final BorderPane mainPane = new BorderPane();
	private static final int WIDTH = 400;
	private static final int HEIGHT = 600;
	private static final int SMALL_SPACING = 5;
	private static final int BIG_SPACING = 10;
	private Book bookOnDisplay;
	private ObservableList<Page> pages;

	public BookDetailsView(BookDetailsPresenter presenter) {
		this.presenter = presenter;
		presenter.setView(this);
	}

	@Override
	public Parent getRoot() {
		return stage.getScene().getRoot();
	}

	@Override
	public void display(String message) {
		baseView.display(message);
	}

	private void setPosition(Stage stage) {

		var baseX = baseView.getStage().getX();
		var baseY = baseView.getStage().getY();
		var computedX = baseX + baseView.getStage().getWidth() / 2 + BIG_SPACING + stage.getWidth() / 2;
		var computedY = baseY + baseView.getStage().getHeight() / 2 - stage.getHeight() / 2;
		//Si la fenêtre dépasse à droite, on la place à gauche
		if (computedX + stage.getWidth() / 2 > Screen.getPrimary().getBounds().getMaxX()) {
			computedX = baseX - baseView.getStage().getWidth() / 2 - BIG_SPACING - stage.getWidth() / 2;
			if (computedX - stage.getWidth() / 2 < Screen.getPrimary().getBounds().getMinX()) {
				computedX = baseX;
			}
		}
		//Si la fenêtre dépasse en bas, on la place en haut TODO: CA MARCHE PAS
		if (computedY + stage.getHeight() / 2 > Screen.getPrimary().getBounds().getMaxY()) {
			System.out.println("dépassement Y");
			computedY = baseY - baseView.getStage().getHeight() / 2 + stage.getHeight() / 2;
		}
		stage.setX(computedX);
		stage.setY(computedY);
	}

	@Override
	public void setBaseView(ViewInterface baseView) {
		this.baseView = baseView;
		launchStage();
	}

	@Override
	public void changeView(ViewsEnum viewName) {
		baseView.changeView(viewName);
	}

	@Override
	public void refresh() {
		mainPane.getChildren().clear();
		displayBook(bookOnDisplay);
	}

	@Override
	public Window getStage() {
		return stage;
	}

	@Override
	public void displayBook(Book book) {
		stage.setTitle(book.getTitle());
		this.bookOnDisplay = book;
		var insets = new Insets(SMALL_SPACING);

		var centerBox = new VBox();
		centerBox.setSpacing(BIG_SPACING);
		centerBox.setAlignment(Pos.CENTER);
		centerBox.setPadding(new javafx.geometry.Insets(5));
		centerBox.setFillWidth(true);
		centerBox.setMaxWidth(WIDTH / 2 - 5);

		var title = new Label(book.getTitle());
		title.getStyleClass().add("details-title");
		title.setWrapText(true);
		title.setAlignment(Pos.CENTER);
		title.setPrefWidth(WIDTH);
		mainPane.setTop(title);
		BorderPane.setMargin(title, insets);

		var iv = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/placeholder.png")).toExternalForm()));
		if (book.getImagePath() != null && !book.getImagePath().isEmpty()) {
			iv = new ImageView(new Image(book.getImagePath()));
		}
		iv.setFitWidth(100);
		iv.setPreserveRatio(true);

		var imageBox = new VBox();
		imageBox.getChildren().add(iv);
		imageBox.setAlignment(Pos.CENTER);
		centerBox.getChildren().add(imageBox);

		centerBox.getChildren().add(new Label(book.getIsbn().toString()));

//		centerBox.getChildren().add(new Label(book.getAuthor().getFullName()));
//		L'utilisateur ne pouvant voir QUE ses livres, on ne l'affiche donc pas

		var summary = new Label(book.getSummary());
		summary.setWrapText(true);
		centerBox.getChildren().add(summary);

		var scrollPane = new ScrollPane();
		scrollPane.setContent(centerBox);
		scrollPane.setFitToWidth(true);
		mainPane.setCenter(scrollPane);
		BorderPane.setMargin(scrollPane, insets);

		var editBtn = new Button("✏ Éditer");
		editBtn.setOnAction(e -> presenter.editBook());
		var deleteBtn = new Button("❌ Supprimer");
		deleteBtn.setOnAction(e -> popupConfirmDeletion());
		var closeBtn = new Button("Fermer");
		closeBtn.setOnAction(action -> presenter.closeView());
		var closeIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/emergency-exit.png"))));
		closeIcon.setFitHeight(20);
		closeIcon.setPreserveRatio(true);
		closeBtn.setGraphic(closeIcon);

		var bottomPane = new HBox();
		bottomPane.setAlignment(Pos.CENTER);
		bottomPane.setSpacing(SMALL_SPACING);
		bottomPane.getChildren().addAll(editBtn, deleteBtn, closeBtn);
		mainPane.setBottom(bottomPane);
		BorderPane.setMargin(bottomPane, insets);

		var rightPane = new BorderPane();
		var tableView = fillPagesTable(book);
		rightPane.setCenter(tableView);

		var pageInputs = new VBox();
		pageInputs.setSpacing(SMALL_SPACING);

		var fieldsInputs = new HBox();
		var contentInput = new TextArea();
		contentInput.setPromptText("Contenu de la page");
		contentInput.setWrapText(true);
		contentInput.setPrefRowCount(2);
		fieldsInputs.getChildren().add(contentInput);

		var buttonBox = new HBox();
		buttonBox.setSpacing(BIG_SPACING);
		buttonBox.setAlignment(Pos.CENTER);
		var addPageBtn = new Button("➕");
		addPageBtn.setOnAction(e -> presenter.addPage(contentInput.getText()));
		var removePageBtn = new Button("➖");
		removePageBtn.setOnAction(e -> presenter.removePage(tableView.getSelectionModel().getSelectedItem()));
//		var pageUpBtn = new Button("⬆");
//		pageUpBtn.setOnAction(e -> presenter.movePageUp(tableView.getSelectionModel().getSelectedItem()));
//		var pageDownBtn = new Button("⬇");
//		pageDownBtn.setOnAction(e -> presenter.movePageDown(tableView.getSelectionModel().getSelectedItem()));
//		buttonBox.getChildren().addAll(addPageBtn, removePageBtn, pageUpBtn, pageDownBtn);
		buttonBox.getChildren().addAll(addPageBtn, removePageBtn);

		pageInputs.getChildren().addAll(fieldsInputs, buttonBox);
		rightPane.setBottom(pageInputs);

		rightPane.setMaxSize(WIDTH / 2 - 5, HEIGHT);
		mainPane.setRight(rightPane);
		BorderPane.setMargin(rightPane, insets);
	}

	private void launchStage() {
		stage.initModality(Modality.NONE);
		// This is a choice I made to allow the user to interact with the main window while the popup is open and vice versa.
		// The reason for this choice is that the only advantage of using a popup is to have a smaller window NOT in the
		// main window so that the user can interact with both at the same time.
		stage.initOwner(baseView.getStage());
		stage.setResizable(false);
		stage.setWidth(WIDTH);
		stage.setHeight(HEIGHT);
		var scene = new Scene(mainPane);
		scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm());
		stage.setScene(scene);
		setPosition(stage);
		stage.show();
	}

	private TableView<Page> fillPagesTable(Book book) {
		//Avec l'aide de https://docs.oracle.com/javafx/2/ui_controls/table-view.htm

		var pagesView = new TableView<Page>();
		pagesView.setEditable(false);
		pagesView.setMaxWidth(WIDTH / 2 - 5);
		pagesView.setMaxHeight(HEIGHT);
		pagesView.setPlaceholder(new Label("Aucune page"));
		pagesView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		pagesView.setRowFactory(tv -> {
			var row = new TableRow<Page>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && !row.isEmpty()) {
					popupEditPage(row.getItem());
				}
			});
			return row;
		});

		var pageCol = new TableColumn<Page, String>("N°");
		pageCol.setCellValueFactory(param -> new SimpleStringProperty(String.format("%d", bookOnDisplay.getPageNumber(param.getValue()))));
		pageCol.setCellFactory(TextFieldTableCell.forTableColumn());
		pageCol.setMaxWidth(20);
		pageCol.setMinWidth(20);

		var contentCol = new TableColumn<Page, String>("Contenu");
		contentCol.setCellValueFactory(new PropertyValueFactory<>("content"));
		contentCol.setCellFactory(TextFieldTableCell.forTableColumn());

		var choicesCol = new TableColumn<Page, String>("Choix");
		choicesCol.setCellValueFactory(param -> {
			var value = new StringJoiner(", ");
			var page = param.getValue();
			var choices = page.getChoices();
			for (var destinationPage : choices.values()) {
				value.add("" + book.getPageNumber(destinationPage));
			}
			return new SimpleStringProperty(value.toString());
		});
		choicesCol.setCellFactory(TextFieldTableCell.forTableColumn());

		pagesView.getColumns().addAll(Arrays.asList(pageCol, contentCol, choicesCol));

		this.pages = FXCollections.observableArrayList(book.getPages());
		pagesView.setItems(pages);

		pagesView.requestFocus();
		pagesView.getFocusModel().focus(0);
		pagesView.getSelectionModel().selectFirst();
		return pagesView;
	}

	private void popupConfirmDeletion() {
		//Inspired by https://www.geeksforgeeks.org/javafx-popup-class/ and https://docs.oracle.com/javase/8/javafx/api/javafx/stage/Popup.html
		var popup = new Popup();
		var yesBtn = new Button("Oui");
		yesBtn.setOnAction(e2 -> {
			popup.hide();
			if (presenter.deleteBook()) {
				changeView(ViewsEnum.MAIN);
			} else {
				display("Erreur: Impossible de supprimer le livre");
			}
			presenter.closeView();
		});
		var noBtn = new Button("Non");
		noBtn.setOnAction(e2 -> popup.hide());
		var popupRoot = new VBox();
		popupRoot.setSpacing(SMALL_SPACING);
		popupRoot.setAlignment(Pos.CENTER);
		popupRoot.getChildren().add(new Label("Êtes-vous sûr de vouloir supprimer ce livre ?"));
		var buttonsBox = new HBox();
		buttonsBox.getChildren().addAll(yesBtn, noBtn);
		buttonsBox.setSpacing(BIG_SPACING);
		buttonsBox.setAlignment(Pos.BOTTOM_CENTER);
		popupRoot.getChildren().add(buttonsBox);
		fillAndPlacePopup(popup, popupRoot);
	}

	private void popupEditPage(Page selected) {
		var popup = new Popup();
		var popupRoot = new VBox();
		popupRoot.paddingProperty().setValue(new Insets(BIG_SPACING));
		popupRoot.setSpacing(SMALL_SPACING);
		popupRoot.setAlignment(Pos.CENTER);
		var contentField = new TextField(selected.getContent());
		contentField.setPromptText("Contenu de la page");
		var choicesBox = new VBox();
		choicesBox.setSpacing(SMALL_SPACING);
		choicesBox.setAlignment(Pos.CENTER);
		var choices = selected.getChoices();
		for (var choice : choices.keySet()) {
			var choiceBox = new HBox();
			choiceBox.setSpacing(SMALL_SPACING);
			choiceBox.setAlignment(Pos.CENTER);
			var choiceField = new TextField(choice);
			choiceField.setPromptText("Choix");
			var destinationField = selectPageChoice(selected);
			addCurrentChoiceToMenu(choices, choice, destinationField);
			choiceBox.getChildren().addAll(choiceField, destinationField);
			choicesBox.getChildren().add(choiceBox);
		}
		var addChoiceBtn = new Button("Ajouter un choix");
		addChoiceBtn.setOnAction(e -> {
			var choiceBox = new HBox();
			choiceBox.setSpacing(SMALL_SPACING);
			choiceBox.setAlignment(Pos.CENTER);
			var choiceField = new TextField();
			choiceField.setPromptText("Choix");
			var destinationField = selectPageChoice(selected);
			choiceBox.getChildren().addAll(choiceField, destinationField);
			choicesBox.getChildren().add(choiceBox);
		});
		var saveBtn = new Button("💾");
		saveBtn.setOnAction(e -> presenter.closeView());
		var cancelBtn = new Button();
		var closeIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/emergency-exit.png"))));
		closeIcon.setFitHeight(15);
		closeIcon.setPreserveRatio(true);
		cancelBtn.setGraphic(closeIcon);
		cancelBtn.setOnAction(e -> popup.hide());
		var buttonsBox = new HBox();
		buttonsBox.getChildren().addAll(saveBtn, cancelBtn);
		buttonsBox.setSpacing(BIG_SPACING);
		buttonsBox.setAlignment(Pos.BOTTOM_CENTER);
		popupRoot.getChildren().addAll(contentField, choicesBox, buttonsBox);
		fillAndPlacePopup(popup, popupRoot);
	}

	private MenuButton selectPageChoice(Page selected) {
		var destinationField = new MenuButton("Page cible");
		destinationField.getItems().addAll(pages.stream()
				.filter(page -> !page.equals(selected) && !selected.getChoices().containsValue(page) &&
						!page.getChoices().containsValue(selected))
				.map(page -> {
					final var content = page.getContent();
					var item = new MenuItem(bookOnDisplay.getPageNumber(page) + ") " + content.substring(0, Math.min(GLIMPSE_SIZE, content.length())));
					item.setOnAction(e -> destinationField.setText(item.getText()));
					return item;
				}).collect(Collectors.toList()));
		return destinationField;
	}

	private void addCurrentChoiceToMenu(Map<String, Page> choices, String choice, MenuButton menu) {
		var choicePageContent = choices.get(choice).getContent();
		var item = new MenuItem("* " + bookOnDisplay.getPageNumber(choices.get(choice)) + ") " + choicePageContent.substring(0, Math.min(GLIMPSE_SIZE, choicePageContent.length())));
		item.setOnAction(e -> menu.setText(item.getText()));
		menu.getItems().add(0, item);
		menu.setText(menu.getItems().get(0).getText());
	}

	private void fillAndPlacePopup(Popup popup, Node popupRoot) {
		popup.getContent().add(popupRoot);
		popup.setAutoHide(true);
		popup.show(stage);
		//Apparemment, les propriétés height et width ne sont définies qu'après le show() lorsqu'on utilise un Popup
		popup.setX(stage.getX() + stage.getWidth() / 2 - popup.getWidth() / 2);
		popup.setY(stage.getY() + stage.getHeight() / 2 - popup.getHeight() / 2);
	}

	@Override
	public void close() {
		stage.close();
	}
}
