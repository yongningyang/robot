package com.innsmap.versionchecker;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class VersionUpdateDialog extends Dialog implements
		View.OnClickListener {
	private TextView tv_title;
	private TextView tv_content;
	private Button bt_update;
	private Button bt_cancel;

	private String title;
	private String content;
	private DialogListener listener;
	private Context context;

	public VersionUpdateDialog(Context context, String content, String title,
                               DialogListener listener) {
		this(context, R.style.dialog_style);
		this.listener = listener;
		this.content = content;
		this.title = title;
	}

	public VersionUpdateDialog(Context context, int themeResId) {
		super(context, themeResId);
	}

	public VersionUpdateDialog(Context context) {
		this(context, R.style.dialog_style);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inns_library_check_version_layout);
		setCanceledOnTouchOutside(true);
		bt_cancel = (Button) findViewById(R.id.bt_version_update_cancel);
		bt_update = (Button) findViewById(R.id.bt_version_update_sure);
		tv_title = (TextView) findViewById(R.id.tv_version_title);
		tv_content = (TextView) findViewById(R.id.tv_version_content);
		if(!TextUtils.isEmpty(content)) {
            tv_content.setText(Html.fromHtml(content));
        }
        if(!TextUtils.isEmpty(title)) {
            tv_title.setText(title);
        }
		bt_cancel.setOnClickListener(this);
		bt_update.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == bt_update) {
			dismiss();
			if (null != listener) {
				listener.onSure();
			}
		} else if (v == bt_cancel) {
			dismiss();
			if (null != listener) {
				listener.onCancel();
			}
		}

	}

	public interface DialogListener {
		void onCancel();

		void onSure();
	}
}
