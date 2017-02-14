package ua.com.vrakin.parser;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Category {

    private String url;
    private String title;
    private int pageCount;

    int getPageCount() {
        return pageCount;
    }

    void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    String getUrl() {
        return url;
    }

    void setUrl(String url) {
        this.url = url;
    }

    String getTitle() {
        return title;
    }

    void setTitle(String title) {
        this.title = title;
    }

    private ConcurrentLinkedQueue<Product> products;

    ConcurrentLinkedQueue<Product> getProducts() {
        return products;
    }

    void setProducts(ConcurrentLinkedQueue<Product> products) {
        this.products = products;
    }

    Category(String url, String title, int pageCount) {
        this.url = url;
        this.title = title;
        this.products = new ConcurrentLinkedQueue<>();
        this.pageCount = pageCount;
    }

    @Override
    public String toString() {
        final String[] result = {"[{\"category\":" +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", products="};
        products.forEach(product -> result[0] += product.toString());
        result[0] += "}";
        return result[0];
    }
}
