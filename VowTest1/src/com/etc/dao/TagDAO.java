package com.etc.dao;

import java.util.List;

import com.etc.entity.Tag;

public interface TagDAO {

	List<Tag> findTagList();
	boolean insertTag(Tag tag);
	Tag findTag(String tagname);
}
