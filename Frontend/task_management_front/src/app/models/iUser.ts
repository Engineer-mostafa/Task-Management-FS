import { IRole } from "./iRole";
import { IProject } from "./iproject";

export interface IUser {
    "id": number,
    "name": string,
    "email": string,
    "password": string,
    "project": IProject,
    "role" : IRole
}
