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

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

public class RenamableTab {

	private static final String CSS_RENAMABLE_TAB_ERROR = "renamable-tab-error";
	private static final String CSS_RENAMABLE_TAB_SELECTED = "renamable-tab-selected";
	private static final String CSS_RENAMABLE_TAB_UNSELECTED = "renamable-tab-unselected";

	private final Tab tab;
	private final TextField textField;
	private final Label label;

	private final StringProperty labelProp;

	private final BooleanBinding isTabEmpty;
	private final ReadOnlyBooleanProperty isTabSelected;

	public RenamableTab(final String initialLabel) {
		tab = new Tab();

		labelProp = new SimpleStringProperty(initialLabel);

		label = new Label();
		label.textProperty().bind(labelProp);
		label.getStyleClass().add(CSS_RENAMABLE_TAB_UNSELECTED);
		tab.setGraphic(label);

		textField = new TextField();

		isTabSelected = tab.selectedProperty();
		isTabSelected.addListener((observable, oldValue, newValue) -> {
			final ObservableList<String> styleClass = label.getStyleClass();
			if (Boolean.TRUE.equals(newValue)) {
				styleClass.remove(CSS_RENAMABLE_TAB_UNSELECTED);
				if (!styleClass.contains(CSS_RENAMABLE_TAB_SELECTED))
					styleClass.add(CSS_RENAMABLE_TAB_SELECTED);
			} else {
				styleClass.remove(CSS_RENAMABLE_TAB_SELECTED);
				if (!styleClass.contains(CSS_RENAMABLE_TAB_UNSELECTED))
					styleClass.add(CSS_RENAMABLE_TAB_UNSELECTED);
			}
		});
		isTabEmpty = textField.textProperty().isEmpty();
		isTabEmpty.addListener((obs, oldVal, newVal) -> {
			final ObservableList<String> styleClass = textField.getStyleClass();
			if (Boolean.TRUE.equals(newVal)) {
				if (!styleClass.contains(CSS_RENAMABLE_TAB_ERROR))
					styleClass.add(CSS_RENAMABLE_TAB_ERROR);
			} else
				styleClass.remove(CSS_RENAMABLE_TAB_ERROR);
		});

		textField.setOnAction(event -> updateOrLeave(tab, labelProp, label, textField));

		textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (Boolean.FALSE.equals(newValue))
				updateOrLeave(tab, labelProp, label, textField);
		});

		textField.setOnKeyPressed(e -> {
			if (e.getCode() == KeyCode.ENTER && textField.getText().isEmpty())
				textField.requestFocus();
		});

		label.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2)
				editLabel();
		});
	}

	public Tab getTab() {
		return tab;
	}

	public StringProperty labelProperty() {
		return labelProp;
	}

	public void editLabel() {
		textField.setText(label.getText());
		tab.setGraphic(textField);
		textField.selectAll();
		textField.requestFocus();
	}

	private static void updateOrLeave(final Tab tab, final StringProperty name, final Label label,
			final TextField textField) {
		final String text = textField.getText();
		if (text.isEmpty()) {
			textField.requestFocus();
		} else {
			name.setValue(text);
			tab.setGraphic(label);
		}
	}
}