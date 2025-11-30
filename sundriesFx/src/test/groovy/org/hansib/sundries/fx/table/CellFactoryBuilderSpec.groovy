package org.hansib.sundries.fx.table

import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn
import javafx.util.Callback
import javafx.stage.Stage
import org.testfx.api.FxToolkit
import org.testfx.framework.spock.ApplicationSpec

public class CellFactoryBuilderSpec extends ApplicationSpec {

	private TableColumn tableColumn = new TableColumn('existing')

	@Override
	public void start(Stage stage) throws Exception {
		FxToolkit.registerPrimaryStage()
	}

	def 'formatter produces FormattingTableCell text'() {
		given:
		Callback<TableColumn<Object, String>, TableCell<Object, String>> defaultFactory = { c -> new TableCell<Object, String>() } as Callback
		def factory = new CellFactoryBuilder(defaultFactory).format({ s -> 'fmt-' + s }).build()

		when:
		def cell = factory.call(tableColumn)
		cell.updateItem('value', false)

		then:
		cell.getText() == 'fmt-value'
	}

	def 'withDragSelection sets drag handlers'() {
		given:
		Callback<TableColumn<Object, String>, TableCell<Object, String>> defaultFactory = { c -> new TableCell<Object, String>() } as Callback
		def factory = new CellFactoryBuilder(defaultFactory).withDragSelection().build()

		when:
		def cell = factory.call(tableColumn)

		then:
		cell.getOnDragDetected() != null
		cell.getOnMouseDragEntered() != null
	}
}
