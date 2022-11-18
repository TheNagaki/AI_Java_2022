package org.helmo.gbeditor.models.exceptions;

/**
 * Exception thrown when the author of the book is not valid.
 */
public class IllegalAuthorException extends IllegalArgumentException {
	/**
	 * Constructor of the exception.
	 */
	public IllegalAuthorException() {
		super("L'auteur doit être valide !");
	}
}
