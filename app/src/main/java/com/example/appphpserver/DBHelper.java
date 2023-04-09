package com.example.appphpserver;

public interface DBHelper {
    public boolean Add(String name, String address);
    public boolean Show(String id);
    public boolean Update(String id, String new_name, String new_address);
    public boolean Delete(String id);
    public boolean List();
    public void close();
}
