package org.hansib.sundries.fx

import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.Button
import javafx.scene.control.ButtonType
import javafx.stage.Stage

import org.testfx.api.FxToolkit
import org.testfx.framework.spock.ApplicationSpec

public class AlertBuilderSpec extends ApplicationSpec {

	@Override
	public void start(Stage stage) throws Exception {
		FxToolkit.registerPrimaryStage()
	}

	def 'can build simple confirmation dialogue'() {

		given:
		Alert ab = null;
		when:
		interact(() -> {
			ab = new AlertBuilder(AlertType.CONFIRMATION, 'Hello World') //
				.withButton(ButtonType.OK, 'indeed').build()
		})

		then:
		ab.alertType == AlertType.CONFIRMATION
		ab.contentText == 'Hello World'
		ab.buttonTypes == [ButtonType.OK]
		ab.getDialogPane().lookupButton(ButtonType.OK).text == 'indeed'
	}

	def 'can set default button'() {

		when:
		Alert ab = null;
		interact(() -> {
			ab = new AlertBuilder(AlertType.CONFIRMATION, 'Hello World') //
				.withButton(ButtonType.PREVIOUS, 'previous')
				.withDefaultButton(ButtonType.NEXT, 'next')
				.withButton(ButtonType.CANCEL, 'cancel').build()
		})

		then:
		Button b = (Button) ab.getDialogPane().lookupButton(ButtonType.NEXT)
		b.isDefaultButton()
	}
}
