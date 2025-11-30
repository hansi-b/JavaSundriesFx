package org.hansib.sundries.fx.table

import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.TableColumn.CellEditEvent
import javafx.stage.Stage
import org.testfx.api.FxToolkit
import org.testfx.framework.spock.ApplicationSpec

public class TableColumnBuilderSpec extends ApplicationSpec {

	def existingCol = new TableColumn('existing')

	@Override
	public void start(Stage stage) throws Exception {
		FxToolkit.registerPrimaryStage()
	}

	def 'header text set via constructor'() {
		when:
		def col = new TableColumnBuilder('name').build()

		then:
		col.getText() == 'name'
	}

	def 'header text set via headerText method'() {

		when:
		def col = new TableColumnBuilder(existingCol).headerText('updated').build()

		then:
		col.getText() == 'updated'
	}

	def 'editable is set via editable'() {

		when:
		def col = new TableColumnBuilder(existingCol).editable().build()

		then:
		col.isEditable()
	}

	def 'onEditCommit sets handler and editable true'() {
		given:
		EventHandler<CellEditEvent> handler = { e -> } as EventHandler

		when:
		def col = new TableColumnBuilder(existingCol).onEditCommit(handler).build()

		then:
		col.getOnEditCommit() == handler
		col.isEditable()
	}
}
