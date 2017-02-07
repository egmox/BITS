package mx.egm.bitsonepat;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Notices extends AppCompatActivity {
    DatabaseHandler db = new DatabaseHandler(this);
    String subject, message, type, imagePath = "sdcard/.bits/notice/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("New Notices");
        setContentView(R.layout.notices);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Snackbar.make(findViewById(R.id.list), "Click on subject to see", Snackbar.LENGTH_SHORT).show();

        final ArrayList<ArrayList<String>> rows = db.getRows("notices");
        final List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
        ListView notices=(ListView)findViewById(R.id.list);
        int i=0;
        final String[] from = { "teacher","course","date","branch","sem","subject", "message"};
        for (ArrayList is : rows) {
            HashMap<String, String> hm = new HashMap<String,String>();
            for(int count=0; count<from.length; count++)
                hm.put(from[count], (rows.get(i)).get(count));
            i++;
            aList.add(hm);
        }
        Collections.reverse(aList);
        Collections.reverse(rows);
        int[] to = { R.id.teacher,R.id.course,R.id.date,R.id.branch,R.id.sem,R.id.subject};// IDs of views in listview_layout
        SimpleAdapter adapter = new SimpleAdapter(this, aList, R.layout.listadapter, from, to);
        notices.setAdapter(adapter);

        notices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                subject = rows.get(position).get(5);
                type = rows.get(position).get(6);
                message = rows.get(position).get(7);
                showDialog(0);
            }
        });
    }

    protected Dialog onCreateDialog(int id)
    {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ((Button)dialog.findViewById(R.id.dialog_ok))
                .setOnClickListener(new View.OnClickListener()
                {public void onClick(View v){removeDialog(0);}});
        return dialog;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog)
    {
        dialog.setTitle(subject != null ? subject : "");
        TextView dialogMessage = (TextView)dialog.findViewById(R.id.dialog_message);
        ImageView dialogImage = (ImageView)dialog.findViewById(R.id.dialog_image);
        try {
            if (type.equals("text")) dialogMessage.setText(message != null ? message : "");
            else dialogImage.setImageDrawable(Drawable.createFromPath(imagePath + type));
            dialogImage.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(new File(imagePath + type)), "image/*");
                    startActivity(intent);
                }});
        }catch (Exception e){}

    }

    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}