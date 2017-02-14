package ua.com.vrakin.parser;

import java.util.Set;

public class Product {

    private String title;

    private String link;

    private String price;

    private Set<String> images;

    private String description;

    public Product(String title, String link, String price, Set<String> image, String description) {
        this.title = title;
        this.link = link;
        this.price = price;
        this.images = image;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Set<String> getImages() {
        return images;
    }

    public void setImages(Set<String> images) {
        this.images = images;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String printImages(){
        StringBuilder stringBuilder = new StringBuilder("{\"images\": [");
        images.forEach(im -> {
            stringBuilder.append("\"" + im + "\",");
        });
        stringBuilder.deleteCharAt(stringBuilder.length()-1);

        stringBuilder.append("]");

        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        return "Product{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", price='" + price + '\'' +
                ", " + printImages() +
                ", description='" + description + '\'' +
                '}';
    }
}
