import {Tire, TireExpands} from './Tire-model';
import {Wheel, WheelExpands} from './Wheel-model';

import {
  EntityStatus,
  checkChanged,
  Entity,
  splitODataBind,
  splitODataBindList,
  DateConverter
} from './tools/angular-tools';


export class Car extends Entity {

  public static collectionName: string = 'Cars';

  constructor(id?: number, version?: number) {
    super(id, version);
  }

  private _identificationNumber: string = null;
  private _wheels: Array<Wheel> = new Array<Wheel>();
  private _tires: Array<Tire> = new Array<Tire>();

  // TODO Korrekt machen fürs Berechtigungskonzept
  // @Visible(forRoles = [Kommunenuser, Vereinsuser])
  get identificationNumber() {
    return this._identificationNumber;
  }

  @checkChanged()
  set identificationNumber(identificationNumber: string) {
    this._identificationNumber = identificationNumber;
  }

  get wheels() {
    return this._wheels;
  }

  @checkChanged()
  set wheels(wheels: Array<Wheel>) {
    this._wheels = wheels;
  }

  get tires() {
    return this._tires;
  }

  @checkChanged()
  set tires(tires: Array<Tire>) {
    this._tires = tires;
  }

  public equalTo(pCar: Car) {
    if (!this.id) {
      return this === pCar;
    }
    return this.id === pCar.id;
  }

  static parseArrayOfCars(jsonArray: any): Array<Car> {
    return Entity.parseArray(jsonArray, Car.parse);
  }

  static parse(json: any): Car {
    if (json === undefined) {
      return undefined;
    }
    if (json === null) {
      return null;
    }
    let createdObject = new Car(json['id'], json['version']);
    delete json['id'];
    delete json['version'];
    return Object.assign(createdObject, json,
      {
        wheels: Wheel.parseArrayOfWheels(json.wheels),
        tires: Tire.parseArrayOfTires(json.tires)
      },
      {
        // Status ist loaded nach dem Parsing
        _entityStatus: EntityStatus.LOADED
      });
  }

  public toJSON(): Car {
    return Object.assign({}, this, {});
  }

  public asODataJSON(): string {
    return JSON.stringify(this.asODataObject());
  }

  public asODataObject(): any {
    let odataObject = Object.assign({}, {
      id: this.id,
      version: this.version
      // _entityStatus has to be removed for odata
    });
    // Copy primitives
    Object.assign(odataObject,
      {
        identificationNumber: this.identificationNumber
      });

    // Convert relations to OData-Binds or Inline-Entites
    Object.assign(odataObject, splitODataBindList("wheels", this.wheels, Wheel.collectionName));
    Object.assign(odataObject, splitODataBindList("tires", this.tires, Tire.collectionName));

    // Convert Enum-Values
    Object.assign(odataObject,
      {});

    return odataObject;
  }

  // Update entityStatus abhängig von den Status der Child-Objekte, damit nested Änderungen nicht zu einem odata@bind an den Parent-Relationen führen
  public updateEntityStatus() {
    if (this._entityStatus == EntityStatus.CHANGED) {
      return;
    }
    if (this.wheels) {
      for (let e of this.wheels) {
        if (EntityStatus.CHANGED == e._entityStatus) {
          this._entityStatus = EntityStatus.CHANGED;
          return;
        }
      }
    }
    if (this.tires) {
      for (let e of this.tires) {
        if (EntityStatus.CHANGED == e._entityStatus) {
          this._entityStatus = EntityStatus.CHANGED;
          return;
        }
      }
    }
  }
}

export class CarExpands {
  wheels: WheelExpands = null;

  public expandWheels(nestedExpand ?: (e: WheelExpands) => void): CarExpands {
    this.wheels = new WheelExpands();
    if (nestedExpand) {
      nestedExpand(this.wheels);
    }
    return this;
  }

  tires: TireExpands = null;

  public expandTires(nestedExpand ?: (e: TireExpands) => void): CarExpands {
    this.tires = new TireExpands();
    if (nestedExpand) {
      nestedExpand(this.tires);
    }
    return this;
  }
}
