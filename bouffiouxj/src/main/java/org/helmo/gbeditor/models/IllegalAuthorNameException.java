package org.helmo.gbeditor.models;

/**
 * Exception thrown when the author name and/or firstName is not valid.
 */
public class IllegalAuthorNameException extends IllegalArgumentException {
	/**
	 * Constructor of the exception.
	 */
	public IllegalAuthorNameException() {
		super("Les noms et prénoms de l'auteur ne peuvent pas être nuls ou vides");
	}
}
