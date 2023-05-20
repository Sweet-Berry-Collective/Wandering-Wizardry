export const fennec_file = /\.fennec$/i

export function getAllFiles(dir: string): string[] {
    const out: string[] = []
    for (const file of Deno.readDirSync(dir)) {
        if (!file.isFile) continue
        if (!file.name.match(fennec_file)) continue
        out.push(file.name.replace(fennec_file, ""))
    }
    return out;
}
