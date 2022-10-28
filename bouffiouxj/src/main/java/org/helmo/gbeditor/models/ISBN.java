package org.helmo.gbeditor.models;

import java.util.regex.Pattern;

public class ISBN {
	private final int linguisticGroup;
	private final int idAuthor;
	private final int idBook;
	private final int checkSum;

	public ISBN(int linguisticGroup, int idAuthor, int idBook) {
		this(linguisticGroup, idAuthor, idBook, computeCheckSum(linguisticGroup, idAuthor, idBook));
	}

	public ISBN(int linguisticGroup, int idAuthor, int idBook, int checkSum) {
		//Check if the idAuthor is between 0 and 9
		if (linguisticGroup < 0 || linguisticGroup > 9) {
			throw new IllegalIsbnLinguisticIdException("The linguistic group id must be between 0 and 9");
		}
		this.linguisticGroup = linguisticGroup;
		//Check if the idAuthor is between 0 and 999999
		if (idAuthor < 0 || idAuthor > 999999) {
			throw new IllegalIsbnFormatException(String.format("The author id must be between 0 and 999999 (was %d)", idAuthor));
		}
		this.idAuthor = idAuthor;
		//Check if the idBook is between 0 and 99
		if (idBook < 0 || idBook > 99) {
			throw new IllegalIsbnBookIdException(String.format("The book id must be between 0 and 99 (was %d)", idBook));
		}
		this.idBook = idBook;
		//Check if the checkSum is between 0 and 10 and if the checkSum is correct
		if (checkSum < 0 || checkSum > 10 || checkSum != getCheckSum()) {
			throw new IllegalIsbnChecksumException(String.format("The checkSum must be between 0 and 9 and must be correct (was %d instead of %d)", checkSum, getCheckSum()), checkSum, getCheckSum());
		}
		this.checkSum = checkSum;
	}

	public ISBN(String isbn) {
		String[] values;
		String regexIsbn13 = "\\d-?\\d{0,6}-?\\d{0,2}-?[\\dX]?";
		String regexIsbn10 = "\\d{0,10}";
		if (Pattern.matches(regexIsbn13, isbn)) {
			values = isbn.split("-");
		} else if (Pattern.matches(regexIsbn10, isbn)) {
			values = new String[4];
			values[0] = isbn.substring(0, 1); //Linguistic group - 1 digit
			values[1] = isbn.substring(1, 7); //Author id - 6 digits
			values[2] = isbn.substring(7, 9); //Book id - 2 digits
			values[3] = isbn.substring(9, 10); //CheckSum - 1 digit
		} else {
			throw new IllegalIsbnFormatException(String.format("ISBN '%s' is not valid because of its format.", isbn));
		}
		this.linguisticGroup = Integer.parseInt(values[0]);
		this.idAuthor = Integer.parseInt(values[1]);
		this.idBook = Integer.parseInt(values[2]);
		int check = (values[3].equals("X")) ? 10 : Integer.parseInt(values[3]);
		if (check != getCheckSum()) {
			throw new IllegalIsbnChecksumException(String.format("The checkSum must be between 0 and 10 and must be correct (was %d instead of %d)", check, getCheckSum()), check, getCheckSum());
		}
		this.checkSum = check;
	}

	public static ISBN createNewISBN(int linguisticGroup, int idAuthor) {
		return new ISBN(linguisticGroup, idAuthor, getRandomIdBook());
	}

	private static int getRandomIdBook() {
		return (int) (Math.random() * 100);
	}

	public static int computeCheckSum(int linguisticGroup, int idAuthor, int idBook) {
		return (linguisticGroup + idAuthor + idBook) % 11;
	}

	public int getCheckSum() {
		return computeCheckSum(linguisticGroup, idAuthor, idBook);
	}

	public int getLinguisticGroup() {
		return linguisticGroup;
	}

	public int getIdAuthor() {
		return idAuthor;
	}

	public int getIdBook() {
		return idBook;
	}

	@Override
	public String toString() {
		return String.format("%d-%06d-%02d-%s", linguisticGroup, idAuthor, idBook, checkSum == 10 ? "X" : checkSum);
	}
}