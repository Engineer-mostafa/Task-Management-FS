export interface ITask {
    "id": number,
    "name": string,
    "description": string,
    "taskCategory": string,
    "taskStatus": string,
    "deadline": Date,
    "userId": number,
    "project_id": number
}
