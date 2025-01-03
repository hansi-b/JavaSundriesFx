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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

import org.hansib.sundries.Errors;

public class AlertBuilder {

	private AlertType alertType;
	private String contentText;
	private Node contentPane;

	private String title;
	private String headerText;

	private Boolean resizable;

	private Map<ButtonType, String> buttonTypes = new LinkedHashMap<>();
	private ButtonType defaultButton;

	public AlertBuilder(AlertType alertType, String contentText) {
		this.alertType = alertType;
		this.contentText = contentText;
	}

	public AlertBuilder(AlertType alertType, Node contentPane) {
		this(alertType, "");
		this.contentPane = contentPane;
	}

	public AlertBuilder withTitle(String title) {
		this.title = title;
		return this;
	}

	public AlertBuilder withHeaderText(String headerText) {
		this.headerText = headerText;
		return this;
	}

	public AlertBuilder withButton(ButtonType type, String text) {
		if (buttonTypes.containsKey(type))
			throw Errors.illegalArg("Duplicate button type %s (current text: '%s', new text: %s)", type,
					buttonTypes.get(type), text);

		buttonTypes.put(type, text);
		return this;
	}

	public AlertBuilder withDefaultButton(ButtonType type, String text) {
		if (defaultButton != null)
			throw Errors.illegalArg("Duplicate default button type (current default type: '%s', new default type: %s)",
					defaultButton, type);
		defaultButton = type;
		withButton(type, text);
		return this;
	}

	public AlertBuilder resizable(boolean resizable) {
		this.resizable = resizable;
		return this;
	}

	public Alert build() {
		Alert alert = new Alert(alertType, contentText, buttonTypes.keySet().toArray(new ButtonType[0]));
		buttonTypes.forEach((type, text) -> {
			Button b = (Button) alert.getDialogPane().lookupButton(type);
			b.setText(text);
			b.setDefaultButton(type == defaultButton);
		});

		if (title != null)
			alert.setTitle(title);
		if (headerText != null)
			alert.setHeaderText(headerText);

		if (contentPane != null)
			alert.getDialogPane().setContent(contentPane);

		if (resizable != null)
			alert.setResizable(resizable);

		return alert;
	}

	/**
	 * Builds the alert, shows it, and waits for a response.
	 * 
	 * @return the result of {@link Alert#showAndWait()}
	 */
	public Optional<ButtonType> showAndWait() {
		return build().showAndWait();
	}

	/**
	 * Builds the alert, shows it, and waits for a response, and returns whether the
	 * response was present and the argument result.
	 * 
	 * @param expectedResult the result to check for
	 * @return true if the response from the alert was present and equal to the
	 *         expected result
	 */
	public boolean showAndWaitFor(ButtonType expectedResult) {
		Optional<ButtonType> response = showAndWait();
		return response.isPresent() && response.get() == expectedResult;
	}
}