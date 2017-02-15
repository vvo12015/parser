package ua.com.vrakin.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

public class Main {

    public static final String LINK = "http://obeyclothing.com";

    public static void main(String[] args) throws IOException, InterruptedException {
        Set<Category> menCategories = linkListMen("men");
        Set<Category> womenCategories = linkListMen("women");

        createRecordListForCategory(menCategories, womenCategories);

        try (FileWriter fileWriter = new FileWriter("src/main/resources/jsonObey.json")){


        fileWriter.write("{\"shop\"{\"url\": \"" + LINK + "\", \"logo\":\"OBEY\", \"title\":\"Obey Clothing\"},");
        fileWriter.write("\"collections\": [");
        fileWriter.write("{\"category\": {\"title\":\"men\"}, \"categories\":[");
        menCategories.forEach(c -> {
            try {
                fileWriter.write(c.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        fileWriter.write("]},");
        fileWriter.write("{\"category\": {\"title\":\"women\"}, \"categories\":[");
        womenCategories.forEach(c -> {
            try {
                fileWriter.write(c.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        fileWriter.write("]}]");}
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private static void createRecordListForCategory(Set<Category> menCategories,
                                                    Set<Category> womenCategories) throws InterruptedException {
        List<Callable<String>> callables = new ArrayList<>();

        menCategories.forEach(category -> callables.add(() -> createRecordList(category)));
        womenCategories.forEach(category -> callables.add(() -> createRecordList(category)));

        ExecutorService executor = Executors.newFixedThreadPool(10);
        executor.invokeAll(callables);
        executor.shutdown();
    }

    private static String createRecordList(Category category) {
        String result = "Error in thread";
        try {
            ConcurrentLinkedQueue<Product> products = category.getProducts();
            int i = 1;
            do{
                String url = category.getUrl();
                if (i>1){
                    url += "?page=" + i;
                }
                i++;
                List<Callable<List<Product>>> callables = new ArrayList<>();
                String finalUrl = url;
                callables.add(()->recordList(finalUrl));
                ExecutorService executor = Executors.newFixedThreadPool(10);
                List<Future<List<Product>>> futures = executor.invokeAll(callables);
                executor.shutdown();
                futures.forEach(f -> {
                    try {
                        f.get().forEach(products::add);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                });
            }while (i < category.getPageCount());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        result = "Thread for category " + category.getTitle() + " finish";
        System.out.println(result);
        return result;
    }

    private static List<Product> recordList(String link) throws IOException {
        List<Product> products = new ArrayList<>();

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

            Element divElement = element.getElementsByAttributeValue("class", "product-images").first();

            Set<String> images = new HashSet<>();

            divElement.children().forEach(el -> images.add(el.attr("src")));

            Element imageElement = aElement.child(0);
            String name = imageElement.attr("alt");

            Element pElement = element.getElementsByAttributeValue("class", "grid-item-info").first().child(1);
            String price = pElement.text();

            products.add(new Product(name, recordLink, price, images, description));
        });

        return products;
    }

    private static Set<Category> linkListMen(String sex) throws IOException {
        Set<Category> links = new HashSet<>();

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
        StringBuilder result = new StringBuilder("");

        Element divElement = document.getElementsByAttributeValue("class", "product-info-description js-sticky_hide")
                .first();

        for (int i = 0; i < divElement.children().size()-3; i++) {
            Element pElement = divElement.child(i);

            String pText = pElement.text();

            if (!pText.contains("Sku")) {
                result.append(pElement.text() + "\n");
            }
        }

        return result.toString();
    }
}
