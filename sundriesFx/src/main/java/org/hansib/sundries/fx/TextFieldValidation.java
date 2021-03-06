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

import java.util.function.Consumer;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import javafx.scene.control.TextField;

/**
 * A builder/decorator to create or augment a {@link TextField} with validating
 * behaviour. Once the text field gets focus, the text is validated while being
 * edited; while the text is invalid, focus is forced into the text field.
 */
public class TextFieldValidation {

	private static class ValidationWrapper {
		private final TextField textField;
		private final Predicate<String> isTextValid;
		private final Consumer<String> validatedTextCallback;
		private final String invalidTextCssStyleClass;

		private ValidationWrapper(TextField textField, Predicate<String> isTextValid,
				Consumer<String> validatedTextCallback, String invalidTextCssStyleClass) {
			this.textField = textField;
			this.isTextValid = isTextValid;
			this.validatedTextCallback = validatedTextCallback;
			this.invalidTextCssStyleClass = invalidTextCssStyleClass;
		}

		private void init() {

			textField.setOnAction(event -> validate());
			textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
				if (Boolean.FALSE.equals(newValue))
					validate();
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
		}

		private void updateCssStyle(String newValue) {
			final ObservableList<String> styleClass = textField.getStyleClass();
			if (isTextValid.test(newValue))
				styleClass.removeIf(invalidTextCssStyleClass::equals);
			else if (!styleClass.contains(invalidTextCssStyleClass))
				styleClass.add(invalidTextCssStyleClass);
		}

		private void validate() {
			String text = textField.getText();
			if (!isTextValid.test(text))
				textField.requestFocus();
			else if (validatedTextCallback != null)
				validatedTextCallback.accept(text);
		}
	}

	private Predicate<String> isTextValid;

	private Consumer<String> validatedTextCallback;

	private String invalidTextCssStyleClass;

	public TextFieldValidation() {
		// no op
	}

	/**
	 * @param isTextValid whether the text field content is valid; defaults to
	 *                    non-empty, non-blank text
	 */
	public TextFieldValidation withValidation(Predicate<String> isTextValid) {
		this.isTextValid = isTextValid;
		return this;
	}

	/**
	 * @param validatedTextCallback called with validated text when the text field
	 *                              focus is left
	 */
	public TextFieldValidation withValidatedTextCallback(Consumer<String> validatedTextCallback) {
		this.validatedTextCallback = validatedTextCallback;
		return this;
	}

	/**
	 * Make the text field use the argument CSS class while its content is invalid.
	 */
	public TextFieldValidation withInvalidCssStyleClass(String invalidTextCssStyleClass) {
		this.invalidTextCssStyleClass = invalidTextCssStyleClass;
		return this;
	}

	/**
	 * @return a new text field with the validation additions
	 */
	public TextField build() {
		return build(null);
	}

	/**
	 * @return the argument text field with the validation additions
	 */
	public TextField build(TextField textField) {
		ValidationWrapper wrapper = new ValidationWrapper(//
				textField != null ? textField : new TextField(), //
				isTextValid != null ? isTextValid : this::isNullOrBlank, //
				validatedTextCallback, //
				invalidTextCssStyleClass);

		wrapper.init();
		return wrapper.textField;
	}

	private boolean isNullOrBlank(String value) {
		return value != null && !value.isBlank();
	}
}