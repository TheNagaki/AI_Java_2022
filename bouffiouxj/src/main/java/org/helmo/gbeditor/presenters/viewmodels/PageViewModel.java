package org.helmo.gbeditor.presenters.viewmodels;

import org.helmo.gbeditor.models.Page;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PageViewModel {
	private String content;
	private final String id;
	private final Map<String, PageViewModel> choices;
	private final Page originalPage;

	public PageViewModel(Page page) {
		originalPage = page;
		this.content = page.getContent();
		this.id = page.getId();
		this.choices = new HashMap<>();
		for (String choice : page.getChoices().keySet()) {
			this.choices.put(choice, new PageViewModel(page.getChoices().get(choice)));
		}
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getId() {
		return id;
	}

	public Map<String, PageViewModel> getChoices() {
		return choices;
	}

	public Page toPage() {
		Page page = new Page(content);
		page.setId(id);
		for (String choice : choices.keySet()) {
			page.addChoice(choice, choices.get(choice).toPage());
		}
		return page;
	}

	public void removeChoice(String choice) {
		choices.remove(choice);
	}

	public void addChoice(String choice, PageViewModel page) {
		choices.put(choice, page);
	}

	public boolean hasBeenModified() {
		if (!Objects.equals(content, originalPage.getContent())) {
			return true;
		}
		if (originalPage.getChoices().size() != choices.size()) {
			return true;
		}
		for (String choice : choices.keySet()) {
			if (!originalPage.getChoices().containsKey(choice)) {
				return true;
			}
			if (!choices.get(choice).toPage().equals(originalPage.getChoices().get(choice))) {
				return true;
			}
		}
		return originalPage.getContent() != null ? !originalPage.getContent().equals(content) : content != null;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		PageViewModel that = (PageViewModel) o;
		return id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}
