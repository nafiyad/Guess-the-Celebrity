package com.example.assignment_2;

/**
 * Model class to represent a guessing item in the game
 */
public class GuessingItem {
    private int imageResId;       // Resource ID for the image
    private String correctAnswer; // The correct answer
    private String[] options;     // All answer options
    private String userAnswer;    // The user's selected answer
    private boolean answered;     // Whether this item has been answered

    public GuessingItem(int imageResId, String correctAnswer, String[] options) {
        this.imageResId = imageResId;
        this.correctAnswer = correctAnswer;
        this.options = options;
        this.userAnswer = null;
        this.answered = false;
    }

    // Getters and setters
    public int getImageResId() {
        return imageResId;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String[] getOptions() {
        return options;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public boolean isAnswered() {
        return answered;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }

    public boolean isCorrect() {
        return correctAnswer.equals(userAnswer);
    }
} 