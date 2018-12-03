package com.innsmap.guomaorobot.util;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.innsmap.guomaorobot.R;

import static android.animation.ObjectAnimator.ofPropertyValuesHolder;

/**
 * Created by long on 2018/7/9.
 */

public class AnimationUtils {
    public static int[] getRes(Context context, int id) {
        TypedArray typedArray = context.getResources().obtainTypedArray(id);
        int len = typedArray.length();
        int[] resId = new int[len];
        for (int i = 0; i < len; i++) {
            resId[i] = typedArray.getResourceId(i, -1);
        }
        typedArray.recycle();
        return resId;
    }

    private static FrameAnimation frameAnimationStandby;
    private static FrameAnimation frameAnimationMain;

    /**
     * 待机中
     *
     * @param imageView
     */
    public static void standby(final ImageView imageView, Context context) {
        int[] id = getRes(context, R.array.standby);
        if (frameAnimationStandby != null) {
            frameAnimationStandby.stopAnimation();
            frameAnimationStandby.setData(imageView, id, 40, true);
        } else {
            frameAnimationStandby = new FrameAnimation(imageView, id, 40, true);
        }
        frameAnimationStandby.setAnimationListener(new FrameAnimation.AnimationListener() {

            @Override
            public void onAnimationStart() {
            }

            @Override
            public void onAnimationEnd() {
                frameAnimationStandby.stopAnimation();
            }

            @Override
            public void onAnimationRepeat() {
            }
        });
    }

    /**
     * 待机中
     *
     * @param imageView
     */
    public static FrameAnimation rotating(final ImageView imageView, Context context) {
        int[] id = getRes(context, R.array.circle_light);
        FrameAnimation frameAnimation = new FrameAnimation(imageView, id, 40, true);
        frameAnimation.setAnimationListener(new FrameAnimation.AnimationListener() {

            @Override
            public void onAnimationStart() {
            }

            @Override
            public void onAnimationEnd() {
            }

            @Override
            public void onAnimationRepeat() {
            }
        });
        return frameAnimation;
    }

