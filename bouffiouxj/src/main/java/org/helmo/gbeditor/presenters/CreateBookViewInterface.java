package org.helmo.gbeditor.presenters;

public interface CreateBookViewInterface extends ViewInterface {
	void setAuthorName(String name);

	void presetISBN(int[] isbn);
}
