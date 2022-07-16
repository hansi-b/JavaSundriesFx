package org.hansib.sundries.fx;

import javafx.stage.Stage;
import javafx.stage.Window;

public class Windows {

	/**
	 * @return the last, i.e.g, current focused stage
	 */
	public static Stage findFocusedStage() {
		return Window.getWindows().stream()//
				.map(w -> (w instanceof Stage s) ? s : null) //
				.filter(w -> w != null && w.isFocused()).reduce((a, b) -> b).orElse(null);
	}
}
