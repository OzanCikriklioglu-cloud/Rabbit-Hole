
rabbit-hole
│
├── SecurityApplication.java          # Ana dosyan (Burada duruyor)
│
├── config/                           
│   ├── CaptchaFilter.java    
│   └── SecurityConfig.java
│
├── controller/                       
│   ├── AdminController.java           # Admin paneli (/admin/**)
│   ├── AuthController.java            # Login/Register yönetimi
│   ├── DashboardController.java       # Kullanıcının özel kasa sayfası
│   └── GlobalErrorController.java
│
├── dto/
│     └── NoteDTO.java
│
├── entity/                           
│   ├── Note.java            # id, content, user_id (Kasadaki veriler)       
│   └── User.java            # id, username, password, role
│
├── repository/                      
│   ├── NoteRepository.java           # JpaRepository       
│   └── UserRepository.java           # JpaRepository (SQL Injection korumalı)  
│
├──security/                         
│    └── CustomUserDetailsService.java  # Veritabanındaki kullanıcıyı Security'ye bağlar
│
└── service/                        
├── CaptchaService.java           #   CaptchaService.java
├── NoteService.java               # Not ekleme ve yetki kontrolü
└── UserService.java               # Şifreleme ve kullanıcı işlemleri 
