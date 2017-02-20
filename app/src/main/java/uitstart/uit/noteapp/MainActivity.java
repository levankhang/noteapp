package uitstart.uit.noteapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener, View.OnClickListener {

    public static final int REQUES_NEWNODE=0;
    public static final int REQUES_UPDATE=1;

    public boolean is_actionmenu_mode=false;

    private Toolbar toolbar;
    private RecyclerView rvNote;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Note> list=new ArrayList<>();
    private ArrayList<Note> list_selected=new ArrayList<>();
    private int counter=0;

    public PublicDateTime  publicDateTime;

    private ImageView imghome, imgback;

    private TextView tvAppName, tvCounter;

    public NoteAdapter adater;
    public NoteDataBase noteDataBase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
        setSupportActionBar(toolbar);
        initRecyclerViewNote();
        addEvents();
        loadData();
        publicDateTime =new PublicDateTime(this);
    }

    private void loadData() {
        adater.refreshData();
    }


    private void addEvents() {
        imgback.setOnClickListener(this);
    }

    private void initRecyclerViewNote() {
        noteDataBase=new NoteDataBase(this);
        layoutManager=new LinearLayoutManager(this);
        list=new ArrayList<Note>();
        adater=new NoteAdapter(this,list);
        rvNote.setLayoutManager(layoutManager);
        rvNote.setHasFixedSize(true);
        rvNote.setAdapter(adater);
    }

    private void addControls() {
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        imgback= (ImageView) findViewById(R.id.imgback);
        imghome= (ImageView) findViewById(R.id.imghome);
        tvAppName= (TextView) findViewById(R.id.tvAppName);
        tvCounter= (TextView) findViewById(R.id.tvCounter);
        rvNote= (RecyclerView) findViewById(R.id.rvNote);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case R.id.item_action_add: addNewNote(); break;
            case R.id.item_action_date: viewNoteOfDay(); break;
            case R.id.item_action_all: viewAllNote(); break;
            case R.id.item_action_report: openTroLyAo(); break;
            case R.id.item_action_set: openSetting(); break;
            case R.id.item_action_unselect_all: unSelectAll(); break;
            case R.id.item_action_delete: deleteSelected(); break;
            case R.id.item_action_select_all: selecteAll(); break;

        }

        return true;
    }

    private void viewNoteOfDay() {
        publicDateTime.changedDateAction(true,false);
    }

    private void openSetting() {
    }

    private void viewAllNote() {
        loadData();
    }

    private void addNewNote() {
        Intent newNodeIntent=new Intent(MainActivity.this,NoteActionActivity.class);
        startActivityForResult(newNodeIntent,REQUES_NEWNODE);
    }

    private void deleteSelected() {
        final Dialog dialog_confirm=new Dialog(this);

        dialog_confirm.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_confirm.setContentView(R.layout.dialog_delete_cofirm);

        Button btnConfirm = (Button) dialog_confirm.findViewById(R.id.btnConfirm);
        Button btnClose= (Button) dialog_confirm.findViewById(R.id.btnClose);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_confirm.dismiss();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adater.removeListSelected(list_selected);
                onActionModeOff();
                dialog_confirm.dismiss();

            }
        });

        int width= (int) (getResources().getDisplayMetrics().widthPixels*0.90);
        int height= (int) (getResources().getDisplayMetrics().heightPixels*0.30);

        dialog_confirm.getWindow().setLayout(width,height);

        dialog_confirm.show();


    }

    private void openTroLyAo() {
        Intent trolyao=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/chatbot.LeVanKhang/?fref=ts"));
        startActivity(trolyao);
    }

    private void unSelectAll() {
        adater.is_selected_all=false;
        adater.notifyDataSetChanged();
        list_selected.clear();
        counter=0;
        updateCounter(counter);
    }

    private void selecteAll() {
        adater.is_selected_all=true;
        adater.notifyDataSetChanged();
        list_selected.clear();
        counter=0;
        for(Note i:list){
            list_selected.add(i);
            counter++;
        }

        updateCounter(counter);
    }

    public void prepareListSelection(View v, int adapterPosition) {
        if(((CheckBox)v).isChecked()){
            list_selected.add(list.get(adapterPosition));
            counter++;
            updateCounter(counter);
        }else {
            list_selected.remove(list.get(adapterPosition));
            counter--;
            updateCounter(counter);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        onActionModeOn();
        return true;
    }

    private void onActionModeOn() {
        is_actionmenu_mode=true;

        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu_action);

        tvAppName.setVisibility(GONE);
        tvCounter.setVisibility(View.VISIBLE);
        imgback.setVisibility(View.VISIBLE);
        imghome.setVisibility(GONE);

        adater.notifyDataSetChanged();
    }

    private void onActionModeOff(){
        is_actionmenu_mode=false;

        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu_main);

        tvAppName.setVisibility(View.VISIBLE);
        tvCounter.setVisibility(GONE);
        imgback.setVisibility(GONE);
        imghome.setVisibility(View.VISIBLE);


        adater.notifyDataSetChanged();

        counter=0;
        updateCounter(counter);
        list_selected.clear();

    }

    private void updateCounter(int counter){
        tvCounter.setText("Có "+counter+" item được chọn");
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.imgback: onBackPressed(); break;
        }

    }

    @Override
    public void onBackPressed() {
        if(is_actionmenu_mode){
            onActionModeOff();
            adater.is_selected_all=false;
        }else{
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data!=null){
            Note note_result= (Note) data.getSerializableExtra("result");
            if(resultCode==REQUES_NEWNODE){
                noteDataBase.insertNote(note_result);
                adater.refreshData();
            }

            if(requestCode==REQUES_UPDATE){
                noteDataBase.updateNote(note_result);
                adater.refreshData();
            }
        }
    }

    public void showInfoOfNote(int adapterPosition) {
        Note n=list.get(adapterPosition);
        final Dialog dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_showinfo_layout);
        dialog.setCancelable(true);

        int width= (int) (getResources().getDisplayMetrics().widthPixels*0.90);
        int height= (int) (getResources().getDisplayMetrics().heightPixels*0.70);
        dialog.getWindow().setLayout(width,height);

        TextView tvName= (TextView) dialog.findViewById(R.id.tvName);
        TextView tvDetail= (TextView) dialog.findViewById(R.id.tvDetail);
        TextView tvTime= (TextView) dialog.findViewById(R.id.tvTime);
        TextView tvDate= (TextView) dialog.findViewById(R.id.tvDate);
        Button btnClose= (Button) dialog.findViewById(R.id.btnClose);

        tvName.setText(n.getName());
        tvDetail.setText(n.getDetai());
        tvTime.setText(n.getTime());
        tvDate.setText(n.getDate());


        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    public void deleteNote(final int position){
        final Dialog dialog_confirm=new Dialog(this);

        dialog_confirm.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_confirm.setContentView(R.layout.dialog_delete_cofirm);

        Button btnConfirm = (Button) dialog_confirm.findViewById(R.id.btnConfirm);
        Button btnClose= (Button) dialog_confirm.findViewById(R.id.btnClose);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adater.notifyDataSetChanged();
                dialog_confirm.dismiss();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adater.deleteNote(list.get(position));
                dialog_confirm.dismiss();
            }
        });

        int width= (int) (getResources().getDisplayMetrics().widthPixels*0.90);
        int height= (int) (getResources().getDisplayMetrics().heightPixels*0.30);

        dialog_confirm.getWindow().setLayout(width,height);

        dialog_confirm.show();

    }

    public void editNote(int postition){
        Note n=list.get(postition);
        Intent intent=new Intent(MainActivity.this,NoteActionActivity.class);
        intent.putExtra("send",n);
        startActivityForResult(intent,REQUES_UPDATE);
    }

}
