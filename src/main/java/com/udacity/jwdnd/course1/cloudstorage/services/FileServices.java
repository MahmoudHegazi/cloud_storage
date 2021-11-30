package com.udacity.jwdnd.course1.cloudstorage.services;
import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.FileViewer;
import com.udacity.jwdnd.course1.cloudstorage.model.FilesData;
import org.springframework.stereotype.Service;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class FileServices {
    private static FileMapper fileMapper;




    public FileServices(FileMapper fileMapper){
        FileServices.fileMapper = fileMapper;
    }


    public FileViewer getFileById(Integer file_id){
        return fileMapper.getFileById(file_id);
    }
    public void add(String file_name, String content_type, String file_size, Integer user_id, Blob file_data){
        fileMapper.insert(file_name, content_type, file_size, user_id, file_data);
    }
    public boolean remove(Integer file_id){
        FileViewer newFile = getFileById(file_id);
        if (newFile != null){
            fileMapper.delete(file_id);
            return true;
        } else {
            return false;
        }

    }

    // this to tell my app which file can have view button


    public FilesData getAllFilesMainData(Integer user_id){
        List<Integer> userFilesIds = fileMapper.getFilesIds(user_id);
        List<Integer> userFilesUserIds = fileMapper.getFilesDataUserId(user_id);
        List<String> userFilesNames = fileMapper.getFilesDataNames(user_id);
        List<String> userFilesContentTypes = fileMapper.getFilesDataContentType(user_id);

        if (userFilesIds.size() < 1){
            return null;
        }
        FilesData mixedObj = new FilesData(userFilesIds, userFilesUserIds, userFilesNames, userFilesContentTypes);
        mixedObj.setIds(userFilesIds);
        return mixedObj;

    }

    public List<Integer> getAllUserFilesIds(Integer user_id){
        return fileMapper.getFilesIds(user_id);
    }

    public List<String> getUserFilesNames(Integer user_id){
        return fileMapper.getFilesDataNames(user_id);
    }

    public List<byte[]> getUserFilesContent(Integer user_id){
        return fileMapper.getUserFilesData(user_id);
    }

}
