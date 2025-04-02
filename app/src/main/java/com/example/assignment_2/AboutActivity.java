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

public class AboutActivity extends AppCompatActivity {
    
    // Constants for SharedPreferences
    private static final String PREFS_NAME = "GamePrefs";
    private static final String KEY_THEME = "theme";
    
    // Default values
    private static final String THEME_DEFAULT = "default";
    private static final String THEME_DARK = "dark";
    
    // UI Components
    private Button btnBackToMain;
    private String currentTheme;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Load and apply theme
        loadAndApplyTheme();
        
        setContentView(R.layout.activity_about);

        // Initialize UI components
        initializeUIComponents();
        
        // Set up click listeners
        setupClickListeners();
        
        // Adjust card colors for current theme
        adjustCardColorsForTheme();
    }
    
    /**
     * Load theme settings and apply the appropriate theme
     */
    private void loadAndApplyTheme() {
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
    
    /**
     * Initialize UI components
     */
    private void initializeUIComponents() {
        btnBackToMain = findViewById(R.id.btnBackToMain);
    }
    
    /**
     * Set up click listeners for buttons
     */
    private void setupClickListeners() {
        btnBackToMain.setOnClickListener(v -> {
            animateButtonClick(v);
            Intent intent = new Intent(AboutActivity.this, MainActivity.class);
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
    
    /**
     * Adjust card backgrounds and text colors based on the current theme
     */
    private void adjustCardColorsForTheme() {
        // Find cards and text elements
        CardView descriptionCard = findViewById(R.id.descriptionCard);
        CardView teamCard = findViewById(R.id.teamCard);
        TextView descriptionText = findViewById(R.id.descriptionText);
        TextView teamHeaderText = findViewById(R.id.teamHeaderText);
        TextView teamMembersText = findViewById(R.id.teamMembersText);
        TextView copyrightText = findViewById(R.id.copyrightText);
        
        // Adjust colors based on theme
        if (currentTheme.equals(THEME_DARK)) {
            // For dark theme
            if (descriptionCard != null) {
                descriptionCard.setCardBackgroundColor(getResources().getColor(R.color.dark_background, null));
            }
            if (teamCard != null) {
                teamCard.setCardBackgroundColor(getResources().getColor(R.color.dark_background, null));
            }
            if (descriptionText != null) {
                descriptionText.setTextColor(getResources().getColor(R.color.dark_text, null));
            }
            if (teamHeaderText != null) {
                teamHeaderText.setTextColor(getResources().getColor(R.color.dark_primary, null));
            }
            if (teamMembersText != null) {
                teamMembersText.setTextColor(getResources().getColor(R.color.dark_text, null));
            }
            if (copyrightText != null) {
                copyrightText.setTextColor(getResources().getColor(R.color.dark_text_secondary, null));
            }
        }
    }
}
