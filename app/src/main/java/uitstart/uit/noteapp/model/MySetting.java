package uitstart.uit.noteapp.model;

import java.io.Serializable;

/**
 * Created by Khang on 2/28/2017.
 */

public class MySetting implements Serializable {
    private int id;
    private int is_notification;
    private String language_code;

    public MySetting(int id, int is_notification, String language_code) {
        this.id = id;
        this.is_notification = is_notification;
        this.language_code = language_code;
    }
    public MySetting(int is_notification, String language_code) {
        this.is_notification = is_notification;
        this.language_code = language_code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIs_notification() {
        return is_notification;
    }

    public void setIs_notification(int is_notification) {
        this.is_notification = is_notification;
    }

    public String getLanguage_code() {
        return language_code;
    }

    public void setLanguage_code(String language_code) {
        this.language_code = language_code;
    }
}
