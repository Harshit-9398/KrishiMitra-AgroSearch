package ap.efficient_farming;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class EquipmentAdapter extends RecyclerView.Adapter<EquipmentAdapter.MyViewHolder> {
    private List<Equipment> equipmentList;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.equipmentImage);
            name = (TextView) itemView.findViewById(R.id.equipmentTitle);
        }
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.single_equipment, parent, false));
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        Equipment equipment = equipmentList.get(position);
        holder.name.setText(equipment.getTitle());
        Picasso.get().load(equipment.getImg_url()).into(holder.image);
    }

    public int getItemCount() {
        return this.equipmentList.size();
    }

    public EquipmentAdapter(List<Equipment> equipmentList, Context context) {
        this.context = context;
        this.equipmentList = equipmentList;
    }
}
