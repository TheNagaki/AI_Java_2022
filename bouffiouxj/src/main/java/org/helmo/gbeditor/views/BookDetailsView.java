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
import org.helmo.gbeditor.presenters.BookDetailsPresenter;
import org.helmo.gbeditor.presenters.ViewsEnum;
import org.helmo.gbeditor.presenters.interfaces.BookDetailsViewInterface;
import org.helmo.gbeditor.presenters.interfaces.ViewInterface;
import org.helmo.gbeditor.presenters.viewmodels.BookViewModel;
import org.helmo.gbeditor.presenters.viewmodels.PageViewModel;

import java.util.*;
import java.util.stream.Collectors;

public class BookDetailsView implements BookDetailsViewInterface {
	private final BookDetailsPresenter presenter;
	private static final int GLIMPSE_SIZE = 10;
	private ViewInterface baseView;
	private final Stage stage = new Stage();
	private final BorderPane mainPane = new BorderPane();
	private static final int WIDTH = 600;
	private static final int HEIGHT = 800;
	private static final int SMALL_SPACING = 5;
	private static final int BIG_SPACING = 10;
	private ObservableList<PageViewModel> pages;
	private String summary;
	private String isbn;
	private String imagePath;
	private String title;
	private Collection<PageViewModel> bookPages;
	private boolean published;

	public BookDetailsView(BookDetailsPresenter presenter) {
		this.presenter = presenter;
		presenter.setView(this);
		stage.setOnCloseRequest(event -> presenter.closeView());
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
		var computedX = baseX + baseView.getStage().getWidth() + SMALL_SPACING;
		var computedY = baseY + baseView.getStage().getHeight() / 2 - stage.getHeight() / 2;
		//Si la fenêtre dépasse à droite, on la place à gauche
		if (computedX + stage.getWidth() / 2 > Screen.getPrimary().getBounds().getMaxX()) {
			computedX = baseX - stage.getWidth() - SMALL_SPACING;
			if (computedX - stage.getWidth() / 2 < Screen.getPrimary().getBounds().getMinX()) {
				computedX = baseX;
			}
		}
		stage.setX(computedX);
		stage.setY(computedY);
	}

	@Override
	public void setBaseView(ViewInterface baseView) {
		if (this.baseView == null) {
			this.baseView = baseView;
			initStage();
		}
	}

	@Override
	public void changeView(ViewsEnum viewName) {
		baseView.changeView(viewName);
	}

	@Override
	public void refresh() {
		mainPane.getChildren().clear();
		presenter.askBookToView();
		displayBook();
	}

	@Override
	public Window getStage() {
		return stage;
	}

	@Override
	public void displayBook() {
		stage.show();
		stage.setTitle(this.title);
		var insets = new Insets(SMALL_SPACING);

		var bookBox = new VBox();
		bookBox.setSpacing(BIG_SPACING);
		bookBox.setAlignment(Pos.CENTER);
		bookBox.setPadding(new javafx.geometry.Insets(5));
		bookBox.setFillWidth(true);
		bookBox.setMaxWidth(WIDTH / 2 - 5);

		var title = new Label(this.title);
		title.getStyleClass().add("details-title");
		title.setWrapText(true);
		title.setAlignment(Pos.CENTER);
		title.setPrefWidth(WIDTH);
		mainPane.setTop(title);
		BorderPane.setMargin(title, insets);

		var iv = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/placeholder.png")).toExternalForm()));
		if (imagePath != null && !imagePath.isEmpty()) {
			iv = new ImageView(new Image(imagePath));
		}
		iv.setFitWidth(100);
		iv.setPreserveRatio(true);

		var imageBox = new VBox();
		imageBox.getChildren().add(iv);
		imageBox.setAlignment(Pos.CENTER);
		bookBox.getChildren().add(imageBox);

		bookBox.getChildren().add(new Label(isbn));

//		centerBox.getChildren().add(new Label(book.getAuthor().getFullName()));
//		L'utilisateur ne pouvant voir QUE ses livres, on ne l'affiche donc pas

		var summary = new Label(this.summary);
		summary.setWrapText(true);
		bookBox.getChildren().add(summary);

		var scrollPane = new ScrollPane();
		scrollPane.setContent(bookBox);
		scrollPane.setFitToWidth(true);
		mainPane.setCenter(scrollPane);
		BorderPane.setMargin(scrollPane, insets);

		var bottomPane = new HBox();
		bottomPane.setAlignment(Pos.CENTER);
		bottomPane.setSpacing(SMALL_SPACING);
		if (!published) {
			var editBtn = new Button("✏ Éditer");
			editBtn.setOnAction(e -> presenter.editBook());
			var publishBtn = new Button("📖 Publier");
			publishBtn.setOnAction(e -> presenter.publishBook());
			var deleteBtn = new Button("❌ Supprimer");
			deleteBtn.setOnAction(e -> popupConfirmBookDeletion());
			bottomPane.getChildren().addAll(editBtn, publishBtn, deleteBtn);
		}
		var closeBtn = new Button("Fermer");
		closeBtn.setOnAction(action -> presenter.closeView());
		var closeIcon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/emergency-exit.png"))));
		closeIcon.setFitHeight(20);
		closeIcon.setPreserveRatio(true);
		closeBtn.setGraphic(closeIcon);
		bottomPane.getChildren().add(closeBtn);
		mainPane.setBottom(bottomPane);
		BorderPane.setMargin(bottomPane, insets);

		var rightPane = new BorderPane();
		var tableView = fillPagesTable(bookPages);
		rightPane.setCenter(tableView);

		var pageInputs = new VBox();
		pageInputs.setSpacing(SMALL_SPACING);
		if (!published) {
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
			addPageBtn.setOnAction(e -> {
				if (!contentInput.getText().isEmpty()) {
					presenter.addPage(contentInput.getText());
					contentInput.clear();
				}
			});
			var editPageBtn = new Button("✏");
			editPageBtn.setOnAction(e -> presenter.editPage(tableView.getSelectionModel().getSelectedItem()));
			var removePageBtn = new Button("➖");
			removePageBtn.setOnAction(e -> presenter.removePage(tableView.getSelectionModel().getSelectedItem()));
			buttonBox.getChildren().addAll(addPageBtn, editPageBtn, removePageBtn);
			pageInputs.getChildren().addAll(fieldsInputs, buttonBox);
		}
		rightPane.setBottom(pageInputs);
		rightPane.setMaxSize(WIDTH / 2 - 5, HEIGHT);
		mainPane.setRight(rightPane);
		BorderPane.setMargin(rightPane, insets);
	}

