package org.hansib.sundries.fx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;

public class ButtonDecorator {
	private Button button;

	public ButtonDecorator(Button button) {
		this.button = button;
	}

	public ButtonDecorator graphic(Node graphic) {
		button.setGraphic(graphic);
		return this;
	}

	public ButtonDecorator onAction(EventHandler<ActionEvent> handler) {
		button.setOnAction(handler);
		return this;
	}

	public ButtonDecorator disabled() {
		button.setDisable(true);
		return this;
	}

	public ButtonDecorator enabled() {
		button.setDisable(false);
		return this;
	}
}