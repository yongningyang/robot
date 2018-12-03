package com.innsmap.versionchecker;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;


//http://blog.csdn.net/u013031725/article/details/51480452
public class CustomProgressDialog extends Dialog implements OnClickListener {
	private static final long MB = 1024 * 1024;
	private RelativeLayout rl_dialog_root;
	private TextView tv_progress;
	private TextView tv_upload_file_name;
	private TextView tv_upload_summary_info;
	private CustomProgressBar cpb_show;
	private Button bt_background_update;
	private Button bt_cancel;

	private VersionUpdateDialog.DialogListener listener;
	private Context context;

	public CustomProgressDialog(Context context, boolean cancelable,
                                OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public CustomProgressDialog(Context context, int themeResId) {
		super(context, themeResId);
		this.context = context;
	}

	public CustomProgressDialog(Context context) {
		this(context, R.style.dialog_style);
	}

	public CustomProgressDialog(Context context, VersionUpdateDialog.DialogListener listener) {
		this(context, R.style.dialog_style);
		this.listener = listener;
	}

	public void setShowInfo(long totalBytes, long fileSize) {
		if (null == cpb_show)
			return;
		tv_progress.setText((totalBytes * 100 / fileSize) + "%");
		tv_upload_summary_info.setText(String.format(
				context.getString(R.string.version_update_summary_info),
				fileSize * 1.0 / MB, totalBytes * 1.0 / MB));
		cpb_show.setPersent((totalBytes * 100.0f / fileSize));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inns_library_dialog_progress_update_layout);
		setCancelable(false);
		rl_dialog_root = (RelativeLayout) findViewById(R.id.rl_dialog_root);
		tv_progress = (TextView) rl_dialog_root.findViewById(R.id.tv_progress);
		tv_upload_file_name = (TextView) rl_dialog_root
				.findViewById(R.id.tv_upload_file_name);
		tv_upload_summary_info = (TextView) rl_dialog_root
				.findViewById(R.id.tv_upload_summary_info);
		cpb_show = (CustomProgressBar) rl_dialog_root
				.findViewById(R.id.cpb_show);
		bt_background_update = (Button) rl_dialog_root
				.findViewById(R.id.bt_background_update);
		bt_cancel = (Button) rl_dialog_root.findViewById(R.id.bt_cancel);
		bt_background_update.setOnClickListener(this);
		bt_cancel.setOnClickListener(this);
		tv_upload_file_name.setText("软件更新中...");
	}

	@Override
	public void onClick(View v) {
		if (v == bt_background_update) {
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
}
