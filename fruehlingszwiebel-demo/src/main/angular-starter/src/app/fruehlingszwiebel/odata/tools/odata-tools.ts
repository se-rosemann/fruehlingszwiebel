import { Configuration } from './../../../app/configuration/configuration.model';
import { ConfigurationService } from './../../../app/configuration/configuration.service';
import { Entity } from './angular-tools';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';

import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import {HttpClient} from "@angular/common/http";


/**
 * Datenklasse für das Result eines OData-Collection-Get
 */
export class ODataResult<T>
{
  "@odata.context": string;
  "@odata.count": number;
  value: Array<T>;

  public count(): number {
    return this["@odata.count"];
  }

  // List-Parsing
  static parse<V>(json: any, valueParseFunction: (valueJson: any) => V): ODataResult<V> {
    let result = Object.create(ODataResult.prototype);
    var jsonList : Array<Object> = json.value;
    let valueList : Array<V> = jsonList.map(valueParseFunction);
    return Object.assign(result, json,
      {
        value: valueList
      });
  }
}

/**
 * Generische Service-Implementierung für den Zugriff auf OData-Endpoints
 */
@Injectable()
export class Odata2Service {

  private backendUrl: String = "http://localhost:8080/api";

  private defaultRequestOptions: any = { withCredentials: true };

  constructor(private http: HttpClient) {
  }

  public static expandOptionsToString(expandOptions: any, depth?: number): String {
    if (!expandOptions) { return null; }
    if (!depth) { depth = 0; }
    // Wir erlauben maximal 10 als Tiefe
    else if (depth > 10) { console.log("Maximale Tiefe für nested expands auf 10 festgelegt!!"); return null; }
    let expands: Array<string> = new Array<string>();
    for (let key of Object.keys(expandOptions)) {
      if (expandOptions[key]) {
        let expandOption = key;
        let nestedExpand: String = this.expandOptionsToString(expandOptions[key], depth + 1);
        if (nestedExpand !== null) {
          expandOption += "(" + nestedExpand + ")";
        }
        expands.push(expandOption);
      }
    }

    if (expands.length > 0) {
      return "$expand=" + expands.join()
    }
    return null;
  }

  // collectionPath ist relativ ohne Angabe der backendUrl
  // Bsp. getEntity('Personen', Person.parse)
  public getEntityCollection<T>(collectionName: string, valueParseFunction: (valueJson: any) => T, expandOptions?: any, filterOptions?: string, count: boolean = false): Observable<ODataResult<T>> {
    let expandOptionsAsString: String = Odata2Service.expandOptionsToString(expandOptions);
    let countAsString = count ? '$count=true' : null
    let parameters = new Array(expandOptionsAsString, filterOptions, countAsString).filter(s => s && s.length > 0).join('&');
    let url = collectionName;
    if (parameters && parameters.length > 0) { url += '?' };
    url += parameters;
    return this.getAndParseEntityCollection(url, valueParseFunction);
  }

  public getEntityCollectionWithSearchOptions<T>(collectionName: string, valueParseFunction: (valueJson: any) => T, pFilter, pSorting, pPageNum, pItemsPerPage, expandOptions?: any): Observable<ODataResult<T>> {
    const expandOptionsAsString: String = Odata2Service.expandOptionsToString(expandOptions);
    const expand = `${expandOptionsAsString}`;
    const skip = `\$skip=${(pPageNum - 1) * pItemsPerPage}`;
    const top = `\$top=${pItemsPerPage}`;

    let url = collectionName + '?$count=true&' + expand + '&' + skip + '&' + top + (pFilter ? '&' + pFilter : '') + (pSorting ? '&' + pSorting : '');

    return this.getAndParseEntityCollection(url, valueParseFunction);
  }

  private getAndParseEntityCollection<T>(collectionPath: string, valueParseFunction: (valueJson: any) => T): Observable<ODataResult<T>> {
    return this.http.get(this.getUrl(collectionPath), this.enrichRequestOptions()).map((response: any) => {
      // Json-Body ...
      let json: any = response.json();
      // ... versuchen wir zu einem OData-Result zu parsen
      let odataResult: ODataResult<T> = ODataResult.parse<T>(json, valueParseFunction);
      return odataResult;
    });
  }

  // Bsp. getEntityById('Personen(3)', Person.parse)
  public getEntityById<T>(collectionPath: string, id: number, valueParseFunction: (valueJson: any) => T, expandOptions ?: any): Observable<T> {
    let url = `${collectionPath}(${id})` + (expandOptions ? '?'+Odata2Service.expandOptionsToString(expandOptions) : '');
    return this.getAndParseEntity(url, valueParseFunction);
  }

  private getAndParseEntity<T>(entityPath: string, valueParseFunction: (valueJson: any) => T): Observable<T> {
    return this.http.get(this.getUrl(entityPath), this.enrichRequestOptions()).map((response: any) => response.json()).map((json: any) => {
      // ... wird direkt zur TargetEntity geparsed
      let entityFromJson: T = valueParseFunction(json);
      return entityFromJson;
    });
  }

  public saveEntity<E extends Entity>(collectionPath: string, entity: E, valueParseFunction: (valueJson: any) => E): Observable<any> {
    let url = entity.id ? `${collectionPath}(${entity.id})` : `${collectionPath}`;
    const headers = new Headers({ 'Content-Type': 'application/json' });
    const options = { headers: headers };
    let entityAsODataJSON = entity.asODataJSON();
    if (entity.id) {
      return this.updateEntity(collectionPath, entity.id, entityAsODataJSON, valueParseFunction, options);
    }
    else {
      return this.createEntity(collectionPath, entityAsODataJSON, valueParseFunction, options);
    }
  }

  private createEntity<T>(collection: string, body: any, valueParseFunction: (valueJson: any) => T, options?: any): Observable<T> {
    return this.http.post(this.getUrl(collection), body, this.enrichRequestOptions(options)).map((response: any) => response.json()).map((json: any) => {
      // ... wird direkt zur TargetEntity geparsed
      let entityFromJson: T = valueParseFunction(json);
      return entityFromJson;
    });
  }

  private updateEntity<T>(collection: string, entityId: number, body: any, valueParseFunction: (valueJson: any) => T, options?: any): Observable<T> {
    if (!entityId) { throw new Error('EntityId must be set.') }
    return this.http.put(this.getUrl(collection) + '(' + entityId + ')', body, this.enrichRequestOptions(options)).map((response: any) => response.json()).map((json: any) => {
      // ... wird direkt zur TargetEntity geparsed
      let entityFromJson: T = valueParseFunction(json);
      return entityFromJson;
    });
  }

  public deleteEntity<E extends Entity>(collection: string, entity : E, options?: any) : Observable<any> {
    let entityId = entity.id;
    return this.http.delete(this.getUrl(collection) + '(' + entityId + ')', this.enrichRequestOptions(options))
  }

  private getUrl(path: string): string {
    if (this.backendUrl.endsWith('/') && path.startsWith('/')) { return (this.backendUrl + path.substr(1)); }
    else if (this.backendUrl.endsWith('/') || path.startsWith('/')) { return (this.backendUrl + path); }
    else { return (this.backendUrl + '/' + path); }
  }

  private enrichRequestOptions(requestOptions?: any): any {
    if (!requestOptions) {
      requestOptions = {};
    }
    return Object.assign(requestOptions, this.defaultRequestOptions);
  }
}

