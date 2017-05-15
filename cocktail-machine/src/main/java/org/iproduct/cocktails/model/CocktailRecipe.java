package org.iproduct.cocktails.model;

import java.util.Arrays;

public class CocktailRecipe {
	private final String name;
	private final IngredientPercentage[] ingredientPercentages;
	
	public CocktailRecipe(String name, IngredientPercentage ...ingredients) {
		this.name = name;
		ingredientPercentages = ingredients; 
	}

	public String getName() {
		return name;
	}

	public IngredientPercentage[] getIngredientPercentages() {
		return ingredientPercentages;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(ingredientPercentages);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CocktailRecipe other = (CocktailRecipe) obj;
		if (!Arrays.equals(ingredientPercentages, other.ingredientPercentages))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CoctailRecipe [ingredientPercentages=" + Arrays.toString(ingredientPercentages) + "]";
	}
	
}
