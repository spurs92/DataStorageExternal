package com.spurs.datastorageexternal;

import android.os.Build;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class MainActivity extends AppCompatActivity {

    EditText edit;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit=(EditText)findViewById(R.id.edit);
        text=(TextView)findViewById(R.id.text);
    }

    //데이터 저장
    public void clickSave(View v){
        String data=edit.getText().toString();
        edit.setText("");

        //외장메모리(SDcard)에 저장하려면..가장먼저 sdcard가 있는지?
        String state=Environment.getExternalStorageState();

        //혹시 외장메모리상태(state)가 연결(Mounted)되어 있지 않은가?
        if(!state.equals(Environment.MEDIA_MOUNTED)){
            Toast.makeText(this, "SDcard is not mounted", Toast.LENGTH_SHORT).show();
            return;
        }

        File path; //data.txt 파일이 저장될 곳의 디렉토리 경로
        File[] dirs;

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){ //API 19버전 이상의 폰이냐..
            dirs=getExternalFilesDirs("myFile");
        }else {
            dirs= ContextCompat.getExternalFilesDirs(this,"myFile");
        }

        path=dirs[0];
        File file=new File(path,"data.txt");//경로명, 파일명을 포함한 파일전체경로로

        try {
            FileWriter fw=new FileWriter(file, true); //두번째 파라미터 : 추가(append) 여부
            PrintWriter writer=new PrintWriter(fw);

            writer.print(data);
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //데이터 읽어오기
    public void clickLoad(View v){

        String state=Environment.getExternalStorageState();

        if(!(state.equals(Environment.MEDIA_MOUNTED) || state.equals(Environment.MEDIA_MOUNTED_READ_ONLY))){
            Toast.makeText(this, "외장메모리 없어", Toast.LENGTH_SHORT).show();
            return;
        }

        File[] dirs;
        File path;

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
            dirs=getExternalFilesDirs("myFile");
        }else {
            dirs=ContextCompat.getExternalFilesDirs(this, "myFile");
        }

        path=dirs[0];

        File file = new File(path,"data.txt");//경롸와 파일명 결합한 최종 경로

        try {
            FileReader fr=new FileReader(file);
            BufferedReader reader=new BufferedReader(fr);

            StringBuffer buffer=new StringBuffer();

            String line=reader.readLine();
            while (line!=null){
                buffer.append(line+"\n");
                line=reader.readLine();
            }
            text.setText(buffer.toString());
            reader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
