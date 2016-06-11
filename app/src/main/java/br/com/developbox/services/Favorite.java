package br.com.developbox.services;

/**
 * Created by fabio on 31/05/2016.
 */
public class Favorite {
    private int id;
    private String title;
    private String url;

    public Favorite(){
        this.id = -1;
        this.title = null;
        this.url = null;
    }
    public Favorite(String title, String url){
        this.setTitle(title);
        this.setUrl(url);
    }
    public Favorite(String title, String url, int id){
        this.setId(id);
        this.setTitle(title);
        this.setUrl(url);
    }
    public int getId(){
        return this.id;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }
    public void setId(String id) {
        this.id = Integer.parseInt(id);
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setUrl(String url) {
        if(!url.contains("http://") && !url.contains("https://")){
            url = "http://"+url;
        }
        this.url = url;
    }
    public void setId(int id){
        this.id = id;
    }

    public String getShareString(){
        return getTitle()+" | "+getUrl();
    }
}
