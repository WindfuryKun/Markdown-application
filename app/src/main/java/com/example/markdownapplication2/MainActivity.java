package com.example.markdownapplication2;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.core.view.GravityCompat;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.tabs.TabLayout;

import io.noties.markwon.Markwon;

/**
 * Markdown编辑器主Activity
 * 提供Markdown编辑、预览和基本的文件操作功能
 */

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private TextView previewText;
    private TabLayout tabLayout;
    private Markwon markwon;
    private String currentFileName = "未命名.md";
    private DrawerLayout drawerLayout;
    private ListView sideMenuList;
    private String[] menuItems = {"设置", "帮助", "关于"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 确保在设置内容视图前应用主题设置
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // 初始化UI组件
        initUI();

        // 初始化Markwon库
        initMarkwon();

        // 设置标签页切换监听器
        setupTabLayoutListener();

        // 设置文本变化监听器
        setupTextWatcher();

        // 设置按钮点击事件
        setupButtonListeners();
        
        // 设置返回按钮处理
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // 如果侧边菜单打开，则先关闭
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawers();
                } else {
                    // 如果没有其他处理，则继续默认的返回行为
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                    setEnabled(true);
                }
            }
        });

        // 系统栏适配
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initUI() {
        editText = findViewById(R.id.edit_text);
        previewText = findViewById(R.id.preview_text);
        tabLayout = findViewById(R.id.tab_layout);
        drawerLayout = findViewById(R.id.drawer_layout);
        sideMenuList = findViewById(R.id.side_menu_list);
        
        // 设置侧边菜单
        setupSideMenu();
    }

    private void initMarkwon() {
        // 初始化Markwon库用于Markdown解析和渲染
        markwon = Markwon.builder(this)
                .build();
    }

    private void setupTabLayoutListener() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) { // 编辑标签
                    editText.setVisibility(View.VISIBLE);
                    previewText.setVisibility(View.GONE);
                } else { // 预览标签
                    editText.setVisibility(View.GONE);
                    previewText.setVisibility(View.VISIBLE);
                    // 更新预览内容
                    updatePreview();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // 不需要处理
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // 不需要处理
            }
        });
    }

    private void setupTextWatcher() {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 不需要处理
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 如果当前是预览模式，自动更新预览
                if (tabLayout.getSelectedTabPosition() == 1) {
                    updatePreview();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 不需要处理
            }
        });
    }

    private void updatePreview() {
        String markdown = editText.getText().toString();
        // 使用Markwon渲染Markdown内容
        markwon.setMarkdown(previewText, markdown);
    }

    private void setupButtonListeners() {
        // 汉堡菜单按钮
        ImageButton menuButton = findViewById(R.id.menu_button);
        menuButton.setOnClickListener(v -> showSideMenu());

        // 更多操作按钮
        ImageButton moreButton = findViewById(R.id.more_button);
        moreButton.setOnClickListener(v -> showMoreOptions());

        // 底部导航栏已移除，相关按钮代码已删除
    }

    private void showSideMenu() {
        // 打开侧边菜单
        drawerLayout.openDrawer(GravityCompat.START);
    }
    
    private void setupSideMenu() {
        // 使用ArrayAdapter设置菜单列表
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 
                android.R.layout.simple_list_item_1, menuItems);
        sideMenuList.setAdapter(adapter);

        // 设置菜单项点击事件
        sideMenuList.setOnItemClickListener((parent, view, position, id) -> {
            handleMenuItemClick(position);
            drawerLayout.closeDrawers();
        });
    }
    
    private void handleMenuItemClick(int position) {
        switch (position) {
            case 0: // 设置
                // 启动设置界面
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            case 1: // 帮助
                // 启动帮助界面
                Intent helpIntent = new Intent(this, HelpActivity.class);
                startActivity(helpIntent);
                break;
            case 2: // 关于
                // 打开关于页面的网页链接
                String aboutUrl = "https://gitee.com/rock654/markdown-application/blob/master/README.md";
                Intent webIntent = new Intent(Intent.ACTION_VIEW);
                webIntent.setData(android.net.Uri.parse(aboutUrl));
                startActivity(webIntent);
                break;
        }
    }

    // 分享功能已移除

    private void showMoreOptions() {
        // 显示更多选项的弹出菜单
        ImageButton moreButton = findViewById(R.id.more_button);
        PopupMenu popupMenu = new PopupMenu(this, moreButton);
        popupMenu.getMenuInflater().inflate(R.menu.file_menu, popupMenu.getMenu());
        
        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_new) {
                newFile();
                return true;
            } else if (id == R.id.menu_open) {
                openFile();
                return true;
            } else if (id == R.id.menu_save) {
                saveFile();
                return true;
            } else if (id == R.id.menu_save_as) {
                saveFileAs();
                return true;
            }
            return false;
        });
        
        popupMenu.show();
    }

    private void showFormatOptions() {
        // 显示格式化选项的弹出菜单
        showToast("格式化选项");
    }

    private static final int REQUEST_OPEN_FILE = 1;
    private static final int REQUEST_SAVE_FILE = 2;
    private String currentFilePath = null;

    private void newFile() {
        // 检查是否需要保存当前文件
        if (!editText.getText().toString().isEmpty()) {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            builder.setTitle("提示")
                   .setMessage("当前内容尚未保存，是否继续新建？")
                   .setPositiveButton("继续", (dialog, which) -> createNewFile())
                   .setNegativeButton("取消", null)
                   .show();
        } else {
            createNewFile();
        }
    }

    private void createNewFile() {
        editText.setText("");
        currentFileName = "未命名.md";
        currentFilePath = null;
        TextView fileNameView = findViewById(R.id.file_name);
        fileNameView.setText(currentFileName);
        showToast("新建文件");
    }

    private void openFile() {
        // 检查是否需要保存当前文件
        if (!editText.getText().toString().isEmpty()) {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            builder.setTitle("提示")
                   .setMessage("当前内容尚未保存，是否继续打开？")
                   .setPositiveButton("继续", (dialog, which) -> openFilePicker())
                   .setNegativeButton("取消", null)
                   .show();
        } else {
            openFilePicker();
        }
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("text/markdown");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // 支持多种文本文件类型
        String[] mimeTypes = {"text/markdown", "text/plain", "text/*"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(intent, REQUEST_OPEN_FILE);
    }

    private void saveFile() {
        if (currentFilePath == null) {
            // 如果是新文件，调用另存为
            saveFileAs();
        } else {
            // 保存到现有文件
            writeToFile(currentFilePath, editText.getText().toString());
        }
    }

    private void saveFileAs() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/markdown");
        intent.putExtra(Intent.EXTRA_TITLE, currentFileName);
        startActivityForResult(intent, REQUEST_SAVE_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_OPEN_FILE && data != null) {
                // 打开文件
                android.net.Uri uri = data.getData();
                if (uri != null) {
                    readFromFile(uri);
                }
            } else if (requestCode == REQUEST_SAVE_FILE && data != null) {
                // 保存文件
                android.net.Uri uri = data.getData();
                if (uri != null) {
                    currentFilePath = uri.toString();
                    // 获取文件名
                    String fileName = uri.getLastPathSegment();
                    if (fileName != null && fileName.contains("/") && fileName.lastIndexOf('/') < fileName.length() - 1) {
                        fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
                    }
                    if (fileName != null) {
                        currentFileName = fileName;
                        TextView fileNameView = findViewById(R.id.file_name);
                        fileNameView.setText(currentFileName);
                    }
                    writeToFile(uri.toString(), editText.getText().toString());
                }
            }
        }
    }

    private void readFromFile(android.net.Uri uri) {
        try {
            // 读取文件内容
            java.io.InputStream inputStream = getContentResolver().openInputStream(uri);
            if (inputStream != null) {
                java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                inputStream.close();
                
                // 设置编辑框内容
                editText.setText(stringBuilder.toString());
                
                // 更新文件名和路径
                currentFilePath = uri.toString();
                String fileName = uri.getLastPathSegment();
                if (fileName != null && fileName.contains("/") && fileName.lastIndexOf('/') < fileName.length() - 1) {
                    fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
                }
                if (fileName != null) {
                    currentFileName = fileName;
                    TextView fileNameView = findViewById(R.id.file_name);
                    fileNameView.setText(currentFileName);
                }
                
                showToast("文件打开成功: " + currentFileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showToast("文件打开失败: " + e.getMessage());
        }
    }

    private void writeToFile(String uriString, String content) {
        try {
            android.net.Uri uri = android.net.Uri.parse(uriString);
            java.io.OutputStream outputStream = getContentResolver().openOutputStream(uri, "wt");
            if (outputStream != null) {
                outputStream.write(content.getBytes());
                outputStream.close();
                showToast("文件保存成功: " + currentFileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showToast("文件保存失败: " + e.getMessage());
        }
    }

    private void showToast(String message) {
        // 简单的Toast提示，实际项目中可以使用更友好的提示方式
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show();
    }

    // 分享功能已移除，相关类和方法已删除
}