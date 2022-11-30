package org.helmo.gbeditor.models;

import org.helmo.gbeditor.models.exceptions.IllegalAuthorNameException;

import java.util.Objects;

/**
 * Author class which represents an author in the GBEditor
 */
public class Author {
	private final String name;
	private final String firstName;
	private final int identifier;
	private static final int MATRICULE_LENGTH = 6;

	/**
	 * Constructor of the Author class
	 *
	 * @param name      the name of the author
	 * @param firstName the first name of the author
	 */
	public Author(String name, String firstName) {
		checkValidity(name, firstName);
		this.name = name;
		this.firstName = firstName;
		this.identifier = computeIdentifier();
	}

	private static void checkValidity(String name, String firstName) {
		if (name == null || firstName == null || name.isBlank() || firstName.isBlank()) {
			throw new IllegalAuthorNameException();
		}
	}

	private int computeIdentifier() {
		return (int) (Math.random() * 1000000);
	}

	/**
	 * Constructor of the Author class with a matricule
	 *
	 * @param name      the name of the author
	 * @param firstName the first name of the author
	 * @param identifier the matricule of the author
	 */
	public Author(String name, String firstName, int identifier) {
		checkValidity(name, firstName);
		this.name = name;
		this.firstName = firstName;
		if ((String.format("%06d", identifier)).length() != MATRICULE_LENGTH || identifier < 0) {
			throw new IllegalArgumentException("Matricule must be 6 digits");
		}
		this.identifier = identifier;
	}

	/**
	 * Getter for the name of the author
	 *
	 * @return the full name of the author
	 */
	public String getFullName() {
		return firstName + " " + name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Author author = (Author) o;
		return name.equals(author.name) && firstName.equals(author.firstName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, firstName);
	}

	public String getName() {
		return name;
	}

	public int getIdentifier() {
		return identifier;
	}

	public String getFirstName() {
		return firstName;
	}

	@Override
	public String toString() {
		return "Author{" +
				"name='" + name + '\'' +
				", firstName='" + firstName + '\'' +
				", matricule=" + identifier +
				'}';
	}
}
