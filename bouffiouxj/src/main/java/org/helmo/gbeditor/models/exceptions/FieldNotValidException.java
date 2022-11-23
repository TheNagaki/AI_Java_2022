package org.helmo.gbeditor.models.exceptions;

import org.helmo.gbeditor.models.BookDataFields;

/**
 * FieldNotValidException is an exception which is thrown when a field is not valid for a book
 */
public class FieldNotValidException extends IllegalArgumentException {
	/**
	 * Constructor of the exception
	 *
	 * @param fieldName the name of the invalid field
	 */
	public FieldNotValidException(BookDataFields fieldName) {
		super("Le champ " + fieldName + " n'est pas valide dans les méta-données du livre.");
	}
}
