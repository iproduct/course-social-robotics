import { CocktailMachinePage } from './app.po';

describe('cocktail-machine App', () => {
  let page: CocktailMachinePage;

  beforeEach(() => {
    page = new CocktailMachinePage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
