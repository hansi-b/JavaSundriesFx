package org.hansib.sundries.fx.table

import static javafx.collections.FXCollections.observableArrayList
import static org.hamcrest.MatcherAssert.assertThat
import static org.testfx.matcher.control.TableViewMatchers.hasTableCell

import javafx.scene.Scene
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.MapValueFactory
import javafx.scene.input.KeyCode
import javafx.util.converter.DefaultStringConverter

import org.hansib.sundries.fx.AppSpecWithScene

public class EditingCellSpec extends AppSpecWithScene {

	private DefaultStringConverter stringConverter = new DefaultStringConverter()

	private TableView tableView

	@Override
	protected Scene createScene() {

		Map row = new HashMap<>(2)
		row.put('name', 'alice')

		tableView = new TableView()
		tableView.setEditable(true)
		tableView.setItems(observableArrayList(row))

		def nameCol = new TableColumn('name')
		tableView.getColumns().setAll(nameCol)

		nameCol.setCellValueFactory(new MapValueFactory<>('name'))
		nameCol.setCellFactory(e -> new EditingCell(stringConverter, (c, s) -> s != null && !s.isEmpty()))
		nameCol.prefWidthProperty().bind(tableView.widthProperty().multiply(.95))
		nameCol.setEditable(true)

		return new Scene(tableView, 100, 80)
	}

	def 'can edit cell'() {

		when:
		def cell = lookup(".table-cell").nth(0).query()

		doubleClickOn(cell)
		clickOn(cell)
		write('charly')
		type(KeyCode.ENTER)

		then:
		assertThat(tableView, hasTableCell('charly'))
	}
}
