package org.helmo.gbeditor.models.exceptions;

/**
 * Exception thrown when the format of the ISBN is not valid.
 */
public class IllegalIsbnFormatException extends IllegalArgumentException {
	/**
	 * Constructs of the exception.
	 */
	public IllegalIsbnFormatException() {
		super("L'isbn du livre est invalide");
	}
}
