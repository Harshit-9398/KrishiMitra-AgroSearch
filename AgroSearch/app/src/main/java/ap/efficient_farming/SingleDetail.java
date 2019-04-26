package ap.efficient_farming;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class SingleDetail extends AppCompatActivity {

    ImageView img;
    TextView title, type, desc, contact, rate, time;
    Button bookeqmt;
    FirebaseFirestore db;
    Equipment equipment;
    private boolean mCallingPermissionGranted;
    private static final int PERMISSIONS_CALL = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_detail);

        equipment = (Equipment) getIntent().getSerializableExtra("obj");

        img = findViewById(R.id.detailImg);
        title = findViewById(R.id.detailTitle);
        type = findViewById(R.id.detailType);
        desc = findViewById(R.id.detailDesc);
        contact = findViewById(R.id.detailContact);
        rate = findViewById(R.id.detailRate);
        time = findViewById(R.id.detailTime);
        bookeqmt = findViewById(R.id.bookEquipment);

        db = FirebaseFirestore.getInstance();
        title.setText(equipment.getTitle());
        type.setText(equipment.getType());
        desc.setText(equipment.getDescription());
        contact.setText(equipment.getContact());
        rate.setText(equipment.getRate());
        time.setText(equipment.getTime());
        Picasso.get().load(equipment.getImg_url()).into(img);

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mCallingPermissionGranted) {
                    doCall();
                } else {
                    getCallingPermission();
                }
            }
        });
        bookeqmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                equipment.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
                db.collection("bookings").add(equipment).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(SingleDetail.this, "Booking confirmed", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SingleDetail.this, "Booking Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void getCallingPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            mCallingPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    PERMISSIONS_CALL);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mCallingPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_CALL: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mCallingPermissionGranted = true;
                    doCall();
                }
            }
        }
    }

    private void doCall() {
        if (ActivityCompat.checkSelfPermission(SingleDetail.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            getCallingPermission();
        } else {
            String phone_no = equipment.getContact();
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phone_no));
            startActivity(callIntent);
        }
    }

}
