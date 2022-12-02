package org.helmo.gbeditor.presenters;

import org.helmo.gbeditor.models.Author;
import org.helmo.gbeditor.presenters.interfaces.ConnexionViewInterface;
import org.helmo.gbeditor.repositories.RepositoryInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ConnexionPresenterTest {

	private RepositoryInterface repo;
	private ConnexionPresenter presenter;
	private ConnexionViewInterface view;

	@BeforeEach
	void setUp() {
		this.repo = mock(RepositoryInterface.class);
		this.view = mock(ConnexionViewInterface.class);
		this.presenter = new ConnexionPresenter(repo);
		presenter.setView(view);
	}

	@Test
	void connectWorksInNormalContext() {
		presenter.connect("name", "firstName");
		verify(repo, atMostOnce()).getAuthors();
		verify(repo, atMostOnce()).addAuthor(any(Author.class));
		verify(view, atMostOnce()).changeView(ViewsEnum.MAIN);
	}

	@Test
	void connectWorksInNormalContextWithExistingAuthor() {
		when(repo.getAuthors()).thenReturn(new LinkedHashSet<>(List.of(new Author("name", "firstName"))));
		presenter.connect("name", "firstName");
		verify(repo, atMostOnce()).getAuthors();
		verify(view, atMostOnce()).changeView(ViewsEnum.MAIN);
	}

	@Test
	void connectWorksInNormalContextWithExistingAuthorWithDifferentCase() {
		when(repo.getAuthors()).thenReturn(new LinkedHashSet<>(List.of(new Author("name", "firstName"))));
		presenter.connect("NaMe", "FiRsTnAmE");
		verify(repo, atMostOnce()).getAuthors();
		verify(view, atMostOnce()).changeView(ViewsEnum.MAIN);
	}

	@Test
	void connectDoesNotWorkWithNullName() {
		presenter.connect(null, "firstName");
		verify(repo, never()).getAuthors();
		verify(repo, never()).addAuthor(any(Author.class));
		verify(view, never()).changeView(ViewsEnum.MAIN);
		verify(view, atMostOnce()).display("Erreur de connexion");
	}

	@Test
	void connectDoesNotWorkWithNullFirstName() {
		presenter.connect("name", null);
		verify(repo, never()).getAuthors();
		verify(repo, never()).addAuthor(any(Author.class));
		verify(view, never()).changeView(ViewsEnum.MAIN);
		verify(view, atMostOnce()).display("Erreur de connexion");
	}

	@Test
	void connectDoesNotWorkWithEmptyName() {
		presenter.connect("", "firstName");
		verify(repo, never()).getAuthors();
		verify(repo, never()).addAuthor(any(Author.class));
		verify(view, never()).changeView(ViewsEnum.MAIN);
		verify(view, atMostOnce()).display("Erreur de connexion");
	}

	@Test
	void connectDoesNotWorkWithEmptyFirstName() {
		presenter.connect("name", "");
		verify(repo, never()).getAuthors();
		verify(repo, never()).addAuthor(any(Author.class));
		verify(view, never()).changeView(ViewsEnum.MAIN);
		verify(view, atMostOnce()).display("Erreur de connexion");
	}

	@Test
	void connectDoesNotWorkWithBlankName() {
		presenter.connect(" ", "firstName");
		verify(repo, never()).getAuthors();
		verify(repo, never()).addAuthor(any(Author.class));
		verify(view, never()).changeView(ViewsEnum.MAIN);
		verify(view, atMostOnce()).display("Erreur de connexion");
	}

	@Test
	void connectDoesNotWorkWithBlankFirstName() {
		presenter.connect("name", " ");
		verify(repo, never()).getAuthors();
		verify(repo, never()).addAuthor(any(Author.class));
		verify(view, never()).changeView(ViewsEnum.MAIN);
		verify(view, atMostOnce()).display("Erreur de connexion");
	}

	@Test
	void setViewWorks() {
		presenter.setView(view);
		assertEquals(view, presenter.getView());
	}

	@Test
	void getViewWorks() {
		assertEquals(view, presenter.getView());
	}

	@Test
	void onQuit_Click() {
		presenter.OnQuit_Click();
		verify(view, atMostOnce()).close();
	}
}