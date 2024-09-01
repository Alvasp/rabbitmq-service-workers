'use client'

import { Fragment, ReactNode } from "react"
import { IQueuedTask } from "../types/definitions"

export type QueuedTasksListProps = {
    queueInfo?: ReactNode,
    items: IQueuedTask[],
    onFileDownload: (filename: string) => Promise<void>

}

export default function QueuedTasksList({ items, onFileDownload, queueInfo }: QueuedTasksListProps) {
    return (
        <div className="bg-white p-8 rounded-lg shadow-md w-full max-w-md">
            <h2 className="text-2xl font-bold mb-6">Queued Tasks</h2>
            {queueInfo}
            <nav className="flex min-w-[240px] flex-col gap-1 p-2 font-sans text-base font-normal text-blue-gray-700">
                {
                    items.map((el) => <div key={el.uuid} role="button border-slate-300 border-b-4 border-indigo-500"
                        className="flex flex-col w-full p-3 leading-tight transition-all rounded-lg outline-none text-start hover:bg-blue-gray-50 hover:bg-opacity-80 hover:text-blue-gray-900 focus:bg-blue-gray-50 focus:bg-opacity-80 focus:text-blue-gray-900 active:bg-blue-gray-50 active:bg-opacity-80 active:text-blue-gray-900">
                        <div className="flex mb-1">
                            <b className="mr-3">UUID</b>
                            <p>{el.uuid}</p>
                        </div>

                        <div className="flex mb-1">
                            <b className="mr-3">STATUS</b>
                            <p>{el.pending ? 'pending...' : el.result?.status === true ? 'success' : 'failure'}</p>
                        </div>

                        <div className="flex mb-1">
                            {!el.pending ?
                                <Fragment>
                                    <b className="mr-3">Result</b>
                                    {el.result?.status === true && el.result.filename ?
                                        <a href="#"
                                            onClick={async () => {
                                                await onFileDownload(el.result!.filename!)
                                            }}
                                            className="underline text-blue-600 hover:text-blue-800 visited:text-purple-600">{el.result.filename}</a>
                                        : <code>{el.result?.errors}</code>}
                                </Fragment>
                                : null}
                        </div>
                    </div>)
                }
            </nav>
        </div>
    )
}