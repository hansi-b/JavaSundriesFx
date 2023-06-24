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

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javafx.scene.control.DatePicker;

public class ValidatingDatePicker {

	private static class DateValidator {
		private static final String CSS_DATE_PICKER_ERROR = "date-picker-error";
		private final DatePicker datePicker;
		private final Styler styler;

		private DateValidator(DatePicker datePicker) {
			this.datePicker = datePicker;
			this.styler = new Styler(datePicker);
		}

		/**
		 * Validates a date picker's editor content; styles the date picker via CSS on
		 * error.
		 * 
		 * @return a validated date; or null, if the editor content is not a valid date
		 */
		private LocalDate validateAndStyle() {
			LocalDate localDate = getValidatedLocalDate();
			styler.addOrRemove(localDate == null, CSS_DATE_PICKER_ERROR);
			return localDate;
		}

		private LocalDate getValidatedLocalDate() {
			try {
				return datePicker.getConverter().fromString(datePicker.getEditor().getText());
			} catch (DateTimeParseException e) {
				return null;
			}
		}
	}

	public static void ensureDatePickerUpdate(DatePicker datePicker) {
		DateValidator validator = new DateValidator(datePicker);

		datePicker.getEditor().textProperty().addListener((obs, oVal, nVal) -> validator.validateAndStyle());
		datePicker.getEditor().focusedProperty().addListener((obj, wasFocused, isFocused) -> {
			if (Boolean.TRUE.equals(isFocused))
				return;
			LocalDate date = validator.validateAndStyle();
			if (date != null)
				datePicker.setValue(date);
			else
				datePicker.getEditor().requestFocus();
		});
	}
}
