import 'rxjs/Rx';
import {Injectable} from "@angular/core";
import {Observable} from "rxjs/Observable";
import 'rxjs/add/observable/of';
import {HttpClient} from "@angular/common/http";

import { Headers, Http, RequestOptions, Response } from '@angular/http';

import { Car, CarExpands } from './Car-model';
import { ODataResult, Odata2Service } from './tools/odata-tools';

@Injectable()
export class CarODataService {

  protected baseUrl : string = Car.collectionName;

  constructor(private odataService2: Odata2Service, private http: HttpClient) { }

  public getCars(): Observable<any[]> {
    let cars: any[] = [{id: 1, name: "Eins"}, {id: 2, name: "Zwei"}];
    // this.http()
    return Observable.of(cars);
  }

  public findCars(expandOptions?: CarExpands, filterOptions?: string, count: boolean = false): Observable<ODataResult<Car>> {
    return this.odataService2.getEntityCollection(this.baseUrl, Car.parse, expandOptions, filterOptions, count);
  }

  public findCarsMitSearchOptions(pFilter, pSorting, pPageNum, pItemsPerPage, expandOptions?: CarExpands ): Observable<ODataResult<Car>> {
    return this.odataService2.getEntityCollectionWithSearchOptions(this.baseUrl, Car.parse, pFilter, pSorting, pPageNum, pItemsPerPage, expandOptions);
  }

  public findAllCars() : Observable<ODataResult<Car>>{
    return this.findCars();
  }

  public findCarById(id: number): Observable<Car> {
    return this.odataService2.getEntityById(this.baseUrl, id, Car.parse);
  }

  public saveCar(pCar: Car): Observable<any> {
    return this.odataService2.saveEntity(this.baseUrl, pCar, Car.parse);
  }

  public deleteCar(pCar: Car): Observable<any> {
    return this.odataService2.deleteEntity(this.baseUrl, pCar);
  }
}

