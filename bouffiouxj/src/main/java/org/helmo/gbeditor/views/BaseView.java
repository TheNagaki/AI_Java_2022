package org.helmo.gbeditor.views;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import org.helmo.gbeditor.presenters.ViewInterface;
import org.helmo.gbeditor.presenters.ViewsEnum;

import java.util.Map;

public class BaseView implements ViewInterface {

	private ViewInterface currentView;
	private final Map<ViewsEnum, ViewInterface> views;

	private final BorderPane principalPane = new BorderPane();

	private final Label message = new Label("");

	public BaseView(Map<ViewsEnum, ViewInterface> views) {
		for (ViewInterface view : views.values()) {
			view.setBaseView(this);
		}
		this.views = views;
		changeView(ViewsEnum.CONNEXION);
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
}
