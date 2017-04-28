package com.etc.service;

import java.util.List;

import com.etc.entity.Tag;
import com.etc.entity.Upload_content;

public interface ContentTagService {

	boolean addContentTag(Upload_content uc,Tag tag);
	
	List<Upload_content>findContentOfTagList(Tag tag);//一个标签下的内容夹列表
}
