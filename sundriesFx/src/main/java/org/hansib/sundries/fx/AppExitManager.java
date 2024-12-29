package org.hansib.sundries.fx;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javafx.application.Platform;

/**
 * A single point of exit management for a JavaFX application. Will usually be a singleton.
 */
public class AppExitManager {

	private final List<Runnable> preExitActions = new ArrayList<>();

	/**
	 * @param action an action to execute before calling exit on the platform
	 */
	public void addPreExitAction(Runnable action) {
		preExitActions.add(Objects.requireNonNull(action));
	}

	public void exit() {
		preExitActions.forEach(Runnable::run);
		Platform.exit();
	}
}
