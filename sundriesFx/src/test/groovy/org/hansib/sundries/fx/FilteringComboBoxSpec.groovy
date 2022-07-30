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

	def 'can update items on focus'() {

		given:
		def items = comboBox.getItems();
		new FilteringComboBox(comboBox).withItemsUpdateOnFocus(() -> items).build()

		when:
		clickOn(button)
		['one', 'two'].each { items << it }
		clickOn(comboBox)
		type(KeyCode.ENTER)

		then:
		comboBox.getSelectionModel().getSelectedItem() == 'one'
	}
}
