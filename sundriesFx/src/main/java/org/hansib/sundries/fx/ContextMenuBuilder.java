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
package org.hansib.sundries.fx;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

import org.hansib.sundries.Errors;

/**
 * A simple builder for {@link ContextMenu}. Mainly syntactic sugar for slightly
 * more elegant creation.
 */
public class ContextMenuBuilder {

	private final List<MenuItem> items;

	public ContextMenuBuilder() {
		items = new ArrayList<>();
	}

	public ContextMenuBuilder item(String label, EventHandler<ActionEvent> eventHandler) {
		return itemInternal(label, eventHandler, null);
	}

	public ContextMenuBuilder item(String label, EventHandler<ActionEvent> eventHandler,
			ObservableValue<Boolean> disableOn) {
		return itemInternal(label, eventHandler, Objects.requireNonNull(disableOn));
	}

	private ContextMenuBuilder itemInternal(String label, EventHandler<ActionEvent> eventHandler,
			ObservableValue<Boolean> disableOn) {
		final MenuItem item = new MenuItem(label);
		item.setOnAction(eventHandler);
		if (disableOn != null)
			item.disableProperty().bind(disableOn);
		items.add(item);
		return this;
	}

	/**
	 * @return a new context menu with this builder's current menu items
	 */
	public ContextMenu build() {
		if (items.isEmpty())
			throw Errors.illegalState("Cannot build context menu without menu items.");

		ContextMenu cm = new ContextMenu();
		cm.getItems().addAll(items);
		return cm;
	}
}