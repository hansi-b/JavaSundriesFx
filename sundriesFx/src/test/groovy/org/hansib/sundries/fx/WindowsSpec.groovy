package org.hansib.sundries.fx

import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.Button
import javafx.scene.layout.StackPane

public class WindowsSpec extends AbstractAppSpec {

	Button button

	@Override
	public Scene createScene() {
		button = new Button('click me');
		button.setOnAction(actionEvent -> {
			def popup = new Alert(AlertType.INFORMATION)
			popup.setTitle('Popup')
			popup.showAndWait()
		})
		return new Scene(new StackPane(button))
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
