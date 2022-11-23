package org.helmo.gbeditor.models.exceptions;

public class PageNotInBookException extends IllegalArgumentException {
	public PageNotInBookException(){
		super("La page n'est pas contenue dans le livre.");
	}
}
