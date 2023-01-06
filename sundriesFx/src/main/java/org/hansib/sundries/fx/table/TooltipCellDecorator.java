/**
 * MIT License
 *
 * SundriesFx - https://github.com/hansi-b/JavaSundriesFx
 *
 * Copyright (c) 2023 Hansi B.
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
import javafx.scene.control.Tooltip;
import javafx.util.Callback;
import javafx.util.Duration;

public class TooltipCellDecorator<O, T> implements Callback<TableColumn<O, T>, TableCell<O, T>> {

	private final Callback<TableColumn<O, T>, TableCell<O, T>> columnCellFactory;
	private final Function<T, String> tooltipStringFunc;

	private TooltipCellDecorator(final Callback<TableColumn<O, T>, TableCell<O, T>> columnCellFactory,
			final Function<T, String> tooltipStringFunc) {
		this.columnCellFactory = columnCellFactory;
		this.tooltipStringFunc = tooltipStringFunc;
	}

	public static <S, T> void decorateColumn(final TableColumn<S, T> column,
			final Function<T, String> tooltipStringFunc) {
		column.setCellFactory(new TooltipCellDecorator<>(column.getCellFactory(), tooltipStringFunc));
	}

	@Override
	public TableCell<O, T> call(final TableColumn<O, T> col) {
		final TableCell<O, T> cell = columnCellFactory.call(col);

		cell.itemProperty().addListener((observable, oldValue, newValue) -> {

			final String tipValue = tooltipStringFunc.apply(newValue);
			if (tipValue == null)
				cell.setTooltip(null);
			else {
				if (cell.getTooltip() == null)
					cell.setTooltip(createTooltip());
				cell.getTooltip().textProperty().set(tipValue);
			}
		});

		return cell;
	}

	private static Tooltip createTooltip() {
		final Tooltip tooltip = new Tooltip();
		tooltip.setShowDelay(Duration.millis(700));
		tooltip.setShowDuration(Duration.minutes(1));
		return tooltip;
	}
}