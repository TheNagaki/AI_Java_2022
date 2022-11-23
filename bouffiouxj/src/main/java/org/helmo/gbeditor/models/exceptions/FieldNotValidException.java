package org.helmo.gbeditor.models.exceptions;

import org.helmo.gbeditor.models.BookDataFields;

public class FieldNotValidException extends IllegalArgumentException {
	public FieldNotValidException(BookDataFields fieldName) {
		super("Le champ " + fieldName + " n'est pas valide dans les méta-données du livre.");
	}
}
