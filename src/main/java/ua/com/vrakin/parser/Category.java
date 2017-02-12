package ua.com.vrakin.parser;

import java.util.ArrayList;
import java.util.List;

public class Category {

    private String url;
    private String name;
    private int pageCount;

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private List<Record> records;

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

    public Category(String url, String name, int pageCount) {
        this.url = url;
        this.name = name;
        this.records = new ArrayList<>();
        this.pageCount = pageCount;
    }

    @Override
    public String toString() {
        final String[] result = {"Category{" +
                "pageCount='" + pageCount + '\'' +
                "url='" + url + '\'' +
                ", name='" + name + '\'' +
                ", records="};
        records.forEach(record -> result[0] += record.toString());
        result[0] += "}";
        return result[0];
    }
}
