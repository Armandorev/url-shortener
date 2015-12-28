describe('URLValidator', function () {
  'use strict';

  var urlValidator;

  beforeEach(module('WordItApp'));

  beforeEach(inject(function (_$rootScope_, _UrlValidator_) {
    urlValidator = _UrlValidator_;
  }));

  it('should prepend protocol if url does not contain a protocol', function () {
    expect(urlValidator.validify('www.google.com')).toBe('http://www.google.com');
    expect(urlValidator.validify('www.pivotal.io')).toBe('http://www.pivotal.io');
    expect(urlValidator.validify('pivotal.io')).toBe('http://pivotal.io');
  });

  it('should not prepend if a protocol is potentially added', function () {
    expect(urlValidator.validify('h')).toBe('h');
    expect(urlValidator.validify('ht')).toBe('ht');
    expect(urlValidator.validify('htt')).toBe('htt');
  });

  it('should not prepend empty string', function () {
    expect(urlValidator.validify('')).toBe('');
  })

});