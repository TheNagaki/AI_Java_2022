package org.helmo.gbeditor.presenters.viewmodels;

import org.helmo.gbeditor.models.Author;

public class AuthorViewModel {
	private String name;
	private String firstName;
	private final int identifier;

	public AuthorViewModel(Author author) {
		this.name = author.getName();
		this.firstName = author.getFirstName();
		this.identifier = author.getIdentifier();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public Author toAuthor() {
		return new Author(name, firstName, identifier);
	}
}
