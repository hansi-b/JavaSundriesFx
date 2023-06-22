package org.hansib.sundries.fx

import javafx.scene.Scene
import javafx.scene.control.Label

public class StylerSpec extends AbstractAppSpec {

	private Label styleable

	private Styler styler

	@Override
	protected Scene createScene() {
		styleable = new Label()
		return new Scene(styleable, 120, 40)
	}

	def 'can add styleclass'() {

		given:
		styler = new Styler(styleable)

		expect:
		styler.add('woodstock') == true
		styleable.getStyleClass().contains('woodstock')
	}

	def 'styleclass is not added twice'() {

		given:
		styler = new Styler(styleable).with('woodstock')

		expect:
		styler.add('woodstock') == false
	}

	def 'can remove styleclass'() {

		given:
		styleable.getStyleClass().add('woodstock')
		styler = new Styler(styleable)

		expect:
		styler.remove('woodstock') == true
		!styleable.getStyleClass().contains('woodstock')
	}

	def 'remove removes multiple styleclass instances'() {

		given:
		styleable.getStyleClass().add('woodstock')
		styleable.getStyleClass().add('woodstock')
		styleable.getStyleClass().add('woodstock')
		styler = new Styler(styleable)

		expect:
		styler.remove('woodstock') == true
		!styleable.getStyleClass().contains('woodstock')
	}

	def 'test adds style on true test'() {

		given:
		styler = new Styler(styleable)

		when:
		styler.addOrRemove(true, 'woodstock')

		then:
		styleable.getStyleClass().contains('woodstock')
	}

	def 'test removes style on false test'() {

		given:
		styler = new Styler(styleable).with('woostock')

		when:
		styler.addOrRemove(false, 'woodstock')

		then:
		!styleable.getStyleClass().contains('woodstock')
	}

	def 'test toggles style on true test'() {

		given:
		styler = new Styler(styleable).with('other')

		when:
		styler.ifOneElseOther(true, 'one', 'other')

		then:
		styleable.getStyleClass().contains('one')
		!styleable.getStyleClass().contains('other')
	}

	def 'test toggles style on false test'() {

		given:
		styler = new Styler(styleable).with('one')

		when:
		styler.ifOneElseOther(false, 'one', 'other')

		then:
		!styleable.getStyleClass().contains('one')
		styleable.getStyleClass().contains('other')
	}
}
