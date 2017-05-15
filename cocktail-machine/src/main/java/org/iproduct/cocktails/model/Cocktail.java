package org.iproduct.cocktails.model;

public class Cocktail {
	private final CocktailRecipe recipe;
	private final double quantity;
	
	public Cocktail(CocktailRecipe recipe, double quantity) {
		this.recipe = recipe;
		this.quantity = quantity;
	}

	public CocktailRecipe getRecipe() {
		return recipe;
	}

	public double getQuantity() {
		return quantity;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(quantity);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((recipe == null) ? 0 : recipe.hashCode());
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
		Cocktail other = (Cocktail) obj;
		if (Double.doubleToLongBits(quantity) != Double.doubleToLongBits(other.quantity))
			return false;
		if (recipe == null) {
			if (other.recipe != null)
				return false;
		} else if (!recipe.equals(other.recipe))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Cocktail [recipe=" + recipe + ", quantity=" + quantity + "]";
	}

}
