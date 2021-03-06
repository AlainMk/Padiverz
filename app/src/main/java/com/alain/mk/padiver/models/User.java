package com.alain.mk.padiver.models;

public class User {

    private String uid;
    private String username;
    private String urlPicture;
    private String email;
    private String tokenId;
    private String deviceToken;
    private String phoneNumber;
    private String address;
    private String language;
    private String bio;
    private String hobbies;
    private String webSite;
    private String githubLink;

    public User() {
    }

    public User(String uid, String username, String email, String urlPicture, String tokenId, String deviceToken) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.urlPicture = urlPicture;
        this.tokenId = tokenId;
        this.deviceToken = deviceToken;
    }

    public User(String username, String deviceToken, String phoneNumber, String address, String language, String bio, String hobbies, String webSite, String githubLink) {
        this.username = username;
        this.deviceToken = deviceToken;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.language = language;
        this.bio = bio;
        this.hobbies = hobbies;
        this.webSite = webSite;
        this.githubLink = githubLink;
    }

    public User(String username, String urlPicture, String deviceToken, String phoneNumber, String address, String language, String bio, String hobbies, String webSite, String githubLink) {
        this.username = username;
        this.urlPicture = urlPicture;
        this.deviceToken = deviceToken;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.language = language;
        this.bio = bio;
        this.hobbies = hobbies;
        this.webSite = webSite;
        this.githubLink = githubLink;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUrlPicture() {
        return urlPicture;
    }

    public void setUrlPicture(String urlPicture) {
        this.urlPicture = urlPicture;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public String getGithubLink() {
        return githubLink;
    }

    public void setGithubLink(String githubLink) {
        this.githubLink = githubLink;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }
}
