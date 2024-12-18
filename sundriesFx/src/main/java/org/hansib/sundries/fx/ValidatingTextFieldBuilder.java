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

import java.util.function.Consumer;
import java.util.function.Predicate;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;

/**
 * A builder to create or augment a {@link TextField} with validating behaviour.
 * Once the text field gets focus, the text is validated while being edited;
 * while the text is invalid, focus is forced into the text field.
 * 
 * NB: The validation uses the text field's
 * {@link TextField#setOnAction(javafx.event.EventHandler)}. If this is set
 * after building the validation, the validation is no longer active.
 */
public class ValidatingTextFieldBuilder {

	private static class ValidationWrapper {

		private final TextField textField;
		private final Styler styler;

		private final Predicate<String> isTextValid;
		private final Consumer<String> validatedTextCallback;
		private final String invalidTextCssStyleClass;

		private ValidationWrapper(TextField textField, Predicate<String> isTextValid,
				Consumer<String> validatedTextCallback, String invalidTextCssStyleClass) {
			this.textField = textField;
			this.styler = new Styler(textField);

			this.isTextValid = isTextValid;
			this.validatedTextCallback = validatedTextCallback;
			this.invalidTextCssStyleClass = invalidTextCssStyleClass;
		}

		private TextField initialise() {

			EventHandler<ActionEvent> orgOnAction = textField.getOnAction();
			textField.setOnAction(event -> {
				if (checkAndHandleValidity() && orgOnAction != null)
					orgOnAction.handle(event);
			});
			textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
				if (Boolean.FALSE.equals(newValue))
					checkAndHandleValidity();
			});

			if (invalidTextCssStyleClass != null) {
				textField.textProperty().addListener((observable, oldValue, newValue) -> updateCssStyle(newValue));
				textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
					if (Boolean.TRUE.equals(newValue))
						updateCssStyle(textField.getText());
				});
				if (textField.isFocused())
					updateCssStyle(textField.getText());
			}
			return textField;
		}

		private void updateCssStyle(String newValue) {
			styler.addOrRemove(!isTextValid.test(newValue), invalidTextCssStyleClass);
		}

		private boolean checkAndHandleValidity() {
			String text = textField.getText();
			boolean isValid = isTextValid.test(text);
			if (!isValid)
				textField.requestFocus();
			else if (validatedTextCallback != null)
				validatedTextCallback.accept(text);
			return isValid;
		}

		private static boolean isNotNullOrBlank(String value) {
			return value != null && !value.isBlank();
		}
	}

	private TextField textField;

	private String initialText;

	private Predicate<String> isTextValid;

	private Consumer<String> validatedTextCallback;

	private String invalidTextCssStyleClass;

	public ValidatingTextFieldBuilder() {
		this(new TextField());
	}

	public ValidatingTextFieldBuilder(TextField textField) {
		this.textField = textField;
	}

	public ValidatingTextFieldBuilder(String initialText) {
		this();
		withInitialText(initialText);
	}

	public ValidatingTextFieldBuilder withInitialText(String initialText) {
		this.initialText = initialText;
		return this;
	}

	/**
	 * @param isTextValid whether the text field content is valid; defaults to
	 *                    non-empty, non-blank text
	 */
	public ValidatingTextFieldBuilder withValidation(Predicate<String> isTextValid) {
		this.isTextValid = isTextValid;
		return this;
	}

	/**
	 * Sets a consumer for a successfully validated text input. If the text field
	 * has an action handler set and validation succeeded on ENTER, this consumer is
	 * called <em>before</em> the action handler is called.
	 * 
	 * @param validatedTextCallback called with validated text when the text field
	 *                              focus is left
	 */
	public ValidatingTextFieldBuilder withValidatedTextCallback(Consumer<String> validatedTextCallback) {
		this.validatedTextCallback = validatedTextCallback;
		return this;
	}

	/**
	 * Make the text field use the argument CSS class while its content is invalid.
	 */
	public ValidatingTextFieldBuilder withInvalidCssStyleClass(String invalidTextCssStyleClass) {
		this.invalidTextCssStyleClass = invalidTextCssStyleClass;
		return this;
	}

	/**
	 * @return a new text field with the validation additions
	 */
	public TextField build() {
		TextField tf = textField != null ? textField : new TextField();
		if (initialText != null)
			tf.setText(initialText);
		ValidationWrapper wrapper = new ValidationWrapper(tf, //
				isTextValid != null ? isTextValid : ValidationWrapper::isNotNullOrBlank, //
				validatedTextCallback, //
				invalidTextCssStyleClass);

		return wrapper.initialise();
	}
}