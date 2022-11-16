package org.helmo.gbeditor.models;

import java.util.Objects;

/**
 * Author class which represents an author in the GBEditor
 */
public class Author {
	private final String name;
	private final String firstName;
	private final int matricule;

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
		this.matricule = computeMatricule();
	}

	private static void checkValidity(String name, String firstName) {
		if (name == null || firstName == null || name.isBlank() || firstName.isBlank()) {
			throw new IllegalAuthorNameException();
		}
	}

	private int computeMatricule() {
		return (int) (Math.random() * 1000000);
	}

	/**
	 * Constructor of the Author class with a matricule
	 *
	 * @param name      the name of the author
	 * @param firstName the first name of the author
	 * @param matricule the matricule of the author
	 */
	public Author(String name, String firstName, int matricule) {
		checkValidity(name, firstName);
		this.name = name;
		this.firstName = firstName;
		if ((String.format("%d", matricule)).length() != MATRICULE_LENGTH) {
			throw new IllegalArgumentException("Matricule must be 6 digits");
		}
		this.matricule = matricule;
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

	public int getMatricule() {
		return matricule;
	}

	public String getFirstName() {
		return firstName;
	}
}
