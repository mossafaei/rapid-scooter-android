package ir.MostafaSafaeipour.rapid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

class PhoneNumberJsonStruct{
    String phoneNumber = "";
}

public class MainActivity extends AppCompatActivity {

    public static final MediaType jsonmedia = MediaType.parse("application/json; charset=utf-8");

    static boolean CheckPhoneNumber(String phoneNumber){
        if (phoneNumber.charAt(0) != '0' || phoneNumber.charAt(1) != '9')
            return false;
        for (int i=0;i<phoneNumber.length();i++){
            if (phoneNumber.charAt(i) < '0' || phoneNumber.charAt(i) > '9')
                return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Gson jsonmanager = new Gson();
        final PhoneNumberJsonStruct newjson = new PhoneNumberJsonStruct();

        final EditText phoneNumber = (EditText) findViewById(R.id.PhoneTextEdit);

        Typeface thisFont = Typeface.createFromAsset(getAssets(),"fonts/avenir.ttc");
        phoneNumber.setTypeface(thisFont);

        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String phonestr = charSequence.toString();
                if (phonestr.length() == 11){
                    if (CheckPhoneNumber(phonestr)){
                        newjson.phoneNumber = phonestr;
                        System.out.println(jsonmanager.toJson(newjson));

                        try{
                            postRequest(jsonmanager.toJson(newjson),"https://www.rapidscooter.ir/api/testapi.php");
                        }catch(IOException e){
                            e.printStackTrace();
                        }


                    }else{
                        phoneNumber.setText("");
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Error")
                                .setMessage("The Number is not correct")
                                .setNegativeButton("OK",null)
                                .show();
                    }
                }else if (phonestr.length() > 11){
                        phoneNumber.setText("");
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Error")
                                .setMessage("The Number is not correct")
                                .setNegativeButton("OK",null)
                                .show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        Button vorod = (Button) findViewById(R.id.VorrodButton);
        vorod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainmapIntent = new Intent(getApplicationContext(),MainGoogleMapActivity.class);
                startActivity(mainmapIntent);
                finish();
                //ProgressDialog dia = ProgressDialog.show(MainActivity.this,"","Please Wait...",true);

              //  new AlertDialog.Builder(MainActivity.this)
                //        .setTitle("hi")
                  //      .setMessage("hehllloomanns")
                    //    .setNegativeButton("OK",null)
                      //  .show();
                //startActivity(new Intent(MainActivity.this , MainMapActivity.class));
            }
        });


    }
    void postRequest(String postBody,String url) throws IOException{
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(jsonmedia,postBody);
        final Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("FAILD!!!");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println(response.body().string());
            }
        });
    }
}
