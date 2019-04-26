package ap.efficient_farming;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AddEquipment extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spinnerType, spinnerMode;
    private String IMAGE_SELECTED="IMAGE SELECTED";
    public static final int GET_FROM_GALLERY = 3;
    private static final String[] type = {"Tractor", "Baler", "Combine", "Plow","Mower","Planter","Sprayer"};
    private static final String[] mode = {"per hour","per day","per week","per month", "per hectare"};
    Button UploadImage;
    Button registerEquipment;
    EditText equipmentTitle;
    EditText equipmentDesc;
    EditText equipmentRate;
    private StorageReference mStorageRef;
    String img_url;
    private ProgressDialog progressDialog;
    FirebaseFirestore db;
    EditText contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );*/
        setContentView(R.layout.activity_add_equipment);
        spinnerType = (Spinner) findViewById(R.id.typeSpinner);
        spinnerMode = (Spinner) findViewById(R.id.SpinnerMode);
        UploadImage = (Button) findViewById(R.id.UploadImage);
        registerEquipment = findViewById(R.id.RegisterEquipment);
        equipmentTitle = findViewById(R.id.EquipmentTitle);
        equipmentDesc = findViewById(R.id.EquipmentDesc);
        equipmentRate = findViewById(R.id.equipRate);
        contact = findViewById(R.id.contact);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        progressDialog = new ProgressDialog(this);
        db = FirebaseFirestore.getInstance();

        UploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);

            }
        });

        registerEquipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadData();
            }
        });

        ArrayAdapter<String> adapterType = new ArrayAdapter<String>(this,
                R.layout.spinner_item, type);
        ArrayAdapter<String> adapterMode = new ArrayAdapter<String>(this,
                R.layout.spinner_item, mode);
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapterType);
        spinnerMode.setAdapter(adapterMode);
        spinnerType.setSelection(0);
        spinnerMode.setSelection(0);
        spinnerType.setOnItemSelectedListener(this);
        spinnerMode.setOnItemSelectedListener(this);
            }
    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        switch (position) {
            case 0:
                // Whatever you want to happen when the first item gets selected
                break;
            case 1:
                // Whatever you want to happen when the second item gets selected
                break;
            case 2:
                // Whatever you want to happen when the third item gets selected
                break;
            case 3:
                //
                break;
            case 4:
                //
                break;
            case 5:
                //
                break;
            case 6:
                //
                break;
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data1 = baos.toByteArray();
                StorageReference riversRef = mStorageRef;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("images/");
                stringBuilder.append(getSaltString());
                stringBuilder.append(".JPG");
                progressDialog.show();
                progressDialog.setMessage("Uploading Image");
                riversRef.child(stringBuilder.toString()).putBytes(data1).addOnSuccessListener((OnSuccessListener) new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        img_url = taskSnapshot.getDownloadUrl().toString();
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    public void onFailure(@NonNull Exception exception) {
                        exception.printStackTrace();
                        progressDialog.dismiss();
                        Log.d("UploadImage", exception.getMessage());
                    }
                });
                UploadImage.setText(IMAGE_SELECTED);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void uploadData() {
        Map equipment = new HashMap();
        String title = equipmentTitle.getText().toString();
        String type = spinnerType.getSelectedItem().toString();
        String description = equipmentDesc.getText().toString();
        String rate = equipmentRate.getText().toString();
        String time = spinnerMode.getSelectedItem().toString();
        equipment.put("title", title);
        equipment.put("type", type);
        equipment.put("description", description);
        equipment.put("rate", rate);
        equipment.put("time", time);
        equipment.put("contact", contact.getText().toString());
        equipment.put("img_url", img_url);
        equipment.put("user_id", FirebaseAuth.getInstance().getCurrentUser().getUid());
        progressDialog.show();
        progressDialog.setMessage("Uploading details");
        db.collection("equipments").add(equipment).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                progressDialog.dismiss();
                Toast.makeText(AddEquipment.this, "Equipment posted", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(AddEquipment.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) {
            salt.append(SALTCHARS.charAt((int) (rnd.nextFloat() * ((float) SALTCHARS.length()))));
        }
        return salt.toString();
    }
}
