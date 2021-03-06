package com.wise.vineetha.creol;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;

import java.sql.ResultSet;
import java.sql.Statement;

public class Display extends AppCompatActivity {
    BootstrapButton dlt,sv,edt;
    BootstrapEditText name,description,requirements,category,duration;
    String email,pname,pdesc;
    private static final String TAG;

    static {
        TAG = "MyActivity";
    }
    SharedPreferences settings;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        Log.i(TAG, "onCreate: started");
        getIncomingIntent();
        sv.setVisibility(View.INVISIBLE);
        edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sv.setVisibility(View.VISIBLE);
                sv.setClickable(true);
                description.setEnabled(true);
                requirements.setEnabled(true);
                category.setEnabled(true);
                duration.setEnabled(true);

            }
        });

        sv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String des=description.getText().toString().trim();
                String req=requirements.getText().toString().trim();
                String dur=duration.getText().toString().trim();
                String cat=category.getText().toString().trim();
                if(des.equals("")||req.equals("")||dur.equals("")||cat.equals(""))
                    Toast.makeText(Display.this,"fill all fields",Toast.LENGTH_LONG).show();
                else
                {
                    try {
                        java.sql.Connection con = DatabaseConnection.CONN();
                        if (con == null) {
                            Toast.makeText(Display.this,"check internet connection",Toast.LENGTH_LONG).show();
                        } else {
                            String query = "update ProjectDetails set descriptn='"+des+"',requirements='"+req+"',duration='"+dur+"',category='"+cat+"' where email='"+email+"' and title='"+pname+"'";
                            Statement stmt = con.createStatement();
                            stmt.executeUpdate(query);
                        }
                    } catch (Exception ex) {
                        Log.e("ERROR", ex.getMessage());
                        Toast.makeText(Display.this,ex.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
                sv.setVisibility(View.INVISIBLE);
                description.setEnabled(false);
                requirements.setEnabled(false);
                duration.setEnabled(false);
                category.setEnabled(false);
            }
        });
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Display.this,"cannot edit",Toast.LENGTH_SHORT);
            }
        });

        dlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean s=false;
                try {
                    java.sql.Connection con = DatabaseConnection.CONN();
                    if (con == null) {
                        Toast.makeText(Display.this,"check internet connection",Toast.LENGTH_LONG).show();
                    } else {
                        String query = "delete from ProjectDetails where descriptn='"+pdesc+"' and email='"+email+"' and title='"+pname+"';";
                        Statement stmt = con.createStatement();
                        stmt.executeUpdate(query);
                        s=true;
                    }
                } catch (Exception ex) {
                    Log.e("ERROR", ex.getMessage());
                    Toast.makeText(Display.this,"error occurred while deleting",Toast.LENGTH_LONG).show();
                    s=false;
                }
                if(s) {
                    Toast.makeText(Display.this,"Deletion Successful", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(Display.this, MainActivity.class));

                }
            }
        });


    }

    private void getIncomingIntent(){
        if(getIntent().hasExtra("project-name") && getIntent().hasExtra("project-description") &&getIntent().hasExtra("idx")) {
            pname = getIntent().getStringExtra("project-name");
            pdesc = getIntent().getStringExtra("project-description");
            int i=getIntent().getIntExtra("idx",0);
            setIntent(pname, pdesc,i);
        }
    }

    private void setIntent(String pname, String pdesc,int i) {
        name = (BootstrapEditText) findViewById(R.id.ptitle);
        description = (BootstrapEditText) findViewById(R.id.pdes);
        requirements=(BootstrapEditText)findViewById(R.id.preq);
        category=(BootstrapEditText)findViewById(R.id.pcat);
        duration=(BootstrapEditText)findViewById(R.id.pdur);
        dlt=(BootstrapButton)findViewById(R.id.delete);
        edt=(BootstrapButton)findViewById(R.id.pedit);
        sv=(BootstrapButton)findViewById(R.id.psave);
        settings=this.getSharedPreferences(Information.PREFS_NAME, Context.MODE_PRIVATE);
        email=settings.getString("email",null);
        description.setClickable(false);
        try {
            java.sql.Connection con = DatabaseConnection.CONN();
            if (con == null) {
                Toast.makeText(this,"check internet connection",Toast.LENGTH_LONG).show();
            } else {
                String query = "select * from dbo.ProjectDetails where email='"+email+"' and title='"+pname+"' and descriptn='"+pdesc+"';";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next()) {
                    requirements.setText(rs.getString(4));
                    category.setText(rs.getString(6));
                    duration.setText(rs.getString(5));
                }
            }
        } catch (Exception ex) {
            Log.e("ERROR", ex.getMessage());
            Toast.makeText(this,"data not found",Toast.LENGTH_LONG).show();

        }
        name.setText(pname);
        description.setText(pdesc);
        name.setEnabled(false);
        description.setEnabled(false);
        requirements.setEnabled(false);
        category.setEnabled(false);
        duration.setEnabled(false);
        if(i==1){
            dlt.setVisibility(View.VISIBLE);
            edt.setVisibility(View.VISIBLE);
        }
    }
}
