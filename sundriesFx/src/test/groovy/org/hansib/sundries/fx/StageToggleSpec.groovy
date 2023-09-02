package org.hansib.sundries.fx;

import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import spock.lang.IgnoreIf

public class StageToggleSpec extends AppSpecWithScene {

	private Button button

	private Stage stage
	private StageToggle toggle

	@Override
	protected Scene createScene() {
		toggle = new StageToggle(() -> {
			stage = new Stage()
		})
		button = new Button('toggle')
		button.setOnAction(e -> {
			toggle.toggle()
		})

		StackPane pane = new StackPane()
		pane.children.add(button)
		return new Scene(pane, 120, 40)
	}

	def 'toggle can show stage'() {

		when:
		clickOn(button)
		then:
		stage.isShowing()
	}

	@IgnoreIf({ Boolean.valueOf(System.properties['testfx.headless']) })
	def 'toggle can hide stage'() {

		when:
		clickOn(button)
		clickOn(button)
		then:
		!stage.isShowing()
	}
}
