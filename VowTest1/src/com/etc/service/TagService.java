package com.etc.service;

import java.util.List;

import com.etc.entity.Tag;

public interface TagService {

	List<Tag> getTagList();
	boolean addTag(Tag tag);
	Tag findTag(String tag);
}
