import {Component, OnInit} from '@angular/core';

import {AppState} from '../app.service';
import {Title} from './title';
import {CarODataService} from "../fruehlingszwiebel/odata/car.odata-service";
import {CarExpands} from "../fruehlingszwiebel/odata/Car-model";
import {Odata2Service} from "../fruehlingszwiebel/odata/tools/odata-tools";
import {CarSpringService} from "../fruehlingszwiebel/spring/car.spring-service";

@Component({
  /**
   * The selector is what angular internally uses
   * for `document.querySelectorAll(selector)` in our index.html
   * where, in this case, selector is the string 'home'.
   */
  selector: 'home',  // <home></home>
  /**
   * We need to tell Angular's Dependency Injection which providers are in our app.
   */
  providers: [
    Title,
    CarODataService,
    Odata2Service,
    CarSpringService
  ],
  /**
   * Our list of styles in our component. We may add more to compose many styles together.
   */
  styleUrls: ['./home.component.css'],
  /**
   * Every Angular template is first compiled by the browser before Angular runs it's compiler.
   */
  templateUrl: './home.component.html'
})
export class HomeComponent implements OnInit {
  /**
   * Set our default values
   */
  public localState = {value: ''};

  public cars: Array<any> = [];

  /**
   * TypeScript public modifiers
   */
  constructor(public appState: AppState,
              public title: Title,
              public carService: CarODataService,
              private carSpringService: CarSpringService) {
  }

  public ngOnInit() {
    console.log('hello `Home` component');
    this.loadCars();
  }

  public loadCars() {
    // Standard-Way
    // this.carService.getCars().subscribe(cars => this.cars = cars);

    // Spring-way
    // this.carSpringService.getCars().subscribe(cars => this.cars = cars);

    // OData-Way
    let expandOptions: CarExpands = new CarExpands().expandTires().expandWheels();
    this.carService.findCars(expandOptions);
  }

  public submitState(value: string) {
    console.log('submitState', value);
    this.appState.set('value', value);
    this.localState.value = '';
  }
}
