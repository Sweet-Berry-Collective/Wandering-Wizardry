import fennec from "https://oliver-makes-code.github.io/FennecConfig/impl/typescript/mod.ts"
import { getAllFiles } from "./common.ts"

type ArchExFennec = {
    kind: "brick"|"wood"
    name?: string
    id: string
    color: string
    stem?: boolean
}

type ArchExJson = {
    name?: string
    base_block: string
    textures: string
    recipes: string
    map_color: string
    types_to_generate: string[]
}

export default function generateArchEx(inputDir: string, outputDir: string) {
    for (const file of getAllFiles(inputDir, outputDir)) {
        const inputFile = inputDir+"/"+file+".fennec"
        const outputFile = outputDir+"/"+file+".json"

        const read = fennecToJson(fennec.parse(Deno.readTextFileSync(inputFile)))
        Deno.writeTextFileSync(outputFile, JSON.stringify(read))

        console.log(inputFile + " -> " + outputFile)
    }
}

function fennecToJson(fennec: ArchExFennec): ArchExJson {
    if (fennec.kind == "brick") {
        return {
            name: fennec.name,
            base_block: fennec.id,
            textures: fennec.id.replace(":", ":block/"),
            recipes: "stonecutting",
            map_color: fennec.color,
            types_to_generate: [
                "arch",
                "octagonal_column",
                "round_arch",
                "roof",
                "wall_column",
                "wall_post",
                "facade",
                "rod",
            ]
        }
    }
    return {
        name: fennec.name,
        base_block: fennec.id,
        textures: fennec.stem ? "wood_with_stem" : "wood_with_log",
        recipes: "sawing",
        map_color: fennec.color,
        types_to_generate: [
            "facade",
            "round_fence_post",
            "round_arch",
            "octagonal_column",
            "beam",
            "fence_post",
            "joist",
            "crown_molding",
            "post_cap",
            "post_lantern",
            "lattice",
            "transom",
        ]
    }
}
