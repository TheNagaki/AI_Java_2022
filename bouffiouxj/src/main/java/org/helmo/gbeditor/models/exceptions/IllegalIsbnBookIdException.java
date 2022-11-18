package org.helmo.gbeditor.models.exceptions;

/**
 * Exception thrown when the id of the book in the ISBN is not valid.
 */
public class IllegalIsbnBookIdException extends IllegalArgumentException {
	/**
	 * Constructor of the exception.
	 */
	public IllegalIsbnBookIdException() {
		super("L'isbn du livre est invalide ou déjà utilisée");
	}
}
