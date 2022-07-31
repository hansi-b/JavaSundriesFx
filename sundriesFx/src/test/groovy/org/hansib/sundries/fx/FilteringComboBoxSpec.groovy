package org.hansib.sundries.fx;

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
		['one', 'two'].each { items << it }
		clickOn(comboBox)
		type(KeyCode.ENTER)

		then:
		comboBox.getSelectionModel().getSelectedItem() == 'one'
	}
}
