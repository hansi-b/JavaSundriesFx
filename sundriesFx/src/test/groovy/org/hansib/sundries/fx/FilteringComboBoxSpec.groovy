package org.hansib.sundries.fx;

import java.util.concurrent.atomic.AtomicBoolean

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
		comboBox = new ComboBox()
		button = new Button("enter")
		return new Scene(new HBox(comboBox, button), 200, 50)
	}

	def 'can later change original items in comboBox'() {

		given:
		def items = comboBox.items
		FilteringComboBox.lcContainsFilter(comboBox).build()

		when:
		items.addAll(['one', 'two'])
		clickOn(comboBox)
		type(KeyCode.DOWN)
		type(KeyCode.ENTER)

		then:
		comboBox.getSelectionModel().getSelectedItem() == 'one'
	}

	def 'can apply string filter'() {

		given:
		comboBox.items.addAll(['one', 'two', 'three'])
		FilteringComboBox.lcContainsFilter(comboBox).build()

		when:
		clickOn(comboBox)
		write('thr')
		type(KeyCode.ENTER)

		then:
		comboBox.getSelectionModel().getSelectedItem() == 'three'
	}

	def 'can apply word set filter'() {

		given:
		comboBox.items.addAll(['one', 'two', 'three'])
		FilteringComboBox.lcAllWordsFilter(comboBox).build()

		when:
		clickOn(comboBox)
		write('t')
		type(KeyCode.SPACE)
		write('ee')
		then:
		comboBox.getEditor().getText() == 't ee'

		when:
		type(KeyCode.ENTER)
		then:
		comboBox.getEditor().getText() == 'three'
		comboBox.getSelectionModel().getSelectedItem() == 'three'
	}

	def 'action on enter is called with string filter'() {

		given:
		comboBox.items.addAll(['one', 'two', 'three'])
		AtomicBoolean wasCalled = new AtomicBoolean(false)

		when:
		FilteringComboBox.lcContainsFilter(comboBox) //
				.withActionOnEnter(() -> wasCalled.set(true)).build()
		clickOn(comboBox)
		type(KeyCode.DOWN)
		type(KeyCode.ENTER)

		then:
		wasCalled.get()
	}
}
