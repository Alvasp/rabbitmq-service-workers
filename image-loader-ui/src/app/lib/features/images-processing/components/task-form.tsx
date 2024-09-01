'use client'

import classNames from "classnames";
import { FormEvent, useState } from "react";
import { TaskTypeEnum } from "../types/definitions";

interface IResolutions {
  width: number,
  height: number
}

export interface IFormEvent {
  file: File,
  taskType: TaskTypeEnum,
  width: number,
  height: number
}

interface IFormModel extends Partial<Pick<IFormEvent, 'file' | 'taskType'>> {
  resolutionIndex?: number
}

const availableResolutions: IResolutions[] = [
  { width: 640, height: 480 },
  { width: 800, height: 600 },
  { width: 1024, height: 768 },
  { width: 1920, height: 1080 }
]

export type TaskFormProps = {
  onSubmit: (task: IFormEvent) => Promise<any>
}

export default function TaskForm({ onSubmit }: TaskFormProps) {
  const [values, setValues] = useState<IFormModel>({})
  const [errors, setErrors] = useState<string[]>([]

  )

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();

    const _errors: string[] = [];
    if (values.file === undefined) {
      _errors.push('file')
    }
    if (values.resolutionIndex === undefined) {
      _errors.push('resolutionIndex')
    }
    if (values.taskType === undefined) {
      _errors.push('taskType')
    }

    if (_errors.length > 0) {
      setErrors(_errors)
      return;
    }

    setErrors([]);
    const resolution = availableResolutions.at(values.resolutionIndex!);

    await onSubmit({
      file: values.file!,
      width: resolution!.width,
      height: resolution!.height,
      taskType: values.taskType!
    })
  }

  const handleClear = () => {
    setValues({})
    setErrors([])
  }

  return (
    <div className="bg-white p-8 rounded-lg shadow-md w-full max-w-md">
      <h2 className="text-2xl font-bold mb-3">Add Images</h2>
      <p className="mb-6">Add some images using the form below and then wait for the status update in the box in the right.</p>

      <form action="#" method="post" encType="multipart/form-data" onSubmit={handleSubmit}>
        <div className="mb-4">
          <label htmlFor="file"
            className="block font-medium text-gray-700">Select File</label>
          <input
            accept="image/jpg, image/jpeg"
            onChange={(event) => {
              setValues({ ...values, file: event.target.files?.item(0) ?? undefined })
            }}
            type="file"
            id="file"
            name="file"
            className={classNames("mt-1 block w-full border border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500",
              { "invalid": errors.includes("file") })}
          />
        </div>

        <div className="mb-4">
          <label htmlFor="type"
            className="block font-medium text-gray-700">Transformation Type</label>
          <select
            value={values.taskType?.toString() || ""}
            onChange={(event) => {
              const { value } = event.target;

              const _values = { ...values, taskType: value ? value as unknown as TaskTypeEnum : undefined };

              setValues(_values)
            }}
            id="type" name="type" className={classNames("mt-1 block w-full border border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500",
              { "invalid": errors.includes("taskType") })} >
            <option value={""} >Select type</option>
            {Object.values(TaskTypeEnum).filter(el => typeof el === "string")
              .map(el => <option
                key={el}
                value={el}
                className="capitalize">{el}</option>
              )}
          </select>
        </div>

        <div className="mb-4">
          <label htmlFor="resolution"
            className={"block  font-medium text-gray-700"}>Resolution</label>
          <select
            value={values.resolutionIndex ?? ""}
            onChange={(event) => {
              const { value } = event.target;
              setValues({ ...values, resolutionIndex: value !== undefined ? Number(value) : undefined })
            }}
            id="resolution"
            name="resolution"
            className={classNames("mt-1 block w-full border border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500 ",
              { "invalid": errors.includes("resolutionIndex") })}>
            <option value={""} >Select resolution</option>
            {availableResolutions.map((el, idx) => <option key={idx} value={idx}>{`${el.width}x${el.height}`}</option>)}
          </select>
        </div>

        <div className="flex justify-between">
          <button type="button"
            onClick={handleClear}
            className="px-4 py-2 bg-slate-200	rounded-md shadow-sm hover:bg-slate-400 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-grey-500 ">Clear</button>
          <button type="submit"
            className="px-4 py-2 bg-blue-500 text-white rounded-md shadow-sm hover:bg-blue-600 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500">Submit</button>
        </div>
      </form >
    </div>
  )
}