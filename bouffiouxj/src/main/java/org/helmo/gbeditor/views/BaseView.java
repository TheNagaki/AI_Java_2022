package org.helmo.gbeditor.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Window;
import org.helmo.gbeditor.presenters.interfaces.ViewInterface;
import org.helmo.gbeditor.presenters.ViewsEnum;

import java.util.Map;

/**
 * Base view of the application
 * It consists of a BorderPane where the Center is the view of the application (MainView, CreateBookView, etc.)
 * The bottom is the footer of the application, which contains the Message to display to the user
 */
public class BaseView implements ViewInterface {

	private ViewInterface currentView;
	private final Map<ViewsEnum, ViewInterface> views;
	private final BorderPane principalPane = new BorderPane();
	private final Label message = new Label("GBEditor by Jules Bouffioux, iteration 3.0");

	/**
	 * Constructor of the BaseView class
	 *
	 * @param views the map of all the other views to display
	 */
	public BaseView(Map<ViewsEnum, ViewInterface> views) {
		message.getStyleClass().add("message");
		message.setAlignment(Pos.BOTTOM_RIGHT);
		for (ViewInterface view : views.values()) {
			view.setBaseView(this);
		}
		this.views = views;
		this.currentView = views.get(ViewsEnum.CONNEXION);
		currentView.refresh();
		principalPane.setCenter(currentView.getRoot());

		message.setAlignment(Pos.CENTER_RIGHT);
		var footer = new BorderPane();
		footer.setPadding(new Insets(2));
		footer.setRight(message);
		principalPane.setBottom(footer);
	}

	@Override
	public Parent getRoot() {
		return principalPane;
	}

	@Override
	public void display(String response) {
		var alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setContentText(response);
		alert.show();
	}

	@Override
	public void setBaseView(ViewInterface baseView) {
		//Do nothing because this is the base view
	}

	@Override
	public void changeView(ViewsEnum viewName) {
		if (views.containsKey(viewName)) {
			currentView = views.get(viewName);
			currentView.refresh();
		}
		principalPane.getChildren().clear();
		principalPane.setCenter(currentView.getRoot());
		principalPane.setBottom(message);
	}

	@Override
	public void refresh() {
		currentView.refresh();
	}

	@Override
	public Window getStage() {
		return principalPane.getScene().getWindow();
	}

	@Override
	public void close() {
		getStage().hide();
	}
}
