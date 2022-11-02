package org.helmo.gbeditor.views;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.helmo.gbeditor.models.Book;
import org.helmo.gbeditor.presenters.MainPresenter;
import org.helmo.gbeditor.presenters.MainViewInterface;
import org.helmo.gbeditor.presenters.ViewInterface;
import org.helmo.gbeditor.presenters.ViewsEnum;

import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

/**
 * Main view of the application
 * It displays all the books of the current author and allows to create a new one
 * It displays the book details in a popup when the user clicks on a book
 * In this popup, the user can edit the book or delete it //TODO: implement the edition of a book
 * It also allows the user to add pages to the book //TODO: implement the addition of pages
 */
public class MainView implements MainViewInterface {

	private final MainPresenter presenter;
	private ViewInterface baseView;
	private final GridPane gridPane = new GridPane();
	private final BorderPane topPane = new BorderPane();
	private final BorderPane mainPane = new BorderPane();

	/**
	 * Constructor of the MainView class
	 *
	 * @param mainPresenter the presenter of the view
	 */
	public MainView(MainPresenter mainPresenter) {
		this.presenter = mainPresenter;
		this.presenter.setView(this);
		initView();
	}

	private void initView() {
		Set<Book> bookSet = presenter.getBooksFromAuthor(); //TODO: give this responsibility to the presenter
		Iterator<Book> ite = bookSet.iterator();
		final int ITEMS_PER_ROW = 4;
		final int NUMBER_OF_ROWS = 2;
		final int BOOK_BOX_SIZE = 3;
		if (ite.hasNext()) {
			for (int i = 0; ite.hasNext() && i < bookSet.size(); i++) {
				Book b = ite.next();
				BorderPane box = new BorderPane();
				var title = new Label(b.getTitle());
				title.getStyleClass().add("thumbnail-title");
				title.setAlignment(Pos.CENTER);
				box.setTop(title);
				ImageView iv = setBookImage(b, 75);
				box.setCenter(iv);

				var bottomBox = new VBox();
				bottomBox.setAlignment(Pos.CENTER);
				var isbn = new Label(b.getIsbn());
				isbn.getStyleClass().add("thumbnail-isbn");
				bottomBox.getChildren().add(isbn);
				box.setBottom(bottomBox);

				box.setMaxWidth((gridPane.getWidth() - 5) / ITEMS_PER_ROW);
				box.setMaxHeight((gridPane.getHeight() - 5) / NUMBER_OF_ROWS);
				final int x = i % ITEMS_PER_ROW;
				final int y = i / ITEMS_PER_ROW;
				box.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, e -> presenter.bookClicked(b));
				gridPane.add(box, BOOK_BOX_SIZE * x, BOOK_BOX_SIZE * y, BOOK_BOX_SIZE, BOOK_BOX_SIZE);
			}
		} else {
			gridPane.add(new Label("Vous n'avez pas encore créé de livre."), 0, 1, 3, 1);
		}
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setHgap(10);
		gridPane.setVgap(10);

		ScrollPane scrollPane = new ScrollPane(gridPane);
		scrollPane.setFitToWidth(true);
		scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		scrollPane.setPadding(new javafx.geometry.Insets(0, 0, 10, 0));

		Label viewTitle = new Label("Mes livres");
		viewTitle.getStyleClass().add("title");
		topPane.setCenter(viewTitle);

		Button b = new Button("Créer un livre");
		b.setOnAction(action -> {
			presenter.setBookToEdit(null);
			baseView.changeView(ViewsEnum.EDIT_BOOK);
		});
		b.setAlignment(Pos.BOTTOM_CENTER);
		VBox buttonBox = new VBox();
		buttonBox.getChildren().add(b);
		buttonBox.setAlignment(Pos.BASELINE_CENTER);

		mainPane.setTop(topPane);
		mainPane.setCenter(scrollPane);
		mainPane.setBottom(buttonBox);

		presenter.askAuthorName();
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
		gridPane.getChildren().clear();
		initView();
		display("");
	}

	@Override
	public void setAuthorName(String name) {
		Label viewAuthorName = new Label(name);
		viewAuthorName.getStyleClass().add("author-name");
		topPane.setRight(viewAuthorName);
	}

	@Override
	public Window getStage() {
		return baseView.getStage();
	}

	@Override
	public void displayBookDetails(Book book) {
		final int WIDTH = 400;
		final int HEIGHT = 400;
		Stage stage = new Stage();
		stage.setTitle(book.getTitle());
		stage.initModality(Modality.APPLICATION_MODAL); //Sets the window so that it appears on top and blocks events from being delivered to any other application window.
		stage.initOwner(baseView.getStage());
		stage.setResizable(false);
		stage.setWidth(WIDTH);
		stage.setHeight(HEIGHT);

		BorderPane root = new BorderPane();
		root.setPadding(new javafx.geometry.Insets(5));

		VBox centerBox = new VBox();
		centerBox.setSpacing(10);
		centerBox.setAlignment(Pos.CENTER);
		centerBox.setPadding(new javafx.geometry.Insets(10, 10, 10, 10));

		Label title = new Label(book.getTitle());
		title.getStyleClass().add("details-title");
		title.setWrapText(true);
		title.setAlignment(Pos.CENTER);
		title.setPrefWidth(WIDTH - 20);
		root.setTop(title);

		ImageView iv = setBookImage(book, 100);
		VBox imageBox = new VBox();
		imageBox.getChildren().add(iv);
		imageBox.setAlignment(Pos.CENTER);
		centerBox.getChildren().add(imageBox);

		centerBox.getChildren().add(new Label(book.getIsbn()));

//		centerBox.getChildren().add(new Label(book.getAuthor().getFullName()));
//		L'utilisateur ne pouvant voir QUE ses livres, on ne l'affiche donc pas

		Label summary = new Label(book.getSummary());
		summary.setWrapText(true);
		centerBox.getChildren().add(summary);

		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setContent(centerBox);
		scrollPane.setFitToWidth(true);
		root.setCenter(scrollPane);

		var deleteBtn = new Button("Supprimer ❌");
		deleteBtn.setOnAction(e -> {
			if (presenter.deleteBook(book)) {
				changeView(ViewsEnum.MAIN);
				stage.close();
			} else {
				display(String.format("Error: can not delete book %s", book.getTitle()));
			}
		});
		var editBtn = new Button("Éditer ✏");
		editBtn.setOnAction(e -> {
			presenter.setBookToEdit(book);
			changeView(ViewsEnum.EDIT_BOOK);
			stage.close();
		});
		//TODO:Add exit emoji to close button
		var closeBtn = new Button("Fermer ✖");
		closeBtn.setOnAction(action -> stage.close());

		var buttonPane = new GridPane();
		buttonPane.setAlignment(Pos.CENTER);
		buttonPane.add(editBtn, 0, 0);
		buttonPane.add(deleteBtn, 1, 0);
		buttonPane.add(closeBtn, 2, 0);
		root.setBottom(buttonPane);

		Scene scene = new Scene(root);
		scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm());
		stage.setScene(scene);
		stage.show();
	}

	private ImageView setBookImage(Book book, int width) {
		ImageView iv = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/placeholder.png")).toExternalForm()));
		if (book.getImage() != null && !book.getImage().isEmpty()) {
			iv = new ImageView(new Image(book.getImage()));
		}
		iv.setFitWidth(width);
		iv.setPreserveRatio(true);
		return iv;
	}
}
