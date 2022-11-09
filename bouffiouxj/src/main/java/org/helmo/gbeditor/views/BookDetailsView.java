package org.helmo.gbeditor.views;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import org.helmo.gbeditor.presenters.BookDetailsViewInterface;
import org.helmo.gbeditor.presenters.ViewInterface;
import org.helmo.gbeditor.presenters.ViewsEnum;

import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;

public class BookDetailsView implements BookDetailsViewInterface {
	private final BookDetailsPresenter presenter;
	private ViewInterface baseView;

	private final Stage stage = new Stage();
	private final BorderPane mainPane = new BorderPane();
	private final int WIDTH = 400;
	private final int HEIGHT = 400;
	private final int SMALL_SPACING = 5;
	private final int BIG_SPACING = 10;

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

	private void setRandomPositionInbound(Stage stage) {
		var screenBounds = Screen.getPrimary().getBounds();
		var x = (int) (Math.random() * (screenBounds.getWidth() - stage.getWidth()));
		var y = (int) (Math.random() * (screenBounds.getHeight() - stage.getHeight()));
		stage.setX(x);
		stage.setY(y);
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
		//Nothing to do here
	}

	@Override
	public void setAuthorName(String authorName) {
		//Nothing to do here
	}

	@Override
	public Window getStage() {
		return stage;
	}

	@Override
	public void displayBook(Book book) {
		stage.setTitle(book.getTitle());
		stage.initModality(Modality.NONE);
		// This is a choice I made to allow the user to interact with the main window while the popup is open and
		// therefore open multiple popups at the same time.
		// The reason for this choice is that the only advantage of using a popup is to have a smaller window NOT in the
		// main window so that the user can interact with both at the same time.
		stage.initOwner(baseView.getStage());
		stage.setResizable(false);
		stage.setWidth(WIDTH);
		stage.setHeight(HEIGHT);

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
		if (book.getImage() != null && !book.getImage().isEmpty()) {
			iv = new ImageView(new Image(book.getImage()));
		}
		iv.setFitWidth(100);
		iv.setPreserveRatio(true);

		var imageBox = new VBox();
		imageBox.getChildren().add(iv);
		imageBox.setAlignment(Pos.CENTER);
		centerBox.getChildren().add(imageBox);

		centerBox.getChildren().add(new Label(book.getIsbn()));

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
		//TODO:Add exit emoji to close button
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
		rightPane.setCenter(fillPagesTable(book));

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
		addPageBtn.setOnAction(e -> presenter.addPage());
		var removePageBtn = new Button("➖");
		removePageBtn.setOnAction(e -> presenter.removePage());
		var pageUpBtn = new Button("⬆");
		pageUpBtn.setOnAction(e -> presenter.movePageUp());
		var pageDownBtn = new Button("⬇");
		pageDownBtn.setOnAction(e -> presenter.movePageDown());
		buttonBox.getChildren().addAll(addPageBtn, removePageBtn, pageUpBtn, pageDownBtn);

		pageInputs.getChildren().addAll(fieldsInputs, buttonBox);
		rightPane.setBottom(pageInputs);

		rightPane.setMaxSize(WIDTH / 2 - 5, HEIGHT);
		mainPane.setRight(rightPane);
		BorderPane.setMargin(rightPane, insets);

		var scene = new Scene(mainPane);
		scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm());
		stage.setScene(scene);
		setRandomPositionInbound(stage);
		stage.show();
	}

	private TableView<Page> fillPagesTable(Book book) {
		//Avec l'aide de https://docs.oracle.com/javafx/2/ui_controls/table-view.htm

		var pagesView = new TableView<Page>();
		pagesView.setEditable(true);
		pagesView.setMaxWidth(WIDTH / 2 - 5);
		pagesView.setMaxHeight(HEIGHT);
		pagesView.setPlaceholder(new Label("Aucune page"));
		pagesView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		pagesView.setRowFactory(tv -> {
			var row = new TableRow<Page>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					//TODO: faire un truc ici
//					presenter.editPage(row.getItem());
					//?
				}
			});
			return row;
		});

		var pageCol = new TableColumn<Page, String>("N°");
		pageCol.setCellValueFactory(book.getPageNumberFactory());
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

		Page firstPage = new Page("Première page");
		Page secondPage = new Page("Deuxième page");
		Page thirdPage = new Page("Troisième page");
		Page fourthPage = new Page("Quatrième page");
		firstPage.addChoice("Choix 1", secondPage);
		firstPage.addChoice("Choix 2", thirdPage);

		secondPage.addChoice("Choix 1", fourthPage);
		secondPage.addChoice("Choix 2", thirdPage);
		pagesView.setItems(FXCollections.observableArrayList(firstPage, secondPage, thirdPage, fourthPage));

		return pagesView;
	}

	private void popupConfirmDeletion() {
		//Inspired by https://www.geeksforgeeks.org/javafx-popup-class/ and https://docs.oracle.com/javase/8/javafx/api/javafx/stage/Popup.html
		var confirmPopup = new Popup();
		var yesBtn = new Button("Oui");
		yesBtn.setOnAction(e2 -> {
			confirmPopup.hide();
			if (presenter.deleteBook()) {
				changeView(ViewsEnum.MAIN);
			} else {
				display("Erreur: Impossible de supprimer le livre");
			}
			presenter.closeView();
		});
		var noBtn = new Button("Non");
		noBtn.setOnAction(e2 -> confirmPopup.hide());
		var popupRoot = new VBox();
		popupRoot.setSpacing(BIG_SPACING);
		popupRoot.setAlignment(Pos.CENTER);
		popupRoot.getChildren().add(new Label("Êtes-vous sûr de vouloir supprimer ce livre ?"));
		var buttonsBox = new HBox();
		buttonsBox.getChildren().addAll(yesBtn, noBtn);
		buttonsBox.setSpacing(BIG_SPACING);
		buttonsBox.setAlignment(Pos.BOTTOM_CENTER);
		popupRoot.getChildren().add(buttonsBox);
		popupRoot.getStyleClass().add("popup-confirmation");
		confirmPopup.getContent().add(popupRoot);
		confirmPopup.setAutoHide(true);
		confirmPopup.show(stage);
		//Apparemment, les propriétés height et width ne sont définies qu'après le show() lorsqu'on utilise un Popup
		confirmPopup.setX(stage.getX() + stage.getWidth() / 2 - confirmPopup.getWidth() / 2);
		confirmPopup.setY(stage.getY() + stage.getHeight() / 2 - confirmPopup.getHeight() / 2);
	}


	@Override
	public void close() {
		stage.close();
	}
}
