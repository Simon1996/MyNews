package simonbabikyan96gmail.com.mynews;

import io.realm.RealmObject;

/**
 * Created by Admin on 23.12.2016.
 */

public class News {
    private String title;
    private String article;
    private String imageUrl;
    private String articleUrl;

    public News(String title, String article, String imageUrl, String articleUrl) {
        this.title = title;
        this.article = article;
        this.imageUrl = imageUrl;
        this.articleUrl = articleUrl;
    }

    public News(){

    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }
}
