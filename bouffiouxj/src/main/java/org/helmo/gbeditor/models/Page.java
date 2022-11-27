package org.helmo.gbeditor.models;

import org.helmo.gbeditor.models.exceptions.IllegalChoiceException;
import org.helmo.gbeditor.models.exceptions.IllegalPageException;

import java.util.*;

/**
 * This class is used to represent a page of a book
 */
public class Page {

	private UUID id = UUID.randomUUID();
	private String content;
	private final Map<String, Page> choices = new HashMap<>();

	/**
	 * This constructor is used to create a new Page with the given content
	 *
	 * @param content the content of the page
	 */
	public Page(String content) {
		if (content == null || content.isBlank()) {
			throw new IllegalPageException();
		}
		this.content = content;
	}

	/**
	 * This method is used to get the content of the page
	 *
	 * @return the content of the page
	 */
	public String getContent() {
		return content + "";
	}

	/**
	 * This method is used to get the choices of the page
	 *
	 * @return the choices of the page
	 */
	public Map<String, Page> getChoices() {
		return new HashMap<>(choices);
	}

	/**
	 * This method is used to add a choice to the page
	 *
	 * @param choice the choice to add
	 * @param page   the page to go to
	 */
	public void addChoice(String choice, Page page) {
		checkChoice(choice);
		checkPage(page);
		if (choices.containsKey(choice)) {
			throw new IllegalChoiceException();
		}
		choices.put(choice, page);
	}

	private void checkPage(Page page) {
		if (page == null || page.equals(this)) {
			throw new IllegalPageException();
		}
	}

	private void checkChoice(String choice) {
		if (choice == null || choice.isBlank()) {
			throw new IllegalChoiceException();
		}
	}

	/**
	 * This method is used to remove a choice from the page
	 *
	 * @param choice the choice to remove
	 */
	public void removeChoice(String choice) {
		if (choice == null || choice.isBlank() || !choices.containsKey(choice)) {
			throw new IllegalChoiceException();
		}
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
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Page page = (Page) o;
		return id.equals(page.id);
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
			sb.add(String.format("%s -> %s", choice, choices.get(choice)));
		}
		return String.format("Page{content='%s', id={%s}, choices={%s}}", content, id, sb);
	}

	/**
	 * This method sets the content of the page
	 *
	 * @param newContent the new content (can not be null or blank)
	 */
	public void setContent(String newContent) {
		if (newContent == null || newContent.isBlank()) {
			throw new IllegalPageException();
		}
		content = newContent;
	}

	/**
	 * This method returns the UUID of the page as a String
	 */
	public String getId() {
		return id.toString();
	}

	/**
	 * This method is used to update the choice of the page based on the choice string
	 *
	 * @param k    the choice string
	 * @param page the new page to go to
	 */
	public void updateChoice(String k, Page page) {
		checkChoice(k);
		checkPage(page);
		if (!choices.containsKey(k)) {
			throw new IllegalChoiceException();
		}
		choices.put(k, page);
	}

	public void setId(String id) {
		this.id = UUID.fromString(id);
	}
}