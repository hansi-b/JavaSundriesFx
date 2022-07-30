package org.hansib.sundries.fx;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Platform;
import javafx.collections.FXCollections;
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

	private Supplier<Collection<E>> itemsSupplier;

	private ComboBox<E> comboBox;

	public FilteringComboBox(ComboBox<E> comboBox) {
		this.comboBox = comboBox;
	}

	public FilteringComboBox<E> initialise(Function<Set<String>, Predicate<E>> matchBuilder,
			StringConverter<E> stringConverter, Runnable onEnter) {

		FilteredList<E> filteredItems = initialiseFilteredItems();

		comboBox.setEditable(true);

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

		comboBox.getEditor().addEventHandler(KeyEvent.KEY_PRESSED, e -> {
			if (e.getCode() == KeyCode.ENTER) {
				onEnter.run();
			}
		});
		comboBox.setConverter(stringConverter);

		return this;
	}

	public FilteringComboBox<E> withItemsUpdateOnFocus(Supplier<Collection<E>> itemsSupplier) {

		this.itemsSupplier = itemsSupplier;
		return this;
	}

	private FilteredList<E> initialiseFilteredItems() {
		ObservableList<E> items = FXCollections.observableArrayList();
		items.setAll(itemsSupplier.get());
		FilteredList<E> filteredList = new FilteredList<>(items, p -> true);
		comboBox.setItems(filteredList);

		comboBox.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (Boolean.TRUE.equals(newValue)) {
				items.setAll(itemsSupplier.get());
			}
		});
		return filteredList;
	}

	public ComboBox<E> build() {
		initialiseFilteredItems();
		return comboBox;
	}

}