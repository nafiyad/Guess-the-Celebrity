package com.example.assignment_2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {
    
    // Constants for SharedPreferences
    private static final String PREFS_NAME = "GamePrefs";
    private static final String KEY_THEME = "theme";
    
    // Default values
    private static final String THEME_DEFAULT = "default";
    private static final String THEME_DARK = "dark";
    
    // UI Components
    private Button btnStart;
    private Button btnSettings;
    private Button btnAbout;
    private CardView titleCard;
    private CardView menuCard;
    // Track the current theme to avoid unnecessary recreates
    private String currentTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Apply theme before setting content view
        applyTheme();
        
        setContentView(R.layout.activity_main);

        // Initialize UI components
        initializeUIComponents();
        
        // Set up click listeners
        setupClickListeners();
    }
    
    /**
     * Initialize UI components
     */
    private void initializeUIComponents() {
        btnStart = findViewById(R.id.btnStartGame);
        btnSettings = findViewById(R.id.btnSettings);
        btnAbout = findViewById(R.id.btnAbout);
        titleCard = findViewById(R.id.titleCard);
        menuCard = findViewById(R.id.menuCard);
    }
    
    /**
     * Set up click listeners for buttons
     */
    private void setupClickListeners() {
        btnStart.setOnClickListener(v -> {
            animateButtonClick(v);
            startActivity(new Intent(MainActivity.this, GameActivity.class));
        });
        
        btnSettings.setOnClickListener(v -> {
            animateButtonClick(v);
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
        });
        
        btnAbout.setOnClickListener(v -> {
            animateButtonClick(v);
            startActivity(new Intent(MainActivity.this, AboutActivity.class));
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
    
    /**
     * Apply theme based on settings
     */
    private void applyTheme() {
        // Load theme setting
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        currentTheme = prefs.getString(KEY_THEME, THEME_DEFAULT);
        
        // Apply appropriate theme
        if (currentTheme.equals(THEME_DARK)) {
            setTheme(R.style.Theme_Dark);
        } else {
            setTheme(R.style.Theme_Default);
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        // Check if theme has changed
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String newTheme = prefs.getString(KEY_THEME, THEME_DEFAULT);
        
        // Only recreate if the theme has actually changed
        if (!newTheme.equals(currentTheme)) {
            currentTheme = newTheme;
            recreate();
        }
    }
}
