# Hướng dẫn Cài đặt Môi trường (Environment Setup Guide)

Tài liệu này cung cấp hướng dẫn chi tiết từng bước để thiết lập môi trường và chạy framework **RestAssured Hybrid Framework** trên máy cá nhân của bạn.

## Prerequisites

Trước khi bắt đầu, hãy đảm bảo máy tính của bạn đã được cài đặt các phần mềm sau:

- **Java Development Kit (JDK):** Yêu cầu bản JDK 11 hoặc JDK 17.
- **Maven:** Phiên bản 3.6 trở lên.
- **IDE:** IntelliJ IDEA.
- **Git:** Dùng để clone mã nguồn.
- **Allure Commandline (Tùy chọn):** Để xem báo cáo Allure sau khi chạy test.

## Environment Variables

Bạn cần cài đặt biến môi trường cho Java và Maven để hệ thống có thể nhận diện các lệnh `java` và `mvn`.

### Trên Windows:

1. Mở Start menu, tìm kiếm "Environment Variables" và chọn **"Edit the system environment variables"**.
2. Click vào nút **Environment Variables**.
3. Ở phần "System variables", click **New**:
   - **Variable name:** `JAVA_HOME`
   - **Variable value:** Đường dẫn tới thư mục cài đặt JDK (VD: `C:\Program Files\Java\jdk-17`)
4. Click **New** lần nữa cho Maven:
   - **Variable name:** `MAVEN_HOME` (hoặc `M2_HOME`)
   - **Variable value:** Đường dẫn tới thư mục giải nén Maven (VD: `C:\apache-maven-3.8.8`)
5. Tìm biến `Path` trong danh sách, chọn và click **Edit**. Thêm 2 dòng sau vào:
   - `%JAVA_HOME%\bin`
   - `%MAVEN_HOME%\bin`
6. Mở Command Prompt (CMD) mới và kiểm tra cài đặt bằng lệnh:
   ```bash
   java -version
   mvn -version
   ```

### Trên macOS/Linux:

Thêm các dòng sau vào file cấu hình shell của bạn (VD: `~/.bash_profile`, `~/.zshrc`, hoặc `~/.bashrc`):

```bash
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home # Sửa lại theo đường dẫn thực tế máy bạn
export MAVEN_HOME=/opt/apache-maven-3.8.8 # Sửa lại theo đường dẫn thực tế máy bạn
export PATH=$JAVA_HOME/bin:$MAVEN_HOME/bin:$PATH
```

Sau đó chạy lệnh: `source ~/.bash_profile` (hoặc file tương ứng) để áp dụng.

## Clone Project

Mở Terminal hoặc Command Prompt, di chuyển đến thư mục làm việc mong muốn và chạy lệnh sau để tải dự án về máy:

```bash
git clone <URL_repository_cua_du_an>
cd automation-rest-assured
```

## Project Configuration

1. Mở **IntelliJ IDEA**.
2. Chọn **File > Open...** (hoặc nút "Open" trên màn hình Welcome).
3. Điều hướng tới thư mục `automation-rest-assured` vừa clone và chọn file `pom.xml`, sau đó click **OK**.
4. Chọn **Open as Project** khi được hệ thống hỏi.
5. IntelliJ sẽ tự động nhận diện đây là dự án Maven. Hãy đợi một chút để IntelliJ bắt đầu (download dependencies) tải toàn bộ các thư viện cần thiết đã khai báo trong `pom.xml`. Bạn có thể theo dõi tiến trình ở góc dưới bên phải màn hình hoặc qua tab "Build".
6. Đảm bảo cấu hình Project SDK đang sử dụng đúng Java 11 hoặc 17 bằng cách vào **File > Project Structure > Project > SDK**.

## Execution

Để chạy toàn bộ các kịch bản kiểm thử (test suite), hãy mở Terminal/Command Prompt, di chuyển vào thư mục gốc của dự án (`automation-rest-assured`) và thực thi lệnh sau:

```bash
mvn clean test -DsuiteXmlFile=testng.xml
```

Lệnh này sẽ:

- Dọn dẹp các bản build cũ (`clean`).
- Biên dịch mã nguồn và chạy các test cases (`test`) dựa trên file cấu hình `testng.xml`.

Sau khi test chạy xong, để xem báo cáo kết quả trên Allure, hãy dùng lệnh:

```bash
allure serve target/allure-results
```

*(Lưu ý: Đảm bảo đường dẫn tới mục allure-results chính xác với cấu hình hệ thống của bạn)*
