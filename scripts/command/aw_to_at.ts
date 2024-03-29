const input = "/src/main/resources/wwizardry.accesswidener";
const output = "/src/main/resources/accesstransformer.cfg"

const base_url = "https://linkieapi.shedaniel.me/api/search?namespace=mojang_srg"

export default async function awToAt(inputDir: string, outputDir: string, version: string) {
    const url = base_url + "&version="+version
    const aw = Deno.readTextFileSync(inputDir+input)
        .split("\n")
        .filter((it: string, idx) => idx > 0 && !it.startsWith("#"))
        .map(it => it.split(" "))

    let out = ""

    for (const line of aw) {
        out += "\n"
        if (!line[2])
            continue
        let str = `public${line[1] == "field" ? "-f" : ""} `
        const prefix = line[2].replaceAll("/", ".")
        let add = ""
        switch (line[1]) {
            case "class":
                str += prefix
                break;
            case "method":
                add += line[4];
                // Falls through
            case "field": {
                let name = line[3]

                if (line[3] != "<init>") {
                    const query = url + "&query=" + line[3];
                    console.log(query)
                    const data = (await (await fetch(query)).json()).entries.filter((it: any) => it.c == line[2])[0]
                    if (!data)
                        break
                    name = data.i;
                    if (!name)
                        break
                }

                str += `${prefix} ${name}${add}`
            }
        }
        out += str
    }
    console.log(inputDir+input + " -> " + outputDir+output)
    Deno.writeTextFileSync(outputDir+output, out.trim())
}
