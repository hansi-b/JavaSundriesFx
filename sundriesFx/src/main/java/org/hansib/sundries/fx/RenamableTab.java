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
package org.hansib.sundries.fx;

import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;

public class RenamableTab {

	private static final String CSS_RENAMABLE_TAB_ERROR = "renamable-tab-error";
	private static final String CSS_RENAMABLE_TAB_SELECTED = "renamable-tab-selected";
	private static final String CSS_RENAMABLE_TAB_UNSELECTED = "renamable-tab-unselected";

	private final Tab tab;
	private final TextField textField;
	private final Label label;
	private Styler styler;

	public RenamableTab(final String initialLabel) {

		label = new Label(initialLabel);
		styler = new Styler(label).with(CSS_RENAMABLE_TAB_UNSELECTED);

		label.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2)
				editLabel();
		});

		tab = new Tab();
		tab.setGraphic(label);

		tab.selectedProperty().addListener((observable, oldValue, newValue) -> styler.ifOneElseOther(newValue,
				CSS_RENAMABLE_TAB_SELECTED, CSS_RENAMABLE_TAB_UNSELECTED));

		textField = new ValidatingTextFieldBuilder() //
				.withInvalidCssStyleClass(CSS_RENAMABLE_TAB_ERROR) //
				.withValidatedTextCallback(this::updateText) //
				.build();
	}

	private void updateText(String text) {
		label.setText(text);
		tab.setGraphic(label);
	}

	public Tab getTab() {
		return tab;
	}

	public StringProperty labelProperty() {
		return label.textProperty();
	}

	public void editLabel() {
		textField.setText(label.getText());
		tab.setGraphic(textField);
		textField.selectAll();
		textField.requestFocus();
	}
}