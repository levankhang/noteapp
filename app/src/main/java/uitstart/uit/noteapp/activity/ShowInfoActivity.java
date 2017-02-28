package uitstart.uit.noteapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import uitstart.uit.noteapp.model.Note;
import uitstart.uit.noteapp.R;

public class ShowInfoActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnClose;
    TextView tvDate, tvTime, tvName, tvDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initDisplay(savedInstanceState);
        addControls();
        addEvents();
        initInfo();
    }

    private void addEvents() {
        btnClose.setOnClickListener(this);
    }

    private void initInfo() {
        Intent intent=getIntent();
        Note n= (Note) intent.getSerializableExtra("note");
        tvDate.setText(n.getDate());
        tvTime.setText(n.getTime());
        tvName.setText(n.getName());
        tvDetail.setText(n.getDetai());
    }

    private void addControls() {
        btnClose= (Button) findViewById(R.id.btnClose);
        tvDate= (TextView) findViewById(R.id.tvDate);
        tvTime= (TextView) findViewById(R.id.tvTime);
        tvName= (TextView) findViewById(R.id.tvName);
        tvDetail= (TextView) findViewById(R.id.tvDetail);
    }

    private void initDisplay(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_show_info);

        int width= (int) (getResources().getDisplayMetrics().widthPixels*0.90);
        int height= (int) (getResources().getDisplayMetrics().heightPixels*0.80);

        getWindow().setLayout(width,height);
        getWindow().setBackgroundDrawableResource(R.color.transparent);

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.btnClose: finish();
        }
    }
}
