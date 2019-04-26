package ap.efficient_farming;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Iterator;

public class Booking extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    RecyclerView recyclerView;
    private ArrayList<Equipment> equipmentList = new ArrayList();
    EquipmentAdapter adapter;
    Spinner filterType;
    private static final String[] type = {"None", "Tractor", "Baler", "Combine", "Plow", "Mower", "Planter", "Sprayer"};
    ArrayList<Equipment> filteredList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        recyclerView = findViewById(R.id.recycler_view);
        filterType = findViewById(R.id.filter_type);

        adapter = new EquipmentAdapter(equipmentList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fetchEquipments();


        ArrayAdapter<String> adapterType = new ArrayAdapter<String>(this,
                R.layout.spinner_item, type);
        filterType.setAdapter(adapterType);

        filterType.setOnItemSelectedListener(this);
        filterType.setSelection(0);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // do whatever
                        Intent intent = new Intent(Booking.this, SingleDetail.class);
                        intent.putExtra("obj", filteredList.get(position));
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
    }

    private void fetchEquipments() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading data");
        pd.show();
        db.collection("equipments").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    pd.dismiss();
                    Iterator it = ((QuerySnapshot) task.getResult()).iterator();
                    while (it.hasNext()) {
                        DocumentSnapshot document = (DocumentSnapshot) it.next();
                        Equipment equipment = new Equipment();
                        equipment.setObject_id(document.getId());
                        equipment.setContact(document.getString("contact"));
                        equipment.setDescription(document.getString("description"));
                        equipment.setImg_url(document.getString("img_url"));
                        equipment.setRate(document.getString("rate"));
                        equipment.setTime(document.getString("time"));
                        equipment.setType(document.getString("type"));
                        equipment.setTitle(document.getString("title"));
                        equipment.setUser_id(document.getString("user_id"));
                        equipmentList.add(equipment);
                        Log.d("nuclode", equipment.getImg_url() + " " + equipment.getTitle());
                        adapter.notifyDataSetChanged();
                    }
                }
                else {
                    pd.dismiss();
                }
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        switch (position) {
            case 0:
                // Whatever you want to happen when the first item gets selected
                search("");
                break;
            case 1:
                search(filterType.getSelectedItem().toString());
                // Whatever you want to happen when the second item gets selected
                break;
            case 2:
                search(filterType.getSelectedItem().toString());
                // Whatever you want to happen when the third item gets selected
                break;
            case 3:
                search(filterType.getSelectedItem().toString());
                //
                break;
            case 4:
                search(filterType.getSelectedItem().toString());
                //
                break;

            case 5:
                search(filterType.getSelectedItem().toString());
                //
                break;
            case 6:
                search(filterType.getSelectedItem().toString());
                //
                break;

            case 7:
                search(filterType.getSelectedItem().toString());
                //
                break;
        }
    }

    /**
     * Callback method to be invoked when the selection disappears from this
     * view. The selection can disappear for instance when touch is activated
     * or when the adapter becomes empty.
     *
     * @param parent The AdapterView that now contains no selected item.
     */
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void search(String query) {

        filteredList = new ArrayList<>();

        for (int i = 0; i < equipmentList.size(); i++) {

            final String text = equipmentList.get(i).getType();
            if (text.toLowerCase().contains(query.toLowerCase())
                    || text.toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(equipmentList.get(i));
            }
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(Booking.this));
        adapter = new EquipmentAdapter(filteredList, Booking.this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();  // data set changed
    }
}
