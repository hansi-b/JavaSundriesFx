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

import java.util.Arrays;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hansib.sundries.Errors;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.StringConverter;

/**
 * Helps augment a combo box with a selection filter on its items.
 */
public class FilteringComboBox<E> {

	private static final Logger log = LogManager.getLogger();

	private final ComboBox<E> comboBox;

	private Runnable onEnter;

	public FilteringComboBox(ComboBox<E> comboBox) {
		this.comboBox = comboBox;
	}

	public FilteringComboBox<E> initialise(Function<Set<String>, Predicate<E>> matchBuilder) {
		comboBox.setEditable(true);

		FilteredList<E> filteredItems = initialiseFilteredItems();

		comboBox.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
			E selected = comboBox.getSelectionModel().getSelectedItem();
			log.info("selected = {}", selected);
			Platform.runLater(() -> {
				Set<String> split = Arrays.stream(newValue.split("\\s+")).filter(s -> !s.isBlank())
						.map(String::toLowerCase).collect(Collectors.toSet());
				Predicate<E> matcher = matchBuilder.apply(split);
				// If the no item in the list is selected or the selected item
				// isn't equal to the current input, we refilter the list.
				// if (selected == null || !selected.equals(cb.getEditor().getText())) {
				if (selected == null) {
					comboBox.hide();
					filteredItems.setPredicate(matcher::test);
					log.info("after pred: {}", filteredItems);
					comboBox.show();
				}
			});
		});

		return this;
	}

	public FilteringComboBox<E> withActionOnEnter(Runnable onEnter) {

		this.onEnter = onEnter;
		return this;
	}

	public FilteringComboBox<E> withConverter(StringConverter<E> stringConverter) {
		comboBox.setConverter(stringConverter);
		return this;
	}

	private FilteredList<E> initialiseFilteredItems() {
		ObservableList<E> items = comboBox.getItems();
		if (items instanceof FilteredList<E>)
			throw Errors.illegalArg("Items on %s must not be a filtered list", comboBox);
		FilteredList<E> filteredList = new FilteredList<>(items, p -> true);
		comboBox.setItems(filteredList);

		return filteredList;
	}

	public ComboBox<E> build() {
		if (onEnter != null)
			comboBox.getEditor().addEventHandler(KeyEvent.KEY_PRESSED, e -> {
				if (e.getCode() == KeyCode.ENTER) {
					onEnter.run();
				}
			});
		return comboBox;
	}

}