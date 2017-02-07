package mx.egm.bitsonepat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Spinner;

public class Opener extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int chooser=this.getIntent().getExtras().getInt("main");
        switch(chooser){
            case R.id.aboutus:
                open(R.layout.about, "About Us");
                break;
            case R.id.admissions:
                open(R.layout.admissions, "Admissions");
                break;
            case R.id.gallery:
                open(R.layout.gallery, "BITS Gallery");
                String[] jsonName={"folders"};//variable name
                String[] jsonData={"http://egmox.96.lt/bits/bits_files/android.php",
                        "gallery", "folders"};//array name and variable value
                String[] respKeys={};
                new db(jsonName, jsonData, respKeys, this).execute();
                break;
            case R.id.resultreqbutton:
                setContentView(R.layout.resultresp);
                this.setTitle("Result");
                Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
                setSupportActionBar(toolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                WebView result=(WebView)findViewById(R.id.resultweb);
                result.loadUrl(this.getIntent().getExtras().getString("url"));
                break;
            case R.id.dev:
                open(R.layout.dev, "About Developers");
                break;
            case R.id.facilities:
                open(R.layout.facilities, "Facilities");
                break;
            case R.id.enq:
                open(R.layout.enq, "Enquiry");
                break;
            case R.id.student:
                open(R.layout.stu_section, "Student Section");
                break;
            case R.id.result:
                open(R.layout.resultreq, "Check result");
                break;
            case (R.id.map2):
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("geo:29.0411911,76.8824852,17z?q=29.0411911,76.8824852,17z")));
                finish();
                break;
            case R.id.programs:
                open(R.layout.programs, "Available Programms");
                break;
            case R.id.branches:
                open(R.layout.branches, "Brances");
                break;
            case R.id.eligibility:
                open(R.layout.eligibility, "Eligibility");
                break;
            case R.id.forms:
                open(R.layout.forms, "Download Forms");
                break;
            case R.id.ldetails:
                open(R.layout.ldetails, "Legal Details");
                break;
            case (R.id.btechform):
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://goo.gl/SeFsq0")));//btech admission form
                break;
            case (R.id.btechform1):
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://goo.gl/SeFsq0")));//btech admission form
                break;
            case (R.id.nursform):
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://goo.gl/oiRRTd")));//Nursing admission form
                break;
            case (R.id.nursform1):
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://goo.gl/oiRRTd")));//Nursing admission form
                break;
            case (R.id.certmedchar):
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://goo.gl/xikxjJ")));//char and med certifiacte
                break;
            case (R.id.certmedchar1):
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://goo.gl/xikxjJ")));//char and med certifiacte
                break;
            case (R.id.safidavit):
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://goo.gl/QNrygR")));//affidavit by students
                break;
            case (R.id.safidavit1):
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://goo.gl/QNrygR")));//affidavit by students
                break;
            case (R.id.pafidavit):
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://goo.gl/iP0c1P")));//affidavit by parents
                break;
            case (R.id.pafidavit1):
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://goo.gl/iP0c1P")));//affidavit by parents
                break;
            case (R.id.placement):
                open(R.layout.placement, "Placement");
                break;
            case (R.id.nav_share):
                Intent txtIntent = new Intent(android.content.Intent.ACTION_SEND);
                txtIntent .setType("text/plain");
                txtIntent .putExtra(android.content.Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=mx.egm.bitsonepat");
                startActivity(Intent.createChooser(txtIntent ,"Share"));
                finish();
            break;
            case R.id.talk:
                open(R.layout.blank, "Know your HODs");
                break;
            default:
                open(R.layout.blank, "Need more time");
        }
    }
    public void open(int id, String title){
        setContentView(id);
        this.setTitle(title);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void clicked(View v){
        Intent intent=new Intent(this, onlineDB.class);
        switch(v.getId()){
            case R.id.notices:
                String[] jsonName1={"lastNotice"};//variable name
                String[] jsonData1={"http://egmox.96.lt/bits/bits_files/android.php", "notice",
                        "1"};//array name and variable value
                String[] keyName={"id", "teacher", "course", "date", "branch", "sem", "subject", "type", "message"};
                String[] keyType={" INT(6) primary key,", " VARCHAR(25),", " VARCHAR(6),", " VARCHAR(10),",
                        " VARCHAR(3),", " VARCHAR(2),", " VARCHAR(60),", " VARCHAR(50),", " VARCHAR(360))"};
                new DatabaseHandler(this, "notices", keyName, keyType);
                intent.putExtra("id",v.getId());
                intent.putExtra("jsonName",jsonName1);
                intent.putExtra("jsonData",jsonData1);
                intent.putExtra("keyName", keyName);
                startActivity(intent);
                //new onlineDB(jsonName1, jsonData1, keyName);
                break;

            case R.id.resultreqbutton:
                String url="http://14.139.236.46:7001/Forms/Student/PrintReportCard.aspx?rollno="
                        +((EditText)findViewById(R.id.roll)).getText().toString()
                        +"&sem="+sem(((Spinner)findViewById(R.id.semester))
                        .getSelectedItem().toString());
                startActivity(new Intent(this, Opener.class).putExtra("main",v.getId()).putExtra("url",url));
                break;
            case R.id.guests:
                startActivity(new Intent(this, Gallery.class).putExtra("gallery",v.getId()));
                break;
            case R.id.shikshakdiwas:
                startActivity(new Intent(this, Gallery.class).putExtra("gallery",v.getId()));
                break;
            case R.id.jobfair16:
                startActivity(new Intent(this, Gallery.class).putExtra("gallery",v.getId()));
                break;
            case R.id.bitsgmail:
                Intent gmail=new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "bits.sonepat@gmail.com", null));
                gmail.putExtra(Intent.EXTRA_SUBJECT, "(EGMOX) Hey, I need information about ");
                startActivity(gmail);
                break;
            case R.id.bitsmail:
                Intent mail=new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "admissions@bits.edu.in", null));
                mail.putExtra(Intent.EXTRA_SUBJECT, "(EGMOX) Hey, I need information about ");
                startActivity(mail);
                break;
            case R.id.egmoxweb:
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://egmox.96.Lt")));//btech admission form
                break;
            case R.id.egmoxmail:
                Intent egmoxmail=new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "egmox2@gmail.com", null));
                egmoxmail.putExtra(Intent.EXTRA_SUBJECT, "(EGMOX) Dude, how are you ");
                startActivity(egmoxmail);
                break;
            case R.id.egmoxfb:
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://fb.com/egmox")));//btech admission form
                break;
            case R.id.egmoxtwitter:
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://twitter.com/egmox")));//btech admission form
                break;
            case R.id.newresult:
                startActivity(new Intent(this,result.class).putExtra("url","http://www.dcrustedp.in/may2016/"));
                break;
            default:
                startActivity(new Intent(this, Opener.class).putExtra("main", v.getId()));
                break;
        }
    }

    public String sem(String semester){
        if(semester.equals("Semester"))
            return "0";
        else return semester;
    }

    public void galleryclicked(View v){
                startActivity(new Intent(this, Gallery.class).putExtra("gallery",v.getId()));
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