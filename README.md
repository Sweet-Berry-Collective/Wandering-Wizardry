# Wandering Wizardry
An exploration-focused Minecraft mod.

## This mod uses [FennecConfig](https://github.com/Oliver-makes-code/FennecConfig)
FennecConfig is a config language designed around readability and user friendliness.

During the `build` task, it transpiles certain fennec files in the `data` directory to JSON,
in order for them to load in Minecraft

## Contributing
If you're contributing translations or tags, make sure to use the `data` directory.

Data there is converted from [FennecConfig](https://github.com/Oliver-makes-code/FennecConfig) to JSON at compiletime.

## Building the mod
To build the mod, you need a JDK >= 17, as well as [Deno](https://deno.com/runtime)

Everything should be done using the `./gradlew build` command.
