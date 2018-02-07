import { WheelPosition} from './WheelPosition-model';

import { EntityStatus, checkChanged, Entity, splitODataBind, splitODataBindList, DateConverter } from './tools/angular-tools';



export class Wheel extends Entity {

	public static collectionName : string = 'Wheels';

	constructor(id?: number, version?: number) { super(id, version); }

private _wheelPosition: WheelPosition = null;

	   get wheelPosition() { return this._wheelPosition; }
	   @checkChanged()
	   set wheelPosition(wheelPosition: WheelPosition) { this._wheelPosition = wheelPosition; }

	public equalTo(pWheel: Wheel) {
		if(!this.id)
		{
			return this === pWheel;
		}
		return this.id === pWheel.id;
	}

	static parseArrayOfWheels(jsonArray: any) : Array<Wheel>
	{
		return Entity.parseArray(jsonArray, Wheel.parse);
	}

	static parse(json: any): Wheel {
		if(json === undefined) { return undefined; }
		if(json === null) { return null; }
		   let createdObject = new Wheel(json['id'], json['version']);
		   delete json['id'];
		   delete json['version'];
		   return Object.assign(createdObject, json,
		   {
		     	wheelPosition: Entity.mapValue(json.wheelPosition, e => WheelPosition[e])
		   },
		   {
		   		// Status ist loaded nach dem Parsing
		   		_entityStatus : EntityStatus.LOADED
		   	});
	}

	public toJSON(): Wheel {
		return Object.assign({}, this, {
		   	wheelPosition: Entity.mapValue(this.wheelPosition, e => WheelPosition[e])
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
		});

		// Convert relations to OData-Binds or Inline-Entites

		// Convert Enum-Values
		Object.assign(odataObject,
		{
			wheelPosition: Entity.mapValue(this.wheelPosition, e => WheelPosition[e])
		});

		return odataObject;
	}

	// Update entityStatus abhängig von den Status der Child-Objekte, damit nested Änderungen nicht zu einem odata@bind an den Parent-Relationen führen
	public updateEntityStatus() {
		if (this._entityStatus == EntityStatus.CHANGED) { return; }
	}
}

export class WheelExpands {
}
