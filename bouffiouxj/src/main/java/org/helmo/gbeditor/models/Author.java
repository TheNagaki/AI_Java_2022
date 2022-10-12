package org.helmo.gbeditor.models;

import java.util.Objects;

public class Author {
	private final String name;
	private final String firstName;

	public Author(String name, String firstName) {
		this.name = name;
		this.firstName = firstName;
	}

	public String getFullName() {
		return firstName + " " + name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Author author = (Author) o;
		return name.equals(author.name) && firstName.equals(author.firstName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, firstName);
	}
}
