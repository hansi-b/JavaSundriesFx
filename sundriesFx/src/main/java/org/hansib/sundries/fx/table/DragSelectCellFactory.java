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
package org.hansib.sundries.fx.table;

import java.util.function.Function;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

/**
 * adapted from https://community.oracle.com/tech/developers/discussion/2621389/
 *
 * Allows for selection of cells by dragging the mouse over them.
 */
public class DragSelectCellFactory<O, T> implements Callback<TableColumn<O, T>, TableCell<O, T>> {

	private static class DragSelectCell<O, T> extends TableCell<O, T> {

		private final Function<T, String> formatter;

		DragSelectCell(final Function<T, String> formatter) {

			this.formatter = formatter;
			setOnDragDetected(e -> {
				startFullDrag();
				getTableColumn().getTableView().getSelectionModel().select(getIndex(), getTableColumn());
			});
			setOnMouseDragEntered(
					e -> getTableColumn().getTableView().getSelectionModel().select(getIndex(), getTableColumn()));
		}

		@Override
		public void updateItem(final T item, final boolean empty) {
			super.updateItem(item, empty);

			final String result;
			if (item == null || empty)
				result = null;
			else if (formatter == null)
				result = item.toString();
			else
				result = formatter.apply(item);

			setText(result);
		}
	}

	private Function<T, String> formatter;

	public DragSelectCellFactory() {
		this(null);
	}

	public DragSelectCellFactory(final Function<T, String> formatter) {
		this.formatter = formatter;
	}

	@Override
	public TableCell<O, T> call(final TableColumn<O, T> col) {
		return new DragSelectCell<>(formatter);
	}
}