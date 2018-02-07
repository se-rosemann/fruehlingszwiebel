
/**
 * Die Klasse EnumMetaData enthält je Enum-Klasse aus dem Model eine variable, die
 * die Enum-Klasse als Instanz hält.
 * Damit kann in Pips o.Ä. auf die entsprechenden Enums und damit auf die Values generisch zugegriffen werden.
 *
 * Als Beispiel die Nutzung einer Pipe in einer selectbox:
 *
 * <select [(ngModel)]="person.geschlecht">
 <option *ngFor="let item of person._enumMetaData.Geschlecht | enumkeys" [value]="item.value">{{item.label}}</option>
 </select>
 */
export class EnumMetaData
{
}
