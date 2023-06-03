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

import javafx.beans.value.ObservableValue;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class TableViewTools {

	public static <S, T> void setPrefWidth(final TableView<S> table, final TableColumn<S, T> col, final double fact) {
		col.prefWidthProperty().bind(table.widthProperty().multiply(fact));
	}

	/**
	 * Initialises the argument column with a cell value factory and a
	 * DragSelectCellFactory. Also sets the selection mode of the column's table to
	 * SelectionMode.MULTIPLE. (Because without, this does not make any sense.)
	 */
	public static <S, T> void initDragSelectCellCol(final TableColumn<S, T> col,
			final Function<S, ObservableValue<T>> cellValueFac, final Function<T, String> formatter) {
		col.setCellValueFactory(cellData -> cellValueFac.apply(cellData.getValue()));
		col.setCellFactory(new DragSelectCellFactory<>(formatter));

		if (col.getTableView().getSelectionModel().getSelectionMode() != SelectionMode.MULTIPLE)
			col.getTableView().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	}

}
