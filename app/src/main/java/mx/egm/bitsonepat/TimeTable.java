package mx.egm.bitsonepat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class TimeTable extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] jsonname={"lastNotice"};//variable name
        String[] jsondata={"1", "http://egmox.96.lt/bits/bits_files/android.php", "notice",
                new DatabaseHandler(this).lastNotice()};//array name and variable value
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}