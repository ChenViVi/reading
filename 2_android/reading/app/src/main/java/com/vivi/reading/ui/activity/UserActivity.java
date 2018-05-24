package com.vivi.reading.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.vivi.reading.R;
import com.vivi.reading.util.ConstUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UserActivity extends AppCompatActivity implements View.OnClickListener {

    private int id;
    public String name;
    private String sex;
    private String imgUrl;
    public String sign;

    public static UserActivity instance = null;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private RequestQueue queue;
    private final int REQUEST_CODE_CAMERA = 1000;
    private final int REQUEST_CODE_GALLERY = 1001;
    private DisplayImageOptions options;

    private ImageView ivBack;
    private ImageView nivUserImg;
    private TextView tvUserName;
    //private Switch switcher;
    private LinearLayout layoutUserName;
    private TextView tvUserNameChange;
    private LinearLayout layoutUserSign;
    private TextView tvUserSign;
    private TextView tvSave;
    private ImageView ivMale;
    private ImageView ivFamale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        instance = this;
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        queue = Volley.newRequestQueue(this);

        ivBack = (ImageView) findViewById(R.id.iv_back);
        nivUserImg = (ImageView) findViewById(R.id.niv_user_img);
        tvUserName = (TextView) findViewById(R.id.tv_user_name);
        //switcher = (Switch) findViewById(R.id.switch_user_sex);
        layoutUserName = (LinearLayout) findViewById(R.id.layout_user_name);
        layoutUserSign = (LinearLayout) findViewById(R.id.layout_user_sign);
        tvUserNameChange = (TextView) findViewById(R.id.tv_user_name_change);
        tvUserSign = (TextView) findViewById(R.id.tv_user_sign);
        tvSave = (TextView) findViewById(R.id.tv_save);
        ivMale = (ImageView) findViewById(R.id.iv_male);
        ivFamale = (ImageView) findViewById(R.id.iv_female);

        id = preferences.getInt("id", -1);
        name = preferences.getString("name", "未登录");
        sex = preferences.getString("sex", "0");
        imgUrl = preferences.getString("imgUrl", "");
        sign = preferences.getString("sign", "我们没有什么不同");

        if (sex.equals("1")) {
            //switcher.setChecked(true);
            ivMale.setImageResource(R.drawable.bg_point);
            ivFamale.setImageResource(R.drawable.bg_point_focus);
        }
        SexOnClickListener sexListener = new SexOnClickListener();
        ivFamale.setOnClickListener(sexListener);
        ivMale.setOnClickListener(sexListener);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if(switcher.isChecked()){
                    sex = "1";
                    Toast.makeText(UserActivity.this, "女", Toast.LENGTH_SHORT).show();
                }
                else {
                    sex = "0";
                    Toast.makeText(UserActivity.this, "男", Toast.LENGTH_SHORT).show();
                }*/
                queue.add(getEditRequest(id,name,sex,sign));
            }
        });

        /*nivUserImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionSheet.createBuilder(UserActivity.this, getSupportFragmentManager())
                        .setCancelButtonTitle("取消")
                        .setOtherButtonTitles("相册", "拍照")
                        .setCancelableOnTouchOutside(true)
                        .setListener(new ActionSheet.ActionSheetListener() {
                            @Override
                            public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
                            }

                            @Override
                            public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                                switch (index) {
                                    case 0:
                                        GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY, functionConfig, onHanlderResultCallback);
                                        break;
                                    case 1:
                                        GalleryFinal.openCamera(REQUEST_CODE_CAMERA, functionConfig, onHanlderResultCallback);
                                        break;
                                }
                            }
                        }).show();
            }
        });*/
        layoutUserName.setOnClickListener(this);
        layoutUserSign.setOnClickListener(this);
        tvUserName.setOnClickListener(this);
        initImageLoader(this);
        //ImageLoader.getInstance().displayImage("http://simg.sinajs.cn/blog7style/images/common/godreply/btn.png",nivUserImg, options);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvUserName.setText(name);
        tvUserNameChange.setText(name);
        tvUserSign.setText(sign);
    }

    @Override
    public void onClick(View v) {
        String type = "name";
        if (v.getId() == R.id.layout_user_sign) {
            type = "sign";
        }
        Intent intent = new Intent(UserActivity.this, EditActivity.class);
        intent.putExtra("type", type);
        startActivity(intent);
    }

    class SexOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.iv_male){
                ivMale.setImageResource(R.drawable.bg_point_focus);
                ivFamale.setImageResource(R.drawable.bg_point);
                sex = "0";
            }
            else {
                ivMale.setImageResource(R.drawable.bg_point);
                ivFamale.setImageResource(R.drawable.bg_point_focus);
                sex = "1";
            }
        }
    }

    private StringRequest getEditRequest(final int userId, final String name, final String sex, final String sign) {
        StringRequest request = new StringRequest(Request.Method.POST, ConstUtils.BASEURL + "setuserinfo.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("editactivity", response);
                        int result = 0;
                        try {
                            JSONObject json= new JSONObject(response);
                            result = json.getInt("result");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (result != 0){
                            Toast.makeText(UserActivity.this, "获取文章失败", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            editor = PreferenceManager.getDefaultSharedPreferences(UserActivity.this).edit();
                            editor.putString("name", name);
                            editor.putString("sex", sex);
                            editor.putString("imgUrl", response);
                            editor.putString("sign", sign);
                            editor.apply();
                        }
                        /*if (response.equals("EDIT_FAILED")) {
                            Toast.makeText(UserActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                        } else {
                            editor = PreferenceManager.getDefaultSharedPreferences(UserActivity.this).edit();
                            editor.putString("name", name);
                            editor.putString("sex", sex);
                            editor.putString("imgUrl", response);
                            editor.putString("sign", sign);
                            editor.apply();
                            finish();
                        }*/
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UserActivity.this, "网络连接超时", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("userId", String.valueOf(userId));
                map.put("name", name);
                map.put("sex", sex);
                map.put("sign", sign);
                return map;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }



    private void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        ImageLoader.getInstance().init(config.build());
    }


    private String toBase64(String path){
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        if(bitmap == null){
            Log.e("base64","bitmap not found!!") ;
            Toast.makeText(UserActivity.this, "获取图片失败", Toast.LENGTH_SHORT).show();
            return null;
        }
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
            byte[] imgBytes = out.toByteArray();
            String imgUrl = Base64.encodeToString(imgBytes, Base64.DEFAULT);
            Log.e("base64",imgUrl) ;
            return imgUrl;
        } catch (Exception e) {
            return null;
        } finally {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}