import fennec from "https://oliver-makes-code.github.io/FennecConfig/impl/typescript/mod.ts"

type RecuseMap<T> = { [K: string]: T|RecuseMap<T> }

const fennec_file = /\.fennec$/i

export default function generateLang(inputDir: string, outputDir: string) {
    for (const file of Deno.readDirSync(inputDir)) {
        if (!file.isFile) continue
        if (!file.name.match(fennec_file)) continue

        const inputFile = inputDir+"/"+file.name
        const outputFile = (outputDir+"/"+file.name).replace(fennec_file, ".json")

        const read = fennec.parse(Deno.readTextFileSync(inputFile)) as RecuseMap<string>
        Deno.writeTextFileSync(outputFile, JSON.stringify(recurseToNormal(read)))
        console.log(`${inputFile} -> ${outputFile}`)
    }
}

function recurseToNormal(map: RecuseMap<string>): Record<string, string> {
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
