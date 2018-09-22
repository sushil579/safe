package com.example.sushil.safe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import static android.widget.Toast.*;

public class signup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);



        TextView signup2 = (TextView) findViewById(R.id.reg2);

        // Set a click listener on that View
        signup2.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the family category is clicked on.
            @Override
            public void onClick(View view) {
                // Create a new intent to open the {@link FamilyActivity}
                Toast.makeText(signup.this,"Registered Succesfully", LENGTH_SHORT).show();
                Intent familyIntent = new Intent(signup.this, LoginActivity.class);
                // Start the new activity
                startActivity(familyIntent);
            }
        });
    }
}
