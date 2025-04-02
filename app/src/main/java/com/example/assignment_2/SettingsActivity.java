package com.example.assignment_2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

public class SettingsActivity extends AppCompatActivity {
    
    // Constants for SharedPreferences
    private static final String PREFS_NAME = "GamePrefs";
    private static final String KEY_NUM_GUESSES = "numGuesses";
    private static final String KEY_THEME = "theme";
    private static final String GAME_STATE = "GameState";
    
    // Default values
    private static final int DEFAULT_GUESSES = 5;
    private static final String THEME_DEFAULT = "default";
    private static final String THEME_DARK = "dark";
    
    // UI Components
    private SeekBar seekBarGuesses;
    private TextView tvGuessesValue;
    private TextView tvStatusMessage;
    private RadioGroup radioGroupTheme;
    private RadioButton radioDefaultTheme;
    private RadioButton radioDarkTheme;
    private Button btnResetGame;
    private Button btnSaveSettings;
    private Button btnBackToMain;
    private TextView guessesLabelText;
    private TextView themeLabelText;
    private CardView guessesCard;
    private CardView themeCard;
    private CardView resetGameCard;
    private CardView buttonsCard;
    
    // Current settings
    private int numGuesses;
    private String currentTheme;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Load saved settings
        loadSettings();
        
        // Apply current theme
        applyTheme(currentTheme);
        
        setContentView(R.layout.activity_settings);
        
        // Initialize UI components
        initializeUI();
        
        // Set up listeners
        setupListeners();
        
