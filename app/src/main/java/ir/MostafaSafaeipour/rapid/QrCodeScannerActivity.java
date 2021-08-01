package ir.MostafaSafaeipour.rapid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class QrCodeScannerActivity extends AppCompatActivity implements ZBarScannerView.ResultHandler {

    private ZBarScannerView mScannerView;
    private boolean FlashToggle = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_qr_code_scanner);
        mScannerView = (ZBarScannerView) findViewById(R.id.zxscan);

        Button flashbutton = (Button) findViewById(R.id.FlashLightButton);
        flashbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!FlashToggle) {
                    mScannerView.setFlash(true);
                    FlashToggle = true;
                }else{
                    mScannerView.setFlash(false);
                    FlashToggle = false;
                }
            }
        });

        Button usecodebutton = (Button) findViewById(R.id.UseCodeButton);
        usecodebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        Button closebutton = (Button) findViewById(R.id.CloseButton);
        closebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mScannerView.stopCamera();
                Intent goBackNoResult = new Intent();
                goBackNoResult.putExtra("QrCode","-1");
                setResult(0,goBackNoResult);
                finish();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();

        mScannerView.setFlash(false);
        FlashToggle = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }


    private boolean CheckResult(String Result){
        boolean Check1=false,Check2=true;
        if (Result.charAt(0) >= 'A' && Result.charAt(0) <= 'Z')
            Check1 = true;
        for (int i=1;i<Result.length();i++){
            if ( !(Result.charAt(i) >= '1' && Result.charAt(i) <= '9') )
                Check2=false;
        }
        if (Check1 && Check2)
            return true;
        return false;
    }

    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        if (CheckResult(rawResult.getContents())) {
            mScannerView.stopCamera();
            Intent goBack = new Intent();
            goBack.putExtra("QrCode", rawResult.getContents());
            setResult(1, goBack);
            finish();
        }else{
            mScannerView.resumeCameraPreview(this);
        }
        // If you would like to resume scanning, call this method below:
        //mScannerView.resumeCameraPreview(this);
    }

}
