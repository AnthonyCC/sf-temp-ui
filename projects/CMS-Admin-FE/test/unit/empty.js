/* global describe, it, expect */
describe("Basic suite", function () {
  "use strict";

  it("positive case", function () {
    expect(1 === +"1").toBe(true);
  });

  it("negative case", function () {
    expect(1 !== +"1").not.toBe(true);
  });
});
