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

			if (invalidTextCssStyleClass != null)
				textField.textProperty().addListener((observable, oldValue, newValue) -> {
					final ObservableList<String> styleClass = textField.getStyleClass();
					if (isTextValid.test(newValue)) {
						styleClass.removeIf(invalidTextCssStyleClass::equals);
					} else {
						if (!styleClass.contains(invalidTextCssStyleClass))
							styleClass.add(invalidTextCssStyleClass);
					}
				});
		}

		private void validate() {
			String text = textField.getText();
			if (!isTextValid.test(text))
				textField.requestFocus();
			else if (validatedTextCallback != null)
				validatedTextCallback.accept(text);
		}
	}

	public static final String CSS_INVALID_TEXT = "validating-text-field-invalid-text";

	private TextField textField;

	private Predicate<String> isTextValid;

	private Consumer<String> validatedTextCallback;

	private String invalidTextCssStyleClass = CSS_INVALID_TEXT;

	public TextFieldValidation() {
		this(null);
	}

	public TextFieldValidation(TextField textField) {
		this.textField = textField;
	}

	public TextFieldValidation on(TextField textField) {
		this.textField = textField;
		return this;
	}

	public TextFieldValidation withValidation(Predicate<String> isTextValid) {
		this.isTextValid = isTextValid;
		return this;
	}

	public TextFieldValidation withValidatedTextCallback(Consumer<String> validatedTextCallback) {
		this.validatedTextCallback = validatedTextCallback;
		return this;
	}

	public TextFieldValidation withInvalidCssStyleClass(String invalidTextCssStyleClass) {
		this.invalidTextCssStyleClass = invalidTextCssStyleClass;
		return this;
	}

	public TextField build() {
		ValidationWrapper wrapper = new ValidationWrapper(//
				textField != null ? textField : new TextField(), //
				isTextValid != null ? isTextValid : this::isNullOrBlank, //
				validatedTextCallback, //
				invalidTextCssStyleClass);

		wrapper.init();
		textField = null;
		return wrapper.textField;
	}

	private boolean isNullOrBlank(String value) {
		return value != null && !value.isBlank();
	}
}