import fennec from "https://oliver-makes-code.github.io/FennecConfig/impl/typescript/mod.ts"
import { getAllFiles } from "./common.ts"

export default function generateBrickRecipes(inputDir: string, outputDir: string) {
    try {
        Deno.mkdirSync(outputDir)
    } catch {}
    for (const file of getAllFiles(inputDir, outputDir)) {
        const inputFile = inputDir+"/"+file+".fennec"

        const recipe: BrickRecipe = fennec.parse(Deno.readTextFileSync(inputFile))

        console.log(inputFile + " -> ")
        getRecipe(recipe, file, outputDir)
    }
}

function getRecipe(recipe: BrickRecipe, file: string, outputDir: string) {
    try {
        Deno.mkdirSync(outputDir+"/"+file)
    } catch {}
    for (const generator of recipe.generators) {
        try {
            Deno.mkdirSync(outputDir+"/"+file+"/"+generator.name)
        } catch {}
        const json = Generator[generator.type](recipe, generator)
        for (const key in json) {
            const outputFile = outputDir+"/"+file+"/"+generator.name+"/"+key+".json"
            Deno.writeTextFileSync(outputFile, JSON.stringify(json[key]))
            console.log("    "+outputFile)
        }
    }
}

type BrickRecipe = {
    block: string
    slab: string
    stairs: string
    wall: string
    generators: GeneratorData[]
}

type GeneratorData = {
    name: string
    type: GeneratorType
    [k: string]: unknown
}

type GeneratorType = keyof typeof Generator

const Generator = {
    from_self_cut(recipe: BrickRecipe, _generator: GeneratorData): { [k in string]: unknown } {
        return {
            slab: {
                type: "stonecutting",
                count: 2,
                ingredient: { item: recipe.block },
                result: recipe.slab
            },
            stairs: {
                type: "stonecutting",
                count: 1,
                ingredient: { item: recipe.block },
                result: recipe.stairs
            },
            wall:  {
                type: "stonecutting",
                count: 1,
                ingredient: { item: recipe.block },
                result: recipe.wall
            }
        }
    },
    from_self_crafted(recipe: BrickRecipe, _generator: GeneratorData): { [k in string]: unknown } {
        return {
            slab: {
                type: "crafting_shaped",
                category: "building",
                show_notification: true,
                key: {
                    "#": { item: recipe.block }
                },
                pattern: [
                    "###"
                ],
                result: {
                    count: 6,
                    item: recipe.slab
                }
            },
            stiars: {
                type: "crafting_shaped",
                category: "building",
                show_notification: true,
                key: {
                    "#": { item: recipe.block }
                },
                pattern: [
                    "#  ",
                    "## ",
                    "###"
                ],
                result: {
                    count: 4,
                    item: recipe.stairs
                }
            },
            wall: {
                type: "crafting_shaped",
                category: "building",
                show_notification: true,
                key: {
                    "#": { item: recipe.block }
                },
                pattern: [
                    "###",
                    "###"
                ],
                result: {
                    count: 6,
                    item: recipe.wall
                }
            }
        }
    },
    from_item_cut(recipe: BrickRecipe, generator: GeneratorData): { [k in string]: unknown } {
        return {
            block: {
                type: "stonecutting",
                count: 1,
                ingredient: { item: generator.item },
                result: recipe.block
            },
            slab: {
                type: "stonecutting",
                count: 2,
                ingredient: { item: generator.item },
                result: recipe.slab
            },
            stairs: {
                type: "stonecutting",
                count: 1,
                ingredient: { item: generator.item },
                result: recipe.stairs
            },
            wall:  {
                type: "stonecutting",
                count: 1,
                ingredient: { item: generator.item },
                result: recipe.wall
            }
        }
    },
    from_item_crafted(recipe: BrickRecipe, generator: GeneratorData): { [k in string]: unknown } {
        const key = generator.item ? { item: generator.item } : { tag: generator.tag }

        return {
            block: {
                type: "crafting_shaped",
                category: "building",
                show_notification: true,
                key: {
                    "#": key
                },
                pattern: generator.pattern,
                result: {
                    item: recipe.block,
                    count: generator.count
                }
            }
        }
    },
    from_mossed_item(recipe: BrickRecipe, generator: GeneratorData): { [k in string]: unknown } {
        const key = generator.item ? { item: generator.item } : { tag: generator.tag }
        return {
            block: {
                type: "crafting_shapeless",
                category: "building",
                show_notification: true,
                ingredients: [
                    key,
                    { tag: "wwizardry:mossy_materials" }
                ],
                result: {
                    item: recipe.block,
                    count: 1
                }
            }
        }
    }
} as const
