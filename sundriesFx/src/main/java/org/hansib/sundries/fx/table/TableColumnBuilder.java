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

import java.util.Comparator;
import java.util.function.Function;

import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.util.Callback;

/**
 * A builder-like class for augmenting table columns with various functions.
 * Differs from a regular builder in that the target column will usually be
 * passed in to the constructor.
 */
public class TableColumnBuilder<S, T> {

	private final TableColumn<S, T> col;

	private String headerText;

	private Function<S, ObservableValue<T>> valueFunc;
	private Comparator<T> comparator;

	private Callback<TableColumn<S, T>, TableCell<S, T>> cellFactory;

	private EventHandler<CellEditEvent<S, T>> commitHandler;

	private boolean editable = false;

	public TableColumnBuilder(String headerText) {
		this.col = new TableColumn<>(headerText);
	}

	public TableColumnBuilder(TableColumn<S, T> col) {
		this.col = col;
	}

	public TableColumnBuilder<S, T> headerText(String headerText) {
		this.headerText = headerText;
		return this;
	}

	public TableColumnBuilder<S, T> value(Function<S, ObservableValue<T>> valueFunc) {
		this.valueFunc = valueFunc;
		return this;
	}

	public TableColumnBuilder<S, T> comparator(Comparator<T> comparator) {
		this.comparator = comparator;
		return this;
	}

	public TableColumnBuilder<S, T> cellFactory(Callback<TableColumn<S, T>, TableCell<S, T>> cellFactory) {
		this.cellFactory = cellFactory;
		return this;
	}

	/**
	 * 
	 * Sets the column's edit handler and additionally sets the table to editable.
	 * 
	 * @param commitHandler the handler to call on an edit commit
	 */
	public TableColumnBuilder<S, T> onEditCommit(EventHandler<CellEditEvent<S, T>> commitHandler) {
		this.commitHandler = commitHandler;
		return this;
	}

	public TableColumnBuilder<S, T> editable() {
		this.editable = true;
		return this;
	}

	/**
	 * @return the decorated column
	 */
	public TableColumn<S, T> build() {
		if (headerText != null)
			col.setText(headerText);
		if (cellFactory != null)
			col.setCellFactory(cellFactory);
		if (valueFunc != null)
			col.setCellValueFactory(cellData -> valueFunc.apply(cellData.getValue()));
		if (commitHandler != null) {
			col.setOnEditCommit(commitHandler);
			col.setEditable(true);
		}
		if (editable)
			col.setEditable(true);
		if (comparator != null)
			col.setComparator(comparator);
		return col;
	}
}
