package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WebScraper {

    public static void main(String[] args) {
        String url = "https://www.bbc.com";

        try {
            Document doc = Jsoup.connect(url).get();
            String title = doc.title();
//            System.out.println("Page Title: " + title);
//
//            System.out.println("\nHeadings:");
//            for (int i = 1; i <= 6; i++) {
//                Elements headings = doc.select("h" + i);
//                for (Element heading : headings) {
//                    System.out.println("h" + i + ": " + heading.text());
//                }
//            }
//            System.out.println("\nLinks:");
//            Elements links = doc.select("a[href]");
//            for (Element link : links) {
//                System.out.println(link.attr("href") + " (" + link.text() + ")");
//            }

            List<NewsArticle> newsArticles = extractNewsData(doc);

            System.out.println("\nNews Articles:");
            for (NewsArticle article : newsArticles) {
                System.out.println(article);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<NewsArticle> extractNewsData(Document doc) throws IOException {
        String url = "https://www.bbc.com";
        List<NewsArticle> articles = new ArrayList<>();

        Elements newsBlocks = doc.select("[data-testid=dundee-card]");

        for (Element block : newsBlocks) {
            String headline = block.select("[data-testid=card-headline]").text();


            if (!headline.isEmpty()) {
                String link = block.select("a").attr("href");
                String fullUrl = link.startsWith("http") ? link : url + link;
                Document newPage = Jsoup.connect(fullUrl).get();
                String author = newPage.select("[data-testid=byline-new-contributors] div div span").text();
                String date = newPage.select("[data-testid=byline-new] time").text();
                NewsArticle newsArticle = new NewsArticle();
                newsArticle.setHeadline(headline);
                newsArticle.setAuthor(author);
                newsArticle.setDate(date);
                articles.add(newsArticle);
            }
        }

        return articles;
    }

    static class NewsArticle {
        private String headline;
        private String date;
        private String author;

        public String getHeadline() {
            return headline;
        }

        public void setHeadline(String headline) {
            this.headline = headline;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String toString() {
            return "Headline: " + headline +
                    "\nDate: " + date +
                    "\nAuthor: " + author + "\n";
        }
    }
}