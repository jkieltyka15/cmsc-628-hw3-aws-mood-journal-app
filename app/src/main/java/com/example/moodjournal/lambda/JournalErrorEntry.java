package com.example.moodjournal.lambda;

public class JournalErrorEntry extends JournalEntry {

    private final String errorMessage;


    /**
     * Constructs a new JournalErrorEntry object
     *
     * @param errorMessage: Error message
     */
    public JournalErrorEntry(String errorMessage) {

        super(null, null, 0);
        this.errorMessage = errorMessage;
    }


    /**
     * Gets the error message
     *
     * @return Error message
     */
    public String getErrorMessage() {
        return errorMessage;
    }
}