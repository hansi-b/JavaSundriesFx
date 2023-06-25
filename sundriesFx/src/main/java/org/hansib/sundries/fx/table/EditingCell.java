/**
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
package org.hansib.sundries.fx.table;

import java.util.function.Predicate;

import org.hansib.sundries.fx.ValidatingTextFieldBuilder;

import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.util.StringConverter;

/**
 * Adapted from
 * https://docs.oracle.com/javase/8/javafx/user-interface-tutorial/table-view.htm
 */
public class EditingCell<S, T> extends TableCell<S, T> { // NOSONAR

	private static final String CSS_VALIDATON_ERROR = "validation-error";

	private TextField textField;

	private final StringConverter<T> stringConverter;
	private final Predicate<String> textValidator;

	public EditingCell(StringConverter<T> stringConverter, Predicate<String> textValidator) {
		this.stringConverter = stringConverter;
		this.textValidator = textValidator;

		setEditable(true);
	}

	@Override
	public void startEdit() {
		if (!isEmpty()) {
			super.startEdit();
			createTextField();
			setText(null);
			setGraphic(textField);
			textField.selectAll();
		}
	}

	@Override
	public void cancelEdit() {
		super.cancelEdit();

		setText(stringConverter.toString(getItem()));
		setGraphic(null);
	}

	@Override
	public void updateItem(T item, boolean empty) {
		super.updateItem(item, empty);

		if (empty) {
			setText(null);
			setGraphic(null);
		} else {
			if (isEditing()) {
				if (textField != null) {
					textField.setText(getString());
				}
				setText(null);
				setGraphic(textField);
			} else {
				setText(getString());
				setGraphic(null);
			}
		}
	}

	private void createTextField() {
		textField = new ValidatingTextFieldBuilder(getString()) //
				.withValidation(textValidator) //
				.withInvalidCssStyleClass(CSS_VALIDATON_ERROR) //
				.withValidatedTextCallback(t -> commit()) //
				.build();
		textField.setOnKeyReleased(t -> {
			if (t.getCode() == KeyCode.ESCAPE) {
				cancelEdit();
				t.consume();
			}
		});
		textField.setMinWidth(getWidth() - getGraphicTextGap() * 2);
	}

	private void commit() {
		String t = textField.getText();
		System.out.println("commit " + t);
		commitEdit(stringConverter.fromString(t));
	}

	private String getString() {
		return getItem() == null ? "" : stringConverter.toString(getItem());
	}
}