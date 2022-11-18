package org.helmo.gbeditor.models.exceptions;

/**
 * Exception thrown when the page of the book is not valid.
 */
public class IllegalPageException extends IllegalArgumentException {
	/**
	 * Constructor of the exception.
	 */
	public IllegalPageException() {
		super("La page doit être valide !");
	}
}
