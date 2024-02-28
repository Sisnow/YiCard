// YourActivity.java
package com.example.yicard;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;


import java.io.IOException;
import java.io.InputStream;

public class HelpActivity extends AppCompatActivity {

    private TextView helpTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        helpTextView = findViewById(R.id.tvHelpContent);

        try {
            // 从 activity_help.xml 文件中读取内容
            InputStream inputStream = getAssets().open("activity_help.xml");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            // 将读取的内容设置到 TextView 中
            String xmlContent = new String(buffer, "UTF-8");
            helpTextView.setText(xmlContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
