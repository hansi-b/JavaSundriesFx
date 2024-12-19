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
package org.hansib.sundries.fx.table;

import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

import org.hansib.sundries.fx.ContextMenuBuilder;

public class CsvCopyTableEnabler {
	private static final KeyCodeCombination defaultCopyControlKey = new KeyCodeCombination(KeyCode.C,
			KeyCombination.CONTROL_DOWN);
	private static final KeyCodeCombination defaultSelectAllControlKey = new KeyCodeCombination(KeyCode.A,
			KeyCombination.CONTROL_DOWN);

	/**
	 * a table row that can be joined to a CSV String
	 */
	public interface CsvRow {
		String asCsv();
	}

	/**
	 * Is called for the menu item strings.
	 */
	public interface MenuItemsLocalizer {
		String selectAll();

		String copySelection();
	}

	private final MenuItemsLocalizer menuItemsLocalizer;

	public CsvCopyTableEnabler(MenuItemsLocalizer menuItemsLocalizer) {
		this.menuItemsLocalizer = menuItemsLocalizer;
	}

	public <T extends CsvRow> void enableSelectAndCopyCapability(final TableView<T> table) {
		table.setOnKeyReleased(e -> {
			if (defaultCopyControlKey.match(e) && table == e.getSource())
				copyCsvToClipboard(table);
			else if (defaultSelectAllControlKey.match(e) && table == e.getSource())
				table.getSelectionModel().selectAll();
		});
		table.setContextMenu(new ContextMenuBuilder() //
				.item(menuItemsLocalizer.selectAll(), e -> table.getSelectionModel().selectAll()) //
				.item(menuItemsLocalizer.copySelection(), e -> copyCsvToClipboard(table)) //
				.build());
	}

	private static <T extends CsvRow> void copyCsvToClipboard(final TableView<T> table) {
		final ObservableList<T> selectedItems = table.getSelectionModel().getSelectedItems();
		final String csv = selectedItems.stream().map(T::asCsv).collect(Collectors.joining(System.lineSeparator()));

		final ClipboardContent clipboardContent = new ClipboardContent();
		clipboardContent.putString(csv);

		Clipboard.getSystemClipboard().setContent(clipboardContent);
	}
}