package org.hansib.sundries.fx

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.testfx.api.FxToolkit
import org.testfx.framework.spock.ApplicationSpec

import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.Button
import javafx.scene.layout.StackPane
import javafx.stage.Stage

public class WindowsSpec extends ApplicationSpec {
	static final Logger log = LogManager.getLogger(WindowsSpec)

	Stage stage

	def setupSpec() {

		if (Boolean.getBoolean('headless')) {
			log.info '>>> HEADLESS MODE'

			System.setProperty('testfx.robot', 'glass')
			System.setProperty('testfx.headless', 'true')
			System.setProperty('prism.order', 'sw')
			System.setProperty('prism.text', 't2k')
		} else {
			log.info '>>> LIVE MODE'
		}
	}

	@Override
	public void start(Stage stage) throws Exception {
		def button = new Button('click me');
		button.setOnAction(actionEvent -> {
			def popup = new Alert(AlertType.INFORMATION)
			popup.setTitle('Popup')
			popup.showAndWait()
		})
		stage.setScene(new Scene(new StackPane(button), 100, 100))
		stage.show()
	}

	@Override
	void init() throws Exception {
		stage = FxToolkit.registerStage {
			new Stage()
		}
		stage.setTitle('WindowsSpec')
	}

	def 'can find simple stage'() {
		expect:
		Windows.findFocusedStage() is stage
	}

	def 'can find popup'() {
		when:
		clickOn(lookup('click me').query())
		then:
		def s = Windows.findFocusedStage()
		s.title == 'Popup'
	}
}
