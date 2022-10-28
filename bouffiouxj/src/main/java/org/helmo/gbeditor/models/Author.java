package org.helmo.gbeditor.models;

import java.util.Objects;

public class Author {
	private final String name;
	private final String firstName;
	private final int matricule;

	public Author(String name, String firstName) {
		if (name == null || firstName == null) {
			throw new IllegalArgumentException("Name and firstName must not be null");
		}
		if (name.isEmpty() || firstName.isEmpty()) {
			throw new IllegalArgumentException("Name and firstName must not be empty");
		}
		this.name = name;
		this.firstName = firstName;
		this.matricule = computeMatricule();
	}

	private int computeMatricule() {
		return (int) (Math.random() * 1000000);
	}

	public Author(String name, String firstName, int matricule) {
		if (name.isBlank()) {
			throw new IllegalArgumentException("Name must not be null or empty");
		}
		this.name = name;
		if (firstName.isBlank()) {
			throw new IllegalArgumentException("First name must not be null or empty");
		}
		this.firstName = firstName;
		if ((String.format("%d", matricule)).length() != 6) {
			throw new IllegalArgumentException("Matricule must be 6 digits");
		}
		this.matricule = matricule;
	}

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
