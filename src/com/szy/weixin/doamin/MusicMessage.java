package com.szy.weixin.doamin;

import com.szy.weixin.base.BaseMessage;
import com.szy.weixin.domain.Musics;

public class MusicMessage extends BaseMessage{

	private Musics Music;

	public Musics getMusic() {
		return Music;
	}

	public void setMusic(Musics music) {
		Music = music;
	}
	
}
