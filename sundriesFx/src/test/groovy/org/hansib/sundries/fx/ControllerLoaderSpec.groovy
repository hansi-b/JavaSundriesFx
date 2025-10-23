package org.hansib.sundries.fx


import javafx.stage.Stage

import org.testfx.api.FxToolkit
import org.testfx.framework.spock.ApplicationSpec
import org.testfx.util.WaitForAsyncUtils

class ControllerLoaderSpec extends ApplicationSpec {

	@Override
	void start(Stage stage) throws Exception {
		FxToolkit.registerPrimaryStage()
	}

	def 'can load controller'() {

		when:
		LoadTestController controller = ControllerLoader.<LoadTestController> of('loadTestController.fxml')
			.load()
		then:
		controller.testTable.getItems().isEmpty()
	}

	def 'can load controller to stage'() {

		given:
		Stage targetStage
		WaitForAsyncUtils.waitForAsyncFx(1_000, () -> targetStage = new Stage())

		expect:
		targetStage != null
		targetStage.getScene() == null

		when:
		WaitForAsyncUtils.waitForAsyncFx(1_000, () -> ControllerLoader.<LoadTestController> of('loadTestController.fxml')
			.withTargetStage(targetStage).load())

		then:
		targetStage.getScene() != null
	}

	def 'IOException during loading is mapped to IllegalState'() {

		when:
		ControllerLoader.<LoadTestController> of('faultyStuff.fxml').load()
		then:
		def ex = thrown IllegalStateException
		ex.message == "Encountered exception loading 'faultyStuff.fxml'"
	}
}
