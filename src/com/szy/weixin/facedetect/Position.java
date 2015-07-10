package com.szy.weixin.facedetect;

import com.szy.weixin.facedetect.position.Center;
import com.szy.weixin.facedetect.position.Eye_left;
import com.szy.weixin.facedetect.position.Eye_right;
import com.szy.weixin.facedetect.position.Mouth_left;
import com.szy.weixin.facedetect.position.Mouth_right;
import com.szy.weixin.facedetect.position.Nose;

public class Position {

	private Center center;
	private String height;
	private String width;
	
	private Eye_left eye_left;
	private Eye_right Eye_right;
	private Mouth_left mouth_left;
	private Mouth_right mouth_right;
	private Nose nose;
	
	public Eye_left getEye_left() {
		return eye_left;
	}
	public void setEye_left(Eye_left eye_left) {
		this.eye_left = eye_left;
	}
	public Eye_right getEye_right() {
		return Eye_right;
	}
	public void setEye_right(Eye_right eye_right) {
		Eye_right = eye_right;
	}
	public Mouth_left getMouth_left() {
		return mouth_left;
	}
	public void setMouth_left(Mouth_left mouth_left) {
		this.mouth_left = mouth_left;
	}
	public Mouth_right getMouth_right() {
		return mouth_right;
	}
	public void setMouth_right(Mouth_right mouth_right) {
		this.mouth_right = mouth_right;
	}
	public Center getCenter() {
		return center;
	}
	public void setCenter(Center center) {
		this.center = center;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public Nose getNose() {
		return nose;
	}
	public void setNose(Nose nose) {
		this.nose = nose;
	}
	
	
}
