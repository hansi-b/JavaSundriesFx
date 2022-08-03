package org.hansib.sundries.fx;

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
		return new Scene(new HBox(comboBox, button))
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

	def 'can set string filter'() {

		given:
		def items = comboBox.items

		Function<String, Predicate<String>> containsMatcher = selection -> new Predicate<String>() {
			@Override
			public boolean test(String value) {
				return value.contains(selection);
			}
		}

		new FilteringComboBox(comboBox).withStringFilterBuilder(containsMatcher).build()

		when:
		['one', 'two', 'three'].each {
			items << it
		}
		clickOn(comboBox)
		write('thr')
		type(KeyCode.ENTER)

		then:
		comboBox.getSelectionModel().getSelectedItem() == 'three'
	}
}
