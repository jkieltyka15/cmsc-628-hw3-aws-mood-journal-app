package com.example.moodjournal.lambda;

public class JournalEntry {

    private String mood;
    private String notes;
    private long timestamp;


    /**
     * Constructs a JournalEntry object
     *
     * @param mood: Mood
     * @param notes: Notes
     * @param timestamp: Timestamp in UNIX epoch
     */
    public JournalEntry(String mood, String notes, long timestamp) {

        this.mood = mood;
        this.notes = notes;
        this.timestamp = timestamp;
    }


    /**
     * Gets mood
     *
     * @return mood
     */
    public String getMood() {
        return mood;
    }


    /**
     * Gets notes
     *
     * @return notes
     */
    public String getNotes() {
        return notes;
    }


    /**
     * Gets timestamp
     *
     * @return timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }
}
