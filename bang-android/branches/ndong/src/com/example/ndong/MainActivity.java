package com.example.ndong;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {  
	  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
          
        // ����ʹ��main.xml����Ľ��沼��  
        setContentView(R.layout.activity_main);  
          
        // ������  
        final TextView text = (TextView) findViewById(R.id.text);  
        Button ok = (Button) findViewById(R.id.ok);  
          
        // �󶨵���¼�������  
        ok.setOnClickListener(new View.OnClickListener() {  
              
            public void onClick(View v) {  
                // ͨ��getResources().getString(R.string.advanced)������ֱ�ӻ���ַ�������  
                // text.setText(getResources().getString(R.string.advanced));  
                text.setText(R.string.test);  
            }  
        });  
    }  
}  
