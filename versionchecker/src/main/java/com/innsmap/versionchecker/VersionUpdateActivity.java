package com.innsmap.versionchecker;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class VersionUpdateActivity extends Activity implements OnClickListener {
	private RelativeLayout layout_title;
    private ImageView backLl;
    private TextView title_tv;
    private ImageView logo;
	private TextView updateVersionTv;
	private Dialog dialog;
	private TextView currentVersionTv, newVersionTv;
	private VersionUpdateDialog mVersionUpdateDialog;
	private UpdateVersionUtils mUpdateVersionUtils;
	private VersionUpdateInfo mUpdateInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inns_library_version_update_layout);
		initView();
		initEvent();
		init();
	}

	private void initView() {
		Intent intent = getIntent();
		int title_color = intent.getIntExtra("titleColor", Integer.MAX_VALUE);
		int text_color = intent.getIntExtra("textColor", Integer.MAX_VALUE);
		int button_text_color = intent.getIntExtra("buttonTextColor", Integer.MAX_VALUE);

		layout_title = (RelativeLayout) findViewById(R.id.layout_title);
		if (title_color != Integer.MAX_VALUE){
			layout_title.setBackgroundColor(title_color);
		}
		backLl = (ImageView) findViewById(R.id.ll_back);
		if (Constants.backImage != null){
			backLl.setImageBitmap(Constants.backImage);
		}
        title_tv = (TextView) findViewById(R.id.title_tv);
		if (text_color != Integer.MAX_VALUE){
			title_tv.setTextColor(text_color);
		}
        logo = (ImageView) findViewById(R.id.logo);
        if (Constants.logoImage != null){
            logo.setImageBitmap(Constants.logoImage);
        }
		updateVersionTv = (TextView) findViewById(R.id.tv_update);
        if (button_text_color != Integer.MAX_VALUE){
            updateVersionTv.setTextColor(button_text_color);
        }
		currentVersionTv = (TextView) findViewById(R.id.tv_current_version);
		newVersionTv = (TextView) findViewById(R.id.tv_new_version);

	}

	private void initEvent() {
		backLl.setOnClickListener(this);
		updateVersionTv.setOnClickListener(this);
	}

	private void init() {
		mUpdateInfo = Constants.updateInfo;
		mUpdateVersionUtils = new UpdateVersionUtils(this);
		currentVersionTv
				.setText("当前版本：" + mUpdateVersionUtils.getVerName(this));
		if (mUpdateInfo == null) {
			newVersionTv
					.setText("最新版本：" + mUpdateVersionUtils.getVerName(this));
		} else
			newVersionTv.setText("最新版本：" + mUpdateInfo.versionName);

	}

	@Override
	public void onClick(View v) {
		int i = v.getId();
		if (i == R.id.ll_back) {
			finish();
		} else if (i == R.id.tv_update) {
			if (Constants.isUpdate){
				Toast.makeText(VersionUpdateActivity.this, "正在后台更新,请不要重复下载", Toast.LENGTH_SHORT).show();
				return;
			}
			mUpdateVersionUtils.setCancelUpdate(false);
			if (Constants.updateInfo == null) {
				Toast.makeText(this, "当前已经是最新版本", Toast.LENGTH_SHORT).show();
				return;
			}
			if (Constants.updateInfo.versionCode > UpdateVersionUtils
					.getVerCode(VersionUpdateActivity.this)) {
				mVersionUpdateDialog = new VersionUpdateDialog(
						VersionUpdateActivity.this, mUpdateInfo.updContent,
						mUpdateInfo.versionName, new VersionUpdateDialog.DialogListener() {

					@Override
					public void onSure() {
						Constants.isUpdate = true;
						mUpdateVersionUtils.downloadFile(
								mUpdateInfo.appUrl,
								mUpdateInfo.appName, mUpdateVersionUtils.mHandler);
						mUpdateVersionUtils.mCustomProgressDialog.show();

					}

					@Override
					public void onCancel() {

					}
				});
				mVersionUpdateDialog.show();
			} else {
				Toast.makeText(this, "当前已经是最新版本",Toast.LENGTH_SHORT).show();
			}

		}
	}

}
