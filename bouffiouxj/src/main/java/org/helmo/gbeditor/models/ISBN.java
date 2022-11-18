package org.helmo.gbeditor.models;

import org.helmo.gbeditor.models.exceptions.IllegalIsbnBookIdException;
import org.helmo.gbeditor.models.exceptions.IllegalIsbnChecksumException;
import org.helmo.gbeditor.models.exceptions.IllegalIsbnFormatException;
import org.helmo.gbeditor.models.exceptions.IllegalIsbnLinguisticIdException;

import java.util.regex.Pattern;

/**
 * This class represents an ISBN in the application.
 */
public class ISBN {
	private final int linguisticGroup;
	private final int idAuthor;
	private final int idBook;
	private final int checkSum;

	private static final String REGEX_ISBN_13 = "\\d-?\\d{0,6}-?\\d{0,2}-?[\\dX]?";
	private static final String REGEX_ISBN_10 = "\\d{0,10}";

	/**
	 * Constructor of the ISBN. It checks the validity of the ISBN.
	 *
	 * @param linguisticGroup The linguistic group of the ISBN.
	 * @param idAuthor        The id of the author of the book.
	 * @param idBook          The id of the book.
	 */
	public ISBN(int linguisticGroup, int idAuthor, int idBook) {
		this(linguisticGroup, idAuthor, idBook, computeCheckSum(linguisticGroup, idAuthor, idBook));
	}

	/**
	 * Constructor of the ISBN. It checks the validity of the ISBN.
	 *
	 * @param linguisticGroup The linguistic group of the ISBN.
	 * @param idAuthor        The id of the author of the book.
	 * @param idBook          The id of the book.
	 * @param checkSum        The check sum of the ISBN.
	 */
	public ISBN(int linguisticGroup, int idAuthor, int idBook, int checkSum) {
		checkValidity(linguisticGroup, idAuthor, idBook, checkSum);
		this.linguisticGroup = linguisticGroup;
		this.idAuthor = idAuthor;
		this.idBook = idBook;
		this.checkSum = checkSum;
	}

	/**
	 * Computes the check sum of the ISBN for the given String prefix.
	 *
	 * @param isbn The prefix of the ISBN as a String (9-999-99 so without the checksum).
	 * @return The check sum of the ISBN.
	 */
	public static String computeCheckSum(String isbn) {
		var isbnWoutControl = "\\d-?\\d{0,6}-?\\d{0,2}";
		if (Pattern.matches(isbnWoutControl, isbn)) {
			var splittedS = isbn.split("-");
			var splittedI = new int[3];
			for (int i = 0; i < splittedS.length; i++) {
				splittedI[i] = Integer.parseInt(splittedS[i]);
			}
			var checkSum = computeCheckSum(splittedI[0], splittedI[1], splittedI[2]);
			return checkSum == 10 ? "X" : String.valueOf(checkSum);
		} else {
			throw new IllegalIsbnFormatException();
		}
	}

	private void checkValidity(int linguisticGroup, int idAuthor, int idBook, int checkSum) {
		verifyLinguisticGroup(linguisticGroup);
		verifyAuthorId(idAuthor);
		verifyBookId(idBook);
		final int checkSumComputed = computeCheckSum(linguisticGroup, idAuthor, idBook);
		verifyCheckSum(checkSum, checkSumComputed);
	}

	private static void verifyCheckSum(int checkSum, int checkSumComputed) {
		//Check if the checkSum is between 0 and 10 and if the checkSum is correct
		if (checkSum < 0 || checkSum > 10 || checkSum != checkSumComputed) {
			throw new IllegalIsbnChecksumException(checkSum, checkSumComputed);
		}
	}

	private static void verifyBookId(int idBook) {
		//Check if the idBook is between 0 and 99
		if (idBook < 0 || idBook > 99) {
			throw new IllegalIsbnBookIdException();
		}
	}

	private static void verifyAuthorId(int idAuthor) {
		//Check if the idAuthor is between 0 and 999999
		if (idAuthor < 0 || idAuthor > 999999) {
			throw new IllegalIsbnFormatException();
		}
	}

	private static void verifyLinguisticGroup(int linguisticGroup) {
		//Check if the idAuthor is between 0 and 9
		if (linguisticGroup < 0 || linguisticGroup > 9) {
			throw new IllegalIsbnLinguisticIdException();
		}
	}

	/**
	 * Constructor of the ISBN. It checks the validity of the ISBN.
	 *
	 * @param isbn The string representation of the ISBN.
	 */
	public ISBN(String isbn) {
		String[] values;
		values = splitIsbn(isbn);
		this.linguisticGroup = Integer.parseInt(values[0]);
		this.idAuthor = Integer.parseInt(values[1]);
		this.idBook = Integer.parseInt(values[2]);
		int check = (values[3].equals("X")) ? 10 : Integer.parseInt(values[3]);
		if (check != getCheckSum()) {
			throw new IllegalIsbnChecksumException(check, getCheckSum());
		}
		this.checkSum = check;
	}

	private static String[] splitIsbn(String isbn) {
		String[] values;
		if (Pattern.matches(REGEX_ISBN_13, isbn)) {
			values = isbn.split("-");
		} else if (Pattern.matches(REGEX_ISBN_10, isbn)) {
			values = new String[4];
			values[0] = isbn.substring(0, 1); //Linguistic group - 1 digit
			values[1] = isbn.substring(1, 7); //Author id - 6 digits
			values[2] = isbn.substring(7, 9); //Book id - 2 digits
			values[3] = isbn.substring(9, 10); //CheckSum - 1 digit
		} else {
			throw new IllegalIsbnFormatException();
		}
		return values;
	}

	/**
	 * Create a new ISBN from the given parameters.
	 *
	 * @param linguisticGroup The linguistic group of the ISBN.
	 * @param idAuthor        The id of the author of the book.
	 * @return The ISBN.
	 */
	public static ISBN createNewISBN(int linguisticGroup, int idAuthor) {
		return new ISBN(linguisticGroup, idAuthor, getRandomIdBook());
	}

	private static int getRandomIdBook() {
		return (int) (Math.random() * 100);
	}

	/**
	 * Compute the check sum of the ISBN.
	 *
	 * @param linguisticGroup The linguistic group of the ISBN.
	 * @param idAuthor        The id of the author of the book.
	 * @param idBook          The id of the book.
	 * @return The check sum.
	 */
	public static int computeCheckSum(int linguisticGroup, int idAuthor, int idBook) {
		return (linguisticGroup + idAuthor + idBook) % 11;
	}

	/**
	 * Gets the check sum of the ISBN.
	 *
	 * @return The check sum.
	 */
	public final int getCheckSum() {
		return computeCheckSum(linguisticGroup, idAuthor, idBook);
	}

	/**
	 * Gets the linguistic group of the ISBN.
	 *
	 * @return The linguistic group.
	 */
	public int getLinguisticGroup() {
		return linguisticGroup;
	}

	/**
	 * Gets the id of the author of the book.
	 *
	 * @return The id of the author.
	 */
	public int getIdAuthor() {
		return idAuthor;
	}

	/**
	 * Gets the id of the book.
	 *
	 * @return The id of the book.
	 */
	public int getIdBook() {
		return idBook;
	}

	@Override
	public String toString() {
		return String.format("%d-%06d-%02d-%s", linguisticGroup, idAuthor, idBook, checkSum == 10 ? "X" : checkSum);
	}
}