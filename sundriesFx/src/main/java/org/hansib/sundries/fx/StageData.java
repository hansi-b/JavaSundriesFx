/*-
 * MIT License
 *
 * SundriesFx - https://github.com/hansi-b/JavaSundriesFx
 *
 * Copyright (c) 2022-2025 Hansi B.
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

import javafx.stage.Stage;

/**
 * The basic data of a stage we might want to save and restore.
 */
public record StageData(double x, double y, double width, double height, boolean isShowing) {

	/**
	 * The constant denoting we have no data.
	 */
	public final static StageData NONE = new StageData(Double.NaN, Double.NaN, Double.NaN, Double.NaN, false);

	/**
	 * @return the stage data of the argument {@code stage}
	 */
	public static StageData of(Stage stage) {
		return new StageData(stage.getX(), stage.getY(), stage.getWidth(), stage.getHeight(), stage.isShowing());
	}

	/**
	 * @return true if {@code this} equals {@link StageData#NONE}
	 */
	public boolean isNone() {
		return this.equals(NONE);
	}

	/**
	 * Applies this {@link StageData} to the argument {@code stage} by setting the
	 * available fields. Does nothing when called on {@link StageData#NONE}.
	 * 
	 * @param stage the target {@link Stage} on which to apply these data
	 */
	public void apply(Stage stage) {

		if (isNone())
			return;

		stage.setX(x);
		stage.setY(y);
		stage.setWidth(width);
		stage.setHeight(height);
		if (isShowing)
			stage.show();
		else
			stage.hide();
	}
}