	@Override
	public void editPage(PageViewModel selectedItem) {
		popupEditPage(selectedItem);
	}

	@Override
	public void confirmPageSuppression(PageViewModel selectedPage) {
		popupConfirmPageDeletion(selectedPage);
	}

	@Override
	public void setBookToDisplay(BookViewModel bookToDisplay) {
		this.title = bookToDisplay.getTitle();
		this.isbn = bookToDisplay.getIsbn();
		this.summary = bookToDisplay.getSummary();
		this.imagePath = bookToDisplay.getImagePath();
		this.published = bookToDisplay.isPublished();
		this.bookPages = bookToDisplay.getPages();
	}

	private void popupConfirmPageDeletion(PageViewModel selectedPage) {
		var references = 0;
		for (PageViewModel p : pages) {
			System.out.println(p.getChoices());
			if (p.getChoices().containsValue(selectedPage)) {
				references++;
			}
		}
		var alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Suppression de page");
		alert.setHeaderText(String.format("Cette page est référencée dans %d autre(s) page(s), êtes-vous sûr ?", references));
		alert.setContentText("Cette action est irréversible.");
		var result = alert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			presenter.confirmPageDeletion(selectedPage);
		}
	}

	private void initStage() {
		stage.initModality(Modality.NONE);
		stage.initOwner(baseView.getStage());
		stage.setResizable(false);
		stage.setWidth(WIDTH);
		stage.setHeight(HEIGHT);
		var scene = new Scene(mainPane);
		scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm());
		stage.setScene(scene);
		setPosition(stage);
	}

	private TableView<PageViewModel> fillPagesTable(Collection<PageViewModel> pages) {
		//Avec l'aide de https://docs.oracle.com/javafx/2/ui_controls/table-view.htm

		var pagesView = new TableView<PageViewModel>();
		pagesView.setEditable(false);
		pagesView.setMaxWidth(WIDTH / 2 - 5);
		pagesView.setMaxHeight(HEIGHT);
		pagesView.setPlaceholder(new Label("Aucune page"));
		pagesView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		pagesView.setRowFactory(tv -> {
			var row = new TableRow<PageViewModel>();
			if (!published) {
				row.setOnMouseClicked(event -> {
					if (event.getClickCount() == 2 && (!row.isEmpty())) {
						presenter.editPage(row.getItem());
					}
				});
			}
			return row;
		});

		var pageCol = new TableColumn<PageViewModel, String>("N°");
		pageCol.setCellValueFactory(param -> new SimpleStringProperty(String.format("%02d", presenter.getPageNumber(param.getValue()))));
		pageCol.setCellFactory(TextFieldTableCell.forTableColumn());
		pageCol.setMaxWidth(20);
		pageCol.setMinWidth(20);
		pageCol.setResizable(false);

		var contentCol = new TableColumn<PageViewModel, String>("Contenu");
		contentCol.setCellValueFactory(new PropertyValueFactory<>("content"));
		contentCol.setCellFactory(TextFieldTableCell.forTableColumn());

		var choicesCol = new TableColumn<PageViewModel, String>("Choix");
		choicesCol.setCellValueFactory(param -> {
			var value = new StringJoiner(", ");
			var page = param.getValue();
			var choices = page.getChoices();
			for (var destinationPage : choices.values()) {
				value.add("" + presenter.getPageNumber(destinationPage));
			}
			return new SimpleStringProperty(value.toString());
		});
		choicesCol.setCellFactory(TextFieldTableCell.forTableColumn());

		pagesView.getColumns().addAll(Arrays.asList(pageCol, contentCol, choicesCol));

		this.pages = FXCollections.observableArrayList(pages);
		pagesView.setItems(this.pages);

		pagesView.requestFocus();
		pagesView.getFocusModel().focus(0);
		pagesView.getSelectionModel().selectFirst();
		pagesView.getSortOrder().add(pageCol);
		return pagesView;
	}

	private void popupConfirmBookDeletion() {
		//Inspired by https://www.geeksforgeeks.org/javafx-popup-class/ and https://docs.oracle.com/javase/8/javafx/api/javafx/stage/Popup.html
		var popup = new Popup();
		var yesBtn = new Button("Oui");
		yesBtn.setOnAction(e2 -> {
			popup.hide();
			presenter.deleteBook();
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

	private void popupEditPage(PageViewModel selectedPage) {
		var popup = new Popup();
		var popupRoot = new VBox();
		popupRoot.paddingProperty().setValue(new Insets(BIG_SPACING));
		popupRoot.setSpacing(SMALL_SPACING);
		popupRoot.setAlignment(Pos.CENTER);
		var contentBox = new HBox();
		var contentField = new TextField(selectedPage.getContent());
		contentField.setPromptText("Contenu de la page");
		var saveContentBtn = new Button("✔");
		saveContentBtn.setOnAction(event -> {
			if (!contentField.getText().isBlank()) {
				selectedPage.setContent(contentField.getText());
			}
		});
		contentBox.setSpacing(2);
		contentBox.getChildren().setAll(contentField, saveContentBtn);
		var choicesBox = new VBox();
		choicesBox.setSpacing(SMALL_SPACING);
		choicesBox.setAlignment(Pos.CENTER);
		var choices = selectedPage.getChoices();
		for (var choice : choices.keySet()) {
			var choiceBox = new HBox();
			choiceBox.setSpacing(SMALL_SPACING);
			choiceBox.setAlignment(Pos.CENTER);
			var choiceLabel = new Label(choice);
			final var directionLabel = new Label("➡");
			var destinationPage = choices.get(choice);
			var content = destinationPage.getContent();
			var destinationLabel = new Label(formatPage(destinationPage, content));
			var deleteChoiceBtn = new Button("❌");
			deleteChoiceBtn.setOnAction(event -> {
				if (!choiceLabel.getText().isBlank() && !destinationLabel.getText().isBlank()) {
					selectedPage.removeChoice(choiceLabel.getText());
					refresh();
				}
			});
			choiceBox.getChildren().addAll(choiceLabel, directionLabel, destinationLabel, deleteChoiceBtn);
			choicesBox.getChildren().add(choiceBox);
		}
		var saveBtn = new Button("✔");
		saveBtn.setOnAction(e -> {
			presenter.updatePage(selectedPage);
			popup.hide();
		});
		var addChoiceBtn = new Button("➕ choix");
		addChoiceBtn.setOnAction(e -> {
			var choiceBox = new HBox();
			choiceBox.setSpacing(SMALL_SPACING);
			choiceBox.setAlignment(Pos.CENTER);
			var choiceField = new TextField();
			choiceField.setPromptText("Choix");
			var destinationField = selectPageChoice(selectedPage);
			var saveChoiceBtn = new Button("💾");
			saveChoiceBtn.setOnAction(event -> {
				var item = destinationField.getItems().size() > 0 ? destinationField.getItems().get(0) : null;
				if (!choiceField.getText().isBlank() && item != null) {
					selectedPage.addChoice(choiceField.getText(), presenter.getPageById(destinationField.getItems().get(0).getId()));
					refresh();
				}
			});
			choiceBox.getChildren().addAll(choiceField, destinationField, saveChoiceBtn);
			choicesBox.getChildren().add(choiceBox);
		});
		var cancelBtn = new Button("✖");
		cancelBtn.setOnAction(e -> popup.hide());
		var buttonsBox = new HBox();
		buttonsBox.getChildren().addAll(saveBtn, addChoiceBtn, cancelBtn);
		buttonsBox.setSpacing(BIG_SPACING);
		buttonsBox.setAlignment(Pos.BOTTOM_CENTER);
		popupRoot.getChildren().addAll(contentBox, choicesBox, buttonsBox);
		fillAndPlacePopup(popup, popupRoot);
	}

	private String formatPage(PageViewModel destinationPage, String content) {
		return String.format("%d) %s", presenter.getPageNumber(destinationPage), content.substring(0, Math.min(GLIMPSE_SIZE, content.length())));
	}

	private MenuButton selectPageChoice(PageViewModel selected) {
		var destinationField = new MenuButton("Page cible");
		destinationField.getItems().addAll(pages.stream()
				.filter(page -> !page.equals(selected) && !selected.getChoices().containsValue(page) &&
						!page.getChoices().containsValue(selected))
				.map(page -> {
					final var content = page.getContent();
					var item = new MenuItem(formatPage(page, content));
					item.setOnAction(e -> destinationField.setText(item.getText()));
					item.idProperty().setValue(page.getId());
					return item;
				}).collect(Collectors.toList()));
		return destinationField;
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