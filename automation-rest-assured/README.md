# RestAssured Hybrid Framework

## Description

RestAssured Hybrid Framework là một framework kiểm thử tự động mạnh mẽ và dễ bảo trì, được thiết kế chuyên biệt cho việc kiểm thử API (API Testing). Framework cung cấp bộ công cụ tối ưu giúp đảm bảo chất lượng phần mềm, dễ dàng tái sử dụng mã nguồn và xuất các báo cáo kiểm thử chi tiết, trực quan.

## Tech Stack

Dự án được xây dựng dựa trên các công nghệ và thư viện cốt lõi sau:

- **Ngôn ngữ lập trình:** Java
- **Thư viện API Testing:** Rest Assured
- **Framework kiểm thử:** TestNG
- **Công cụ quản lý dự án & Build:** Maven
- **Công cụ Reporting:** Allure Report
- **Xử lý JSON:** Jackson / Gson

## Key Features

- **Data Driven Testing (DDT):** Hỗ trợ thực thi kịch bản kiểm thử với nhiều bộ dữ liệu khác nhau thông qua TestNG DataProviders hoặc đọc từ file bên ngoài (JSON, Excel, CSV).
- **Schema Validation:** Xác thực cấu trúc dữ liệu trả về (JSON/XML Schema) để đảm bảo tính toàn vẹn của API.
- **Logging:** Cơ chế ghi log chi tiết cho các Request và Response, hỗ trợ quá trình debug và truy vết lỗi nhanh chóng.
- **Integration với CI/CD:** Dễ dàng tích hợp vào các pipeline Continuous Integration / Continuous Deployment (như Jenkins, GitLab CI, GitHub Actions) để thực thi tự động.

## Project Structure

Dự án tuân theo cấu trúc chuẩn của một dự án Maven để đảm bảo sự tách biệt rõ ràng và dễ quản lý:

```text
RestAssured-Hybrid-Framework/
├── src/
│   ├── main/
│   │   └── java/          # Chứa core framework, API clients, utility helpers, POJO classes
│   └── test/
│       └── java/          # Chứa các test scripts và test classes
├── config/                # Chứa các file cấu hình cho từng môi trường (dev, qa, staging...)
├── test-data/             # Chứa dữ liệu đầu vào cho kiểm thử (file JSON, Excel...)
├── pom.xml                # Quản lý dependencies và cấu hình Maven build
└── testng.xml             # File cấu hình test suite của TestNG
```

## Reporting

Framework sử dụng **Allure Report** để tạo các báo cáo kiểm thử đẹp mắt, dễ đọc và cung cấp cái nhìn tổng quan cho cả team DEV và QA.
Sau khi chạy test xong, bạn có thể xem báo cáo bằng lệnh sau tại thư mục gốc của dự án:

```bash
allure serve target/allure-results
```

_(Lưu ý: Bạn cần cài đặt Allure commandline trên máy để chạy lệnh này)_

## Environment Config (chuẩn hóa)

- File chung: `src/test/resources/config/configs.properties`
- File theo môi trường:
  - `src/test/resources/config/configs-dev.properties`
  - `src/test/resources/config/configs-qa.properties`

Chạy theo môi trường bằng Maven:

```bash
mvn clean test -Denv=qa
```

Nếu không truyền `-Denv`, framework mặc định dùng `dev`.

## Domain Pattern

Framework được tách theo domain `client/service/assertion`:

- `domain/auth/*`
- `domain/booking/*`
- `domain/user/*`

`Token` được quản lý bằng `ThreadLocal` để an toàn khi chạy song song.
