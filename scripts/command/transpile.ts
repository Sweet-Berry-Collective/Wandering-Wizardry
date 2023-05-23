import fennec from "https://oliver-makes-code.github.io/FennecConfig/impl/typescript/mod.ts"
import { getAllFiles } from "./common.ts"

export default function genericTranspile(inputDir: string, outputDir: string) {
    for (const file of getAllFiles(inputDir, outputDir)) {
        const inputFile = inputDir+"/"+file+".fennec"
        const outputFile = outputDir+"/"+file+".json"

        const read = fennec.parse(Deno.readTextFileSync(inputFile))
        Deno.writeTextFileSync(outputFile, JSON.stringify(read))

        console.log(inputFile + " -> " + outputFile)
    }
}
