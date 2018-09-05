# android_music_player

[Release] Nộp bài tập Android Music Player:
Release APK file.
Chi tiết:
-	Hiển thị được danh sách các bài hát có trong máy: Theo tên bài hát, theo thư mục.
-	Chạy được bài hát: next, previous, play, pause, điều chỉnh trạng thái bài nhạc đang phát bằng seekbar.
-	Chạy được bài hát dưới background dựa theo tuỳ chọn trong màn hình settings.
-	Khi bản nhạc được chạy, hiển thị điều khiển trên khung notification của thiết bị.
-	Kết nối được với server thông qua HTTP Request: Nút Test API load lyric, ảnh từ API
-	Định nghĩa được dữ liệu bằng Room.
-	Truy cập dữ liệu của Room bằng DAO.
-	Sử dụng xml để lưu trữ thông tin cài đặt.
-	Hỗ trợ xoay màn hình.

**************************************************************************
Yêu cầu bài tập:
Viết một App Android là một trình nghe nhạc với mô tả như bên dưới 
　(dự kiến là làm trong vòng 20h).

◼︎ Màn hình (Tổng số 3 màn hình):

① Màn hình hiển thị danh sách bài nhạc.

- Là màn hình chính được mở lên khi ứng dụng được mở lên.

- Màn hình có ba phần:

    +  Thanh tiêu đề có chứa tên chương trình và nút setting để dẫn tới màn hình setting.
    
    +  Danh sách bài hát là một RecycleView hiển thị danh sách các file nhạc. 
        Có 2 cách để xem các file nhạc là xem theo tên file và xem theo thư mục. 
        Khi người dùng bấm vào một bài nhạc, di chuyển đến màn hình hiển thị trình chơi nhạc.
        
    + Trình nghe nhạc thu nhỏ bao gồm ba nút. 
    
      -- Nút previous di chuyển về bài nhạc trước trong danh sách sắp xếp theo tên file. 
      
      -- Nút next di chuyển đến bài hát tiếp theo trong danh sách sắp xếp theo tên file. 
      
      -- Nút play được hiển thị khi bài nhạc đang được phát, nếu không, hiển thị nút pause. 
      
      Khi bấm vào nút play/pause, trạng thái của bài hát sẽ được thay đổi. 
      Ảnh bìa được lấy từ API. Tiêu đề bài hát chính là tên của bài hát hiện tại.

② Màn hình hiển thị trình chơi nhạc.
- Là màn hình hiện ra khi người dùng bấm vào một bài nhạc trong danh sách bài nhạc hoặc bấm vào tên bài nhạc trên thanh notification.

    -- Trong trường hợp người dùng đến từ màn hình hiển thị danh sách bài nhạc. Bài nhạc sẽ được phát. 
    
    -- Trong trường hợp đến từ thanh notification. Trạng thái phát nhạc được giữ nguyên.

- Màn hình có ba phần: 
    +  Thanh tiêu đề có chứa tên của bài hát và nút setting để dẫn tới màn hình setting.
    +  Lời bài hát là dữ liệu được lấy từ trên API. 
        Lời bài hát sẽ chỉ load một lần khi ứng dụng còn tồn tại. Nếu ứng dụng bị kill đi (Bởi người dùng hoặc OS), lời bài hát sẽ được load lại.
    +  Trình nghe nhạc bao gồm ba nút. (mô tả giống như của phần ① nói trên)

③ Màn hình setting.
- Màn hình setting bao gồm các chức năng cài đặt như sau:

+ Tuỳ chọn cho phép nghe nhạc khi ẩn ứng dụng xuống hay không.

+ Hiển thị thông tin nhà phát triển.

---------------------------------------------------------------------
Các điểm cần chú ý về dự án:
・Hiển thị giao diện tuỳ ý tưởng/ý thích.

・Hiển thị được danh sách các bài hát có trong máy.

・Chạy được bài hát. (Không cần xử lý các kết nối âm thanh có dây/không dây - tai nghe, loa...)

・Chạy được bài hát dưới background dựa theo tuỳ chọn trong màn hình settings.

・Khi bản nhạc được chạy, hiển thị điều khiển trên khung notification của thiết bị.

・Kết nối được với server thông qua HTTP Request.

・Định nghĩa được dữ liệu bằng Room.

・Truy cập dữ liệu của Room bằng DAO.

・Sử dụng xml để lưu trữ thông tin cài đặt.

・Hỗ trợ xoay màn hình.
