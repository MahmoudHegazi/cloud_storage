package com.udacity.jwdnd.course1.cloudstorage.model;

import java.util.List;

public class FilesData {

    public static List<Integer> uids;
    public static List<Integer> ids;
    public static List<String> names;
    public static List<String> contentTypes;


    public FilesData(List<Integer> ids, List<Integer> uids, List<String> names, List<String> contentTypes){
        FilesData.ids = ids;
        FilesData.uids = uids;
        FilesData.names = names;
        FilesData.contentTypes = contentTypes;
    }


    public List<Integer> getIds() {
        return ids;
    }





    public void setIds(List<Integer> ids) {
        FilesData.ids = (java.util.List<java.lang.Integer>) ids;
    }

    public List<Integer> getUids() {
        return uids;
    }

    public List<String> getNames() {
        return names;
    }

    public List<String> getContentTypes() {
        return contentTypes;
    }


}
