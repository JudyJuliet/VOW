package com.etc.dao;

import java.util.List;

import com.etc.entity.Tag;
import com.etc.entity.Upload_content;

public interface ContentTagDAO {

	boolean insertContentTag(Upload_content uc,Tag tag);
	
	List<Upload_content>findContentOfTagList(Tag tag);//一个标签下的内容夹列表
	//boolean deleteContentTag(Upload_content uc,Tag tag);
	boolean deleteContentTag(Upload_content uc);
}
