
import { EntityStatus, checkChanged, Entity, splitODataBind, splitODataBindList, DateConverter } from './tools/angular-tools';



export class Tire extends Entity {

	public static collectionName : string = 'Tires';

	constructor(id?: number, version?: number) { super(id, version); }

	private _tireType: string = null;
	private _mileAge: number = null;

	   // TODO Korrekt machen fürs Berechtigungskonzept
	   // @Visible(forRoles = [Kommunenuser, Vereinsuser])
	   	get tireType() { return this._tireType; }
	   	@checkChanged()
	   	set tireType(tireType: string) { this._tireType = tireType; }
	   // TODO Korrekt machen fürs Berechtigungskonzept
	   // @Visible(forRoles = [Kommunenuser, Vereinsuser])
	   	get mileAge() { return this._mileAge; }
	   	@checkChanged()
	   	set mileAge(mileAge: number) { this._mileAge = mileAge; }

	public equalTo(pTire: Tire) {
		if(!this.id)
		{
			return this === pTire;
		}
		return this.id === pTire.id;
	}

	static parseArrayOfTires(jsonArray: any) : Array<Tire>
	{
		return Entity.parseArray(jsonArray, Tire.parse);
	}

	static parse(json: any): Tire {
		if(json === undefined) { return undefined; }
		if(json === null) { return null; }
		   let createdObject = new Tire(json['id'], json['version']);
		   delete json['id'];
		   delete json['version'];
		   return Object.assign(createdObject, json,
		   {
		   },
		   {
		   		// Status ist loaded nach dem Parsing
		   		_entityStatus : EntityStatus.LOADED
		   	});
	}

	public toJSON(): Tire {
		return Object.assign({}, this, {
		   });
	}

	public asODataJSON() : string {
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
			tireType: this.tireType,
			mileAge: this.mileAge
		});

		// Convert relations to OData-Binds or Inline-Entites

		// Convert Enum-Values
		Object.assign(odataObject,
		{
		});

		return odataObject;
	}

	// Update entityStatus abhängig von den Status der Child-Objekte, damit nested Änderungen nicht zu einem odata@bind an den Parent-Relationen führen
	public updateEntityStatus() {
		if (this._entityStatus == EntityStatus.CHANGED) { return; }
	}
}

export class TireExpands {
}
