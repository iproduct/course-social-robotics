export interface Ingredient {
  name: string;
  percentage: number;
}

export class Cocktail {
  constructor(public name: string, public ingredients: Array<Ingredient>) {}
}
