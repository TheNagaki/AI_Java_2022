package org.helmo.gbeditor.models;

/**
 * Exception thrown when the linguistic group of the ISBN is not valid
 */
public class IllegalIsbnLinguisticIdException extends IllegalArgumentException {
	/**
	 * Constructor of the exception
	 */
	public IllegalIsbnLinguisticIdException() {
		super("Le groupe linguistique doit être compris entre 0 et 9");
	}
}
