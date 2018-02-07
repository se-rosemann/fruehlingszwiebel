import * as moment from 'moment/moment';
import { EnumMetaData } from './enum-metadata';

/**
 * Enum, das genutzt wird um den Status einer Entity zu beschreiben
 * Je nach Status kann können Verlinkungen wie OData-Bind gesetzt werden
 */
export enum EntityStatus {
  CREATED,
  LOADED,
  CHANGED
}

export function checkChanged() {
  return function (target, propertyKey, descriptor) {
    let setterMethod: any = descriptor.set;
    let getterMethod: any = descriptor.get;
    descriptor.set = function (descriptor) {
      let oldValue = getterMethod.apply(this);
      let newValue = arguments[0];
      if (newValue !== oldValue) {
        // Status wird auf CHANGED gesetzt, falls die Entity nicht CREATED war
        // CREATED impliziert, dass geändert wird
        if (this._entityStatus !== EntityStatus.CREATED) {
          this._entityStatus = EntityStatus.CHANGED;
        }
      }
      return setterMethod.apply(this, arguments);
    }
  };
}

export function splitODataBindList<E extends Entity>(attributeName: string, entities: Array<E>, collectionName: string): any {
  if (entities === undefined) { return undefined; }
  if (entities === null) { return null; }
  let bindings: Array<string> = new Array;
  let inlineEntities: Array<E> = new Array;
  entities.forEach((entity: E) => {
    // Checken, ob der status passt, bevor die binds gesetzt werden
    entity.updateEntityStatus();

    if (EntityStatus.LOADED == entity._entityStatus) {
      bindings.push(collectionName + '(' + entity.id + ')');
    }
    else {
      inlineEntities.push(entity.asODataObject());
    }
  })
  let result: any = {};
  result[attributeName + "@odata.bind"] = bindings;
  result[attributeName] = inlineEntities;
  return result;
}

export function splitODataBind<E extends Entity>(attributeName: string, entity: E, collectionName: string): any {
  if (entity === undefined) { return undefined; }
  if (entity === null) { return null; }

  // Checken, ob der status passt, bevor die binds gesetzt werden
  entity.updateEntityStatus();

  let result: any = {};
  if (EntityStatus.LOADED == entity._entityStatus) {
    result[attributeName + "@odata.bind"] = collectionName + '(' + entity.id + ')';
    return result;
  }
  else {
    result[attributeName] = entity.asODataObject();
    return result;
  }
}

export abstract class Entity {
  public _enumMetaData : EnumMetaData = new EnumMetaData;

  public _entityStatus: EntityStatus = EntityStatus.CREATED;

  private _id: number = null;
  private _version: number = null;

  constructor(id?: number, version?: number) {
    if (id !== undefined) {
      this._id = id;
    }
    if (version !== undefined) {
      this._version = version;
    }
  }

  get id() { return this._id; }
  get version() { return this._version; }

  public abstract asODataJSON(): string;
  public abstract asODataObject(): any;

  // Generische Methode zum Aktualisieren des Entity-Status anhand der Status der Childs
  public abstract updateEntityStatus();

  static mapValue<I, O>(input: I, mapFunction: (i: I) => O) {
    if (input === undefined) { return undefined; }
    if (input === null) { return null; }
    return mapFunction(input);
  }

  static parseArray<T>(jsonArray: any, singleEntryParseFunction: (json: string) => T): Array<T> {
    return Entity.mapArray<any,T>(jsonArray, singleEntryParseFunction);
  }

  static mapArray<I, O>(inputArray: Array<I>, entryMapFunction: (i: I) => O): Array<O> {
    if (inputArray === undefined) { return undefined; }
    if (inputArray === null) { return null; }
    return inputArray.map(entryMapFunction);
  }
}

export class DateConverter {
  public static SERVER_DATEFORMAT = 'YYYY-MM-DD';

  constructor() { }

  static getMomentFromString(dateString: string): moment.Moment {
    if(!dateString)
    {
      return null;
    }
    const ret = moment(dateString, DateConverter.SERVER_DATEFORMAT);
    if(!ret.isValid())
    {
      return null;
    }
    return ret;
  }

  static parseMomentForServer(theMoment : moment.Moment)
  {
    if(theMoment === undefined) { return undefined; }
    if(theMoment === null) { return null; }
    return theMoment.format(DateConverter.SERVER_DATEFORMAT);
  }
}

export function objectEquals(x, y) {
  // if both are function
  if (x instanceof Function) {
    if (y instanceof Function) {
      return x.toString() === y.toString();
    }
    return false;
  }
  if (x === null || x === undefined || y === null || y === undefined) { return x === y; }
  if (x === y || x.valueOf() === y.valueOf()) { return true; }

  // if one of them is date, they must had equal valueOf
  if (x instanceof Date) { return false; }
  if (y instanceof Date) { return false; }

  // if they are not function or strictly equal, they both need to be Objects
  if (!(x instanceof Object)) { return false; }
  if (!(y instanceof Object)) { return false; }

  var p = Object.keys(x);
  return Object.keys(y).every(function (i) { return p.indexOf(i) !== -1; }) ?
    p.every(function (i) { return objectEquals(x[i], y[i]); }) : false;
}

export function format(string: String, ...args: any[]): String {
  return string.replace(/{(\d+)}/g, function (match, number) {
    return typeof args[number] != 'undefined'
      ? args[number]
      : match
      ;
  });
}
