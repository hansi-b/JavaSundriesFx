package org.hansib.sundries.fx;

import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.input.KeyCode
import javafx.scene.layout.HBox

public class TextFieldValidationSpec extends AbstractAppSpec {

	TextField textField
	Button button

	@Override
	protected Scene createScene() {
		button = new Button("enter")
		textField = new TextFieldValidation().build()
		return new Scene(new HBox(textField, button))
	}

	def 'successful validation allows focus change'() {

		when:
		clickOn(textField)
		write("abc")
		push(KeyCode.TAB)

		then:
		button.focused
	}

	def 'failing default validation forces back focus'() {

		when:
		clickOn(textField) // is empty
		push(KeyCode.TAB)

		then:
		textField.focused
	}
}
