package uitstart.uit.noteapp.activity;

import android.app.AlarmManager;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.Toast;

import com.akexorcist.localizationactivity.LocalizationActivity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import uitstart.uit.noteapp.database.SettingDataBase;
import uitstart.uit.noteapp.model.MySetting;
import uitstart.uit.noteapp.model.Note;
import uitstart.uit.noteapp.adapter.NoteAdapter;
import uitstart.uit.noteapp.database.NoteDataBase;
import uitstart.uit.noteapp.model.PublicDateTime;
import uitstart.uit.noteapp.R;

import static android.view.View.GONE;

public class MainActivity extends LocalizationActivity implements View.OnLongClickListener, View.OnClickListener {

    public static final int REQUES_NEWNODE=0;
    public static final int REQUES_UPDATE=1;

    public boolean is_actionmenu_mode=false;

    private Toolbar toolbar;
    private RecyclerView rvNote;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Note> list=new ArrayList<>();
    private ArrayList<Note> list_selected=new ArrayList<>();
    private int counter=0;

    public PublicDateTime publicDateTime;

    private ImageView imghome, imgback;

    private TextView tvAppName, tvCounter;

    public static NoteAdapter adater;
    public static NoteDataBase noteDataBase;
    public static AlarmManager alarmManager;
    public static SettingDataBase settingDataBase;

    public static String current_language_code="en";
    public static MySetting mySetting;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        addControls();
        setSupportActionBar(toolbar);
        initRecyclerViewNote();
        addEvents();
        loadDataNote();
        loadDataSetting();

        publicDateTime =new PublicDateTime();
        alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);

        resetLanguage(mySetting.getLanguage_code());

    }

    private void loadDataSetting() {
        settingDataBase=new SettingDataBase(this);
        settingDataBase.insertSettingDefault(); // mặc định cho phép thông báo và ngôn ngữ tiếng anh
        mySetting=settingDataBase.getMySetting();
    }

    @Override
    public void onResume() {
        super.onResume();
        resetLanguage(mySetting.getLanguage_code());
    }

    public void resetLanguage(String lang) {

        if(!current_language_code.equals(lang)) {
            setLanguage(lang);
            current_language_code=lang;
        }
    }


    private void loadDataNote() {
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
        publicDateTime.viewNoteOfDay(this);
    }

    private void openSetting() {
        Intent intentSetting=new Intent(MainActivity.this,SettingActivity.class);
        startActivity(intentSetting);
    }

    private void viewAllNote() {
        loadDataNote();
    }

    private void addNewNote() {
        Intent newNodeIntent=new Intent(MainActivity.this,NoteActionActivity.class);
        startActivityForResult(newNodeIntent,REQUES_NEWNODE);
    }

    private void deleteSelected() {
        final Dialog dialog_confirm=new Dialog(this);

        dialog_confirm.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_confirm.setContentView(R.layout.dialog_confirm);
        dialog_confirm.getWindow().setBackgroundDrawableResource(R.color.transparent);

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

                //xóa alarm
                for(Note i:list_selected)
                    alarmManager.cancel(i.createPendingIntent(MainActivity.this));

                adater.removeListSelected(list_selected);
                onActionModeOff();
                dialog_confirm.dismiss();

            }
        });

        int width= (int) (getResources().getDisplayMetrics().widthPixels*0.70);
        int height= (int) (getResources().getDisplayMetrics().heightPixels*0.20);

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
        updateCounter(counter);

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
        tvCounter.setText(getResources().getString(R.string.count)+" "+counter);
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

                if (resultCode == REQUES_NEWNODE) {
                    long id=noteDataBase.insertNote(note_result);
                    adater.refreshData();

                    note_result.setId(Integer.parseInt(id+""));

                    //test thêm alarm
                    alarmManager.set(AlarmManager.RTC_WAKEUP,
                            createCalendar(note_result.getDate(),note_result.getTime()).getTimeInMillis(),
                            note_result.createPendingIntent(this));

                    Toast.makeText(this,getResources().getString(R.string.added),Toast.LENGTH_LONG).show();
                }

                if (requestCode == REQUES_UPDATE) {
                    Note n= (Note) noteDataBase.getNote(note_result.getId());

                    // sửa lại thông tin alarm
                    alarmManager.cancel(n.createPendingIntent(MainActivity.this));
                    alarmManager.set(AlarmManager.RTC_WAKEUP,
                            createCalendar(note_result.getDate(),note_result.getTime()).getTimeInMillis(),
                            note_result.createPendingIntent(this));

                    noteDataBase.updateNote(note_result);
                    adater.refreshData();

                    Toast.makeText(this,getResources().getString(R.string.updated),Toast.LENGTH_LONG).show();
                }
        }
    }

    public void showInfoOfNote(int adapterPosition) {
        Intent showInfoIntent=new Intent(MainActivity.this,ShowInfoActivity.class);
        showInfoIntent.putExtra("note",list.get(adapterPosition));
        startActivity(showInfoIntent);
    }


    public void deleteNote(final int position){
        adater.notifyDataSetChanged();
        final Dialog dialog_confirm=new Dialog(this);

        dialog_confirm.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_confirm.setContentView(R.layout.dialog_confirm);
        dialog_confirm.getWindow().setBackgroundDrawableResource(R.color.transparent);

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

                //test xóa alarm
                alarmManager.cancel(list.get(position).createPendingIntent(MainActivity.this));

                adater.deleteNote(list.get(position));

                dialog_confirm.dismiss();
            }
        });

        int width= (int) (getResources().getDisplayMetrics().widthPixels*0.70);
        int height= (int) (getResources().getDisplayMetrics().heightPixels*0.20);

        dialog_confirm.getWindow().setLayout(width,height);

        dialog_confirm.show();

    }

    public void editNote(int postition){
        adater.notifyDataSetChanged();
        Note n=list.get(postition);
        Intent intent=new Intent(MainActivity.this,NoteActionActivity.class);
        intent.putExtra("send",n);
        startActivityForResult(intent,REQUES_UPDATE);
    }

    public Calendar createCalendar(String date, String time){
        Calendar calendar=Calendar.getInstance();
        Date d=null;
        try {
            d=PublicDateTime.FORMAT_CALENDAR.parse(date+" "+time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(d);
        calendar.set(Calendar.SECOND,0);

        return calendar;
    }

}
