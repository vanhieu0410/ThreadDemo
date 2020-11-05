package khoapham.vn.threademo;

import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mImgShow;
    private SeekBar mSeekBar;

    public static final String URL = "https://www.gettyimages.ca/gi-resources/images/Homepage/Hero/UK/CMS_Creative_164657191_Kingfisher.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImgShow = (ImageView) findViewById(R.id.imgShow);
        mSeekBar = (SeekBar) findViewById(R.id.seekBar);
        mSeekBar.setMax(100);

    }

    @Override
    public void onClick(View v) {
        if (isNetworkConnected()){
//            new DownLoad(this,mSeekBar,mImgShow).execute(URL);
            Handler uiHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message msg) {
                    Object data = msg.obj;
                    if (data instanceof Integer){
                        mSeekBar.setProgress((Integer) msg.obj);
                    } else {
                        mImgShow.setImageBitmap((Bitmap) msg.obj);
                    }

                    return true;
                }
            });
            new DownloadWithThread("xxx", uiHandler).start();
            mSeekBar.setVisibility(View.VISIBLE);
            mImgShow.setVisibility(View.VISIBLE);
        }else {
            Toast.makeText(this, "Không có kết nối mạng !", Toast.LENGTH_SHORT).show();
        }

    }

    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);

        return connectivityManager.getActiveNetworkInfo() != null;
    }

}
