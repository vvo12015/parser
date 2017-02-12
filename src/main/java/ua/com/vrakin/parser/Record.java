package ua.com.vrakin.parser;

public class Record {

    private String name;

    private String link;

    private String price;

    private String image;

    private String description;

    public Record(String name, String link, String price, String image, String description) {
        this.name = name;
        this.link = link;
        this.price = price;
        this.image = image;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Record{" +
                "name='" + name + '\'' +
                ", link='" + link + '\'' +
                ", price='" + price + '\'' +
                ", image='" + image + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
