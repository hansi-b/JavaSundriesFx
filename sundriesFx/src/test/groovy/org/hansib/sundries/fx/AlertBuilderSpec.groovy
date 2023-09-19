
package org.hansib.sundries.fx

import org.testfx.api.FxToolkit
import org.testfx.framework.spock.ApplicationSpec

import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.ButtonType
import javafx.stage.Stage

public class AlertBuilderSpec extends ApplicationSpec {

	@Override
	public void start(Stage stage) throws Exception {
		FxToolkit.registerPrimaryStage()
	}

	def 'can build simple confirnmation dialogue'() {

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
}
