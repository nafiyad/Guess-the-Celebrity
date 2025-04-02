package com.example.assignment_2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Provider class for game data
 */
public class GameDataProvider {
    
    private List<GuessingItem> allItems;     // All available guessing items
    private List<GuessingItem> currentItems; // Items for the current game
    private int currentPosition;             // Current position in the game
    private int maxGuesses;                  // Maximum number of guesses for this game
    
    public GameDataProvider(int maxGuesses) {
        this.maxGuesses = maxGuesses;
        initializeItems();
        resetGame();
    }
    
    /**
     * Initialize all possible guessing items
     */
    private void initializeItems() {
        allItems = new ArrayList<>();
        
        // Add celebrities - using resource IDs from drawable-nodpi folder
        allItems.add(new GuessingItem(
                R.drawable.celebrity1,
                "Tom Hanks",
                new String[]{"Tom Hanks", "Brad Pitt", "Leonardo DiCaprio", "Will Smith"}
        ));
        
        allItems.add(new GuessingItem(
                R.drawable.celebrity2,
                "Jennifer Lawrence",
                new String[]{"Jennifer Lawrence", "Emma Watson", "Scarlett Johansson", "Natalie Portman"}
        ));
        
        allItems.add(new GuessingItem(
                R.drawable.celebrity3,
                "Robert Downey Jr.",
                new String[]{"Robert Downey Jr.", "Chris Evans", "Chris Hemsworth", "Mark Ruffalo"}
        ));
        
        allItems.add(new GuessingItem(
                R.drawable.celebrity4,
                "Meryl Streep",
                new String[]{"Meryl Streep", "Helen Mirren", "Julia Roberts", "Sandra Bullock"}
        ));
        
        allItems.add(new GuessingItem(
                R.drawable.celebrity5,
                "Denzel Washington",
                new String[]{"Denzel Washington", "Morgan Freeman", "Samuel L. Jackson", "Idris Elba"}
        ));
        
        allItems.add(new GuessingItem(
                R.drawable.celebrity6,
                "Chris Pratt",
                new String[]{"Chris Pratt", "Chris Pine", "Chris Evans", "Chris Hemsworth"}
        ));
        
        allItems.add(new GuessingItem(
                R.drawable.celebrity7,
                "Keanu Reeves",
                new String[]{"Keanu Reeves", "Tom Cruise", "Johnny Depp", "Hugh Jackman"}
        ));
        
        allItems.add(new GuessingItem(
                R.drawable.celebrity8,
                "Dwayne Johnson",
                new String[]{"Dwayne Johnson", "Jason Statham", "Vin Diesel", "John Cena"}
        ));
        
        allItems.add(new GuessingItem(
                R.drawable.celebrity9,
                "Angelina Jolie",
                new String[]{"Angelina Jolie", "Charlize Theron", "Margot Robbie", "Gal Gadot"}
        ));
        
        allItems.add(new GuessingItem(
                R.drawable.celebrity10,
                "Emma Stone",
                new String[]{"Emma Stone", "Anne Hathaway", "Emma Watson", "Jennifer Lawrence"}
        ));
        
        // Shuffle options for each item
        for (GuessingItem item : allItems) {
            List<String> options = Arrays.asList(item.getOptions());
            Collections.shuffle(options);
        }
    }
    
    /**
     * Reset the game with a new set of random items
     */
    public void resetGame() {
        // Shuffle all items and pick the first maxGuesses
        Collections.shuffle(allItems);
        
        // First, reset the state of all items in the pool
        for (GuessingItem item : allItems) {
            item.setAnswered(false);
            item.setUserAnswer(null);
        }
        
        // Create a new list with the selected items to avoid sharing references
        currentItems = new ArrayList<>();
        int itemsToSelect = Math.min(maxGuesses, allItems.size());
        
        for (int i = 0; i < itemsToSelect; i++) {
            GuessingItem original = allItems.get(i);
            
            // Create a fresh copy to avoid reference issues
            GuessingItem itemCopy = new GuessingItem(
                original.getImageResId(),
                original.getCorrectAnswer(),
                original.getOptions()
            );
            
            currentItems.add(itemCopy);
        }
        
        // Reset position
        currentPosition = 0;
    }
    
    /**
     * Get the current guessing item
     */
    public GuessingItem getCurrentItem() {
        if (currentPosition >= 0 && currentPosition < currentItems.size()) {
            return currentItems.get(currentPosition);
        }
        return null;
    }
    
    /**
     * Move to the next item and return it
     */
    public GuessingItem nextItem() {
        if (currentPosition < currentItems.size() - 1) {
            currentPosition++;
        }
        return getCurrentItem();
    }
    
    /**
     * Move to the previous item and return it
     */
    public GuessingItem previousItem() {
        if (currentPosition > 0) {
            currentPosition--;
        }
        return getCurrentItem();
    }
    
    /**
     * Check if there's a next item
     */
    public boolean hasNextItem() {
        return currentPosition < currentItems.size() - 1;
    }
    
    /**
     * Check if there's a previous item
     */
    public boolean hasPreviousItem() {
        return currentPosition > 0;
    }
    
    /**
     * Get the current position
     */
    public int getCurrentPosition() {
        return currentPosition;
    }
    
    /**
     * Get the count of guessing items
     */
    public int getItemCount() {
        return currentItems.size();
    }
    
    /**
     * Submit an answer for the current item
     */
    public boolean submitAnswer(String answer) {
        GuessingItem currentItem = getCurrentItem();
        if (currentItem != null && !currentItem.isAnswered()) {
            currentItem.setUserAnswer(answer);
            currentItem.setAnswered(true);
            return currentItem.isCorrect();
        }
        return false;
    }
    
    /**
     * Get the number of correct answers
     */
    public int getCorrectAnswersCount() {
        int count = 0;
        for (GuessingItem item : currentItems) {
            if (item.isAnswered() && item.isCorrect()) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * Check if all items have been answered
     */
    public boolean isGameComplete() {
        for (GuessingItem item : currentItems) {
            if (!item.isAnswered()) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Set max guesses and reset game
     */
    public void setMaxGuesses(int maxGuesses) {
        this.maxGuesses = maxGuesses;
        resetGame();
    }
} 