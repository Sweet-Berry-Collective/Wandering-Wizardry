// deno-lint-ignore-file no-empty
export const fennec_file = /\.fennec$/i

export function getAllFiles(dir: string, walk?: string): string[] {
    if (walk) try {
        Deno.mkdirSync(walk)
    } catch {}
    const out: string[] = []
    for (const file of Deno.readDirSync(dir)) {
        if (!file.isFile && !walk) continue
        if (walk && file.isDirectory) {
            try {
                Deno.mkdirSync(walk+"/"+file.name)
            } catch {}
            for (const name of getAllFiles(dir+"/"+file.name, walk+"/"+file.name)) {
                out.push(file.name+"/"+name)
            }
            continue
        }
        if (!file.name.match(fennec_file)) continue
        out.push(file.name.replace(fennec_file, ""))
    }
    return out;
}
