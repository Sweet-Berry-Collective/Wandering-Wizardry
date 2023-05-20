import { CLI, literal, Builtin, named } from "https://oliver-makes-code.github.io/ts-cli/mod.tsx"
import generateLang from "./command/lang.ts";

const cli = new CLI();

cli.register(
    {
        args: [literal("lang"), named(Builtin.STRING, "Input"), named(Builtin.STRING, "Output")],
        call: generateLang,
        description: "Generates lang files"
    }
)

cli.execute()
