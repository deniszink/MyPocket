package com.denis.mypocket;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.annotation.ColorRes;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;

public class AnimationUtils {

    public interface OnRevealAnimationListener {
        void onRevealHide();
        void onRevealShow();
    }

    public static void animateRevealHide(final Context ctx, final View view, @ColorRes int color,
                                         final int finalRadius, OnRevealAnimationListener listener) {
        int cx = (view.getLeft() + view.getRight()) / 2;
        int cy = (view.getTop() + view.getBottom()) / 2;
        int initialRadius = view.getWidth();

        Animator anim =
                ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, finalRadius);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                view.setBackgroundColor(ctx.getResources().getColor(color));
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                listener.onRevealHide();
                view.setVisibility(View.INVISIBLE);
            }
        });
        anim.setDuration(500);
        anim.start();
    }

    public static void animateRevealShow(final Context ctx, final View view, final int startRadius,
                                         @ColorRes int color, int x, int y, OnRevealAnimationListener listener) {
        float finalRadius = (float) Math.hypot(view.getWidth(), view.getHeight());

        Animator anim =
                ViewAnimationUtils.createCircularReveal(view, x, y, startRadius, finalRadius);
        anim.setDuration(500);
        anim.setStartDelay(100);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setBackgroundColor(ctx.getResources().getColor(color));
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.VISIBLE);
                listener.onRevealShow();
            }
        });
        anim.start();
    }


}
