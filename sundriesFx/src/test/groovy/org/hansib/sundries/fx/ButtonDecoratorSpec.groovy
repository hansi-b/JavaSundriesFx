package org.hansib.sundries.fx;

import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label

public class ButtonDecoratorSpec extends AbstractAppSpec {

	Button button
	//Label label

	@Override
	protected Scene createScene() {
		button = new Button()

		return new Scene(button, 100,40)
	}

	def 'graphic can be set'() {

		given:
		Label label = new Label("label")

		when:
		waitForAsyncFx(() -> {
			new ButtonDecorator(button).graphic(label)
		})
		then:
		button.getGraphic() === label
	}

	def 'button can be enabled'() {

		when:
		new ButtonDecorator(button).enabled()
		then:
		!button.isDisabled()
	}

	def 'button can be disabled'() {

		when:
		new ButtonDecorator(button).disabled()
		then:
		button.isDisabled()
	}
}
