package com.android.anqiansong.request;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * 文件请求载体<br>
 * <a href="http://blog.csdn.net/qq243223991">安前松博客</a>
 */

public class XUploadFile extends XPost {
    public HashMap<String, List<File>> files;
}
