package ta.pratiwi.onfish.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ta.pratiwi.onfish.R;
import ta.pratiwi.onfish.app.Config;
import ta.pratiwi.onfish.app.Request;
import ta.pratiwi.onfish.app.SessionManagerUser;
import ta.pratiwi.onfish.model.JenisIkan;

public class TambahDaganganActivity extends AppCompatActivity {

    private Button btnGambar, btnSubmit;
    private ImageView imgDagangan;
    private List<JenisIkan> itemList;
    private ProgressDialog pDialog;

    private Spinner jenisIkan;
    private SpinAdapter spinAdapter;

    public String SERVER = Config.URL+"jenis_ikan.php";
    public String SERVER_POST = Config.URL+"dagangan_tambah.php";
    public String id_jenis_ikan, timestamp;

    SessionManagerUser session;

    private static final String TAG = TambahDaganganActivity.class.getSimpleName();

    int RESULT_SELECT_IMAGE = 1;

    Uri image;

    private EditText edtNama, edtBerat, edtHarga, edtDeskripsi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_dagangan);

        session = new SessionManagerUser(getApplicationContext());

        HashMap<String, String> user = session.getUserDetails();
        final String id_penjual = user.get(SessionManagerUser.KEY_ID_PELANGGAN);

        getSupportActionBar().setTitle("Tambah Dagangan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imgDagangan = (ImageView) findViewById(R.id.img_dagangan);

        edtNama = (EditText) findViewById(R.id.edt_nama);
        edtBerat = (EditText) findViewById(R.id.edt_berat);
        edtHarga = (EditText) findViewById(R.id.edt_harga);
        edtDeskripsi = (EditText) findViewById(R.id.edt_desk);

        jenisIkan = (Spinner) findViewById(R.id.list_jenis_ikan);
        jenisIkan.setPrompt("Jenis Ikan");

        itemList = new ArrayList<>();

        new getJenisIkan().execute();

        spinAdapter = new SpinAdapter(TambahDaganganActivity.this,
                android.R.layout.simple_spinner_item,
                itemList);

        jenisIkan.setAdapter(spinAdapter);

        btnGambar = (Button) findViewById(R.id.btn_gmbr);
        btnGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pilihGambar();
            }
        });

        btnSubmit = (Button) findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imgDagangan.getDrawable() != null){
                    //String nama = edtNama.getText().toString();
                    String berat = edtBerat.getText().toString();
                    String harga = edtHarga.getText().toString();
                    String desk = edtDeskripsi.getText().toString();
                    //get image in bitmap format
                    Bitmap image = ((BitmapDrawable) imgDagangan.getDrawable()).getBitmap();
                    String file_name = "IMG_"+timestamp;

                    new inputDagangan(
                            id_penjual,
                            id_jenis_ikan,
                            //nama,
                            berat,
                            harga,
                            desk,
                            file_name,
                            image
                    ).execute();
                }else{
                    Toast.makeText(getApplicationContext(),"Foto perlu ditambahkan",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class getJenisIkan extends AsyncTask<Void,Void,String> {

        //variabel untuk tangkap data
        private int scs = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(TambahDaganganActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(Void... params) {
            try{
                //susun parameter
                HashMap<String,String> detail = new HashMap<>();

                try {
                    //convert this HashMap to encodedUrl to send to php file
                    String dataToSend = hashMapToUrl(detail);
                    //make a Http request and send data to saveImage.php file
                    String response = Request.post(SERVER,dataToSend);

                    //dapatkan respon
                    Log.e("Respon", response);

                    JSONObject ob = new JSONObject(response);
                    scs = ob.getInt("success");

                    if (scs == 1) {
                        JSONArray products = ob.getJSONArray("daftar");

                        for (int i = 0; i < products.length(); i++) {
                            JSONObject c = products.getJSONObject(i);

                            // Storing each json item in variable
                            String idjenis = c.getString("id_jenis_ikan");
                            String nama_ikan = c.getString("nama_ikan");
                            String foto = c.getString("foto");

                            JenisIkan k = new JenisIkan();
                            k.setId_jenis_ikan(idjenis);
                            k.setNama_ikan(nama_ikan);
                            k.setLink_foto(foto);

                            itemList.add(k);

                        }
                    } else {
                        // no data found

                    }

                } catch (JSONException e){
                    e.printStackTrace();
                }

            } catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            spinAdapter.notifyDataSetChanged();
            pDialog.dismiss();

        }

    }

    private class inputDagangan extends AsyncTask<Void,Void,String> {
        private String id_penjual;
        private String id_jenis_ikan;
        //private String nama;
        private String jum_kg;
        private String harga;
        private String desk;
        private String file_name;
        private Bitmap bitmap;

        public inputDagangan(
                String id_penjual,
                String id_jenis_ikan,
                //String nama,
                String jum_kg,
                String harga,
                String desk,
                String file_name,
                Bitmap bitmap){
            this.id_penjual = id_penjual;
            this.id_jenis_ikan = id_jenis_ikan;
            //this.nama = nama;
            this.jum_kg = jum_kg;
            this.harga = harga;
            this.desk = desk;
            this.harga = harga;
            this.file_name = file_name;
            this.bitmap = bitmap;
        }

        private String scs = "";
        private String psn = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(TambahDaganganActivity.this);
            pDialog.setMessage("Loading..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            //kompress image ke format jpg
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
            /*
            * encode image ke base64 agar bisa di ambil/dibaca nanti pada file upload_bukti_bayar.php
            * */
            final String encodeImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

            try {
                //menganbil data-data yang akan dikirim

                //generate hashMap to store encodedImage and the name
                HashMap<String,String> detail = new HashMap<>();
                detail.put("id_penjual", id_penjual);
                detail.put("id_jenis_ikan", id_jenis_ikan);
                //detail.put("nama", nama);
                detail.put("jum_kg", jum_kg);
                detail.put("harga", harga);
                detail.put("desk", desk);
                detail.put("file_name", file_name);
                detail.put("image", encodeImage);

                try{
                    //convert this HashMap to encodedUrl to send to php file
                    String dataToSend = hashMapToUrl(detail);
                    //make a Http request and send data to php file
                    String response = Request.post(SERVER_POST,dataToSend);

                    //dapatkan respon
                    Log.e("Respon", response);

                    JSONObject ob = new JSONObject(response);
                    scs = ob.getString("success");
                    psn = ob.getString("message");

                }catch (JSONException e){
                    e.printStackTrace();
                    Log.e(TAG, "ERROR  " + e);
                    Toast.makeText(getApplicationContext(),"Maaf, terjadi error!",Toast.LENGTH_SHORT).show();
                    //return null;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }



        @Override
        protected void onPostExecute(String s) {
            pDialog.dismiss();

            if(scs.contains("1")){
                Toast.makeText(TambahDaganganActivity.this, psn, Toast.LENGTH_SHORT).show();
                finish();
            }
            if(scs.contains("0")){
                Toast.makeText(TambahDaganganActivity.this, psn,Toast.LENGTH_SHORT).show();
            }

        }

    }

    private String hashMapToUrl(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    public class SpinAdapter extends ArrayAdapter<JenisIkan> {

        // Your sent context
        private Context context;
        // Your custom values for the spinner (User)
        private List<JenisIkan> values;

        public SpinAdapter(Context context, int textViewResourceId,
                           List<JenisIkan> values) {
            super(context, textViewResourceId, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public int getCount(){
            return values.size();
        }

        @Override
        public JenisIkan getItem(int position){
            return values.get(position);
        }

        @Override
        public long getItemId(int position){
            return position;
        }


        // And the "magic" goes here
        // This is for the "passive" state of the spinner
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
            TextView label = (TextView) super.getView(position, convertView, parent);

            // Then you can get the current item using the values array (Users array) and the current position
            // You can NOW reference each method you has created in your bean object (User class)
            label.setText(values.get(position).getNama_ikan());

            // And finally return your dynamic (or custom) view for each spinner item
            id_jenis_ikan = values.get(position).getId_jenis_ikan();
            return label;
        }

        // And here is when the "chooser" is popped up
        // Normally is the same view, but you can customize it if you want
        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            TextView label = (TextView) super.getDropDownView(position, convertView, parent);

            label.setText(values.get(position).getNama_ikan());

            return label;
        }
    }

    private void pilihGambar() {
        //open album untuk pilih image
        Intent gallaryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallaryIntent, RESULT_SELECT_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_SELECT_IMAGE && resultCode == RESULT_OK && data != null){
            //set the selected image to image variable
            image = data.getData();
            imgDagangan.setImageURI(Uri.parse(compressImage(image.toString())));

            //get the current timeStamp and strore that in the time Variable
            Long tsLong = System.currentTimeMillis() / 1000;
            timestamp = tsLong.toString();

            btnGambar.setText(timestamp+".JPG");

        }
    }

    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      set max Height and width values of the compressed image

        float maxHeight = 800.0f;
        float maxWidth = 800.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

}
