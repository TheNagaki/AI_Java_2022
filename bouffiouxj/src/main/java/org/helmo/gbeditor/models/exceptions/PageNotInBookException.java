package org.helmo.gbeditor.models.exceptions;

/**
 * PageNotInBookException is an exception which is thrown when a page is not in a book
 */
public class PageNotInBookException extends IllegalArgumentException {
	/**
	 * Constructor of the exception
	 */
	public PageNotInBookException() {
		super("La page n'est pas contenue dans le livre.");
	}
}
