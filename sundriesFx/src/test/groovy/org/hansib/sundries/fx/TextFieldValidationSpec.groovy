package org.hansib.sundries.fx;

import java.util.concurrent.atomic.AtomicReference

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
		textField = new TextField()
		return new Scene(new HBox(textField, button))
	}

	def 'successful validation allows focus change'() {

		given:
		new TextFieldValidation(textField).build()

		when:
		clickOn(textField)
		write("abc")
		push(KeyCode.TAB)

		then:
		button.focused
	}

	def 'failing default validation forces back focus'() {

		given:
		new TextFieldValidation(textField).build()

		when:
		clickOn(textField) // is empty
		push(KeyCode.TAB)

		then:
		textField.focused
	}

	def 'can use custom validation'() {

		given:
		new TextFieldValidation(textField).withValidation(t -> "x".compareTo(t) < 0).build();

		when:
		clickOn(textField)
		write("u")
		push(KeyCode.TAB)

		then:
		textField.focused

		when:
		push(KeyCode.BACK_SPACE)
		write("y")
		push(KeyCode.TAB)

		then:
		button.focused
	}

	def 'validated callback is called when leaving field'() {

		given:
		AtomicReference<String> result = new AtomicReference("")
		new TextFieldValidation(textField).withValidatedTextCallback(t -> result.set(t)).build();

		when:
		clickOn(textField) // is empty
		write("hello world")
		push(KeyCode.TAB)

		then:
		button.focused
		result.get() == "hello world"
	}

	def 'validated callback is not called on validation failure'() {

		given:
		AtomicReference<String> result = new AtomicReference("")
		new TextFieldValidation(textField).withValidation(t -> "x".compareTo(t) < 0).withValidatedTextCallback(t -> result.set(t)).build();

		when:
		clickOn(textField) // is empty
		write("abc")
		push(KeyCode.TAB)

		then:
		textField.focused
		result.get() == ""
	}

	def 'custom css is applied'() {

		when:
		new TextFieldValidation(textField).withInvalidCssStyleClass("invalid-css").build()

		then:
		textField.getStyleClass().contains("invalid-css")

		when:
		write("x")

		then:
		!textField.getStyleClass().contains("invalid-css")

		when:
		push(KeyCode.BACK_SPACE)

		then:
		textField.getStyleClass().contains("invalid-css")
	}
}
