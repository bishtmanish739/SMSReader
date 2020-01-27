package in.technicalkeeda.smsreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static java.sql.Types.TIMESTAMP;

public class MainActivity extends AppCompatActivity {
    public static final ArrayList<String> sms_num = new ArrayList<String>();
    public static final ArrayList<String> sms_body = new ArrayList<String>();


    public static void getSmsLogs(Cursor c, Context con) {
            int i=0;


        try {

            if (c.moveToFirst()) {
                do {
                    Log.d("error",
                            ""
                                    + c.getString(c
                                    .getColumnIndexOrThrow("address")));
                    if (c.getString(c.getColumnIndexOrThrow("address")) == null) {
                        c.moveToNext();
                        continue;
                    }

                    String Number = c.getString(
                            c.getColumnIndexOrThrow("address")).toString();
                    String Body = c.getString(c.getColumnIndexOrThrow("body"))
                            .toString();
                    //Log.d("error",Number+Body);

                    String time=c.getString(c.getColumnIndexOrThrow("date"));
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
                    String dateString = formatter.format(new Date(Long.parseLong(time)));


                    Number+=" "+dateString;
                   /* formatter = new SimpleDateFormat("HH:mm:ss z");
                     dateString = formatter.format(new Date(Long.parseLong(time)));
                     Number+=" "+dateString;
*/
                    sms_num.add(i,Number);

                    sms_body.add(i,Body);
                    i++;

                } while (c.moveToNext());
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    Context con;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        con = getApplicationContext();
        sms_num.clear();
        sms_body.clear();
        if (ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.READ_SMS") == PackageManager.PERMISSION_GRANTED) {
        }
        else{
            final int REQUEST_CODE_ASK_PERMISSIONS = 123;
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{"android.permission.READ_SMS"}, REQUEST_CODE_ASK_PERMISSIONS);
        }
        if (ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.READ_SMS") == PackageManager.PERMISSION_GRANTED) {

            Uri myMessage = Uri.parse("content://sms/");

            ContentResolver cr = con.getContentResolver();
            Cursor c = cr.query(myMessage, new String[]{"_id",
                            "address", "date", "body", "read"}, null,
                    null, null);

            startManagingCursor(c);
            MainActivity.getSmsLogs(c, con);
            getSmsLogs(c, con);
          //  Log.d("Num", sms_num.get(sms_num.size()-1 ));
           // sms_body.add("HIII");
          //  Log.d("error",sms_body.get(1));
           // Toast.makeText(con, sms_body.get(1), Toast.LENGTH_SHORT).show();
            TextView textView=findViewById(R.id.text);
            textView.setText(sms_body.get(0)+sms_num.get(0)+"\n");
            String s=sms_body.get(0);
          // int i= s.indexOf("Hi");

            Toast.makeText(con, s, Toast.LENGTH_SHORT).show();


        }
        else{
            final int REQUEST_CODE_ASK_PERMISSIONS = 123;
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{"android.permission.READ_SMS"}, REQUEST_CODE_ASK_PERMISSIONS);
        }
    }
}
