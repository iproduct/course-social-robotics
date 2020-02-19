import { IptpiDemoPage } from './app.po';

describe('iptpi-demo App', function() {
  let page: IptpiDemoPage;

  beforeEach(() => {
    page = new IptpiDemoPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
