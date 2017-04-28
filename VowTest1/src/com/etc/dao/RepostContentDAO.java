package com.etc.dao;

import com.etc.entity.Repost_content;
import com.etc.entity.Upload_content;

public interface RepostContentDAO {

	boolean insertRepostContent(Repost_content rc);
	boolean setDeleteRepostContent(Upload_content uc);
	int countRepostTimes(Upload_content uc);
}
