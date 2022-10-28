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
import javafx.scene.layout.VBox;
import org.helmo.gbeditor.models.Book;
import org.helmo.gbeditor.presenters.MainPresenter;
import org.helmo.gbeditor.presenters.ViewInterface;
import org.helmo.gbeditor.presenters.ViewsEnum;

import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

/**
 * Main view of the application
 * It displays all the books of the current author and allows to create a new one
 * It also allows to delete a book and to edit it //TODO: implement the edit
 */
public class MainView implements ViewInterface {

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
				ImageView iv = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("/placeholder.png")).toExternalForm()));
				if (b.getImage() != null && !b.getImage().isEmpty()) {
					iv = new ImageView(new Image(b.getImage()));
				}
				iv.setFitWidth(75);
				iv.setPreserveRatio(true);
				box.setCenter(iv);

				var bottomBox = new VBox();
				bottomBox.setAlignment(Pos.CENTER);
				var isbn = new Label(b.getIsbn());
				isbn.getStyleClass().add("thumbnail-isbn");
				bottomBox.getChildren().add(isbn);
				var deleteBtn = new Button("❌");
				deleteBtn.setOnAction(e -> {
					if (presenter.deleteBook(b)) {
						changeView(ViewsEnum.MAIN);
					} else {
						display(String.format("Error: can not delete book %s", b.getTitle()));
					}
				});
				var editBtn = new Button("✏");
				editBtn.setOnAction(e -> {
					display("This feature is not implemented yet");
//					presenter.setBookToEdit(b);
//					changeView(ViewsEnum.EDIT);
				});
				var buttonPane = new GridPane();
				buttonPane.add(editBtn, 0, 0);
				buttonPane.add(deleteBtn, 1, 0);
				bottomBox.getChildren().add(buttonPane);

				box.setBottom(bottomBox);

				box.setMaxWidth((gridPane.getWidth() - 5) / ITEMS_PER_ROW);
				box.setMaxHeight((gridPane.getHeight() - 5) / NUMBER_OF_ROWS);
				final int x = i % ITEMS_PER_ROW;
				final int y = i / ITEMS_PER_ROW;
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
		b.setOnAction(action -> baseView.changeView(ViewsEnum.CREATE_BOOK));
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
}
