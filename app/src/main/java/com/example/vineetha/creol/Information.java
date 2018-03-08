package com.example.vineetha.creol;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Statement;

public class Information extends AppCompatActivity {
    Button save;
    EditText email,name,college,skills,works;
    String eml,uname,clg,sk,wk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        save=(Button)findViewById(R.id.saveinfo);
        email=(EditText)findViewById(R.id.Email);
        name=(EditText)findViewById(R.id.Name);
        college=(EditText)findViewById(R.id.College);
        skills=(EditText)findViewById(R.id.Skills);
        works=(EditText)findViewById(R.id.Work);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertInfo id=new InsertInfo();
                id.execute("");
            }
        });
    }
    public class InsertInfo extends AsyncTask<String,Void,String>
    {
        String z = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {
            eml=email.getText().toString().trim();
            uname = name.getText().toString().trim();
            clg = college.getText().toString().trim();
            sk= skills.getText().toString().trim();
            wk = works.getText().toString().trim();

        }

        @Override
        protected void onPostExecute(String r) {
            if(isSuccess) {
                Intent i=new Intent(Information.this,MainActivity.class);
                startActivity(i);
                finish();
            }
            else
                Toast.makeText(Information.this,z,Toast.LENGTH_LONG).show();

        }

        @Override
        protected String doInBackground(String... params) {
            if(eml.equals("")|| uname.equals("") || clg.equals("") || sk.equals("") || wk.equals("")){
                z = "Please fill all the fields";
                Log.e("Error",z);
            }else{
                try {
                    java.sql.Connection con = DatabaseConnection.CONN();
                    if (con == null) {
                        z = "Error in connection with SQL server";
                    }else{
                        Statement stmt = con.createStatement();
                        int flag = stmt.executeUpdate("insert into dbo.Information values('"+eml+"','"+uname+"','"+clg+"','"+sk+"','"+wk+"');");
                        z = "successfull";
                        //Toast.makeText(InsertData.this,z,Toast.LENGTH_LONG).show();
                        isSuccess=true;
                    }
                }catch (Exception ex)
                {
                    isSuccess = false;
                    z = "Exceptions";
                    Log.e("ERROR", ex.getMessage());

                }
            }
            return z;
        }
    }
}