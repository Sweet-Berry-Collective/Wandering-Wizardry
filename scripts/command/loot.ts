import fennec from "https://oliver-makes-code.github.io/FennecConfig/impl/typescript/mod.ts"

export default function loot(inputFile: string, outputDir: string) {
    const read = fennec.parse(Deno.readTextFileSync(inputFile)) as string[]
    console.log(inputFile+" ->")
    for (const block of read) {
        const output = {
            type: "block",
            pools: [{
                rolls: 1,
                entries: [{
                    type: "item",
                    name: `wwizardry:${block}`
                }],
                conditions: [{
                    condition: "survives_explosion"
                }]
            }]
        }
        const outputFile = outputDir+"/"+block+".json";
        console.log("    "+outputFile)
        Deno.writeTextFileSync(outputFile, JSON.stringify(output))
    }
}
