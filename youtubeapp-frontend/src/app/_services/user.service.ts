import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {User} from "../_models/user";
import {map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  register(user: User){
    const data = JSON.stringify(user);
    const config = { headers: new HttpHeaders().set('Content-Type', 'application/json') };
    return this.http.post(`http://localhost:8080/users/register`, data, config);
  }
}
