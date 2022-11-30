package org.helmo.gbeditor.models.exceptions;

/**
 * Exception thrown when a book is published and the user tries to update it
 */
public class CannotUpdatePublishedBookException extends IllegalArgumentException {
	/**
	 * Constructor of the CannotUpdatePublishedBookException exception
	 */
	public CannotUpdatePublishedBookException() {
		super("Vous ne pouvez pas modifier un livre publié.");
	}
}
