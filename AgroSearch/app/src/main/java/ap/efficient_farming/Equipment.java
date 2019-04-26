package ap.efficient_farming;

import java.io.Serializable;

public class Equipment implements Serializable {
    String contact, description, img_url, rate, time, title, type, user_id, object_id;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getObject_id() {
        return object_id;
    }

    public void setObject_id(String object_id) {
        this.object_id = object_id;
    }

    public Equipment(String contact, String description, String img_url, String rate, String time, String title, String type, String user_id, String object_id) {
        this.contact = contact;
        this.description = description;
        this.img_url = img_url;
        this.rate = rate;
        this.time = time;
        this.title = title;
        this.type = type;
        this.user_id = user_id;
        this.object_id = object_id;
    }

    public Equipment() {
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
