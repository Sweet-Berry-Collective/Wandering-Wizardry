import fennec from "https://oliver-makes-code.github.io/FennecConfig/impl/typescript/mod.ts"
import { getAllFiles } from "./common.ts"

type RecuseMap = { [K: string]: string|RecuseMap }

export default function generateLang(inputDir: string, outputDir: string) {
    for (const file of getAllFiles(inputDir)) {
        const inputFile = inputDir+"/"+file+".fennec"
        const outputFile = outputDir+"/"+file+".json"

        const read = fennec.parse(Deno.readTextFileSync(inputFile)) as RecuseMap
        Deno.writeTextFileSync(outputFile, JSON.stringify(recurseToNormal(read)))
        console.log(`${inputFile} -> ${outputFile}`)
    }
}

function recurseToNormal(map: RecuseMap): Record<string, string> {
    const out: Record<string, string> = {}
    for (const key in map) {
        const val = map[key]
        if (typeof val == "string") {
            out[key] = val
            continue
        }
        const normal = recurseToNormal(val)
        for (const nkey in normal) {
            out[`${key}.${nkey}`] = normal[nkey]
        }
    }
    return out
}
