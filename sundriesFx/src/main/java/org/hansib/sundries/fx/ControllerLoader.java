/*-
 * MIT License
 *
 * SundriesFx - https://github.com/hansi-b/JavaSundriesFx
 *
 * Copyright (c) 2022-2023 Hansi B.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.hansib.sundries.fx;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.hansib.sundries.Errors;
import org.hansib.sundries.ResourceLoader;

/**
 * Builder wrapper around loading controllers.
 */
public class ControllerLoader<C> {

	private final String fxmlName;
	private final String fxmlResourcePattern;
	private final ResourceLoader resourceLoader;

	private Stage stage;
	private Supplier<C> controllerFactory;

	ControllerLoader(String fxmlSourceStringFormat, ResourceLoader resourceLoader, String fxmlName) {
		this.fxmlName = fxmlName;

		if (!fxmlSourceStringFormat.contains("%s"))
			throw Errors.illegalArg("Argument fxmlSourceStringFormat '%s' does not contain String placeholder %%s",
				fxmlSourceStringFormat);
		this.fxmlResourcePattern = fxmlSourceStringFormat;
		this.resourceLoader = Objects.requireNonNull(resourceLoader);
	}

	/**
	 * Instantiates a new loader with the default fxmlSourceStringFormat of "fxml/%s"
	 */
	public ControllerLoader(String fxmlName) {
		this("fxml/%s", fxmlName);
	}

	/**
	 * @param fxmlSourceStringFormat a String format pattern used to resolve argument fxml Names passed to this
	 *                               {@link ControllerLoader}; must have one %s String placeholder
	 */
	public ControllerLoader(String fxmlSourceStringFormat, String fxmlName) {
		this(fxmlSourceStringFormat, new ResourceLoader(), fxmlName);
	}

	public static <T> ControllerLoader<T> of(String fxmlName) {
		return new ControllerLoader<>(fxmlName);
	}

	public ControllerLoader<C> withTargetStage(Stage stage) {
		this.stage = stage;
		return this;
	}

	public ControllerLoader<C> withControllerFactory(Supplier<C> controllerFactory) {
		this.controllerFactory = controllerFactory;
		return this;
	}

	/**
	 * Loads the argument FXML file and returns the controller.
	 *
	 * @throws IllegalStateException thrown as a wrapper if an IOException is thrown on loading the fxml content
	 */
	public C load() {
		return loadInternal();
	}

	private C loadInternal() {
		FXMLLoader fxmlLoader = new FXMLLoader(
			resourceLoader.getResourceUrl(String.format(fxmlResourcePattern, fxmlName)));
		if (controllerFactory != null)
			fxmlLoader.setControllerFactory(c -> controllerFactory.get());
		Parent load;
		try {
			load = fxmlLoader.load();
		} catch (IOException e) {
			throw Errors.illegalState(e, "Encountered exception loading '%s'", fxmlName);
		}
		if (stage != null) {
			Consumer<Parent> loadConsumer = (Parent p) -> stage.setScene(new Scene(p));
			loadConsumer.accept(load);
		}
		return fxmlLoader.getController();
	}
}