package Maven.demo;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service // Đánh dấu đây là một Service của Spring
public class CrawlerService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper mapper = new ObjectMapper();

    private final String TOPIC_NAME = "test_kafka";

    public void startCrawling() {
        System.out.println("Bắt đầu tiến trình cào dữ liệu đa luồng từ Spring Boot...");
        ExecutorService executor = Executors.newFixedThreadPool(5);

        for (int i = 1; i <= 50; i++) {
            final int pageNumber = i;
            executor.submit(() -> {
                String baseUrl = "https://books.toscrape.com/catalogue/category/books_1/page-" + pageNumber + ".html";
                try {
                    Document doc = Jsoup.connect(baseUrl).get();
                    Elements linkTags = doc.select("h3 a");
                    
                    for (Element linkTag : linkTags) {
                        String title = linkTag.attr("title");
                        String fullLink = linkTag.absUrl("href");

                        Book myBook = new Book(title, fullLink);
                        String bookJson = mapper.writeValueAsString(myBook);

                        //Chỉ cần 1 dòng này để bắn lên Kafka
                        kafkaTemplate.send(TOPIC_NAME, title, bookJson);
                    }
                    System.out.println("Đã xong trang " + pageNumber);
                } catch (Exception e) {
                    System.out.println("Lỗi ở trang " + pageNumber);
                }
            });
        }
        executor.shutdown();
    }
}