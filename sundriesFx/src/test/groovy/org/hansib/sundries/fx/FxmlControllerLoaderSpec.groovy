package org.hansib.sundries.fx;

import org.testfx.api.FxToolkit
import org.testfx.framework.spock.ApplicationSpec

import javafx.scene.control.TableView
import javafx.stage.Stage

public class FxmlControllerLoaderSpec extends ApplicationSpec {

	@Override
	public void start(Stage arg0) throws Exception {
		FxToolkit.registerPrimaryStage()
	}

	def 'can load controller'(){

		when:
		LoadTestController controller = new FxmlControllerLoader().loadAndGetController('loadTestController.fxml')
		then:
		controller.testTable.getItems().isEmpty()
	}

	def 'can load controller with callback'(){

		given:
		def loaded = []

		when:
		LoadTestController controller = new FxmlControllerLoader().loadAndGetController('loadTestController.fxml', p -> loaded.add(p))

		then:
		loaded.size() == 1
		loaded[0] instanceof TableView
		controller.testTable.getItems().isEmpty()
	}

	def 'IOException during loading is mapped to IllegalState'(){

		when:
		LoadTestController controller = new FxmlControllerLoader().loadAndGetController('faultyStuff.fxml')
		then:
		def ex = thrown IllegalStateException
		ex.message == "Encountered exception loading 'faultyStuff.fxml'"
	}
}
