package org.helmo.gbeditor.repositories;

/**
 * This exception is thrown when the image extension does not match the allowed ones or does not correspond to its content
 */
public class IllegalImageExtensionException extends IllegalArgumentException {
	/**
	 * IllegalImageExtensionException constructor
	 */
	public IllegalImageExtensionException() {
		super("L'extension de l'image choisie ne correspond pas à son contenu");
	}
}
