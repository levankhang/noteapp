package uitstart.uit.noteapp;

import java.io.Serializable;

/**
 * Created by Khang on 2/19/2017.
 */

public class Note implements Serializable {
    private int id;
    private String date;
    private String time;
    private String name;
    private String detai;

    public Note(int id, String date, String time, String name, String detai) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.name = name;
        this.detai = detai;
    }

    public Note(String date, String time, String name, String detai) {
        this.date = date;
        this.time = time;
        this.name = name;
        this.detai = detai;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetai() {
        return detai;
    }

    public void setDetai(String detai) {
        this.detai = detai;
    }
}
