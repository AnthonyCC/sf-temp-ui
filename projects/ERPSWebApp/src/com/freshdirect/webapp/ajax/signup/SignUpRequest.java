package com.freshdirect.webapp.ajax.signup;

public class SignUpRequest {

    private String email;
    private String password;
    private String zipCode;
    private String serviceType;
    private String captchaToken;
    private boolean receiveNews = true;
    private String emailPreferenceLevel = "0";
    private boolean plainTextEmail;
    private boolean termsAccepted = true;
    private boolean socialLoginOnly;
    private boolean tcAgree = true;
    private String companyName;
    private String workPhone;
    private String firstName;
    private String lastName;
    private String successPage;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getCaptchaToken() {
        return captchaToken;
    }

    public void setCaptchaToken(String captchaToken) {
        this.captchaToken = captchaToken;
    }

    public boolean isReceiveNews() {
        return receiveNews;
    }

    public void setReceiveNews(boolean receiveNews) {
        this.receiveNews = receiveNews;
    }

    public String getEmailPreferenceLevel() {
        return emailPreferenceLevel;
    }

    public void setEmailPreferenceLevel(String emailPreferenceLevel) {
        this.emailPreferenceLevel = emailPreferenceLevel;
    }

    public boolean isPlainTextEmail() {
        return plainTextEmail;
    }

    public void setPlainTextEmail(boolean plainTextEmail) {
        this.plainTextEmail = plainTextEmail;
    }

    public boolean isTermsAccepted() {
        return termsAccepted;
    }

    public void setTermsAccepted(boolean termsAccepted) {
        this.termsAccepted = termsAccepted;
    }

    public boolean isSocialLoginOnly() {
        return socialLoginOnly;
    }

    public void setSocialLoginOnly(boolean socialLoginOnly) {
        this.socialLoginOnly = socialLoginOnly;
    }

    public boolean isTcAgree() {
        return tcAgree;
    }

    public void setTcAgree(boolean tcAgree) {
        this.tcAgree = tcAgree;
    }

    public String getWorkPhone() {
        return workPhone;
    }

    public void setWorkPhone(String workPhone) {
        this.workPhone = workPhone;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSuccessPage() {
        return successPage;
    }

    public void setSuccessPage(String successPage) {
        this.successPage = successPage;
    }

}
