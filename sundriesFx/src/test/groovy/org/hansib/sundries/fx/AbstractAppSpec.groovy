package org.hansib.sundries.fx

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.testfx.api.FxToolkit
import org.testfx.framework.spock.ApplicationSpec

import javafx.scene.Scene
import javafx.stage.Stage

abstract public class AbstractAppSpec extends ApplicationSpec {
	static final Logger log = LogManager.getLogger(AbstractAppSpec)

	Stage stage

	def setupSpec() {
		def isHeadless = Boolean.getBoolean('headless')
		log.info "HEADLESS MODE = $isHeadless"
		if (isHeadless) {
			System.setProperty('testfx.robot', 'glass')
			System.setProperty('testfx.headless', 'true')
			System.setProperty('prism.order', 'sw')
			System.setProperty('prism.text', 't2k')
		}
	}

	@Override
	void init() throws Exception {
		stage = FxToolkit.registerPrimaryStage()
		log.info "INIT=$stage"
	}

	@Override
	public void start(Stage stage) throws Exception {
		stage.setScene(createScene())
		stage.show()
	}

	abstract Scene createScene();
}
