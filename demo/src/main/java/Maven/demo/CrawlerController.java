package Maven.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/crawler")
public class CrawlerController {

    @Autowired
    private CrawlerService crawlerService;

    @PostMapping("/start")
    public ResponseEntity<String> startCrawlJob() {
        // Gọi service cào dữ liệu chạy ngầm
        crawlerService.startCrawling();
        
        // Trả về phản hồi cho người dùng ngay lập tức
        return ResponseEntity.ok("Tiến trình cào dữ liệu đang được chạy ngầm. Hãy kiểm tra Kafka UI!");
    }
}