package org.helmo.gbeditor.views;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Window;
import org.helmo.gbeditor.models.Book;
import org.helmo.gbeditor.presenters.MainPresenter;
import org.helmo.gbeditor.presenters.interfaces.MainViewInterface;
import org.helmo.gbeditor.presenters.interfaces.ViewInterface;
import org.helmo.gbeditor.presenters.ViewsEnum;

import java.util.*;

/**
 * Main view of the application
 * It displays all the books of the current author and allows to create a new one
 * It displays the book details in a popup when the user clicks on a book
 * In this popup, the user can edit the book or delete it
 * It also allows the user to add pages to the book
 */
public class MainView implements MainViewInterface {

	private final MainPresenter presenter;
	private ViewInterface baseView;
	private final GridPane gridPane = new GridPane();
	private final BorderPane topPane = new BorderPane();
	private final BorderPane mainPane = new BorderPane();
	private Set<Book> booksFromAuthor = new LinkedHashSet<>();

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
		presenter.askBooksFromAuthor();
		Iterator<Book> ite = booksFromAuthor.iterator();
		final int ITEMS_PER_ROW = 4;
		final int NUMBER_OF_ROWS = 2;
		final int BOOK_BOX_SIZE = 3;
		if (ite.hasNext()) {
			for (int i = 0; ite.hasNext() && i < booksFromAuthor.size(); i++) {
				var b = ite.next();
				var box = new BorderPane();
				var title = new Label(presenter.getTitle(b));
				title.getStyleClass().add("thumbnail-title");
				title.setAlignment(Pos.CENTER);
				box.setTop(title);
				var iv = setBookImage(b, 75);
				box.setCenter(iv);

				var bottomBox = new HBox();
				bottomBox.setAlignment(Pos.CENTER);
				var isbn = new Label(presenter.getIsbn(b));
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

		var scrollPane = new ScrollPane(gridPane);
		scrollPane.setFitToWidth(true);
		scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		scrollPane.setPadding(new javafx.geometry.Insets(0, 0, 10, 0));

		var viewTitle = new Label("Mes livres");
		viewTitle.getStyleClass().add("title");
		topPane.setCenter(viewTitle);

		var createBookBtn = new Button("Créer un livre");
		createBookBtn.setOnAction(action -> {
			presenter.setBookToEdit(null);
			baseView.changeView(ViewsEnum.EDIT_BOOK);
		});
		var quitBtn = new Button("Quitter");
		quitBtn.setOnAction(action -> presenter.onQuit_Click());
		var buttonBox = new HBox();
		buttonBox.setSpacing(10);
		buttonBox.getChildren().addAll(createBookBtn, quitBtn);
		buttonBox.setAlignment(Pos.BASELINE_CENTER);

		mainPane.setTop(topPane);
		mainPane.setCenter(scrollPane);
		mainPane.setBottom(buttonBox);
		BorderPane.setMargin(buttonBox, new javafx.geometry.Insets(10, 0, 0, 0));

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
		var viewAuthorName = new Label(name);
		viewAuthorName.getStyleClass().add("author-name");
		topPane.setRight(viewAuthorName);
	}

	@Override
	public Window getStage() {
		return baseView.getStage();
	}

	private ImageView setBookImage(Book book, int width) {
		var iv = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/placeholder.png")).toExternalForm()));
		if (presenter.getImagePath(book) != null && !presenter.getImagePath(book).isEmpty()) {
			iv = new ImageView(new Image(presenter.getImagePath(book)));
		}
		iv.setFitWidth(width);
		iv.setPreserveRatio(true);
		return iv;
	}

	/**
	 * This method is used to show all the books from the author in the database.
	 *
	 * @param books the books to set
	 */
	@Override
	public void setBooksFromAuthor(Set<Book> books) {
		if (books != null) {
			booksFromAuthor = books;
		}
	}

	@Override
	public void close() {
		baseView.close();
	}
}
