// deno-lint-ignore-file no-empty
import fennec from "https://oliver-makes-code.github.io/FennecConfig/impl/typescript/mod.ts"
import { getAllFiles } from "./common.ts"

type TagData = {
    [path: string]: string[]|TagData
}

export default function generateTags(inputDir: string, outputDir: string) {
    try {
        Deno.mkdirSync(outputDir)
    } catch {}
    for (const file of getAllFiles(inputDir)) {
        const inputFile = inputDir+"/"+file+".fennec"

        const read = fennec.parse(Deno.readTextFileSync(inputFile)) as TagData
        
        try {
            Deno.mkdirSync(outputDir+"/"+file)
            Deno.mkdirSync(outputDir+"/"+file+"/tags")
        } catch {}

        const paths = getPaths(read, outputDir+"/"+file+"/tags")
        console.log(inputFile + " ->")
        for (const path in paths) {
            const outputFile = outputDir+"/"+file+"/tags/"+path+".json"
            const values = paths[path]
            Deno.writeTextFileSync(outputFile, JSON.stringify({replace: false, values}))
            console.log("    "+outputFile)
        }
    }
}

function getPaths(data: TagData, dir: string): Record<string, string[]> {
    try {
        Deno.mkdirSync(dir)
    } catch {}
    const out: Record<string, string[]> = {}

    for (const key in data ) {
        const val = data[key]
        if (Array.isArray(val)) {
            out[key] = val
            continue
        }
        const paths = getPaths(val, dir+"/"+key);
        for (const path in paths) {
            out[`${key}/${path}`] = paths[path]
        }
    }

    return out
}