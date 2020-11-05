package khoapham.vn.threademo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DownLoad extends AsyncTask<String, Integer,Bitmap> {
    private Context mCOntext;
    private SeekBar mSeekBar;
    private ImageView mImageView;
    private Bitmap mBitMap;
    private InputStream mInputStream;
    private OutputStream mOutputStream;
    private URL mURL;

    public DownLoad(Context mCOntext,SeekBar mSeekBar, ImageView mImageView){
        this.mCOntext = mCOntext;
        this.mSeekBar = mSeekBar;
        this.mImageView =mImageView;
    }


    @Override
    protected Bitmap doInBackground(String... strings) {
            try {
                mURL = new URL(strings[0]);
                URLConnection connection = mURL.openConnection();
                int sizeFile = connection.getContentLength();
                mInputStream = new BufferedInputStream(mURL.openStream());
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                mOutputStream = new BufferedOutputStream(byteArrayOutputStream);
                // ByteArray chứa dữ liệu để decor bitmap
                // Datatype chỉ đọc dc 1kb 1 lần nên dùng while đọc hết dữ liệu

                byte[] dataType = new byte[1024];
                int data = 0;       // số lần đọc từ stream
                long totalData = 0;  // tổng dữ liệu để tính toán progressbar
                while ((data = mInputStream.read(dataType)) > 0) {
                    totalData += data;
                    publishProgress((int) (totalData * 100 / sizeFile));
                    mOutputStream.write(dataType, 0, data);
                    Thread.sleep(10);
                }
                mOutputStream.flush();
                BitmapFactory.Options bOptions = new BitmapFactory.Options();
                bOptions.inSampleSize = 1;
                byte[] dataComplete = byteArrayOutputStream.toByteArray();
                mBitMap = BitmapFactory.decodeByteArray(dataComplete,0,dataComplete.length,bOptions);
            }
            catch (MalformedURLException e){
                e.printStackTrace();
            }
            catch (IOException e){
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                try {
                    mInputStream.close();
                    mOutputStream.close();
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }

            return mBitMap;

    }



    @Override
    protected void onProgressUpdate(Integer... values) {
        mSeekBar.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        mImageView.setImageBitmap(bitmap);
    }
}
