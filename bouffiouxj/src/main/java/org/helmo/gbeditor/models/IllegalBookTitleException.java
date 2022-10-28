package org.helmo.gbeditor.models;

/**
 * Exception thrown when the title of the book is not valid.
 */
public class IllegalBookTitleException extends IllegalArgumentException {
	/**
	 * Constructor of the exception.
	 *
	 */
	public IllegalBookTitleException() {
		super("Le titre du livre doit avoir une taille comprise entre 1 et 150 caractères");
	}
}
