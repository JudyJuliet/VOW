package com.etc.dao;

import com.etc.entity.Content_file;
import com.etc.entity.Upload_content;

public interface ContentFileDAO {

	boolean insertContentFile(Content_file cf);
	boolean deleteContentFile(Upload_content uc);
}
