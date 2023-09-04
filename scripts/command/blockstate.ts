import fennec from "https://oliver-makes-code.github.io/FennecConfig/impl/typescript/mod.ts"
import { getAllFiles } from "./common.ts"

type OrArray<T> = T | Array<T>

type X = {x?: number}
type Y = {y?: number}

type ModelPointer = ({
    model: string,
    uvlock?: boolean
} & (X | Y)) | string

type Or = { or?: boolean, and?: undefined }
type And = { or?: undefined, and?: boolean }
type Condition = Record<string, string | boolean | number>

type ConditionalModel = {
    when?: Condition,
    apply: OrArray<ModelPointer>
} & (Or | And)

type StateDef = ConditionalModel|ModelPointer

type BlockState = {
    multipart?: boolean,
    state: OrArray<StateDef>
}

export default function generateBlockStates(inputDir: string, outputDir: string) {
    try {
            Deno.mkdirSync(outputDir)
    } catch {}

    for (const file of getAllFiles(inputDir, outputDir)) {
        const inputFile = inputDir+"/"+file+".fennec"
        const outputFile = outputDir+"/"+file+".json"

        const state: BlockState|OrArray<StateDef> = fennec.parse(Deno.readTextFileSync(inputFile))

        const transformed = transformBlockState(state)

        Deno.writeTextFileSync(outputFile, JSON.stringify(transformed))

        console.log(inputFile + " -> " + outputFile)
    }
}

function transformBlockState(blockstate: BlockState|OrArray<StateDef>): Record<string, any> {
    if (blockstate.hasOwnProperty("state")) {
        blockstate = blockstate as BlockState
        if (blockstate.multipart)
            return transformMultipart(blockstate.state)
        return transformVariants(blockstate.state)
    }
    return transformVariants(blockstate as OrArray<StateDef>)
}

function iterate<T>(state: OrArray<T>, consumer: (state: T) => void) {
    if (Array.isArray(state)) {
        for (let i of state) {
            consumer(i)
        }
    } else {
        consumer(state)
    }
}

function transformVariants(state: OrArray<StateDef>): Record<string, any> {
    let out: Record<string, any> = {}

    function add(name: string, value: any) {
        if (out[name]) out[name].push(value)
        else out[name] = [value]
    }

    iterate(state, state => {
        if (typeof state == "string") {
            add("", {
                model: state
            })
        } else if (state.hasOwnProperty("model")) {
            add("", state)
        } else {
            let cond = state as ConditionalModel
            let str = ""
            if (cond.when) {
                for (let i in cond.when) {
                    str = `${str},${i}=${cond.when[i]}`
                }
                str = str.substring(1)
            }

            iterate(cond.apply, model => {
                if (typeof model == "string") {
                    add(str, {
                        model: model
                    })
                } else {
                    add(str, model)
                }
            })
        }
    })

    for (let k in out) {
        if (out[k].length == 1) {
            out[k] = out[k][0]
        }
    }

    return {
        "variants": out
    }
}

function transformMultipart(state: OrArray<StateDef>): Record<string, any> {
    let out: any[] = []

    iterate(state, state => {
        if (typeof state == "string") {
            out.push({
                apply: {
                    model: state
                }
            })
        } else if (state.hasOwnProperty("model")) {
            out.push({
                apply: state
            })
        } else {
            let cond = state as ConditionalModel
            let when: {[k:string]: any} = {}
            if (cond.when) {
                if (cond.or) {
                    when.OR = cond.when
                } else if (cond.and) {
                    when.AND = cond.when
                } else {
                    when = cond.when
                }
            }
            let apply: any[] = []

            iterate(cond.apply, model => {
                if (typeof model == "string") {
                    apply.push({
                        model: model
                    })
                } else {
                    apply.push(model)
                }
            })

            if (apply.length == 1) {
                out.push({when, apply: apply[0]})
            } else if (apply.length != 0) {
                out.push({when, apply})
            }
        }
    })

    return {
        "multipart": out
    }
}
