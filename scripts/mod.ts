import { CLI, literal, Builtin, named } from "https://oliver-makes-code.github.io/ts-cli/mod.tsx"
import generateLang from "./command/lang.ts";
import genericTranspile from "./command/transpile.ts";
import generateTags from "./command/tags.ts";
import loot from "./command/loot.ts";
import generateArchEx from "./command/archex.ts"

const cli = new CLI();

cli.register(
    {
        args: [literal("lang"), named(Builtin.STRING, "Input"), named(Builtin.STRING, "Output")],
        call: generateLang,
        description: "Generates lang files"
    },
    {
        args: [literal("tags"), named(Builtin.STRING, "Input"), named(Builtin.STRING, "Output")],
        call: generateTags,
        description: "Generates tag files"
    },
    {
        args: [literal("transpile"), named(Builtin.STRING, "Input"), named(Builtin.STRING, "Output")],
        call: genericTranspile,
        description: "Transpiles and copies a dir to another"
    },
    {
        args: [literal("loot"), named(Builtin.STRING, "Input"), named(Builtin.STRING, "Output")],
        call: loot,
        description: "Generates simple loot tables for a list of blocks"
    },
    {
        args: [literal("archex"), named(Builtin.STRING, "Input"), named(Builtin.STRING, "Output")],
        call: generateArchEx,
        description: "Generates arch-ex staticdata"
    }
)

cli.execute()
