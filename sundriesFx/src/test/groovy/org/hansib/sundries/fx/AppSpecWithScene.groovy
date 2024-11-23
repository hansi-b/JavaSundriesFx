package org.hansib.sundries.fx

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.testfx.api.FxToolkit
import org.testfx.framework.spock.ApplicationSpec
import org.testfx.util.WaitForAsyncUtils

import javafx.scene.Scene
import javafx.stage.Stage

abstract public class AppSpecWithScene extends ApplicationSpec {

	protected static final Logger log = LogManager.getLogger(AppSpecWithScene)

	protected Stage stage

	@Override
	public void start(Stage stage) throws Exception {
		this.stage = FxToolkit.registerPrimaryStage()
		FxToolkit.setupScene(() -> createScene())
		FxToolkit.showStage()
	}

	/**
	 * Has to be overridden to add + use elements in the scene.
	 */
	abstract protected Scene createScene()

	static void waitForAsyncFx(Runnable runnable) {
		WaitForAsyncUtils.waitForAsyncFx(1_000, runnable)
	}

	static boolean isHeadless() { Boolean.getBoolean('testfx.headless') }

	@Override
	void stop() throws Exception {
		FxToolkit.cleanupStages()
	}
}
