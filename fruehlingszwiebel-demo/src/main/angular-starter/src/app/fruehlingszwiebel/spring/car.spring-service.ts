import 'rxjs/Rx';
import {Injectable} from "@angular/core";
import {Observable} from "rxjs/Observable";
import 'rxjs/add/observable/of';
import {HttpClient} from "@angular/common/http";

import { Headers, Http, RequestOptions, Response } from '@angular/http';

import { Car, CarExpands } from './Car-model';
import { ODataResult, Odata2Service } from './tools/odata-tools';

@Injectable()
export class CarSpringService {

  protected baseUrl: string = "http://localhost:8080/api/car";

  constructor(private http: HttpClient) {
  }

  public getCars(): Observable<any> {
    return this.http.get(this.baseUrl);
  }
}
