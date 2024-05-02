import { IUser } from "./iUser";

export interface ILoginResponse {
    "access_token": string,
    "refresh_token": string,
    "user": IUser

}