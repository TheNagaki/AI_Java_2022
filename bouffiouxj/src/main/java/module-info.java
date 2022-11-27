module org.helmo {
	requires javafx.controls;
	requires java.desktop;
	requires javafx.graphics;
	requires com.google.gson;
	opens org.helmo.gbeditor.repositories to com.google.gson;
	opens org.helmo.gbeditor.repositories.exceptions to com.google.gson;
	opens org.helmo.gbeditor.models to com.google.gson, javafx.base;
	exports org.helmo.gbeditor;
	opens org.helmo.gbeditor.presenters to javafx.base;
	opens org.helmo.gbeditor.presenters.viewmodels to javafx.base;
}