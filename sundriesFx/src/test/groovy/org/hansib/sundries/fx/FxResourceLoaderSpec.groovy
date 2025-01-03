package org.hansib.sundries.fx

import javafx.scene.control.TableView
import javafx.stage.Stage

import org.testfx.api.FxToolkit
import org.testfx.framework.spock.ApplicationSpec

public class FxResourceLoaderSpec extends ApplicationSpec {

	@Override
	public void start(Stage stage) throws Exception {
		FxToolkit.registerPrimaryStage()
	}

	def 'can load controller'() {

		when:
		LoadTestController controller = new FxResourceLoader().loadFxmlAndGetController('loadTestController.fxml')
		then:
		controller.testTable.getItems().isEmpty()
	}

	def 'can load controller with callback'() {

		given:
		def loaded = []

		when:
		LoadTestController controller = new FxResourceLoader().loadFxmlAndGetController('loadTestController.fxml', p -> loaded.add(p))

		then:
		loaded.size() == 1
		loaded[0] instanceof TableView
		controller.testTable.getItems().isEmpty()
	}

	def 'IOException during loading is mapped to IllegalState'() {

		when:
		LoadTestController controller = new FxResourceLoader().loadFxmlAndGetController('faultyStuff.fxml')
		then:
		def ex = thrown IllegalStateException
		ex.message == "Encountered exception loading 'faultyStuff.fxml'"
	}
}
