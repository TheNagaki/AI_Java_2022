package org.helmo.gbeditor.presenters;

import org.helmo.gbeditor.models.Author;
import org.helmo.gbeditor.models.Book;
import org.helmo.gbeditor.models.Page;
import org.helmo.gbeditor.presenters.interfaces.BookDetailsViewInterface;
import org.helmo.gbeditor.presenters.interfaces.MainViewInterface;
import org.helmo.gbeditor.presenters.viewmodels.BookViewModel;
import org.helmo.gbeditor.presenters.viewmodels.PageViewModel;
import org.helmo.gbeditor.repositories.RepositoryInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BookDetailsPresenterTest {

	private BookDetailsViewInterface view;
	private BookDetailsPresenter presenter;
	private Book book;
	private MainPresenter mainPresenter;
	private RepositoryInterface repo;

	@BeforeEach
	void setUp() {
		repo = mock(RepositoryInterface.class);
		view = mock(BookDetailsViewInterface.class);
		mainPresenter = mock(MainPresenter.class);
		presenter = new BookDetailsPresenter(repo);
		presenter.setView(view);
		presenter.setMainPresenter(mainPresenter);
		book = new Book("title", new Author("name", "firstName"), "summary");
	}

	@Test
	void getView() {
		assertEquals(view, presenter.getView());
	}

	@Test
	void setView() {
		BookDetailsViewInterface view2 = mock(BookDetailsViewInterface.class);
		presenter.setView(view2);
		assertEquals(view2, presenter.getView());
	}

	@Test
	void displayBookSetsAllBookDetailsInTheView() {
		presenter.displayBook(book);
		verify(view).setBookToDisplay(new BookViewModel(book));
		verify(view, atMostOnce()).displayBook();
	}

	@Test
	void displayBookSetsAllBookDetailsInTheViewWithNullBookOrDoesNotSetThem() {
		presenter.displayBook(null);
		verify(view, never()).setBookToDisplay(any());
		verify(view, never()).displayBook();
	}

	@Test
	void closeViewClosesView() {
		presenter.closeView();
		verify(view, atMostOnce()).close();
	}

	@Test
	void closeViewClosesViewAndCallsMainPresenter() {
		presenter.closeView();
		verify(view, times(1)).close();
		verify(mainPresenter, times(1)).bookDetailsClosed();
	}

	@Test
	void deleteBookWorksWithNormalBook() {
		presenter.displayBook(book);
		presenter.deleteBook();
		verify(repo, times(1)).deleteBook(book);
		verify(view, times(1)).close();
	}

	@Test
	void deleteBookDoesNotWorkWithNullBook() {
		presenter.displayBook(null);
		presenter.deleteBook();
		verify(repo, never()).deleteBook(book);
		verify(view, never()).close();
	}

	@Test
	void editBook() {
		presenter.displayBook(book);
		presenter.editBook();
		verify(mainPresenter, times(1)).setBookToEdit(book);
	}

	@Test
	void editBookWithNullBook() {
		presenter.displayBook(null);
		presenter.editBook();
		verify(mainPresenter, never()).setBookToEdit(book);
	}

	@Test
	void addPage() {
		presenter.displayBook(book);
		presenter.addPage("cyril");
		verify(repo, times(1)).updatesAddBook(book);
		assertEquals(1, book.getPages().size());
	}

	@Test
	void removePage() {
		presenter.displayBook(book);
		presenter.addPage("tests");
		presenter.removePage(new PageViewModel(book.getPages().iterator().next()));
		verify(repo, times(2)).updatesAddBook(book);
		assertEquals(0, book.getPages().size());
	}

	@Test
	void removePageWithNullPageDoesNotUpdateTheBook() {
		presenter.displayBook(book);
		presenter.addPage("test");
		presenter.removePage(null);
		verify(repo, times(1)).updatesAddBook(book);
		assertEquals(1, book.getPages().size());
	}

	@Test
	void editPage() {
		presenter.displayBook(book);
		presenter.addPage("test");
		var pVM = new PageViewModel(book.getPages().iterator().next());
		presenter.editPage(pVM);
		verify(view, times(1)).editPage(pVM);
	}

	@Test
	void editNullPageDoesNotEditPage() {
		presenter.displayBook(book);
		presenter.addPage("test");
		presenter.editPage(null);
		verify(view, never()).editPage(any());
	}

	@Test
	void updatePage() {
		presenter.displayBook(book);
		presenter.addPage("test");
		var pVM = new PageViewModel(book.getPages().iterator().next());
		pVM.setContent("test2");
		presenter.updatePage(pVM);
		verify(repo, times(2)).updatesAddBook(book);
		assertEquals(pVM.toPage(), book.getPages().iterator().next());
	}

	@Test
	void updateNullPageDoesNotUpdatePage() {
		presenter.displayBook(book);
		presenter.addPage("test");
		presenter.updatePage(null);
		verify(repo, times(1)).updatesAddBook(book);
		assertEquals("test", book.getPages().iterator().next().getContent());
	}

	@Test
	void getPageNumber() {
		presenter.displayBook(book);
		presenter.addPage("test");
		presenter.addPage("test2");
		presenter.addPage("test3");
		var ite = book.getPages().iterator();
		assertEquals(1, presenter.getPageNumber(new PageViewModel(ite.next())));
		assertEquals(2, presenter.getPageNumber(new PageViewModel(ite.next())));
		assertEquals(3, presenter.getPageNumber(new PageViewModel(ite.next())));
	}

	@Test
	void getPageNumberWithNullPage() {
		presenter.displayBook(book);
		presenter.addPage("test");
		presenter.addPage("test2");
		presenter.addPage("test3");
		assertEquals(-1, presenter.getPageNumber(null));
	}

	@Test
	void setBookToDisplayIsUsedWhenDisplayingBook() {
		presenter.displayBook(book);
		verify(view, times(1)).setBookToDisplay(new BookViewModel(book));
	}

	@Test
	void setBookToDisplayIsNotUsedWhenDisplayingNullBook() {
		presenter.displayBook(null);
		verify(view, never()).setBookToDisplay(any());
	}

	@Test
	void confirmPageDeletion() {
		presenter.displayBook(book);
		presenter.addPage("test");
		var pVM = new PageViewModel(book.getPages().iterator().next());
		presenter.confirmPageDeletion(pVM);
		verify(repo, times(2)).updatesAddBook(book);
	}

	@Test
	void confirmPageDeletionWithNullPageDoesNotDeletePage() {
		presenter.displayBook(book);
		presenter.addPage("test");
		presenter.confirmPageDeletion(null);
		verify(repo, times(1)).updatesAddBook(book);
		verify(view, never()).refresh();
	}

	@Test
	void askPagesIsCalledOnDisplayBookAndEachTimeANewPageIsAdded() {
		presenter.displayBook(book);
		presenter.addPage("test");
		presenter.addPage("test2");
		presenter.addPage("test3");
		presenter.askBookToView();
		verify(view, times(5)).setBookToDisplay(any());
	}

	@Test
	void askPagesWithNullBookDoesNotUpdateView() {
		presenter.displayBook(null);
		presenter.askBookToView();
		verify(view, never()).setBookToDisplay(any());
	}

	@Test
	void getPageById() {
		presenter.displayBook(book);
		Page page = new Page("test");
		book.addPage(page);
		var ite = book.getPages().iterator();
		assertEquals(ite.next(), presenter.getPageById(page.getId()).toPage());
	}

	@Test
	void getPageByIdWithNullIdReturnsNull() {
		presenter.displayBook(book);
		assertThrows(IllegalArgumentException.class, () -> presenter.getPageById(null));
	}

	@Test
	void setBaseView() {
		presenter.setBaseView(mock(MainViewInterface.class));
		verify(view, times(1)).setBaseView(any());
	}

	@Test
	void getBaseView() {
		var baseView = mock(MainViewInterface.class);
		presenter.setBaseView(baseView);
		assertEquals(baseView, presenter.getBaseView());
	}
}