package uitstart.uit.noteapp.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import uitstart.uit.noteapp.R;
import uitstart.uit.noteapp.activity.MainActivity;
import uitstart.uit.noteapp.model.Note;

import static android.view.View.GONE;

/**
 * Created by Khang on 2/19/2017.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder>{

    private ArrayList<Note> list_adapter=new ArrayList<>();
    private Context context;
    private MainActivity mainActivity;
    public boolean is_selected_all=false;

    public NoteAdapter(Context context,ArrayList<Note> list_adapter) {
        this.list_adapter=list_adapter;
        this.context = context;
        mainActivity=(MainActivity)context;
    }

    @Override
    public NoteAdapter.NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note,parent,false);
        return new NoteViewHolder(itemView,mainActivity);
    }

    @Override
    public void onBindViewHolder(NoteAdapter.NoteViewHolder holder, int position) {
        Note n=list_adapter.get(position);
        holder.tvDate.setText(n.getDate());
        holder.tvTime.setText(n.getTime());
        holder.tvName.setText(n.getName());
        holder.tvDetail.setText(n.getDetai());
        if(!mainActivity.is_actionmenu_mode){
            holder.checkbox_list.setVisibility(GONE);
        }else{
            holder.checkbox_list.setVisibility(View.VISIBLE);
            holder.checkbox_list.setChecked(is_selected_all);
        }


        // init spinner

        ArrayList<String> spinner_list=new ArrayList<>();
        spinner_list.add("");
        spinner_list.add(context.getResources().getString(R.string.delete_action));
        spinner_list.add(context.getResources().getString(R.string.edit_action));
        ArrayAdapter<String> spinner_adapter=new ArrayAdapter<String>(mainActivity,android.R.layout.simple_spinner_item,spinner_list){
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v=null;

                if(position==0){
                    TextView tv=new TextView(getContext());
                    tv.setHeight(0);
                    tv.setVisibility(GONE);
                    v=tv;
                }else
                    v=super.getDropDownView(position,null,parent);

                parent.setVerticalScrollBarEnabled(false);
                return v;
            }
        };
        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.spinner.setAdapter(spinner_adapter);
    }


    @Override
    public int getItemCount() {
        return list_adapter.size();
    }


    public static class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener, AdapterView.OnItemSelectedListener{
        TextView tvDate, tvTime, tvName, tvDetail;
        CheckBox checkbox_list;
        CardView cardView;
        MainActivity mainActivity;
        Spinner spinner;

        public NoteViewHolder(View itemView, final MainActivity mainActivity) {
            super(itemView);
            tvDate= (TextView) itemView.findViewById(R.id.tvDate);
            tvTime= (TextView) itemView.findViewById(R.id.tvTime);
            tvName= (TextView) itemView.findViewById(R.id.tvName);
            tvDetail= (TextView) itemView.findViewById(R.id.tvDetail);
            checkbox_list= (CheckBox) itemView.findViewById(R.id.check_list);
            cardView= (CardView) itemView.findViewById(R.id.cardView);
            spinner= (Spinner) itemView.findViewById(R.id.spinner);

            this.mainActivity=mainActivity;

            cardView.setOnClickListener(this);
            cardView.setOnLongClickListener(this);
            checkbox_list.setOnClickListener(this);
            spinner.setOnItemSelectedListener(this);

        }

        @Override
        public void onClick(View v) {
            int id=v.getId();
            switch (id){
                case R.id.check_list: mainActivity.prepareListSelection(v,getAdapterPosition()); break;
                case R.id.cardView: mainActivity.showInfoOfNote(getAdapterPosition()); break;
            }

        }

        @Override
        public boolean onLongClick(View v) {
            mainActivity.onLongClick(v);
            return true;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (position){
                case 1: mainActivity.deleteNote(getAdapterPosition()); break;
                case 2: mainActivity.editNote(getAdapterPosition()); break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    public void removeListSelected(ArrayList<Note> list_selected){
        for (Note n:list_selected) {
            list_adapter.remove(n);
            mainActivity.noteDataBase.deleteNote(n);
        }
        notifyDataSetChanged();
    }

    public void deleteNote(Note n){
        list_adapter.remove(n);
        mainActivity.noteDataBase.deleteNote(n);
        notifyDataSetChanged();
    }

    public void refreshData(){
        list_adapter.clear();

        for(Note i: mainActivity.noteDataBase.getAllNote()){
            list_adapter.add(i);
        }

        notifyDataSetChanged();
    }

    public void refreshData(String date){
        list_adapter.clear();

        for(Note i: mainActivity.noteDataBase.getNote(date)){
            list_adapter.add(i);
        }

        notifyDataSetChanged();
    }
}
