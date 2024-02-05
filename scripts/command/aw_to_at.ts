const input = "/src/main/resources/wwizardry.accesswidener";
const output = "/src/generated/resources/META-INF/accesstransformer.cfg"

export default function awToAt(root: string) {
    const aw = Deno.readTextFileSync(root+input)
        .split("\n")
        .filter((it: string, idx) => idx > 0 && !it.startsWith("#"))
        .map(it => it.split(" "))

    let out = ""

    for (const line of aw) {
        out += "\n"
        if (!line[2])
            continue
        let str = "public "
        const prefix = line[2].replaceAll("/", ".")
        switch (line[1]) {
            case "class":
                str += prefix
                break;
            case "field":
                str += `${prefix} ${line[3]}`
                break;
            case "method":
                str += `${prefix} ${line[3]}${line[4]}`
                break;
        }
        out += str
    }
    console.log(root+input + " -> " + root+output)
    Deno.writeTextFileSync(root+output, out.trim())
}
