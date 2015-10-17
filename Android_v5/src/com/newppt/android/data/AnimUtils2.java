package com.newppt.android.data;

import com.newppt1.android_v5.R;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;


public class AnimUtils2 {

	public AnimUtils2() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 动画隐藏上下布局
	 *
	 * @param relativeLayoutTop
	 * @param linearLayoutButtom
	 * @param offest
	 */
	public void startAnimOut(View relativeLayoutTop, View linearLayoutButtom,
			int offest) {
		layoutTopOut(relativeLayoutTop, offest);
		layoutButtomOut(linearLayoutButtom, offest);

	}

	/**
	 * 动画显示上下布局
	 * 
	 * @param relativeLayoutTop
	 * @param linearLayoutButtom
	 * @param offest
	 */
	public void startAnimIn(View relativeLayoutTop, View linearLayoutButtom,
			int offest) {

		layoutTopIn(relativeLayoutTop, offest);
		layoutButtomIn(linearLayoutButtom, offest);
	}

	/**
	 * 动画向右隐藏布局
	 * 
	 * @param view
	 * @param offest
	 */
	public void drawViewOut(View view, int offest) {
		AnimationSet setDraw = new AnimationSet(true);
		TranslateAnimation drawAnimation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f,
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f);
		drawAnimation.setDuration(offest);
		setDraw.addAnimation(drawAnimation);
		// setDraw.setFillAfter(true);
		view.startAnimation(setDraw);
		view.setVisibility(View.GONE);
	}

	/**
	 * 动画向左显示布局
	 * 
	 * @param view
	 * @param offest
	 */
	public void drawViewIn(View view, int offest) {
		AnimationSet setDraw = new AnimationSet(true);
		TranslateAnimation drawAnimation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f);
		drawAnimation.setDuration(offest);
		setDraw.addAnimation(drawAnimation);
		// setDraw.setFillAfter(true);
		view.startAnimation(setDraw);
		view.setVisibility(View.VISIBLE);
	}

	/**
	 * 动画放大
	 * 
	 * @param view
	 * @param offest
	 * @param x
	 * @param y
	 */
	public void imageZoomOut(View view, int offest, float x, float y) {
		AnimationSet setImage = new AnimationSet(true);
		ScaleAnimation imageAnimation = new ScaleAnimation(1f, 1.4f, 1f, 1.4f,
				Animation.ABSOLUTE, x, Animation.ABSOLUTE, y);
		imageAnimation.setDuration(offest);
		setImage.addAnimation(imageAnimation);
		setImage.setFillAfter(true);
		view.startAnimation(setImage);

	}

	/**
	 * 动画缩小
	 * 
	 * @param view
	 * @param offest
	 * @param x
	 * @param y
	 */
	public void imageZoomIn(View view, int offest, float x, float y) {
		AnimationSet setImage = new AnimationSet(true);
		ScaleAnimation imageAnimation = new ScaleAnimation(1.4f, 1f, 1.4f, 1f,
				Animation.ABSOLUTE, x, Animation.ABSOLUTE, y);
		imageAnimation.setDuration(offest);
		setImage.addAnimation(imageAnimation);
		setImage.setFillAfter(true);
		view.startAnimation(setImage);

	}

	/**
	 * 动画隐藏下View
	 * 
	 * @param view
	 * @param offest
	 */
	public void layoutButtomOut(View view, int offest) {
		AnimationSet setButtom = new AnimationSet(true);
		TranslateAnimation animationButtom = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f);
		animationButtom.setDuration(offest);
		setButtom.addAnimation(animationButtom);

		view.startAnimation(setButtom);

		// 重要
		view.setVisibility(View.GONE);
	}

	/**
	 * 动画隐藏上View
	 * 
	 * @param view
	 * @param offest
	 */
	public void layoutTopOut(View view, int offest) {
		AnimationSet setTop = new AnimationSet(true);
		TranslateAnimation animationTop = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, -1f);
		animationTop.setDuration(offest);
		setTop.addAnimation(animationTop);

		view.startAnimation(animationTop);
		// 重要
		view.setVisibility(View.GONE);
	}

	/**
	 * 动画显示上View
	 * 
	 * @param view
	 * @param offest
	 */
	public void layoutTopIn(View view, int offest) {
		AnimationSet setTop = new AnimationSet(true);
		TranslateAnimation animationTop = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, -1f, Animation.RELATIVE_TO_SELF, 0f);
		animationTop.setDuration(offest);
		setTop.addAnimation(animationTop);

		view.startAnimation(animationTop);
		// 重要
		view.setVisibility(View.VISIBLE);
	}

	/**
	 * 动画显示下View
	 * 
	 * @param view
	 * @param offest
	 */
	public void layoutButtomIn(View view, int offest) {
		AnimationSet setButtom = new AnimationSet(true);
		TranslateAnimation animationButtom = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f);
		animationButtom.setDuration(offest);
		setButtom.addAnimation(animationButtom);

		view.startAnimation(setButtom);

		// 重要
		view.setVisibility(View.VISIBLE);
	}

	public void layoutLeftOut(View view, int offest) {
		AnimationSet setLeft = new AnimationSet(true);
		TranslateAnimation animationLeft = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,-1f,
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f);
		animationLeft.setDuration(offest);
		setLeft.addAnimation(animationLeft);

		view.startAnimation(setLeft);

		view.setVisibility(View.GONE);
	}

	public void layoutLeftIn(View view, int offest) {
		AnimationSet setLeft = new AnimationSet(true);
		TranslateAnimation animationLeft = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, -1f, Animation.RELATIVE_TO_SELF,0f,
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,0f);
		animationLeft.setDuration(offest);
		setLeft.addAnimation(animationLeft);

		view.startAnimation(setLeft);

		view.setVisibility(View.VISIBLE);
	}

	public void rotateView(View view, int offest) {
		AnimationSet rotateSet = new AnimationSet(true);
		RotateAnimation animationRotate = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF,	-10f);
		animationRotate.setDuration(offest);
		rotateSet.addAnimation(animationRotate);
		
		view.startAnimation(rotateSet);
	}
	
	public void left_in(Context context, View view) {
		Animation anim = AnimationUtils.loadAnimation(context, R.anim.left_in);
//		view.setAnimation(anim);
		view.startAnimation(anim);
	}
	
	public void left_Out(Context context, View view) {
		Animation anim = AnimationUtils.loadAnimation(context, R.anim.left_out);
//		view.setAnimation(anim);
		view.startAnimation(anim);
	}
	
	public void right_in(Context context, View view) {
		Animation anim = AnimationUtils.loadAnimation(context, R.anim.right_in);
//		view.setAnimation(anim);
		view.startAnimation(anim);
	}
	
	public void right_Out(Context context, View view) {
		Animation anim = AnimationUtils.loadAnimation(context, R.anim.right_out);
//		view.setAnimation(anim);
		view.startAnimation(anim);
	}
}
