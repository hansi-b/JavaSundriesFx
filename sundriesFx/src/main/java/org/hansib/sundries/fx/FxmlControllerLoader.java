/**
 * MIT License
 *
 * SundriesFx - https://github.com/hansi-b/JavaSundriesFx
 *
 * Copyright (c) 2022 Hansi B.
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

import org.hansib.sundries.Errors;
import org.hansib.sundries.ResourceLoader;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FxmlControllerLoader {

	private final String fxmlResourcePattern;
	private final ResourceLoader resourceLoader;

	FxmlControllerLoader(String fxmlSourceStringFormat, ResourceLoader resourceLoader) {
		Objects.requireNonNull(resourceLoader);
		if (!fxmlSourceStringFormat.contains("%s"))
			throw Errors.illegalArg("Argument fxmlSourceStringFormat '%s' does not contain String placeholder %%s",
					fxmlSourceStringFormat);

		this.fxmlResourcePattern = fxmlSourceStringFormat;
		this.resourceLoader = resourceLoader;
	}

	/**
	 * Instantiates a new loader with the default fxmlSourceStringFormat of
	 * "fxml/%s"
	 */
	public FxmlControllerLoader() {
		this("fxml/%s");
	}

	/**
	 * @param fxmlSourceStringFormat a String format pattern used to resolve
	 *                               argument fxml Names passed to this
	 *                               {@link FxmlControllerLoader}
	 */
	public FxmlControllerLoader(String fxmlSourceStringFormat) {
		this(fxmlSourceStringFormat, new ResourceLoader());
	}

	public <C> C loadToStage(String fxmlName, Stage stage) {
		return loadAndGetController(fxmlName, (Parent p) -> stage.setScene(new Scene(p)));
	}

	/**
	 * @throws IllegalStateException thrown as a wrapper if an IOException is thrown
	 *                               on loading the fxml content
	 */
	public <C> C loadAndGetController(String fxmlName) {
		return loadAndGetControllerInternal(fxmlName, null);
	}

	/**
	 * @param loadConsumer the consumer for the result of type P of loading the FXML
	 * @throws IllegalStateException thrown as a wrapper if an IOException is thrown
	 *                               on loading the fxml content
	 * @throws NullPointerException  on a null loadConsumer
	 * 
	 */
	public <C, P> C loadAndGetController(String fxmlName, Consumer<P> loadConsumer) {
		Objects.requireNonNull(loadConsumer);
		return loadAndGetControllerInternal(fxmlName, loadConsumer);
	}

	private <C, P> C loadAndGetControllerInternal(String fxmlName, Consumer<P> loadConsumer) {
		FXMLLoader fxmlLoader = getFxmlLoader(fxmlName);
		P load;
		try {
			load = fxmlLoader.load();
		} catch (IOException e) {
			throw Errors.illegalState(e, "Encountered exception loading '%s'", fxmlName);
		}
		if (loadConsumer != null)
			loadConsumer.accept(load);
		return fxmlLoader.getController();
	}

	private FXMLLoader getFxmlLoader(String fxmlName) {
		return new FXMLLoader(resourceLoader.getResourceUrl(String.format(fxmlResourcePattern, fxmlName)));
	}
}