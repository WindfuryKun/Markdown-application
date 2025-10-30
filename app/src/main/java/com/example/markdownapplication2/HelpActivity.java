package com.example.markdownapplication2;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import io.noties.markwon.Markwon;

/**
 * 帮助界面，显示Markdown语法说明
 */
public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        // 设置返回按钮处理
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // 处理返回逻辑
                finish();
            }
        });

        // 初始化UI
        initUI();

        // 系统栏适配
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.help_content), (v, insets) -> {
            androidx.core.graphics.Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initUI() {
        // 设置返回按钮
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        // 初始化Markdown渲染器
        Markwon markwon = Markwon.builder(this).build();
        TextView helpText = findViewById(R.id.help_text);

        // Markdown语法说明文本
        String markdownSyntax = "# Markdown语法说明\n\n" +
                "## 标题\n" +
                "    # 一级标题\n" +
                "    ## 二级标题\n" +
                "    ### 三级标题\n\n" +
                "## 强调\n" +
                "    *斜体文本* 或 _斜体文本_\n" +
                "    **粗体文本** 或 __粗体文本__\n" +
                "    ***粗斜体文本*** 或 ___粗斜体文本___\n\n" +
                "## 列表\n" +
                "### 无序列表\n" +
                "    - 项目一\n" +
                "    - 项目二\n" +
                "    - 项目三\n\n" +
                "### 有序列表\n" +
                "    1. 第一步\n" +
                "    2. 第二步\n" +
                "    3. 第三步\n\n" +
                "## 链接\n" +
                "    [链接文本](https://example.com)\n\n" +
                "## 图片\n" +
                "    ![图片描述](图片URL)\n\n" +
                "## 代码\n" +
                "### 行内代码\n" +
                "    `行内代码`\n\n" +
                "### 代码块示例\n" +
                "    ```java\n" +
                "    public class Example {\n" +
                "        public static void main(String[] args) {\n" +
                "            System.out.println(\"Hello, Markdown!\");\n" +
                "        }\n" +
                "    }\n" +
                "    ```\n\n" +
                "## 引用\n" +
                "    > 这是一段引用文本\n" +
                "    > 可以有多行\n\n" +
                "## 表格\n" +
                "    | 表头1   | 表头2   |\n" +
                "    |---------|---------|\n" +
                "    | 单元格1 | 单元格2 |\n" +
                "    | 单元格3 | 单元格4 |\n\n" +
                "## 分割线\n" +
                "    ---\n\n" +
                "## 任务列表\n" +
                "    - [x] 已完成的任务\n" +
                "    - [ ] 未完成的任务\n";

        // 使用Markwon渲染Markdown内容
        markwon.setMarkdown(helpText, markdownSyntax);
    }
}