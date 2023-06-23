package org.hansib.sundries.fx;

import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference

import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.input.KeyCode
import javafx.scene.layout.HBox

public class ValidatingTextFieldBuilderSpec extends AbstractAppSpec {

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
		new ValidatingTextFieldBuilder(textField).build()

		when:
		clickOn(textField)
		write("abc")
		push(KeyCode.TAB)

		then:
		button.focused
	}

	def 'failing default validation forces back focus'() {

		given:
		new ValidatingTextFieldBuilder(textField).build()

		when:
		clickOn(textField) // is empty
		push(KeyCode.TAB)

		then:
		textField.focused
	}

	def 'successful validation executes existing action handler'() {

		given:
		AtomicInteger executed = new AtomicInteger(0)
		textField.setOnAction(e -> executed.getAndIncrement())
		new ValidatingTextFieldBuilder(textField).withValidation(s -> 'abc'.equals(s)) build()

		when:
		clickOn(textField)
		write('abc')
		push(KeyCode.ENTER)

		then:
		textField.focused
		executed.get() == 1
	}

	def 'failing validation does not execute existing action handler'() {

		given:
		AtomicInteger executed = new AtomicInteger(0)
		textField.setOnAction(e -> executed.getAndIncrement())
		new ValidatingTextFieldBuilder(textField).withValidation(s -> 'abc'.equals(s)) build()

		when:
		clickOn(textField)
		write('x')
		push(KeyCode.ENTER)

		then:
		textField.focused
		executed.get() == 0
	}

	def 'can use custom validation'() {

		given:
		new ValidatingTextFieldBuilder(textField).withValidation(t -> "x".compareTo(t) < 0).build()

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
		AtomicReference<String> result = new AtomicReference('')
		new ValidatingTextFieldBuilder(textField).withValidatedTextCallback(t -> result.set(t)).build()

		when:
		clickOn(textField) // is empty
		write('hello world')
		push(KeyCode.TAB)

		then:
		button.focused
		result.get() == 'hello world'
	}

	def 'validated callback is called before existing action handler'() {

		given:
		AtomicReference<String> result = new AtomicReference('')

		// append to result string:
		textField.setOnAction(e -> result.set(result.get() + 'onAction'))

		new ValidatingTextFieldBuilder(textField).withValidatedTextCallback(t -> result.set(result.get() + t)).build()

		when:
		clickOn(textField) // is empty
		write('-hello world-')
		push(KeyCode.ENTER)

		then:
		textField.focused
		result.get() == '-hello world-onAction'
	}

	def 'validated callback is not called on validation failure'() {

		given:
		AtomicReference<String> result = new AtomicReference("")
		new ValidatingTextFieldBuilder(textField).withValidation(t -> 'x'.compareTo(t) < 0).withValidatedTextCallback(t -> result.set(t)).build()

		when:
		clickOn(textField) // is empty
		write('abc')
		push(KeyCode.TAB)

		then:
		textField.focused
		result.get() == ''
	}

	def 'custom css is applied'() {

		when:
		new ValidatingTextFieldBuilder(textField).withInvalidCssStyleClass('invalid-css').build()

		then:
		textField.getStyleClass().contains('invalid-css')

		when:
		write("x")

		then:
		!textField.getStyleClass().contains('invalid-css')

		when:
		push(KeyCode.BACK_SPACE)

		then:
		textField.getStyleClass().contains('invalid-css')
	}
}
