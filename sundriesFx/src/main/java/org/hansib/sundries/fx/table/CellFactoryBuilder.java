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

import java.util.function.Function;
import java.util.function.UnaryOperator;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class CellFactoryBuilder<S, T> {

	private final Callback<TableColumn<S, T>, TableCell<S, T>> defaultCellFactory;

	private Function<T, String> formatter;
	private boolean withDragSelection;

	public CellFactoryBuilder(Callback<TableColumn<S, T>, TableCell<S, T>> defaultCellFactory) {
		this.defaultCellFactory = defaultCellFactory;
	}

	public CellFactoryBuilder<S, T> format(Function<T, String> formatter) {
		this.formatter = formatter;
		return this;
	}

	public CellFactoryBuilder<S, T> withDragSelection() {
		this.withDragSelection = true;
		return this;
	}

	public Callback<TableColumn<S, T>, TableCell<S, T>> build() {
		Callback<TableColumn<S, T>, TableCell<S, T>> cellFactory;
		if (formatter != null)
			cellFactory = c -> new FormattingTableCell<>(formatter);
		else
			cellFactory = defaultCellFactory;

		if (withDragSelection)
			cellFactory = decorate(cellFactory, cell -> {
				cell.setOnDragDetected(e -> {
					cell.startFullDrag();
					cell.getTableColumn().getTableView().getSelectionModel().select(cell.getIndex(),
							cell.getTableColumn());
				});
				cell.setOnMouseDragEntered(e -> cell.getTableColumn().getTableView().getSelectionModel()
						.select(cell.getIndex(), cell.getTableColumn()));
				return cell;
			});

		return cellFactory;
	}

	private static <X, Y> Callback<TableColumn<X, Y>, TableCell<X, Y>> decorate(
			Callback<TableColumn<X, Y>, TableCell<X, Y>> orgCellFactory, UnaryOperator<TableCell<X, Y>> decor) {
		return param -> decor.apply(orgCellFactory.call(param));
	}
}