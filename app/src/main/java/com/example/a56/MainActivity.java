package com.example.a56;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private final String baseUrl = "http://192.168.1.64:8080"; // сюда нужно будет вписать ваш IP из ipconfig в CMD

    private TextView list;
    private EditText name, age;
    Button get, send;
    private Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = (EditText) findViewById(R.id.name);
        age = (EditText) findViewById(R.id.age);
        get = (Button) findViewById(R.id.get);
        send = (Button) findViewById(R.id.send);
        list = (TextView) findViewById(R.id.list);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n = name.getText().toString();
                int a = Integer.valueOf(age.getText().toString());
                student = new Student(n, a);
                new SendAsyncTask().execute();
            }
        });
        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetAsyncTask().execute();
            }
        });
    }

    class GetAsyncTask extends AsyncTask{
        List<Student> listStudent;

        @Override
        protected Object doInBackground(Object[] objects) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            StudentService service = retrofit.create(StudentService.class);
            Call<ArrayList<Student>> call = service.getStudents();
            try {
                Response<ArrayList<Student>> userResponse = call.execute();
                listStudent = userResponse.body();
                Log.d("SEND_AND_RETURN", "get list with length " + listStudent.size());
            } catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            String str = "";
            if (listStudent == null) return;
            for (Student s : listStudent){
                str += "Студент: " + s + "\n";
            }
            list.setText(str);
        }
    }

    class SendAsyncTask extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            StudentService service = retrofit.create(StudentService.class);
            Call<Student> call = service.putStudent(student);
            try {
                Response<Student> userResponse = call.execute();
                Student s = userResponse.body();
                Log.d("SEND_AND_RETURN", "Student: " + s);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
