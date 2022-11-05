package org.hansib.sundries.fx;

import java.util.concurrent.atomic.AtomicBoolean
import java.util.function.Function
import java.util.function.Predicate

import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.input.KeyCode
import javafx.scene.layout.HBox

public class FilteringComboBoxSpec extends AbstractAppSpec {

	ComboBox comboBox
	Button button

	@Override
	protected Scene createScene() {
		button = new Button("enter")
		comboBox = new ComboBox()
		return new Scene(new HBox(comboBox, button), 100,40)
	}

	def 'can later change original items from comboBox'() {

		given:
		def items = comboBox.items
		new FilteringComboBox(comboBox).build()

		when:
		['one', 'two'].each {
			items << it
		}
		clickOn(comboBox)
		type(KeyCode.ENTER)

		then:
		comboBox.getSelectionModel().getSelectedItem() == 'one'
	}

	Function<String, Predicate<String>> containsMatcher = selection -> new Predicate<String>() {
		@Override
		public boolean test(String value) {
			return value.contains(selection);
		}
	}

	def 'can set string filter'() {

		given:
		['one', 'two', 'three'].each {
			comboBox.items << it
		}

		new FilteringComboBox(comboBox).withStringFilterBuilder(containsMatcher).build()

		when:
		clickOn(comboBox)
		write('thr')
		type(KeyCode.ENTER)

		then:
		comboBox.getSelectionModel().getSelectedItem() == 'three'
	}

	def 'action on enter is called without string filter'() {

		given:
		['one', 'two', 'three'].each {
			comboBox.items << it
		}
		AtomicBoolean wasCalled = new AtomicBoolean(false)

		when:
		new FilteringComboBox(comboBox).withActionOnEnter(() -> wasCalled.set(true)).build()
		clickOn(comboBox)
		type(KeyCode.DOWN)
		type(KeyCode.ENTER)

		then:
		wasCalled.get()
	}

	def 'action on enter is called with string filter'() {

		given:
		['one', 'two', 'three'].each {
			comboBox.items << it
		}
		AtomicBoolean wasCalled = new AtomicBoolean(false)

		when:
		new FilteringComboBox(comboBox).withStringFilterBuilder(containsMatcher)//
				.withActionOnEnter(() -> wasCalled.set(true)).build()
		clickOn(comboBox)
		write('thr')
		type(KeyCode.ENTER)

		then:
		wasCalled.get()
	}
}
