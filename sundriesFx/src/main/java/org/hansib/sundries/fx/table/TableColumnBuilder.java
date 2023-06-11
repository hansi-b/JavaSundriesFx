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

import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;

/**
 * A builder-like class for augmenting table columns with various functions.
 * Differs from a regular builder in that the target column will usually be
 * passed in to the constructor.
 */
public class TableColumnBuilder<S, T> {

	private final TableColumn<S, T> col;

	private Function<T, String> formatter;
	private Function<S, ObservableValue<T>> valueFunc;

	private boolean withDragSelection = false;

	private Comparator<T> comparator;

	public TableColumnBuilder(TableColumn<S, T> col) {
		this.col = col;
	}

	public TableColumnBuilder<S, T> value(Function<S, ObservableValue<T>> valueFunc) {
		this.valueFunc = valueFunc;
		return this;
	}

	public TableColumnBuilder<S, T> format(Function<T, String> formatter) {
		this.formatter = formatter;
		return this;
	}

	public TableColumnBuilder<S, T> comparator(Comparator<T> comparator) {
		this.comparator = comparator;
		return this;
	}

	public TableColumnBuilder<S, T> withDragSelection() {
		this.withDragSelection = true;
		return this;
	}

	private static class FormattingTableCell<S, T> extends TableCell<S, T> { // NOSONAR

		private final Function<T, String> formatter;

		private FormattingTableCell(Function<T, String> formatter, boolean withDragSelection) {
			Objects.nonNull(formatter);
			this.formatter = formatter;

			if (withDragSelection) {
				setOnDragDetected(e -> {
					startFullDrag();
					getTableColumn().getTableView().getSelectionModel().select(getIndex(), getTableColumn());
				});
				setOnMouseDragEntered(
						e -> getTableColumn().getTableView().getSelectionModel().select(getIndex(), getTableColumn()));
			}
		}

		@Override
		/**
		 * Compare with javafx.scene.control.TableColumn.DEFAULT_CELL_FACTORY
		 */
		public void updateItem(final T item, final boolean empty) {
			super.updateItem(item, empty);
			setText(item == null || empty ? null : formatter.apply(item));
		}
	}

	/**
	 * @return the decorated column
	 */
	public TableColumn<S, T> build() {
		if (valueFunc != null)
			col.setCellValueFactory(cellData -> valueFunc.apply(cellData.getValue()));
		if (formatter != null)
			/*
			 * FIXME -> have to split out the drag selection; this should be independent of
			 * formatting
			 */
			col.setCellFactory(param -> new FormattingTableCell<>(formatter, withDragSelection));
		if (comparator != null)
			col.setComparator(comparator);
		return col;
	}
}
