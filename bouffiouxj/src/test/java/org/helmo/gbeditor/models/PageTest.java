package org.helmo.gbeditor.models;

import org.helmo.gbeditor.models.exceptions.IllegalChoiceException;
import org.helmo.gbeditor.models.exceptions.IllegalPageException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PageTest {
	private String content;
	private Page page;

	@BeforeEach
	void setUp() {
		content = "test";
		page = new Page(content);
	}

	@Test
	void getContent() {
		assertEquals(content, page.getContent());
		assertNotSame(content, page.getContent());
	}

	@Test
	void getNullContentThrowsException() {
		assertThrows(IllegalPageException.class, () -> new Page(null));
	}

	@Test
	void getEmptyContentThrowsException() {
		assertThrows(IllegalPageException.class, () -> new Page(""));
	}

	@Test
	void getChoicesReturnsEmptyCollectionWhenNoChoicesInserted() {
		assertTrue(page.getChoices().isEmpty());
	}

	@Test
	void getChoicesReturnsACopyOfTheChoices() {
		Page page2 = new Page("test2");
		page.addChoice("choice", page2);
		assertNotSame(page.getChoices(), page.getChoices());
	}

	@Test
	void addChoice() {
		Page page2 = new Page("test2");
		page.addChoice("choice", page2);
		assertEquals(page2, page.getChoices().get("choice"));
	}

	@Test
	void addChoiceWithNullChoiceThrowsException() {
		Page page2 = new Page("test2");
		assertThrows(IllegalArgumentException.class, () -> page.addChoice(null, page2));
	}

	@Test
	void addChoiceWithEmptyChoiceThrowsException() {
		Page page2 = new Page("test2");
		assertThrows(IllegalArgumentException.class, () -> page.addChoice("", page2));
	}

	@Test
	void addChoicesWithNullPageThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> page.addChoice("choice", null));
	}

	@Test
	void addChoiceWithExistingChoiceThrowsException() {
		Page page2 = new Page("test2");
		page.addChoice("choice", page2);
		assertThrows(IllegalArgumentException.class, () -> page.addChoice("choice", page2));
	}

	@Test
	void addChoiceWithSamePageThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> page.addChoice("choice", page));
	}

	@Test
	void addChoiceWithNullChoiceAndNullPageThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> page.addChoice(null, null));
	}

	@Test
	void addChoiceWithEmptyChoiceAndNullPageThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> page.addChoice("", null));
	}

	@Test
	void addChoiceWithNullChoiceAndEmptyPageThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> page.addChoice(null, new Page("")));
	}

	@Test
	void addChoiceWithEmptyChoiceAndEmptyPageThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> page.addChoice("", new Page("")));
	}

	@Test
	void addTwoChoicesToSamePageWorks() {
		Page page2 = new Page("test2");
		page.addChoice("choice", page2);
		page.addChoice("choice2", page2);
		assertEquals(page2, page.getChoices().get("choice"));
		assertEquals(page2, page.getChoices().get("choice2"));
		assertEquals(2, page.getChoices().size());
	}

	@Test
	void removeChoice() {
		Page page2 = new Page("test2");
		var key = "choice";
		page.addChoice(key, page2);
		assertTrue(page.getChoices().containsKey(key));
		page.removeChoice(key);
		assertTrue(page.getChoices().isEmpty());
	}

	@Test
	void removeChoiceWithNullKeyThrowsException() {
		assertThrows(IllegalChoiceException.class, () -> page.removeChoice(null));
	}

	@Test
	void removeChoiceWithEmptyKeyThrowsException() {
		assertThrows(IllegalChoiceException.class, () -> page.removeChoice(""));
	}

	@Test
	void removeChoiceWithNonExistingKeyThrowsException() {
		assertThrows(IllegalChoiceException.class, () -> page.removeChoice("choice"));
	}

	@Test
	void setContent() {
		String newContent = "newContent";
		page.setContent(newContent);
		assertEquals(newContent, page.getContent());
	}

	@Test
	void setContentWithNullContentThrowsException() {
		assertThrows(IllegalPageException.class, () -> page.setContent(null));
	}

	@Test
	void setContentWithEmptyContentThrowsException() {
		assertThrows(IllegalPageException.class, () -> page.setContent(""));
	}

	@Test
	void setContentWithSameContentWorks() {
		page.setContent(content);
		assertEquals(content, page.getContent());
	}

	@Test
	void getId() {
		assertNotNull(page.getId());
	}

	@Test
	void twoPagesAreNotEquals() {
		Page page2 = new Page("test2");
		assertNotEquals(page, page2);
	}

	@Test
	void equalsWithSamePageWorks() {
		assertEquals(page, page);
	}

	@Test
	void equalsWithSameContentDoesNotWork() {
		Page page2 = new Page(content);
		assertNotEquals(page, page2);
	}

	@Test
	void equalsWithSameContentAndChoicesDoesNotWork() {
		Page page2 = new Page(content);
		Page page3 = new Page(content);
		page.addChoice("choice", page2);
		page3.addChoice("choice", page2);
		assertNotEquals(page, page3);
	}

	@Test
	void equalsWorksWithIdAndAnyContent() {
		Page page2 = new Page("test2");
		page2.setId(page.getId());
		assertEquals(page, page2);
	}

	@Test
	void updateChoice() {
		Page page2 = new Page("test2");
		Page page3 = new Page("test3");
		page.addChoice("choice", page2);
		page.updateChoice("choice", page3);
		assertEquals(page3, page.getChoices().get("choice"));
	}

	@Test
	void updateChoiceWithNullKeyThrowsException() {
		Page page2 = new Page("test2");
		assertThrows(IllegalChoiceException.class, () -> page.updateChoice(null, page2));
	}

	@Test
	void updateChoiceWithEmptyKeyThrowsException() {
		Page page2 = new Page("test2");
		assertThrows(IllegalChoiceException.class, () -> page.updateChoice("", page2));
	}

	@Test
	void updateChoiceWithNonExistingKeyThrowsException() {
		Page page2 = new Page("test2");
		assertThrows(IllegalChoiceException.class, () -> page.updateChoice("choice", page2));
	}

	@Test
	void updateChoiceWithNullPageThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> page.updateChoice("choice", null));
	}

	@Test
	void updateChoiceWithEmptyPageThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> page.updateChoice("choice", new Page("")));
	}

	@Test
	void updateChoiceWithSamePageThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> page.updateChoice("choice", page));
	}

	@Test
	void updateChoiceWithSamePageAndSameKeyWorks() {
		Page page2 = new Page("test2");
		page.addChoice("choice", page2);
		page.updateChoice("choice", page2);
		assertEquals(page2, page.getChoices().get("choice"));
	}
}