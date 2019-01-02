package databinding.rapidera.firebasedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    EditText et_firebasemessage;
    TextView tv_databasevalue;
    DatabaseReference myRef;
    ValueEventListener valueEventListener;
    long start = System.currentTimeMillis();
    long end = System.currentTimeMillis();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_firebasemessage = findViewById(R.id.et_firebasemessage);
        tv_databasevalue = findViewById(R.id.tv_databasevalue);


        FirebaseDatabase database = FirebaseDatabase.getInstance();

         /*
            This sets the Database Offline Power.
            Handles Temporary Network Interuption
         */
        database.setPersistenceEnabled(true);

        myRef = database.getReference("message");

        setListeners();

    }

    private void setListeners() {
        valueEventListener = myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("RT Datasnapshot:",dataSnapshot.toString());
                end = System.currentTimeMillis();
                tv_databasevalue.setText("Calculated Time: "+(end  - start)+" ms\nDatabase Snapshot:\n"+dataSnapshot.getValue().toString());
                et_firebasemessage.setText(dataSnapshot.getValue().toString());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        et_firebasemessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                start = System.currentTimeMillis();
                myRef.setValue(charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myRef.removeEventListener(valueEventListener);
    }
}
