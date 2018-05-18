package com.vivi.reading.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Toast;

import com.vivi.reading.R;
import com.vivi.reading.util.ConnectPHPToGetJSON;
import com.vivi.reading.util.ConstUtils;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WelcomeActivity extends AppCompatActivity {

    private SharedPreferences shared;
    private Editor editor;
    protected int result;

    // 版本更新检查
    private Context context;
    private String URL_Ver = ConstUtils.BASEURL + "appversion.php";
    private String URL_APK = ConstUtils.BASEURL + "download/";
    private String APK_NAME = "reading.apk";

    /* 下载中 */
    private static final int DOWNLOAD = 1;
    /* 下载结束 */
    private static final int DOWNLOAD_FINISH = 2;
    private static final int DOWNLOAD_CANCEL = 3;

    private String verOld;
    private String verNew;
    private int verCheck = -1;
    private ProgressDialog progressDialog;
    private ProgressDialog pd;

    /* 下载保存路径 */
    private String mSavePath;
    /* 记录进度条数量 */
    private int progress;
    /* 是否取消更新 */
    private boolean cancelUpdate = false;
    private Thread connectThread;

    public static int PERMISSION_STORAGE = 0;

    private Handler errHander = new Handler() {
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 0:
                    // List<BasicNameValuePair> params = new
                    // ArrayList<BasicNameValuePair>();
                    // params.add(new BasicNameValuePair("req", "version"));
                    // new Thread(new
                    // com.brbl.brblorderaide.ConnectPHPToGetJSON(URL_Ver,vHandler,params)).start();
                    break;
                case 1:
                    connLogin();
                    break;
                default:
                    break;
            }
        };
    };

    private Handler vHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (msg.obj == null) {// 获取数据失败
                Toast.makeText(context, "网络连接失败", Toast.LENGTH_LONG).show();
            } else {
                try {
                    verNew = ((JSONObject) msg.obj).getString("ver");
                    APK_NAME = ((JSONObject) msg.obj).getString("name");
                    editor.putString("versiononline", verNew);
                    editor.commit();
                    if (verOld.equals(verNew)) {
                        new Thread(new Runnable() {
                            public void run() {
                                try {
                                    Thread.sleep(3000);
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                connLogin();
                            }
                        }).start();
                    } else {
                        showNoticeDialog();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            super.handleMessage(msg);
        };

    };

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 正在下载
                case DOWNLOAD:
                    // 设置进度条位置
                    progressDialog.setProgress(progress);
                    break;
                case DOWNLOAD_FINISH:
                    // 安装文件
                    installApk();
                    break;
                case DOWNLOAD_CANCEL:
                    finish();
                    cancelUpdate = true;
                    break;
                default:
                    break;
            }
        };
    };

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View startView = View.inflate(this, R.layout.activity_welcome, null);
        setContentView(startView);
        context = this;
        shared = getSharedPreferences("userInfo", 0);
        editor = shared.edit();


        AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
        aa.setDuration(2000);
        startView.setAnimation(aa);
        aa.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
                List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
                params.add(new BasicNameValuePair("req", "version"));
                connectThread =  new Thread(new ConnectPHPToGetJSON(URL_Ver, vHandler, params));
                connectThread.start();
                verOld = getVersion();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void changeSlowly(final long delaytime, final int type) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(delaytime);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                errHander.sendEmptyMessage(type);
            }
        }).start();
    }

    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            Log.e("version=",version);
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private void showNoticeDialog() {
        // 构造对话框
        Builder builder = new Builder(context,android.R.style.Theme_DeviceDefault_Light_Dialog);
        builder.setTitle("软件更新");
        builder.setMessage("检测到新版本，立即更新吗？");
        // 更新
        builder.setPositiveButton("更新", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // 显示下载对话框
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && WelcomeActivity.this.checkSelfPermission(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    WelcomeActivity.this.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, PERMISSION_STORAGE);
                }
                else showDownloadDialog();
            }
        });
        // 稍后更新
        builder.setNegativeButton("稍后更新", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                connLogin();
            }
        });
        Dialog noticeDialog = builder.create();
        noticeDialog.setCancelable(false);
        noticeDialog.show();
    }

    private void showDownloadDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("请稍等...");
        progressDialog.setTitle("下载中");
        progressDialog.setProgress(0);
        progressDialog.setMax(100);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        // 下载文件
        downloadApk();
    }

    private void downloadApk() {
        // 启动新线程下载软件
        new downloadApkThread().start();
    }

    private class downloadApkThread extends Thread {
        @Override
        public void run() {
            try {
                // 判断SD卡是否存在，并且是否具有读写权限
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    // 获得存储卡的路径
                    String sdpath = Environment.getExternalStorageDirectory() + "/";
                    mSavePath = sdpath + "download";
                    URL url = new URL(URL_APK + APK_NAME);
                    // 创建连接
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    // 获取文件大小
                    int length = conn.getContentLength();
                    // 创建输入流
                    InputStream is = conn.getInputStream();

                    File file = new File(mSavePath);
                    // 判断文件目录是否存在
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    File apkFile = new File(mSavePath, APK_NAME);
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    int count = 0;
                    // 缓存
                    byte buf[] = new byte[1024];
                    // 写入到文件中
                    do {
                        int numread = is.read(buf);
                        count += numread;
                        // 计算进度条位置
                        progress = (int) (((float) count / length) * 100);
                        // 更新进度
                        mHandler.sendEmptyMessage(DOWNLOAD);
                        if (numread <= 0) {
                            // 下载完成
                            mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                            break;
                        }
                        // 写入文件
                        fos.write(buf, 0, numread);
                    } while (!cancelUpdate);// 点击取消就停止下载.
                    fos.close();
                    is.close();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 取消下载对话框显示
            progressDialog.dismiss();
        }
    };

    private void installApk() {
        File apkfile = new File(mSavePath, APK_NAME);
        if (!apkfile.exists()) {
            return;
        }
        // 通过Intent安装APK文件
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        context.startActivity(i);
        finish();
    }

    private void connLogin() {
        Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showDownloadDialog();
            } else {
                Toast.makeText(this, "请给予存储权限！", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
