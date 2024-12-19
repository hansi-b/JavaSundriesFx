package org.hansib.sundries.fx

import javafx.stage.Stage

import org.testfx.api.FxToolkit
import org.testfx.framework.spock.ApplicationSpec

public class ContextMenuBuilderSpec extends ApplicationSpec {

	@Override
	public void start(Stage stage) throws Exception {
		FxToolkit.registerPrimaryStage()
	}

	def 'can build context menu'() {

		given:
		def cmb = new ContextMenuBuilder()
		cmb.item("Hello", e -> {
			println "yes"
		})

		when:
		def cm = cmb.build()

		then:
		cm.getItems().size() == 1
		cm.getItems().get(0).getText() == 'Hello'
	}


	def 'building without items throws exception'() {

		given:
		def cmb = new ContextMenuBuilder()

		when:
		def cm = cmb.build()

		then:
		def ex = thrown IllegalStateException
		ex.message == 'Cannot build context menu without menu items.'
	}
}
