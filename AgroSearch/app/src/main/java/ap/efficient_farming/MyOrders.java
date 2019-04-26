package ap.efficient_farming;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Iterator;

public class MyOrders extends AppCompatActivity {

    RecyclerView orderRv;
    private ArrayList<Equipment> equipmentList = new ArrayList();
    EquipmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        orderRv = findViewById(R.id.ordersRV);
        adapter = new EquipmentAdapter(equipmentList, this);
        orderRv.setLayoutManager(new LinearLayoutManager(this));
        orderRv.setAdapter(adapter);

        fetchOrders();

    }

    private void fetchOrders() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading data");
        pd.show();
        db.collection("bookings").whereEqualTo("user_id", FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    pd.dismiss();
                    Log.d("abcde", "order found " + FirebaseAuth.getInstance().getCurrentUser().getUid());
                    Iterator it = ((QuerySnapshot) task.getResult()).iterator();
                    while (it.hasNext()) {
                        Log.d("abcde", "in while loop");
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
                    }
                    Log.d("abcde", "Adapter data");
                    adapter.notifyDataSetChanged();
                }
                else {
                    Log.d("abcde", "nothing found");
                    pd.dismiss();
                }
            }
        });
    }


}
