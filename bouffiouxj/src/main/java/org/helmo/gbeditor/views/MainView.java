package org.helmo.gbeditor.views;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import org.helmo.gbeditor.models.Book;
import org.helmo.gbeditor.presenters.MainPresenter;
import org.helmo.gbeditor.presenters.ViewInterface;
import org.helmo.gbeditor.presenters.ViewsEnum;

import java.util.Iterator;
import java.util.Set;

public class MainView implements ViewInterface {

	private final MainPresenter presenter;
	private ViewInterface baseView;
	private final GridPane gridPane = new GridPane();
	private final BorderPane topPane = new BorderPane();
	private final BorderPane mainPane = new BorderPane();

	public MainView(MainPresenter presenter) {
		this.presenter = presenter;
		this.presenter.setView(this);
		initView();
	}

	private void initView() {
		Set<Book> bookSet = presenter.getBooksFromAuthor(); //TODO: give this responsibility to the presenter
		Iterator<Book> ite = bookSet.iterator();
		int ITEMS_PER_ROW = 5;
		if (ite.hasNext()) {
			for (int i = 0; ite.hasNext() && i < bookSet.size(); i++) {
				Book b = ite.next();
				BorderPane box = new BorderPane();
//				box.topProperty() TODO: align title to horizontal center
				box.setTop(new Label(b.getTitle()));
				ImageView iv = new ImageView(new Image((b.getImage() == null) ? getClass().getResource("/placeholder.png").toExternalForm() : b.getImage()));
				iv.setFitWidth(50);
				iv.setFitHeight(50);
				box.setCenter(iv);
//				box.setBottom(new Label(b.getSummary()));

				final int x = i % ITEMS_PER_ROW;
				final int y = i / ITEMS_PER_ROW;
				gridPane.add(box, 2 * x, 2 * y, 2, 2);
			}
		} else {
			gridPane.add(new Label("Vous n'avez pas encore créé de livre."), 0, 1, 3, 1);
		}
		Button b = new Button("Créer un livre");
		b.setOnAction(action -> baseView.changeView(ViewsEnum.CREATE_BOOK));
		gridPane.add(b, 0, 2 * (1 + bookSet.size() / ITEMS_PER_ROW), 3, 1);
		gridPane.setAlignment(Pos.BASELINE_CENTER);
		gridPane.setHgap(10);
		gridPane.setVgap(10);

		Label viewTitle = new Label("Mes livres");
		viewTitle.getStyleClass().add("title");
		topPane.setCenter(viewTitle);

		mainPane.setTop(topPane);
		mainPane.setCenter(gridPane);

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
	}

	public void setAuthorName(String name) {
		Label viewAuthorName = new Label(name);
		viewAuthorName.getStyleClass().add("authorName");
		topPane.setRight(viewAuthorName);
	}
}
