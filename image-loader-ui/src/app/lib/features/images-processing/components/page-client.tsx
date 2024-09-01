'use client'

import { Fragment, useState } from "react";
import { IQueueDetailApiResponse, IQueuedTask } from "../types/definitions";
import TaskForm, { IFormEvent } from "./task-form";
import QueuedTasksList from "./queued-tasks-list";
import Countdown from "./countdown";

export default function PageClient() {
    const [jobs, setJobs] = useState<IQueuedTask[]>([]);

    const [verifying, setVerifying] = useState(false);

    const addQueueJob = async (task: IFormEvent) => {
        const { file, taskType, width, height } = task;

        const data = new FormData();
        data.append('file', file);
        data.append('type', taskType.toString());
        data.append('width', width.toString());
        data.append('height', height.toString());

        const result = await fetch('/api', { method: 'POST', body: data });

        if (!result.ok) {
            alert('Operation did not succeed');
            return;
        }

        const resultData = await result.json();

        setJobs([
            ...jobs,
            {
                uuid: resultData.uuid,
                pending: true,
                result: {
                    status: false,
                    errors: null,
                    filename: null
                }
            }
        ]);
    };

    const checkQueuedJobs = async () => {
        const pendings = jobs.filter(el => el.pending)

        if (pendings.length === 0) {
            console.debug('No jobs to check');
            return;
        }

        setVerifying(true);
        console.debug('Queue check verifying');

        try {

            const data = await fetchQueue();

            const dataMap: Map<string, IQueuedTask> = new Map(data.data.map(el => [el.uuid, el]));

            setJobs(prevJobs =>
                prevJobs.map(el => {
                    if (!el.pending) {
                        return el;
                    }
                    if (!dataMap.has(el.uuid)) {
                        return el;
                    }
                    return dataMap.get(el.uuid)!;
                })
            );

        } catch (error) {
            console.error('Error checking job queue:', error);
        } finally {
            setVerifying(false);
        }
    };

    const fetchQueue = async () => {
        const query = jobs.filter(el => el.pending).map(el => el.uuid).join(',');
        const result = await fetch(`/api?uuids=${query}`);

        if (!result.ok) {
            throw new Error('Failed to fetch job statuses : ' + result.status.toString());
        }

        const data = (await result.json()) as IQueueDetailApiResponse;

        return data;
    }

    const fileDownload = async (filename: string) => {
        try {
            const res = await fetch(`/api/download?file=${filename}`)

            if (!res.ok) {
                throw new Error('File download failed');
            }

            const blob = await res.blob();

            const url = window.URL.createObjectURL(blob);

            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', filename);

            document.body.appendChild(link);
            link.click();

            document.body.removeChild(link);
            window.URL.revokeObjectURL(url);
        } catch (err) {
            console.error('Error downloading file:', err);
            alert('Failed to download file');
        }

    }


    return (
        <div className="grid grid-cols-2 gap-8">
            <div>
                <TaskForm onSubmit={addQueueJob} />
            </div>
            <div>
                <QueuedTasksList
                    items={jobs}
                    onFileDownload={fileDownload}
                    queueInfo={
                        <Fragment>
                            {
                                jobs.filter(el => el.pending).length > 0 &&
                                <QueueStatusRetry
                                    retrying={verifying}
                                    retryFunction={checkQueuedJobs} />
                            }
                        </Fragment>
                    } />
            </div>
        </div>
    );
}

const QueueStatusRetry = ({ retrying, retryFunction }: { retrying: boolean, retryFunction: () => Promise<any> }) => {
    if (retrying) {
        return <p>Loading...</p>;
    }

    return (
        <div className="flex">Fetching queue again in
            <Countdown from={5} onCountDownEnd={retryFunction} />
            seconds again
        </div>
    )
}