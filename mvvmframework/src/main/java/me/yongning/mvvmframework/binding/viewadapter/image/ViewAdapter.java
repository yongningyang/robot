package me.yongning.mvvmframework.binding.viewadapter.image;


import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

/**
 * @author yongningyang@gmail.com
 * @date 2018/11/26
 * @Description
 */
public final class ViewAdapter {
    @BindingAdapter(value = {"url", "placeholderRes", "errRes", "bitmap", "imgRes"}, requireAll = false)
    public static void setImageUri(ImageView imageView, String url, int placeholderRes, int errRes, Bitmap bitmap, int imgRes) {
        if (bitmap != null && !bitmap.isRecycled()) {
            imageView.setImageBitmap(bitmap);
        } else if (imgRes > 0) {
            imageView.setImageResource(imgRes);
        } else if (!TextUtils.isEmpty(url)) {
            //使用Glide框架加载图片
            Glide.with(imageView.getContext())
                    .load(url)
                    .apply(new RequestOptions().placeholder(placeholderRes).error(errRes))
                    .into(imageView);
        }
    }
}

