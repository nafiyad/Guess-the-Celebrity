package com.example.assignment_2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {
    
    // Constants for SharedPreferences
    private static final String PREFS_NAME = "GamePrefs";
    private static final String KEY_NUM_GUESSES = "numGuesses";
    private static final String KEY_THEME = "theme";
    private static final String GAME_STATE = "GameState";
    
    // Default values
    private static final int DEFAULT_GUESSES = 5;
    private static final String THEME_DEFAULT = "default";
    private static final String THEME_DARK = "dark";
    
    // Game variables
    private int maxGuesses;
    private String currentTheme;
    private GameDataProvider gameDataProvider;
    
    // UI Components
    private TextView tvProgress;
    private ImageView ivCelebrity;
    private RadioGroup rgOptions;
    private RadioButton[] radioOptions = new RadioButton[4];
    private TextView tvFeedback;
    private Button btnSubmit;
    private ImageButton btnPrevious;
    private ImageButton btnNext;
    private Button btnBackToMain;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Load settings
        loadSettings();
        
        // Apply theme
        applyTheme(currentTheme);
        
        setContentView(R.layout.activity_game);
        
        // Initialize game data provider
        gameDataProvider = new GameDataProvider(maxGuesses);
        
        // Initialize UI components
        initializeUI();
        
        // Set up initial state
        updateUI();
        
        // Set up listeners
        setupListeners();
    }
    
    /**
     * Load user settings and game state
     */
    private void loadSettings() {
        // Load user settings
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        maxGuesses = prefs.getInt(KEY_NUM_GUESSES, DEFAULT_GUESSES);
        currentTheme = prefs.getString(KEY_THEME, THEME_DEFAULT);
        
        // Check if game state exists
        SharedPreferences gameState = getSharedPreferences(GAME_STATE, MODE_PRIVATE);
        if (!gameState.contains("initialized")) {
            // Initialize game state
            SharedPreferences.Editor editor = gameState.edit();
            editor.putBoolean("initialized", true);
            // Add any other game state initialization here
            editor.apply();
        }
    }
    
    /**
     * Apply the theme based on settings
     */
    private void applyTheme(String theme) {
        if (theme.equals(THEME_DARK)) {
            setTheme(R.style.Theme_Dark);
        } else {
            setTheme(R.style.Theme_Default);
        }
    }
    
    /**
     * Initialize UI components
     */
    private void initializeUI() {
        // Find views
        tvProgress = findViewById(R.id.tvProgress);
        ivCelebrity = findViewById(R.id.ivCelebrity);
        rgOptions = findViewById(R.id.rgOptions);
        
        radioOptions[0] = findViewById(R.id.rbOption1);
        radioOptions[1] = findViewById(R.id.rbOption2);
        radioOptions[2] = findViewById(R.id.rbOption3);
        radioOptions[3] = findViewById(R.id.rbOption4);
        
        tvFeedback = findViewById(R.id.tvFeedback);
        btnSubmit = findViewById(R.id.btnSubmit);
        
        // Navigation buttons are now ImageButtons
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
        
        // Back button
        btnBackToMain = findViewById(R.id.btnBackToMain);
    }
    
    /**
     * Set up button click listeners
     */
    private void setupListeners() {
        // Submit button
        btnSubmit.setOnClickListener(v -> {
            animateButtonClick(v);
            handleSubmit();
        });
        
        // Previous button
        btnPrevious.setOnClickListener(v -> {
            animateButtonClick(v);
            gameDataProvider.previousItem();
            updateUI();
        });
        
        // Next button
        btnNext.setOnClickListener(v -> {
            animateButtonClick(v);
            gameDataProvider.nextItem();
            updateUI();
        });
        
        // Back to Main Menu button
        btnBackToMain.setOnClickListener(v -> {
            animateButtonClick(v);
            Intent intent = new Intent(GameActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
    
    /**
     * Handle the submit answer button click
     */
    private void handleSubmit() {
        int selectedId = rgOptions.getCheckedRadioButtonId();
        
        if (selectedId == -1) {
            // No option selected
            tvFeedback.setText(getString(R.string.select_answer));
            tvFeedback.setTextColor(Color.RED);
            return;
        }
        
        // Find which option was selected
        String selectedAnswer = null;
        for (int i = 0; i < radioOptions.length; i++) {
            if (radioOptions[i].getId() == selectedId) {
                selectedAnswer = radioOptions[i].getText().toString();
                break;
            }
        }
        
        // Submit answer and get result
        boolean isCorrect = gameDataProvider.submitAnswer(selectedAnswer);
        
        // Show feedback
        if (isCorrect) {
            tvFeedback.setText(getString(R.string.correct_answer));
            tvFeedback.setTextColor(Color.GREEN);
        } else {
            tvFeedback.setText(getString(R.string.wrong_answer, gameDataProvider.getCurrentItem().getCorrectAnswer()));
            tvFeedback.setTextColor(Color.RED);
        }
        
        // Disable submit button and radio buttons
        btnSubmit.setEnabled(false);
        disableOptions();
        
        // Check if game is complete
        if (gameDataProvider.isGameComplete()) {
            int correctCount = gameDataProvider.getCorrectAnswersCount();
            String gameCompleteMessage = getString(R.string.game_complete, correctCount, gameDataProvider.getItemCount());
            tvFeedback.setText(tvFeedback.getText() + "\n\n" + gameCompleteMessage);
            Toast.makeText(this, gameCompleteMessage, Toast.LENGTH_LONG).show();
        }
        
        // Highlight the correct answer
        highlightCorrectAnswer();
    }
    
    /**
     * Apply animation to button clicks
     * @param view The button being clicked
     */
    private void animateButtonClick(View view) {
        AlphaAnimation animation = new AlphaAnimation(1.0f, 0.5f);
        animation.setDuration(150);
        view.startAnimation(animation);
    }
    
    /**
     * Update the UI based on the current question
     */
    private void updateUI() {
        GuessingItem currentItem = gameDataProvider.getCurrentItem();
        
        if (currentItem == null) {
            return;
        }
        
        // Update progress text
        int currentPos = gameDataProvider.getCurrentPosition() + 1;
        int totalItems = gameDataProvider.getItemCount();
        tvProgress.setText(getString(R.string.question_format, currentPos, totalItems));
        
        // Update image
        ivCelebrity.setImageResource(currentItem.getImageResId());
        
        // Reset all UI elements to default state
        resetUIState();
        
        // Update options
        String[] options = currentItem.getOptions();
        for (int i = 0; i < radioOptions.length; i++) {
            radioOptions[i].setText(options[i]);
        }
        
        // Update navigation buttons
        btnPrevious.setEnabled(gameDataProvider.hasPreviousItem());
        btnNext.setEnabled(gameDataProvider.hasNextItem());
        
        // If already answered, show result and disable options
        if (currentItem.isAnswered()) {
            btnSubmit.setEnabled(false);
            
            // Show the user's answer
            for (RadioButton radioButton : radioOptions) {
                if (radioButton.getText().toString().equals(currentItem.getUserAnswer())) {
                    radioButton.setChecked(true);
                    break;
                }
            }
            
            // Show feedback
            if (currentItem.isCorrect()) {
                tvFeedback.setText(getString(R.string.correct_answer));
                tvFeedback.setTextColor(Color.GREEN);
            } else {
                tvFeedback.setText(getString(R.string.wrong_answer, currentItem.getCorrectAnswer()));
                tvFeedback.setTextColor(Color.RED);
            }
            
            // Disable options
            disableOptions();
            
            // Highlight correct answer
            highlightCorrectAnswer();
        } else {
            // Enable submit and options
            btnSubmit.setEnabled(true);
            enableOptions();
        }
    }
    
    /**
     * Reset the UI state to default for a fresh question
     */
    private void resetUIState() {
        // Clear selection
        rgOptions.clearCheck();
        
        // Reset radio button text colors
        for (RadioButton radioButton : radioOptions) {
            radioButton.setTextColor(getCurrentTextColor());
        }
        
        // Reset feedback
        tvFeedback.setText("");
        
        // Enable options by default (will be disabled later if needed)
        enableOptions();
    }
    
    /**
     * Get the current text color based on the theme
     */
    private int getCurrentTextColor() {
        // Get the current theme's text color attribute
        int[] attrs = {android.R.attr.textColor};
        android.content.res.TypedArray a = obtainStyledAttributes(attrs);
        int color = a.getColor(0, Color.BLACK);
        a.recycle();
        return color;
    }
    
    /**
     * Disable all radio button options
     */
    private void disableOptions() {
        for (RadioButton radioButton : radioOptions) {
            radioButton.setEnabled(false);
        }
    }
    
    /**
     * Enable all radio button options
     */
    private void enableOptions() {
        for (RadioButton radioButton : radioOptions) {
            radioButton.setEnabled(true);
        }
    }
    
    /**
     * Highlight the correct answer after submission
     */
    private void highlightCorrectAnswer() {
        GuessingItem currentItem = gameDataProvider.getCurrentItem();
        if (currentItem == null || !currentItem.isAnswered()) {
            return; // Only highlight answers for answered questions
        }
        
        String correctAnswer = currentItem.getCorrectAnswer();
        String userAnswer = currentItem.getUserAnswer();
        
        for (RadioButton radioButton : radioOptions) {
            String optionText = radioButton.getText().toString();
            
            // Highlight correct answer in green
            if (optionText.equals(correctAnswer)) {
                radioButton.setTextColor(Color.GREEN);
            } 
            // Highlight incorrect user selection in red
            else if (optionText.equals(userAnswer) && !currentItem.isCorrect()) {
                radioButton.setTextColor(Color.RED);
            }
            // Leave other options with default text color
            else {
                radioButton.setTextColor(getCurrentTextColor());
            }
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Make sure UI is updated when returning to the activity
        updateUI();
    }
}
