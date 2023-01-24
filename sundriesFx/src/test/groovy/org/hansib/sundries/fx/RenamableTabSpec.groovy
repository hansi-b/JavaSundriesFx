package org.hansib.sundries.fx

import javafx.scene.Scene
import javafx.scene.control.TabPane
import javafx.scene.input.KeyCode

public class RenamableTabSpec extends AbstractAppSpec {

	private TabPane tabPane
	private RenamableTab renamableTab

	@Override
	protected Scene createScene() {
		tabPane = new TabPane()

		renamableTab = new RenamableTab('first text')
		waitForAsyncFx(() -> {
			tabPane.getTabs().add(renamableTab.getTab())
		})
		return new Scene(tabPane, 120, 40)
	}

	def 'can add renamable tab'() {

		expect:
		tabPane.getTabs()[0].graphic.text == 'first text'
	}

	def 'can edit tab text'() {

		when:
		doubleClickOn(queryNthTab(0))
		write('Scooby Doo').type(KeyCode.ENTER)

		then:
		tabPane.getTabs()[0].graphic.text == 'Scooby Doo'
	}

	/**
	 * from https://github.com/TestFX/TestFX/issues/634
	 *
	 * @param idx the zero-based index of the tab to be selected
	 * @return the selected project tab - actually only the skin class
	 */
	def queryNthTab(idx) {
		lookup(".tab-pane > .tab-header-area > .headers-region > .tab").nth(idx).query()
	}
}
