package com.example.markdownapplication2;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.widget.SwitchCompat;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * 设置界面
 * 提供应用主题设置等功能
 */
public class SettingsActivity extends AppCompatActivity {
    private SwitchCompat themeSwitch;
    private TextView themeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // 设置返回按钮处理
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // 处理返回逻辑
                finish();
            }
        });

        // 初始化UI组件
        initUI();

        // 设置系统栏适配
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settings_content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initUI() {
        // 设置标题
        TextView title = findViewById(R.id.settings_title);
        title.setText("设置");

        // 返回按钮
        findViewById(R.id.back_button).setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        // 主题设置项
        themeText = findViewById(R.id.theme_text);
        themeSwitch = findViewById(R.id.theme_switch);

        // 检查当前主题模式并设置开关状态
        int currentNightMode = AppCompatDelegate.getDefaultNightMode();
        themeSwitch.setChecked(currentNightMode == AppCompatDelegate.MODE_NIGHT_YES);
        updateThemeText();

        // 设置开关点击事件
        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // 切换到深色模式
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                // 切换到浅色模式
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            // 更新主题文本
            updateThemeText();
            // 重新创建Activity以应用新的主题设置
            recreate();
        });
    }

    private void updateThemeText() {
        // 更新主题文本显示
        boolean isDarkMode = themeSwitch.isChecked();
        themeText.setText(isDarkMode ? "深色模式 (当前)" : "浅色模式 (当前)");
    }
}