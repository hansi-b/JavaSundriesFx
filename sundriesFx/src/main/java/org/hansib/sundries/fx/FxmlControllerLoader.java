package org.hansib.sundries.fx;

import java.io.IOException;
import java.util.function.Consumer;

import org.hansib.sundries.Errors;
import org.hansib.sundries.ResourceLoader;

import javafx.fxml.FXMLLoader;

public class FxmlControllerLoader {

	private final ResourceLoader resourceLoader;

	FxmlControllerLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	public FxmlControllerLoader() {
		this(new ResourceLoader());
	}

	public <T> T getController(String fxmlName) {
		return getFxmlLoader(fxmlName).getController();
	}

	/**
	 * @throws IllegalStateException thrown as a wrapper if an IOException is thrown
	 *                               on loading the fxml content
	 */
	public <C, P> C loadAndGetController(String fxmlName) {
		FXMLLoader fxmlLoader = getFxmlLoader(fxmlName);
		try {
			fxmlLoader.load();
		} catch (IOException e) {
			throw Errors.illegalState(e, "Encountered exception loading '%s'", fxmlName);
		}
		return fxmlLoader.getController();
	}

	/**
	 * @throws IllegalStateException thrown as a wrapper if an IOException is thrown
	 *                               on loading the fxml content
	 */
	public <C, P> C loadAndGetController(String fxmlName, Consumer<P> loadConsumer) {
		FXMLLoader fxmlLoader = getFxmlLoader(fxmlName);
		P load;
		try {
			load = fxmlLoader.load();
		} catch (IOException e) {
			throw Errors.illegalState(e, "Encountered exception loading '%s'", fxmlName);
		}
		loadConsumer.accept(load);
		return fxmlLoader.getController();
	}

	private FXMLLoader getFxmlLoader(String fxmlName) {
		return new FXMLLoader(resourceLoader.getResourceUrl("fxml/" + fxmlName));
	}
}