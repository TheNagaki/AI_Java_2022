package org.helmo.gbeditor.presenters;

import javafx.scene.Parent;

public interface ViewInterface {
	Parent getRoot();
	void display(String response);

	void setBaseView(ViewInterface baseView);

	void changeView(ViewsEnum viewName);

	void refresh();

	void setAuthorName(String authorName);
}
