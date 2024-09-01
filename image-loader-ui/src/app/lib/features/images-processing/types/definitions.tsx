
export enum QueuedTaskStatus {
    PENDING, SUCCESS, ERROR
}
export interface IQueuedTask {
    uuid: string,
    pending: boolean,
    result: IQueuedTaskDetail | null,
}

export interface IQueuedTaskDetail {
    status: boolean,
    filename: string | null,
    errors: string | null
}

export interface IQueueDetailApiResponse {
    data: IQueuedTask[]
}

export enum TaskTypeEnum {
    RESIZE, WATERMARK
}


