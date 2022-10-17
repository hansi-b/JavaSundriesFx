package org.hansib.sundries.fx;

import java.time.Duration;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class LoadTestController {

	public static class MyRow {
		// placeholder
	}

	@FXML
	private TableColumn<MyRow, Duration> firstCol;

	@FXML
	TableView<MyRow> testTable;

	@FXML
	void initialize() {
		testTable.setItems(FXCollections.observableArrayList());
	}
}
