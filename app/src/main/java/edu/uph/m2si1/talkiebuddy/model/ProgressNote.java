package edu.uph.m2si1.talkiebuddy.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ProgressNote extends RealmObject {
    @PrimaryKey
    private int id;
    private String date;
    private String topic;
    private String words;
    private String summary;
    private int confidence;
    private String duration;
    private String notes;


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public String getWords() { return words; }
    public void setWords(String words) { this.words = words; }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    public int getConfidence() { return confidence; }
    public void setConfidence(int confidence) { this.confidence = confidence; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
