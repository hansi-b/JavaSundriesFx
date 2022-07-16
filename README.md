# JavaFxTemplate
Experimental convenience template for a modular [JavaFX](https://openjfx.io/) [Gradle](https://gradle.org/) project.

Makes use of the magnificent [Badass JLink Plugin](https://badass-jlink-plugin.beryx.org/) for bundle creation. Includes a couple of other useful plug-ins (see `buildSrc`.)

Contains a minimal example "hello world" application which opens a window with a button and logs to the console.

_Note_: Based on the limitations below, I would personally currently not use a modular project.

## Usage

This call should suffice to create an executable bundle:

`./gradlew :fxApp:jpackageImage`

An executable `appFx` should be created in `fxApp/build/jpackage/appFx/bin/`

## Limitations

- Works with the command line and in IntelliJ, but not in Eclipse. (Most likely related to [this problem](https://github.com/eclipse/buildship/issues/658). It seems to work in Eclipse if you remove the `utilities` sub-project and dependency.)
- Tests with Spock may not work entirely due to [this issue](https://github.com/spockframework/spock/issues/1227). Have to include Groovy 4 as an implementation dependency for the tests to run.
