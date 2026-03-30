# Giải thích `fis.yml`

Tài liệu này mô tả ý nghĩa các cấu hình đang có trong `fis.yml`. Giá trị cụ thể như IP/username/password được giữ nguyên trong file cấu hình, còn ở đây chỉ giải thích mục đích. Lưu ý bảo mật: các trường như `username`, `password`, `bootstrap-servers` là thông tin nhạy cảm, nên quản lý bằng biến môi trường hoặc hệ thống secret nếu triển khai thực tế.

**1) Logging**
- `logging.level.ROOT`: mức log mặc định của toàn ứng dụng (INFO).
- `logging.level.com.fis.ioc.processor`: tăng mức log riêng cho package `com.fis.ioc.processor` (DEBUG) để dễ theo dõi.

**2) Spring Application**
- `spring.application.name`: tên ứng dụng Spring (processor).
- `spring.application.metadata`: URL metadata nội bộ mà ứng dụng sẽ gọi hoặc tham chiếu.

**3) Temporal**
- `spring.temporal.namespace`: namespace của Temporal (default).
- `spring.temporal.connection.target`: địa chỉ kết nối Temporal.
- `spring.temporal.workersAutoDiscovery.packages`: package để tự động quét worker.

**4) Kafka**
- `spring.kafka.bootstrap-servers`: địa chỉ Kafka broker.
- `spring.kafka.consumer`: cấu hình consumer.
- `spring.kafka.consumer.properties.spring.json.trusted.packages`: cho phép deserialize JSON từ mọi package.
- `spring.kafka.consumer.properties.spring.json.use.type.headers`: đọc type info từ header.
- `spring.kafka.consumer.properties.max.poll.interval.ms`: thời gian tối đa giữa 2 lần poll.
- `spring.kafka.consumer.properties.fetch.max.bytes`, `max.partition.fetch.bytes`: giới hạn kích thước message.
- `spring.kafka.consumer.enable-auto-commit`: tắt auto commit để tự kiểm soát offset.
- `spring.kafka.consumer.auto-offset-reset`: nếu chưa có offset thì đọc từ đầu (earliest).
- `spring.kafka.consumer.key-deserializer`, `value-deserializer`: class deserialize key/value.
- `spring.kafka.consumer.group-id`: consumer group.
**Kafka Producer**
- `spring.kafka.producer.key-serializer`, `value-serializer`: class serialize key/value.
- `spring.kafka.producer.properties.spring.json.add.type.headers`: thêm type info vào header.
- `spring.kafka.producer.properties.max.request.size`: giới hạn kích thước request gửi đi.
- `spring.kafka.producer.properties.buffer.memory`: bộ nhớ buffer cho producer.

**5) Datasource**
Mục `spring.datasource` cấu hình nhiều nguồn dữ liệu (multi-datasource).

**5.1) Raw**
- `spring.datasource.raw.*`: cấu hình PostgreSQL cho raw.
- `jdbc-url`, `username`, `password`: thông tin kết nối.
- `type`: dùng HikariCP.
- `hikari.*`: cấu hình pool (tên pool, min/max, timeout).

**5.2) Staging**
- `spring.datasource.staging.*`: PostgreSQL cho staging.
- Pool tên `HikariPool-Staging`, min/max connection.

**5.3) DW (Data Warehouse)**
- `spring.datasource.dw.*`: PostgreSQL cho datawarehouse.
- Cấu hình tương tự staging.

**5.4) Analysis**
- `spring.datasource.analysis.*`: PostgreSQL cho analysis.
- Cấu hình tương tự staging.

**5.5) ClickHouse**
- `spring.datasource.clickhouse.*`: ClickHouse datasource.
- `jdbc-url`: URL kết nối ClickHouse.
- `driver-class-name`: driver ClickHouse.
- `hikari.*`: cấu hình pool riêng.

**6) Snapshot Job**
- `snapshot.source-table`: bảng nguồn để chụp snapshot.
- `snapshot.target-table`: bảng đích lưu snapshot.
- `snapshot.cron`: lịch chạy (cron) — ở đây là 23:58 ngày cuối tháng.
- `snapshot.timezone`: timezone chạy cron.

**7) Server**
- `server.port`: cổng chạy ứng dụng (8088).

**8) Samples**
- `samples.data.language`: ngôn ngữ mẫu (english).

**9) Application Kafka Topics**
- `application.kafka-topic.*`: mapping tên topic Kafka dùng trong nghiệp vụ.
- Ví dụ: `ioc-metadata-update-rule`, `ioc-processor-init-schema`, ...

**Gợi ý cải thiện**
Nếu muốn an toàn hơn khi triển khai:
- Di chuyển `username`, `password`, `bootstrap-servers`, `jdbc-url` sang biến môi trường.
- Bật `spring.config.import` (đang comment) để lấy cấu hình từ config server nếu cần.
