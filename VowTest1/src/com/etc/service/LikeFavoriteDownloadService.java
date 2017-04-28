package com.etc.service;

import java.util.List;

import com.etc.entity.Like_favorite_download;
import com.etc.entity.Upload_content;
import com.etc.entity.User;

public interface LikeFavoriteDownloadService {

	Boolean addLikeFavoriteDownload(Like_favorite_download lfd);
	List<Upload_content>getFavoriteList(User user);//查看  我的收藏列表
//	int findLikeTimes(Upload_content uc);//一个内容夹的被赞次数
//	int findFavoriteTimes(Upload_content uc);//一个内容夹的被收藏次数
//	int findDownloadTimes(Upload_content uc);//一个内容夹的被下载次数
	
	Boolean checkIfLike(User user, Upload_content uc);
	Boolean checkIfFavorite(User user, Upload_content uc);
}