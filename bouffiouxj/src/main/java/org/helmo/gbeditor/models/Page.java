package org.helmo.gbeditor.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * This class is used to represent a page of a book
 */
public class Page {
	private String content;
	private final Map<String, Page> choices = new HashMap<>();

	/**
	 * This constructor is used to create a new Page with the given content
	 *
	 * @param content the content of the page
	 */
	public Page(String content) {
		this.content = content;
	}

	/**
	 * This method is used to get the content of the page
	 *
	 * @return the content of the page
	 */
	public String getContent() {
		return content;
	}

	/**
	 * This method is used to set the content of the page
	 *
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * This method is used to get the choices of the page
	 *
	 * @return the choices of the page
	 */
	public Map<String, Page> getChoices() {
		return choices;
	}

	/**
	 * This method is used to add a choice to the page
	 *
	 * @param choice the choice to add
	 * @param page   the page to go to
	 */
	public void addChoice(String choice, Page page) {
		choices.put(choice, page);
	}

	/**
	 * This method is used to remove a choice from the page
	 *
	 * @param choice the choice to remove
	 */
	public void removeChoice(String choice) {
		choices.remove(choice);
	}

	/**
	 * This method is used to compare two pages (based on their content)
	 *
	 * @param o the page to compare
	 * @return true if the pages are the same, false otherwise
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Page page = (Page) o;
		return content.equals(page.content);
	}

	/**
	 * This method is used to get the hashcode of the page
	 *
	 * @return the hashcode of the page
	 */
	@Override
	public int hashCode() {
		return Objects.hash(content);
	}

	/**
	 * This method is used to get a human-readable representation of the page (as a string)
	 *
	 * @return a string representation of the page
	 */
	@Override
	public String toString() {
		StringJoiner sb = new StringJoiner(",\n");
		for (String choice : choices.keySet()) {
			sb.add(String.format("%s -> %s", choice, choices.get(choice).getContent()));
		}
		return String.format("Page{content='%s', choices={%s}}", content, sb);
	}
}
