package com.etc.service;

import com.etc.entity.Repost_content;
import com.etc.entity.Upload_content;

public interface RepostContentService {

	boolean insertRepostContent(Repost_content rc);

	int countRepostTimes(Upload_content uc);
}
