import { Component, OnInit } from '@angular/core';
import { Cocktail, Ingredient } from '../common/cocktail.model';

@Component({
  selector: 'app-cocktails',
  templateUrl: './cocktails.component.html',
  styleUrls: ['./cocktails.component.css']
})
export class CocktailsComponent implements OnInit {
  title = 'Mohito Ingredients';
  ingredientName = '';
  ingredientPercentage: number;
  cocktails: Cocktail[] = [
    {
        name: 'Mohito',
        ingredients: [
          {name: 'Gin', percentage: 25},
          {name: 'Lemon juice', percentage: 10},
          {name: 'Soda', percentage: 65}
        ]
    }
  ];

  ingredients: string[] = ['Gin', 'Lemon juice', 'Soda'];

  constructor() { }

  ngOnInit() {
  }

  addIngredient() {
    this.cocktails[0].ingredients.push(
      {name: this.ingredientName, percentage: this.ingredientPercentage}
    );
    this.ingredientName = '';
    this.ingredientPercentage = undefined;
  }

}
