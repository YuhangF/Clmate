package com.example.climate;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private List<Integer> idList = new ArrayList<>();
    private List<Integer> pidList = new ArrayList<>();
    private List<String> city_nameList = new ArrayList<>();
    private List<String> city_codeList = new ArrayList<>();
    ArrayAdapter simpleAdapter;
    Button OK,MyConcern;
    EditText Research;
    ListView ProvinceList;


    private void parseJSONWithJSONObject(String jsonData){
            //解析String类型的Data信息
        try{
            JSONArray jsonArray = new JSONArray(jsonData);
            for(int i = 0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Integer id = jsonObject.getInt("id");
                Integer pid = jsonObject.getInt("pid");
                String city_code = jsonObject.getString("city_code");
                String city_name = jsonObject.getString("city_name");
                if(pid == 0 ) {
                    //将id、pid、city_code、city_name添加到对应列表中
                    idList.add(id);
                    pidList.add(pid);
                    city_codeList.add(city_code);
                    city_nameList.add(city_name);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String getJson(String fileName, Context context) {
        //从json文件中获得字符串信息
        StringBuilder stringBuilder = new StringBuilder();
        try {
            InputStream is = context.getAssets().open(fileName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OK = findViewById(R.id.ok);
        Research = findViewById(R.id.research);
        ProvinceList = findViewById(R.id.provincelist);
        OK.setOnClickListener(this);
        MyConcern = findViewById(R.id.myconcern);
        MyConcern.setOnClickListener(this);

        String responseData = getJson("data.json",this);//从data.json文件中获得String城市信息
        parseJSONWithJSONObject(responseData);


        simpleAdapter = new ArrayAdapter(MainActivity.this,
                android.R.layout.simple_list_item_1,city_nameList);

        ProvinceList.setAdapter(simpleAdapter);
        ProvinceList = findViewById(R.id.provincelist);
        ProvinceList.setOnItemClickListener(new AdapterView.OnItemClickListener(){      //配置ArrayList点击按钮
            @Override
            public void  onItemClick(AdapterView<?> parent, View view , int position , long id){
                //实现响应操作
                int tran = idList.get(position);
                Intent intent = new Intent(MainActivity.this,
                        com.example.climate.SecondActivity.class);
                intent.putExtra("tran",tran);
                startActivity(intent);//调到SecondActivity.class类
            }
        });




    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.ok:
                String researchcitycode = String.valueOf(Research.getText());
                if(researchcitycode.length()>9){
                    Toast.makeText(this,"数字长度不能大于九位！",Toast.LENGTH_LONG).show();
                }
                else if(researchcitycode.length()==0){
                    Toast.makeText(this,"输入内容为空",Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(MainActivity.this,
                            com.example.climate.Climate.class);

                        intent.putExtra("trancitycode", researchcitycode);
                        System.out.println("进入Activity");
                        startActivity(intent);

                }

                break;
            case R.id.myconcern:

                SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
                String citycode = pref.getString("citycode","");
                Intent intent = new Intent(MainActivity.this, com.example.climate.Climate.class);
                intent.putExtra("trancitycode",citycode);
                startActivity(intent);


                intent = new Intent(MainActivity.this, com.example.climate.MyConcernList.class);
                startActivity(intent);
                break;
        }
    }



}
