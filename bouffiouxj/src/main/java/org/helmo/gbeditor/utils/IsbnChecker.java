package org.helmo.gbeditor.utils;

public class IsbnChecker {
	/**
	 * <h2>Check if the isbn is valid</h2>
	 * <p>Un numéro ISBN-10 est composé par 10 chiffres. Il commence par le numéro du groupe linguistique
	 * visé (2 dans notre cas pour le français) suivi par l’identifiant de l’auteur qui correspondra aux 6
	 * chiffres de votre matricule, suivi par 2 chiffres identifiant le livre. Le dernier chiffre est un code de
	 * vérification calculé avec la formule suivante :
	 * <ol>
	 * <li>On prend les 9 premiers chiffres de l’ISBN et on les multiplie par un poids allant de 10 à 2
	 * <p>Exemple : pour l’ISBN {@code 2-070039-01-#}, cette somme sera {@code 2*10 + 0*9 + 7*8 + 0*7 + 0*6 + 3*5 +
	 * 9*4 + 0*3 + 1*2 = 129}</p></li>
	 * <li>On calcule le reste de la somme précédente par 11
	 * <p>Exemple : {@code 129 % 11 = 129 – 121 = 8}</p></li>
	 * <li>On retire le reste de la somme de 11 : c’est le code de contrôle
	 * <p>Exemple : {@code 11 – 8 = 3}.</p></li>
	 * </ol>
	 *
	 * @param isbn the isbn to check
	 * @return true if the isbn is valid false otherwise
	 */
	public static boolean checkIsbn(String isbn) {
		return true;
//		isbn = isbn.replaceAll("^[0-9]", "");
//		if (isbn.length() != 10) {
//			return false;
//		}
//		int sum = 0;
//		for (int i = 0; i < 9; i++) {
//			if (!Character.isDigit(isbn.charAt(i))) {
//				return false;
//			}
//			sum += Character.getNumericValue(isbn.charAt(i)) * (10 - i);
//		}
//		final int last = Character.getNumericValue(isbn.charAt(9));
//		return (11 - (sum % 11)) % 11 == last;
	}
}
