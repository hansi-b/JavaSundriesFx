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

import javafx.css.Styleable;

/**
 * Deals with adding and removing style classes (CSS). Just hides all the
 * tedious if..else add/remove details.
 * 
 * NB: Maintains a set-like notion of style class membership: Removing a class
 * removes all its occurrences from the classes list; adding a class has no
 * effect if it is already present.
 */
public class Styler {

	private final Styleable styleable;

	public Styler(Styleable styleable) {
		this.styleable = styleable;
	}

	public Styler with(String... styles) {
		for (String style : styles)
			add(style);
		return this;
	}

	/**
	 * Adds the argument style if it is not already present.
	 * 
	 * @param styleClass the style class name to add
	 * @return true if this style class was added, false if it was already present
	 */
	public boolean add(String styleClass) {
		if (contains(styleClass))
			return false;
		return styleable.getStyleClass().add(styleClass);
	}

	/**
	 * Removes all instances of the argument style.
	 * 
	 * @param styleClass the style class name to remove
	 * @return true if this style class was removed at least once, false if it was
	 *         not present
	 */
	public boolean remove(String styleClass) {
		return styleable.getStyleClass().removeIf(styleClass::equals);
	}

	public boolean contains(String styleClass) {
		return styleable.getStyleClass().contains(styleClass);
	}

	/**
	 * Adds or removes the argument styleClass depending on the argument boolean; if
	 * true, the class is added, otherwise removed.
	 * 
	 * @param styleTest  the test for the style; on true, the styleClass is added,
	 *                   otherwise removed
	 * @param styleClass the styleClass to be added or removed
	 */
	public void addOrRemove(boolean styleTest, String styleClass) {
		if (styleTest)
			add(styleClass);
		else
			remove(styleClass);
	}

	/**
	 * Adds one of the two argument styleClasses depending on the argument boolean,
	 * and removes the respective other; if the test is true, the oneeStyleClass is
	 * added, and the otherStyleClass is removed; on a false test, the other way
	 * around.
	 * 
	 * @param styleTest       the test for the style
	 * @param oneStyleClass   the styleClass to be added on a true test
	 * @param otherStyleClass the styleClass to be added on a false test
	 */
	public void ifOneElseOther(boolean styleTest, String oneStyleClass, String otherStyleClass) {
		if (styleTest) {
			remove(otherStyleClass);
			add(oneStyleClass);
		} else {
			remove(oneStyleClass);
			add(otherStyleClass);
		}
	}
}
