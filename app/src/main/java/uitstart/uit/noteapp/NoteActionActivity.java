package uitstart.uit.noteapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class NoteActionActivity extends AppCompatActivity implements View.OnClickListener{

    private Intent intentCalled;
    private Note note=null;

    boolean is_edit_action=false;

    private EditText edtName, edtDetail;
    private TextView tvNameMode;
    public Button btnDone, btnCancel, btnTime, btnDate;

    PublicDateTime publicDateTime=new PublicDateTime(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initDisplay(savedInstanceState);
        addControls();
        addEvents();
        initMode();
        initTheme();
    }

    private void initTheme() {
        if(is_edit_action)
            initThemeForEdit();
        if(!is_edit_action)
            initThemeForAddNewNote();
    }

    private void initThemeForAddNewNote() {
        btnDate.setText(PublicDateTime.FORMAT_DATE.format(PublicDateTime.calendar.getTime()));
        btnTime.setText(PublicDateTime.FORMAT_TIME.format(PublicDateTime.calendar.getTime()));
    }

    private void initThemeForEdit() {
        edtName.setText(note.getName());
        edtDetail.setText(note.getDetai());
        btnDate.setText(note.getDate());
        btnTime.setText(note.getTime());
        tvNameMode.setText("Chỉnh sửa ghi chú");
    }

    private void initMode() {
        intentCalled=getIntent();
        note= (Note) intentCalled.getSerializableExtra("send");

        if(note!=null){
            is_edit_action=true;
        }else
            is_edit_action=false;
    }

    private void addEvents() {
        btnCancel.setOnClickListener(this);
        btnDone.setOnClickListener(this);
        btnTime.setOnClickListener(this);
        btnDate.setOnClickListener(this);
    }

    private void addControls() {
        edtDetail= (EditText) findViewById(R.id.edtDetail);
        edtName= (EditText) findViewById(R.id.edtName);
        btnCancel= (Button) findViewById(R.id.btnCancel);
        btnDone= (Button) findViewById(R.id.btnDone);
        btnDate= (Button) findViewById(R.id.btnDate);
        btnTime= (Button) findViewById(R.id.btnTime);
        tvNameMode= (TextView) findViewById(R.id.tvNameMode);

    }

    private void initDisplay(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_note);

        int width= (int) (getResources().getDisplayMetrics().widthPixels*0.90);
        int height= (int) (getResources().getDisplayMetrics().heightPixels*0.90);

        getWindow().setLayout(width,height);

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.btnCancel: finish(); break;
            case R.id.btnDone:
                createNoteResult();
                sendNewNodeToMain(is_edit_action?MainActivity.REQUES_UPDATE:MainActivity.REQUES_NEWNODE); break;
            case R.id.btnTime: publicDateTime.changedTimeAction(false,true);break;
            case R.id.btnDate: publicDateTime.changedDateAction(false,true); break;
        }
    }

    private void createNoteResult() {
        int id=-1;
        if(is_edit_action)
            id=note.getId();
        note=new Note(btnDate.getText().toString(),btnTime.getText().toString(),edtName.getText().toString(),edtDetail.getText().toString());
        if(is_edit_action)
            note.setId(id);
    }

    private void sendNewNodeToMain(int REQUES_CODE) {
        if(note!=null)
            intentCalled.putExtra("result",note);
        setResult(REQUES_CODE,intentCalled);
        finish();
    }
}
