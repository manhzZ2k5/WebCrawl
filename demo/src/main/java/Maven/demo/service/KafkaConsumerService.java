package Maven.demo.service;

import Maven.demo.entity.Book;
import Maven.demo.repository.BookRepository;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
public class KafkaConsumerService {
    private final BookRepository bookRepository; // final để đảm bảo biến luôn có giá trị (không null)
    private final ObjectMapper mapper = new ObjectMapper(); 

    public KafkaConsumerService (BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }
    
    @KafkaListener(topics = "test_kafka", groupId = "crawler-group", concurrency = "3")

    public void consume (String message){
        try{
        Book book = mapper.readValue(message, Book.class); // truyền và message và thông tin về kiểu dữ liệu Book đã định nghĩa ở Book.java

        if (book.getTitle() == null || book.getLink() == null) return;
        bookRepository.save(book);
        System.out.println("Save book: "+book.getTitle());
        }    
        catch (Exception e){
            System.out.println("Lỗi Bookid: "+e.getMessage());
        }

    }


}
