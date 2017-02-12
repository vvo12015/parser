package ua.com.vrakin.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static final String LINK = "http://obeyclothing.com";

    public static void main(String[] args) throws IOException {
        List<Category> menCategories = linkListMen("men");
        List<Category> womenCategories = linkListMen("women");

        menCategories.forEach(category -> {
            try {
                List<Record> records = new ArrayList<>();
                int i = 1;
                do{
                    String url = category.getUrl();
                    if (i>1){
                        url += "?page=" + i;
                    }
                    i++;
                    recordList(url).forEach(record -> records.add(record));
                }while (i < category.getPageCount());
                category.setRecords(records);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
                System.out.println(category);
            }
        });
    /*    womenCategories.forEach(category -> {
            try {
                category.setRecords(recordList(category.getUrl()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
*/
        menCategories.forEach(System.out::println);
        System.out.println("WOMEN");
        womenCategories.forEach(System.out::println);
    }

    private static List<Record> recordList(String link) throws IOException {
        List<Record> records = new ArrayList<>();

        Document document = Jsoup.connect(link).get();

        Elements elements = document.getElementsByAttributeValue("class", "grid-item grid-item--product");

        elements.forEach(element -> {
            Element aElement = element.getElementsByAttributeValue("class", "grid-item-image").first();

            String recordLink = aElement.attr("href");
            if (!(recordLink.substring(0, 4).equals("http"))){
                recordLink = LINK + recordLink;
            }
            String description="";
            try {
                description = getDescription(recordLink);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Element imageElement = aElement.child(0);
            String image = imageElement.attr("src");
            if (image.length() > 0) {
                image = LINK + image.substring(1);
            }
            String name = imageElement.attr("alt");

            Element pElement = element.getElementsByAttributeValue("class", "grid-item-info").first().child(1);
            String price = pElement.text();

            records.add(new Record(name, recordLink, price, image, description));
        });

        return records;
    }

    private static List<Category> linkListMen(String sex) throws IOException {
        List<Category> links = new ArrayList<>();

        Document document = Jsoup.connect(LINK + "/collections/" + sex).get();

        Element divElement = document.getElementsByAttributeValue("class", "sub_nav mobile-hide__block").get(0);

        Elements aElements = divElement.getElementsByAttribute("href");

        aElements.forEach(liElement ->{
            String url = liElement.attr("href");
            if ((url.indexOf("insta"))==-1) {
                url = "http://obeyclothing.com" + liElement.attr("href");
                String title = liElement.text();

                try {
                    int pageCount = getPageCount(url);
                    links.add(new Category(url, title, pageCount));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        return links;
    }

    public static Integer getPageCount(String link) throws IOException {
        int pageCount = 0;

        Document document = Jsoup.connect(link).get();

        Element element = document.getElementsByAttributeValue("class", "pagination-numbered").first();

        try {
            pageCount = Integer.parseInt(element.children().last().text());
        }catch (Exception e){}

        return pageCount;
    }

    public static String getDescription(String link) throws IOException {

        Document document = Jsoup.connect(link).get();

        Element divElement = document.getElementsByAttributeValue("class", "product-info-description js-sticky_hide")
                .first();
        Element pElement = divElement.child(1);

        return pElement.text();
    }
}
