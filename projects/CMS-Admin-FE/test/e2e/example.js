/* global describe, it, expect, browser, element, by */
describe('Example page tester', function() {
  "use strict";

  it('should press the button', function() {
    browser.ignoreSynchronization = true;
    browser.get('http://localhost:8000/');

    var button = element(by.id('button'));

    expect(button).not.toBe(null);

    // press button twice
    button.click();
  });
});
