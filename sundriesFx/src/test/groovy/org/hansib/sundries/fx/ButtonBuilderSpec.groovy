package org.hansib.sundries.fx

import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.stage.Stage

import org.testfx.api.FxToolkit
import org.testfx.framework.spock.ApplicationSpec

public class ButtonBuilderSpec extends ApplicationSpec {

	private Button button

	@Override
	public void start(Stage stage) throws Exception {
		FxToolkit.registerPrimaryStage()
		button = new Button()
	}

	def 'graphic can be set'() {

		given:
		Label label = new Label("label")

		when:
		new ButtonBuilder(button).graphic(label).build()
		then:
		button.getGraphic() === label
	}

	def 'button can be enabled'() {

		when:
		new ButtonBuilder(button).enabled().build()
		then:
		!button.isDisabled()
	}

	def 'button can be disabled'() {

		when:
		new ButtonBuilder(button).disabled().build()
		then:
		button.isDisabled()
	}
}
