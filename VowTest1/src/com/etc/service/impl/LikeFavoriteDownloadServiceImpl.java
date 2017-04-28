package com.etc.service.impl;

import java.util.List;

import com.etc.dao.LikeFavoriteDownloadDAO;
import com.etc.dao.impl.LikeFavoriteDownloadDAOImpl;
import com.etc.entity.Like_favorite_download;
import com.etc.entity.Upload_content;
import com.etc.entity.User;
import com.etc.service.LikeFavoriteDownloadService;


	public class LikeFavoriteDownloadServiceImpl implements LikeFavoriteDownloadService {

		LikeFavoriteDownloadDAO lfdDAO=new LikeFavoriteDownloadDAOImpl();
		
		@Override
		public Boolean addLikeFavoriteDownload(Like_favorite_download lfd) {
			// TODO Auto-generated method stub
			return lfdDAO.insertLikeFavoriteDownload(lfd);
		}

		@Override
		public List<Upload_content> getFavoriteList(User user) {
			// TODO Auto-generated method stub
			return lfdDAO.getFavoriteList(user);
		}

		@Override
		public Boolean checkIfLike(User user, Upload_content uc) {
			// TODO Auto-generated method stub
			return lfdDAO.checkIfLike(user, uc);
		}

		@Override
		public Boolean checkIfFavorite(User user, Upload_content uc) {
			// TODO Auto-generated method stub
			return lfdDAO.checkIfFavorite(user, uc);
		}

	}
