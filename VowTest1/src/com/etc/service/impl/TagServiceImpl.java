package com.etc.service.impl;

import java.util.List;

import com.etc.dao.TagDAO;
import com.etc.dao.impl.TagDAOImpl;
import com.etc.entity.Tag;
import com.etc.service.TagService;

public class TagServiceImpl implements TagService {

	TagDAO tagDAO=new TagDAOImpl();
	
	@Override
	public List<Tag> getTagList() {
		// TODO Auto-generated method stub
		return tagDAO.findTagList();
	}

	@Override
	public boolean addTag(Tag tag) {
		// TODO Auto-generated method stub
		return tagDAO.insertTag(tag);
	}

	@Override
	public Tag findTag(String tagname) {
		// TODO Auto-generated method stub
		
		return tagDAO.findTag(tagname);
	}

}
