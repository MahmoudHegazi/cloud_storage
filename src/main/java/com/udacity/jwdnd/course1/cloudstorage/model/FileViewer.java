package com.udacity.jwdnd.course1.cloudstorage.model;

public class FileViewer {
    static Integer file_id;
    static String file_name;
    static String content_type;
    static String file_size;
    static Integer user_id;
    static byte[] file_data;




    public FileViewer(Integer file_id, String file_name, String content_type, String file_size, Integer user_id, byte [] file_data){
        FileViewer.file_id = file_id;
        FileViewer.file_name = file_name;
        FileViewer.content_type = content_type;
        FileViewer.file_size = file_size;
        FileViewer.user_id = user_id;
        FileViewer.file_data = file_data;

    }

    public Integer getFile_id() {
        return file_id;
    }


    public String getFile_size() {
        return file_size;
    }

    public String getContent_type() {
        return content_type;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public String getFile_name() {
        return file_name;
    }

    public byte[] getFile_data() {
        return file_data;
    }
}
