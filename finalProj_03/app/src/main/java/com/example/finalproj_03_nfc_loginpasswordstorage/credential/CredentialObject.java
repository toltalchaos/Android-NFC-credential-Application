package com.example.finalproj_03_nfc_loginpasswordstorage.credential;

public class CredentialObject {
    private String userName;
    private String password;
    private String serviceName;
    private String serviceUserName;
    private String servicePassword;

    public CredentialObject(String userName, String password, String serviceName, String serviceUserName, String servicePassword) {
        this.userName = userName;
        this.password = password;
        this.serviceName = serviceName;
        this.serviceUserName = serviceUserName;
        this.servicePassword = servicePassword;
    }

    public CredentialObject() {

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceUserName() {
        return serviceUserName;
    }

    public void setServiceUserName(String serviceUserName) {
        this.serviceUserName = serviceUserName;
    }

    public String getServicePassword() {
        return servicePassword;
    }

    public void setServicePassword(String servicePassword) {
        this.servicePassword = servicePassword;
    }
}