    /**
     * 唤醒1
     *
     * @param imageView
     * @param main_tv_1
     * @param main_tv_2
     * @param main_tv_3
     */
    public static void wakeUp(final ImageView imageView, final Context context, final ScrollView scrollView, final TextView main_tv_1, final TextView main_tv_2, final TextView main_tv_3) {
        int[] id = getRes(context, R.array.wake_up_hi);
        if (frameAnimationMain != null) {
            frameAnimationMain.stopAnimation();
            frameAnimationMain.setData(imageView, id, 40, false);
        } else {
            frameAnimationMain = new FrameAnimation(imageView, id, 40, false);
        }
        frameAnimationMain.setAnimationListener(new FrameAnimation.AnimationListener() {

            @Override
            public void onAnimationStart() {
            }

            @Override
            public void onAnimationEnd() {
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                wakeUpJump(imageView, context, main_tv_1, main_tv_2, main_tv_3);
            }

            @Override
            public void onAnimationRepeat() {
            }
        });
    }
    /**
     * 唤醒2
     *
     * @param imageView
     */
    public static void wakeUpJump(final ImageView imageView, final Context context, final TextView main_tv_1, final TextView main_tv_2, final TextView main_tv_3) {
        int[] id = getRes(context, R.array.wake_up_jump);
        if (frameAnimationMain != null) {
            frameAnimationMain.stopAnimation();
            frameAnimationMain.setData(imageView, id, 40, false);
        } else {
            frameAnimationMain = new FrameAnimation(imageView, id, 40, false);
        }
        frameAnimationMain.setAnimationListener(new FrameAnimation.AnimationListener() {

            @Override
            public void onAnimationStart() {
            }

            @Override
            public void onAnimationEnd() {
                akimbo(imageView, context);
                main_tv_1.setVisibility(View.VISIBLE);
                main_tv_2.setVisibility(View.VISIBLE);
                main_tv_3.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat() {
            }
        });
    }
    /**
     * 叉腰
     *
     * @param imageView
     */
    public static void akimbo(final ImageView imageView, Context context) {
        int[] id = getRes(context, R.array.akimbo);
        if (frameAnimationMain != null) {
            frameAnimationMain.stopAnimation();
            frameAnimationMain.setData(imageView, id, 40, false);
        } else {
            frameAnimationMain = new FrameAnimation(imageView, id, 40, false);
        }
        frameAnimationMain.setAnimationListener(new FrameAnimation.AnimationListener() {

            @Override
            public void onAnimationStart() {
            }

            @Override
            public void onAnimationEnd() {
            }

            @Override
            public void onAnimationRepeat() {
            }
        });
    }

    /**
     * 打招呼
     *
     * @param imageView
     */
    public static void preview(final ImageView imageView, Context context, final ScrollView scrollView) {
        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
        int[] id = getRes(context, R.array.preview);
        if (frameAnimationMain != null) {
            frameAnimationMain.stopAnimation();
            frameAnimationMain.setData(imageView, id, 40, false);
        } else {
            frameAnimationMain = new FrameAnimation(imageView, id, 40, false);
        }
        frameAnimationMain.setAnimationListener(new FrameAnimation.AnimationListener() {

            @Override
            public void onAnimationStart() {
            }

            @Override
            public void onAnimationEnd() {
            }

            @Override
            public void onAnimationRepeat() {
            }
        });
    }
    /**
     * 打招呼
     *
     * @param imageView
     */
    public static void standbyPreview(final ImageView imageView, Context context, final ScrollView scrollView) {
        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
        int[] id = getRes(context, R.array.preview);
        if (frameAnimationStandby != null) {
            frameAnimationStandby.stopAnimation();
            frameAnimationStandby.setData(imageView, id, 40, false);
        } else {
            frameAnimationStandby = new FrameAnimation(imageView, id, 40, false);
        }
        frameAnimationStandby.setAnimationListener(new FrameAnimation.AnimationListener() {

            @Override
            public void onAnimationStart() {
            }

            @Override
            public void onAnimationEnd() {
                frameAnimationStandby.stopAnimation();
            }

            @Override
            public void onAnimationRepeat() {
            }
        });
    }
    /**
     * 无结果
     *
     * @param imageView
     */
    public static void searchNone(final ImageView imageView, Context context, final ScrollView scrollView) {
        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
        int[] id = getRes(context, R.array.search_none);
        if (frameAnimationMain != null) {
            frameAnimationMain.stopAnimation();
            frameAnimationMain.setData(imageView, id, 40, false);
        } else {
            frameAnimationMain = new FrameAnimation(imageView, id, 40, false);
        }
        frameAnimationMain.setAnimationListener(new FrameAnimation.AnimationListener() {

            @Override
            public void onAnimationStart() {
            }

            @Override
            public void onAnimationEnd() {
            }

            @Override
            public void onAnimationRepeat() {
            }
        });
    }

    /**
     * 指纹
     *
     * @param imageView
     */
    public static void fingerprint(final ImageView imageView, Context context) {
        int[] id = getRes(context, R.array.fingerprint);
        if (frameAnimationMain != null) {
            frameAnimationMain.restartAnimation();
        } else {
            frameAnimationMain = new FrameAnimation(imageView, id, 40, false);
        }
        frameAnimationMain.setAnimationListener(new FrameAnimation.AnimationListener() {

            @Override
            public void onAnimationStart() {
            }

            @Override
            public void onAnimationEnd() {
            }

            @Override
            public void onAnimationRepeat() {
            }
        });
    }


    /**
     * 待机页面圆点动画停止
     *
     * @param frameAnimation
     */
    public static void stopRotating(FrameAnimation frameAnimation){
        if (frameAnimation != null) {
            frameAnimation.setRepeat(false);
            frameAnimation = null;
        }
    }
    /**
     * 待机页面圆点动画停止
     *
     * @param frameAnimation
     */
    public static void stop(FrameAnimation frameAnimation){
        if (frameAnimation != null) {
            frameAnimation.stopAnimation();
            frameAnimation = null;
        }
    }

    public static void setNoRepeat(){
        if (frameAnimationStandby != null) {
            frameAnimationStandby.setRepeat(false);
        }
    }

    public static void stopStandby(){
        if (frameAnimationStandby != null){
            frameAnimationStandby.stopAnimation();
//            frameAnimationStandby = null;
        }
    }

    public static void stopMain(){
        if (frameAnimationMain != null){
            frameAnimationMain.stopAnimation();
//            frameAnimationMain = null;
        }
    }


    /**
     * view进行左右抖动
     *
     * @param view
     * @return
     */
    public static void nope(View view) {
        int delta = 10;
        PropertyValuesHolder pvhTranslateX = PropertyValuesHolder.ofKeyframe(View.TRANSLATION_X,
                Keyframe.ofFloat(0f, 0),
                Keyframe.ofFloat(.10f, -delta),
                Keyframe.ofFloat(.26f, delta),
                Keyframe.ofFloat(.42f, -delta),
                Keyframe.ofFloat(.58f, delta),
                Keyframe.ofFloat(.74f, -delta),
                Keyframe.ofFloat(.90f, delta),
                Keyframe.ofFloat(1f, 0f)
        );

        ObjectAnimator jitter = ofPropertyValuesHolder(view, pvhTranslateX).
                setDuration(500);
        jitter.setRepeatCount(Animation.INFINITE);
        jitter.start();
    }

    /**
     * view进行上下抖动
     *
     * @param view
     * @return
     */
    public static void nopeY(View view) {
        int delta = 10;
        PropertyValuesHolder pvhTranslateX = PropertyValuesHolder.ofKeyframe(View.TRANSLATION_Y,
                Keyframe.ofFloat(0f, 0),
                Keyframe.ofFloat(.10f, -delta),
                Keyframe.ofFloat(.26f, delta),
                Keyframe.ofFloat(.42f, -delta),
                Keyframe.ofFloat(.58f, delta),
                Keyframe.ofFloat(.74f, -delta),
                Keyframe.ofFloat(.90f, delta),
                Keyframe.ofFloat(1f, 0f)
        );

        ObjectAnimator jitter = ofPropertyValuesHolder(view, pvhTranslateX).
                setDuration(5000);
        jitter.setRepeatCount(Animation.INFINITE);
        jitter.start();
    }

    /**
     * view进行左右抖动
     *
     * @param view
     * @return
     */
    public static void tada(View view) {
        PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofKeyframe(View.SCALE_X,
                Keyframe.ofFloat(0f, 1f),
                Keyframe.ofFloat(.1f, 1.1f),
                Keyframe.ofFloat(.2f, 1f),
                Keyframe.ofFloat(.3f, 0.9f),
                Keyframe.ofFloat(.4f, 1f),
                Keyframe.ofFloat(.5f, 1.1f),
                Keyframe.ofFloat(.6f, 1f),
                Keyframe.ofFloat(.7f, .9f),
                Keyframe.ofFloat(.8f, 1f),
                Keyframe.ofFloat(.9f, 1.1f),
                Keyframe.ofFloat(1f, 1f)
        );

        PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofKeyframe(View.SCALE_Y,
                Keyframe.ofFloat(0f, 1f),
                Keyframe.ofFloat(.1f, 1.1f),
                Keyframe.ofFloat(.2f, 1f),
                Keyframe.ofFloat(.3f, 0.9f),
                Keyframe.ofFloat(.4f, 1f),
                Keyframe.ofFloat(.5f, 1.1f),
                Keyframe.ofFloat(.6f, 1f),
                Keyframe.ofFloat(.7f, .9f),
                Keyframe.ofFloat(.8f, 1f),
                Keyframe.ofFloat(.9f, 1.1f),
                Keyframe.ofFloat(1f, 1f)
        );

//        PropertyValuesHolder pvhRotate = PropertyValuesHolder.ofKeyframe(View.ROTATION,
//                Keyframe.ofFloat(0f, 0f),
//                Keyframe.ofFloat(.1f, -3f * 1f),
//                Keyframe.ofFloat(.2f, -3f * 1f),
//                Keyframe.ofFloat(.3f, 3f * 1f),
//                Keyframe.ofFloat(.4f, -3f * 1f),
//                Keyframe.ofFloat(.5f, 3f * 1f),
//                Keyframe.ofFloat(.6f, -3f * 1f),
//                Keyframe.ofFloat(.7f, 3f * 1f),
//                Keyframe.ofFloat(.8f, -3f * 1f),
//                Keyframe.ofFloat(.9f, 3f * 1f),
//                Keyframe.ofFloat(1f, 0)
//        );

//        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, pvhScaleX, pvhScaleY, pvhRotate).
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, pvhScaleX, pvhScaleY).
                setDuration(10000);
        objectAnimator.setRepeatCount(Animation.INFINITE);
        objectAnimator.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static ObjectAnimator standbyRotation(ImageView imageView){
        Path path = new Path();
        RectF rectF = new RectF(200, 1560, 800, 1860);
        path.addOval(rectF, Path.Direction.CCW);
        ObjectAnimator traslateAnimator = ObjectAnimator.ofFloat(imageView, "x", "y", path);
        traslateAnimator.setDuration(12000);
        traslateAnimator.setRepeatCount(Animation.INFINITE);
        traslateAnimator.setInterpolator(new LinearInterpolator());
//        traslateAnimator.start();
        return traslateAnimator;
    }


    /**
     *
     * 指纹页面提示动画
     *
     * @param view
     */
    public static ObjectAnimator testTips(View view) {
        PropertyValuesHolder pvhTranslateX = PropertyValuesHolder.ofKeyframe(View.TRANSLATION_X,
                Keyframe.ofFloat(0f, 700),
                Keyframe.ofFloat(1f, 0f)
        );
        PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofKeyframe(View.SCALE_X,
                Keyframe.ofFloat(0f, 0.5f),
                Keyframe.ofFloat(1f, 1f)
        );

        PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofKeyframe(View.SCALE_Y,
                Keyframe.ofFloat(0f, 0.5f),
                Keyframe.ofFloat(1f, 1f)
        );
        PropertyValuesHolder alpha = PropertyValuesHolder.ofKeyframe(View.ALPHA,
                Keyframe.ofFloat(0f, 1f),
                Keyframe.ofFloat(1f, 0f)
        );
        ObjectAnimator animator = ofPropertyValuesHolder(view, pvhScaleX, pvhScaleY, pvhTranslateX, alpha).
                setDuration(1500);
        animator.setRepeatCount(Animation.INFINITE);
        animator.start();
        return animator;
    }

}
