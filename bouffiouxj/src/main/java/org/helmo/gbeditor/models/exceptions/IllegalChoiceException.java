package org.helmo.gbeditor.models.exceptions;

/**
 * This exception is thrown when a choice is not valid
 */
public class IllegalChoiceException extends IllegalArgumentException {
	public IllegalChoiceException() {
		super("Le choix sélectionné n'est pas valide");
	}
}
