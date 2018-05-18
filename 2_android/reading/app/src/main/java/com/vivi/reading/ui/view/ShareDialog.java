package com.vivi.reading.ui.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.vivi.reading.R;
import com.vivi.reading.util.ConstUtils;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ShareDialog extends Dialog
{
	private ImageView img_QQ;
	private ImageView img_Qzone;
	private ImageView img_Wx;
	private ImageView img_Friends;
	private Button btnCancel;

	private String share_appname;
	private String share_title;
	private String share_msg;
	private String share_logourl = ConstUtils.BASEURL + "img/" +"icon_reading_logo.png";
	private String share_url = "";

	private Tencent mTencent;
	private Activity context;
	private IWXAPI api;

	public ShareDialog(Activity context, String url, String title, String msg)
	{
		super(context, R.style.dialog);
		this.share_title = title;
		this.share_msg = msg;
		this.share_url = url;
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_share);

		mTencent = Tencent.createInstance(ConstUtils.Tencent_APP_ID, context);

		img_QQ = (ImageView) findViewById(R.id.iv_share_qq);
		img_Qzone = (ImageView) findViewById(R.id.iv_share_qzone);
		img_Wx = (ImageView) findViewById(R.id.iv_share_weixin);
		img_Friends = (ImageView) findViewById(R.id.iv_share_frends);
		btnCancel = (Button) findViewById(R.id.btn_cancel);

		api = WXAPIFactory.createWXAPI(context, "wxaf610c1b8e5d1798", true);
		api.registerApp("wxaf610c1b8e5d1798");
		
		View.OnClickListener listener = new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				WXWebpageObject webpage = new WXWebpageObject();
				webpage.webpageUrl = share_url;
				
				WXMediaMessage msg = new WXMediaMessage(webpage);
				msg.title = share_title;
				msg.description = share_msg;
				
				//
				Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_logo);
				msg.thumbData = bmpToByteArray(thumb, true);
				
				SendMessageToWX.Req req = new SendMessageToWX.Req();
				req.transaction = buildTransaction("webpage");
				req.message = msg;
				
				switch (view.getId())
				{
					case R.id.iv_share_qq:
					{
						Log.v("lishide", "qq");

						final Bundle params = new Bundle();
						params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
						params.putString(QQShare.SHARE_TO_QQ_TITLE, share_title);
						params.putString(QQShare.SHARE_TO_QQ_SUMMARY, share_msg);
						params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, share_url);
						params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, share_logourl);
						params.putString(QQShare.SHARE_TO_QQ_APP_NAME, share_appname);

						mTencent.shareToQQ(context, params, qqShareListener);
					}
						break;
					case R.id.iv_share_qzone:
					{
						Log.v("lishide", "qzone");
						final Bundle params = new Bundle();

						ArrayList<String> imageUrls = new ArrayList<String>();
						imageUrls.add(share_logourl);

						params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
						params.putString(QzoneShare.SHARE_TO_QQ_TITLE, share_title);// 必填
						params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, share_msg);// 选填
						params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, share_url);// 必填
						params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
						mTencent.shareToQzone(context, params, qqShareListener);
					}
						break;
					case R.id.iv_share_weixin:
						/*if (isWeixinAvilible(context))
						{
							//req.scene = SendMessageToWX.Req.WXSceneSession;
							//api.sendReq(req);
							share2weixin(0);
						}
						else
						{
							Toast.makeText(context, "该手机未安装微信", Toast.LENGTH_SHORT).show();
						}*/
						share2weixin();
						Log.e("share","share_weixin");
						break;
					case R.id.iv_share_frends:
						/*if (isWeixinAvilible(context))
						{
							//req.scene = SendMessageToWX.Req.WXSceneTimeline;
							//api.sendReq(req);
							share2weixin(1);
						}
						else
						{
							Toast.makeText(context, "该手机未安装微信", Toast.LENGTH_SHORT).show();
						}*/
						share2weixinFriends();
						Log.e("share","share_firends");
						break;
				}

				ShareDialog.this.dismiss();
			}
		};

		img_QQ.setOnClickListener(listener);
		img_Qzone.setOnClickListener(listener);
		img_Wx.setOnClickListener(listener);
		img_Friends.setOnClickListener(listener);
		btnCancel.setOnClickListener(listener);
	}

	/*private void share2weixin(int flag) {
		if (!api.isWXAppInstalled()) {
			Toast.makeText(context, "您还未安装微信客户端",
					Toast.LENGTH_SHORT).show();
			return;
		}

		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = share_url;
		WXMediaMessage msg = new WXMediaMessage(webpage);

		msg.title = share_title;
		msg.description = share_msg;
		Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_logo);
		msg.setThumbImage(thumb);
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = String.valueOf(System.currentTimeMillis());
		req.message = msg;
		req.scene = flag;
		api.sendReq(req);
	}*/
	private void share2weixin() {
		if (!api.isWXAppInstalled()) {
			Toast.makeText(context, "您还未安装微信客户端",
					Toast.LENGTH_SHORT).show();
			return;
		}
		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = share_url;
		WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title = share_title;
		msg.description = share_msg;
		Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
		msg.setThumbImage(thumb);
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = String.valueOf(System.currentTimeMillis());
		req.message = msg;
		req.scene = SendMessageToWX.Req.WXSceneTimeline;
		api.sendReq(req);
	}
	private void share2weixinFriends() {
		if (!api.isWXAppInstalled()) {
			Toast.makeText(context, "您还未安装微信客户端",
					Toast.LENGTH_SHORT).show();
			return;
		}
		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = share_url;
		WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title = share_title;
		msg.description = share_msg;
		Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
		msg.setThumbImage(thumb);
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = String.valueOf(System.currentTimeMillis());
		req.message = msg;
		req.scene = SendMessageToWX.Req.WXSceneSession;
		api.sendReq(req);
	}

	IUiListener qqShareListener = new IUiListener()
	{
		@Override
		public void onCancel()
		{
//			Toast.makeText(context, "onCancel: ", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onComplete(Object response)
		{
//			Toast.makeText(context, "onComplete: " + response.toString(), Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onError(UiError e)
		{
//			Toast.makeText(context, "onError: " + e.errorMessage, Toast.LENGTH_SHORT).show();
		}
	};
	
	public boolean isWeixinAvilible(Context context)
	{
		final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
		List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
		if (pinfo != null)
		{
			for (int i = 0; i < pinfo.size(); i++)
			{
				String pn = pinfo.get(i).packageName;
				if (pn.equals("com.tencent.mm"))
				{
					return true;
				}
			}
		}

		return false;
	}
	
	private String buildTransaction(final String type)
	{
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}
	
	private byte[] bmpToByteArray(final Bitmap bitmap, final boolean needRecycle)
	{
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
		if (needRecycle)
		{
			bitmap.recycle();
		}
		byte[] result = output.toByteArray();
		try
		{
			output.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
}
