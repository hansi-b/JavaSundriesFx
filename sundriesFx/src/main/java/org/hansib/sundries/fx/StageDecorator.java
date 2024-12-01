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
package org.hansib.sundries.fx;

import java.util.Objects;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.stage.Stage;

public class StageDecorator {

	private final Stage stage;

	public StageDecorator(Stage stage) {
		this.stage = Objects.requireNonNull(stage);
	}

	/**
	 * Makes this decorator's stage resize to its previous size when reopened.
	 */
	public void addSizeRestore() {
		stage.setOnShown(composeHandlers(e -> {
			stage.setX(stage.getX());
			stage.setY(stage.getY());
			stage.setWidth(stage.getWidth());
			stage.setHeight(stage.getHeight());
		}, stage.getOnShown()));
	}

	private static <E extends Event> EventHandler<E> composeHandlers(EventHandler<E> first, EventHandler<E> second) {
		if (first == null)
			return second;
		if (second == null)
			return first;
		return event -> {
			first.handle(event);
			second.handle(event);
		};
	}
}