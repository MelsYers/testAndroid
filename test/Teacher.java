package com.example.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Teacher {

    private String username, email;
    private List<Article> articles;

    public Teacher(String username, String email) {
        this.username = username;
        this.email = email;
        articles = new ArrayList<>();
    }

    public Teacher() {
        articles = new ArrayList<>();
    }

    public void addArticle(Article article){
        articles.add(article);
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
