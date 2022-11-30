package org.helmo.gbeditor.models.exceptions;

/**
 * Exception thrown when there is not enough pages in a book
 */
public class NotEnoughPagesException extends IllegalArgumentException {
	/**
	 * Constructor of the NotEnoughPagesException exception
	 */
	public NotEnoughPagesException() {
		super("Le livre doit contenir au moins une page.");
	}
}
