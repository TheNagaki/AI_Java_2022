package org.helmo.gbeditor.models.exceptions;

/**
 * Exception thrown when the checksum of the ISBN is not valid.
 */
public class IllegalIsbnChecksumException extends IllegalArgumentException {

	/**
	 * Constructs of the exception.
	 *
	 * @param expectedChecksum the expected checksum
	 * @param actualChecksum   the actual checksum
	 */
	public IllegalIsbnChecksumException(int expectedChecksum, int actualChecksum) {
		super(String.format("Le chiffre de contrôle de l'ISBN n'est pas valide (%d au lieu de %d)", actualChecksum, expectedChecksum));
	}
}
