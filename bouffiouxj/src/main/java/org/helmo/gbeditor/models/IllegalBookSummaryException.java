package org.helmo.gbeditor.models;

/**
 * Exception thrown when the summary of the book is not valid.
 */
public class IllegalBookSummaryException extends IllegalArgumentException {
	/**
	 * Constructor of the exception.
	 *
	 */
	public IllegalBookSummaryException() {
		super("Le résumé du livre doit avoir une taille comprise entre 1 et 500 caractères");
	}
}
