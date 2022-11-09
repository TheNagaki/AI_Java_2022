package org.helmo.gbeditor.views;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Window;
import org.helmo.gbeditor.presenters.ViewInterface;
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

	private final Label message = new Label("GBEditor by Jules Bouffioux, iteration 1.0");

	/**
	 * Constructor of the BaseView class
	 *
	 * @param views the map of all the other views to display
	 */
	public BaseView(Map<ViewsEnum, ViewInterface> views) {
		for (ViewInterface view : views.values()) {
			view.setBaseView(this);
		}
		this.views = views;
		this.currentView = views.get(ViewsEnum.CONNEXION);
		currentView.refresh();
		principalPane.setCenter(currentView.getRoot());
		principalPane.setBottom(message);
	}

	@Override
	public Parent getRoot() {
		return principalPane;
	}

	@Override
	public void display(String response) {
		message.setText(response);
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
	public void setAuthorName(String authorName) {
		//Do nothing because this view is not used in this context
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
