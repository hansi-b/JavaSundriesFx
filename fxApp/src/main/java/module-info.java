module org.hansib.javaFxApp {

	exports javafx_app.app;

	requires transitive javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;

	requires org.apache.logging.log4j;

	// required for Spock tests
	// see https://github.com/spockframework/spock/issues/1227
	requires org.apache.groovy;
}