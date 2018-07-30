package com.freshdirect.webapp.ajax.expresscheckout.validation.contraint;

import org.junit.Assert;
import org.junit.Test;

import com.freshdirect.webapp.ajax.expresscheckout.validation.constraint.PhoneConstraint;

public class PhoneConstraintTest {

    @Test
    public void nullPhoneNumberIsNotValid() {
        PhoneConstraint contraint = new PhoneConstraint(false);
        Assert.assertFalse(contraint.isValid(null));
    }

    @Test
    public void emptyPhoneNumberIsNotValid() {
        PhoneConstraint contraint = new PhoneConstraint(false);
        Assert.assertFalse(contraint.isValid(""));
    }

    @Test
    public void lessThenTenPhoneNumberIsNotValid() {
        PhoneConstraint contraint = new PhoneConstraint(false);
        Assert.assertFalse(contraint.isValid("123456789"));
    }

    @Test
    public void moreThenTenPhoneNumberIsNotValid() {
        PhoneConstraint contraint = new PhoneConstraint(false);
        Assert.assertFalse(contraint.isValid("12345678901"));
    }

    @Test
    public void startWithZeroPhoneNumberIsNotValid() {
        PhoneConstraint contraint = new PhoneConstraint(false);
        Assert.assertFalse(contraint.isValid("0123456789"));
    }

    @Test
    public void alphabeticPhoneNumberIsNotValid() {
        PhoneConstraint contraint = new PhoneConstraint(false);
        Assert.assertFalse(contraint.isValid("abc12345"));
    }

    @Test
    public void tenDigitsPhoneNumberIsValid() {
        PhoneConstraint contraint = new PhoneConstraint(false);
        Assert.assertTrue(contraint.isValid("1234567890"));
    }

    @Test
    public void tenDigitsWithParanthezisPhoneNumberIsValid() {
        PhoneConstraint contraint = new PhoneConstraint(false);
        Assert.assertTrue(contraint.isValid("(123)4567890"));
    }

    @Test
    public void tenDigitsWithParanthezisAndSpacesPhoneNumberIsValid() {
        PhoneConstraint contraint = new PhoneConstraint(false);
        Assert.assertTrue(contraint.isValid("(123) 456 7890"));
    }

    @Test
    public void nullPhoneNumberIsNotValidInOptionalMode() {
        PhoneConstraint contraint = new PhoneConstraint(true);
        Assert.assertFalse(contraint.isValid(null));
    }

    @Test
    public void emptyPhoneNumberIsValidInOptionalMode() {
        PhoneConstraint contraint = new PhoneConstraint(true);
        Assert.assertTrue(contraint.isValid(""));
    }

    @Test
    public void lessThenTenPhoneNumberIsNotValidInOptionalMode() {
        PhoneConstraint contraint = new PhoneConstraint(true);
        Assert.assertFalse(contraint.isValid("123456789"));
    }

    @Test
    public void moreThenTenPhoneNumberIsNotValidInOptionalMode() {
        PhoneConstraint contraint = new PhoneConstraint(true);
        Assert.assertFalse(contraint.isValid("12345678901"));
    }

    @Test
    public void startWithZeroPhoneNumberIsNotValidInOptionalMode() {
        PhoneConstraint contraint = new PhoneConstraint(true);
        Assert.assertFalse(contraint.isValid("0123456789"));
    }

    @Test
    public void alphabeticPhoneNumberIsNotValidInOptionalMode() {
        PhoneConstraint contraint = new PhoneConstraint(true);
        Assert.assertFalse(contraint.isValid("abc12345"));
    }

    @Test
    public void tenDigitsPhoneNumberIsValidInOptionalMode() {
        PhoneConstraint contraint = new PhoneConstraint(true);
        Assert.assertTrue(contraint.isValid("1234567890"));
    }

    @Test
    public void tenDigitsWithParanthezisPhoneNumberIsValidInOptionalMode() {
        PhoneConstraint contraint = new PhoneConstraint(true);
        Assert.assertTrue(contraint.isValid("(123)4567890"));
    }

    @Test
    public void tenDigitsWithParanthezisAndSpacesPhoneNumberIsValidInOptionalMode() {
        PhoneConstraint contraint = new PhoneConstraint(true);
        Assert.assertTrue(contraint.isValid("(123) 456 7890"));
    }

}