        // Adjust colors for the current theme
        adjustColorsForTheme();
    }
    
    private void loadSettings() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        numGuesses = prefs.getInt(KEY_NUM_GUESSES, DEFAULT_GUESSES);
        currentTheme = prefs.getString(KEY_THEME, THEME_DEFAULT);
    }
    
    private void applyTheme(String theme) {
        if (theme.equals(THEME_DARK)) {
            setTheme(R.style.Theme_Dark);
        } else {
            setTheme(R.style.Theme_Default);
        }
    }
    
    private void initializeUI() {
        // Find views
        seekBarGuesses = findViewById(R.id.seekBarGuesses);
        tvGuessesValue = findViewById(R.id.tvGuessesValue);
        tvStatusMessage = findViewById(R.id.tvStatusMessage);
        radioGroupTheme = findViewById(R.id.radioGroupTheme);
        radioDefaultTheme = findViewById(R.id.radioDefaultTheme);
        radioDarkTheme = findViewById(R.id.radioDarkTheme);
        btnResetGame = findViewById(R.id.btnResetGame);
        btnSaveSettings = findViewById(R.id.btnSaveSettings);
        btnBackToMain = findViewById(R.id.btnBackToMain);
        guessesLabelText = findViewById(R.id.guessesLabelText);
        themeLabelText = findViewById(R.id.themeLabelText);
        guessesCard = findViewById(R.id.guessesCard);
        themeCard = findViewById(R.id.themeCard);
        resetGameCard = findViewById(R.id.resetGameCard);
        buttonsCard = findViewById(R.id.buttonsCard);
        
        // Set initial values
        seekBarGuesses.setProgress(numGuesses - 1); // Adjust for 0-based index
        tvGuessesValue.setText(String.valueOf(numGuesses));
        
        if (currentTheme.equals(THEME_DARK)) {
            radioDarkTheme.setChecked(true);
        } else {
            radioDefaultTheme.setChecked(true);
        }
    }
    
    /**
     * Adjust colors for cards and text based on current theme
     */
    private void adjustColorsForTheme() {
        if (currentTheme.equals(THEME_DARK)) {
            // Set card backgrounds to darker colors
            if (guessesCard != null) {
                guessesCard.setCardBackgroundColor(getResources().getColor(R.color.dark_background, null));
            }
            if (themeCard != null) {
                themeCard.setCardBackgroundColor(getResources().getColor(R.color.dark_background, null));
            }
            if (resetGameCard != null) {
                resetGameCard.setCardBackgroundColor(getResources().getColor(R.color.dark_background, null));
            }
            if (buttonsCard != null) {
                buttonsCard.setCardBackgroundColor(getResources().getColor(R.color.dark_background, null));
            }
            
            // Set text colors to be more visible on dark background
            if (guessesLabelText != null) {
                guessesLabelText.setTextColor(getResources().getColor(R.color.dark_text, null));
            }
            if (themeLabelText != null) {
                themeLabelText.setTextColor(getResources().getColor(R.color.dark_text, null));
            }
            if (tvGuessesValue != null) {
                tvGuessesValue.setTextColor(getResources().getColor(R.color.dark_text, null));
            }
            if (radioDefaultTheme != null) {
                radioDefaultTheme.setTextColor(getResources().getColor(R.color.dark_text, null));
            }
            if (radioDarkTheme != null) {
                radioDarkTheme.setTextColor(getResources().getColor(R.color.dark_text, null));
            }
        }
    }
    
    private void setupListeners() {
        // SeekBar listener
        seekBarGuesses.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Add 1 to progress since SeekBar starts at 0
                int value = progress + 1;
                tvGuessesValue.setText(String.valueOf(value));
            }
            
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Not needed
            }
            
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Not needed
            }
        });
        
        // Reset Game button
        btnResetGame.setOnClickListener(v -> {
            animateButtonClick(v);
            resetGame();
        });
        
        // Save Settings button
        btnSaveSettings.setOnClickListener(v -> {
            animateButtonClick(v);
            saveSettings();
        });
        
        // Back to Main button
        btnBackToMain.setOnClickListener(v -> {
            animateButtonClick(v);
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
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
    
    private void resetGame() {
        // Clear any game state
        SharedPreferences gameState = getSharedPreferences(GAME_STATE, MODE_PRIVATE);
        SharedPreferences.Editor editor = gameState.edit();
        editor.clear();
        editor.apply();
        
        // Show success message
        showStatusMessage(getString(R.string.game_reset_success));
    }
    
    private void saveSettings() {
        // Get current values
        numGuesses = seekBarGuesses.getProgress() + 1; // Add 1 since SeekBar is 0-based
        
        if (radioDarkTheme.isChecked()) {
            currentTheme = THEME_DARK;
        } else {
            currentTheme = THEME_DEFAULT;
        }
        
        // Save to SharedPreferences
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putInt(KEY_NUM_GUESSES, numGuesses);
        editor.putString(KEY_THEME, currentTheme);
        editor.apply();
        
        // Show success message with animation
        showStatusMessage(getString(R.string.settings_saved));
        
        // Apply theme after a short delay to allow the message to be seen
        new android.os.Handler().postDelayed(() -> recreate(), 1000);
    }
    
    /**
     * Show a status message in the status TextView with animation
     */
    private void showStatusMessage(String message) {
        tvStatusMessage.setText(message);
        tvStatusMessage.setAlpha(0f);
        tvStatusMessage.setVisibility(View.VISIBLE);
        
        // Update status message text color based on theme
        if (currentTheme.equals(THEME_DARK)) {
            tvStatusMessage.setTextColor(getResources().getColor(R.color.dark_text, null));
            tvStatusMessage.setBackgroundColor(getResources().getColor(R.color.dark_background, null));
        }
        
        // Fade in animation
        tvStatusMessage.animate()
            .alpha(1f)
            .setDuration(300)
            .start();
        
        // Make the message disappear after 3 seconds with fade out
        new android.os.Handler().postDelayed(() -> {
            if (!isFinishing()) {
                tvStatusMessage.animate()
                    .alpha(0f)
                    .setDuration(300)
                    .withEndAction(() -> tvStatusMessage.setVisibility(View.GONE))
                    .start();
            }
        }, 3000);
    }
}
