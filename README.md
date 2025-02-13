# 🐦 Twitter Clone API Project

## 📋 Proje Açıklaması
Bu proje, Twitter benzeri bir sosyal medya platformunun backend API'sini içeren bir Spring Boot uygulamasıdır. Basic Authentication kullanılarak güvenlik sağlanmış ve kullanıcıların yetkileri dahilinde işlem yapabilecekleri bir sistem tasarlanmıştır.

## 🛠 Kullanılan Teknolojiler
- **Spring Boot** 3.x ☕
- **Spring Security** 🔒
- **Spring Data JPA** 📊
- **PostgreSQL** 🐘
- **Swagger/OpenAPI** 📚
- **JUnit & Mockito** 🧪
- **Maven** 🏗

## ⚙️ Özellikler

### 👤 Kullanıcı İşlemleri
- **Kayıt (Register)**
  - Kullanıcı adı ve şifre ile kayıt
  - Benzersiz kullanıcı adı kontrolü
  - Şifre güvenlik validasyonları

- **Giriş (Login)**
  - Basic Authentication ile güvenli giriş
  - Oturum yönetimi

### 📝 Tweet İşlemleri
- **Tweet Oluşturma**
  - Sadece giriş yapmış kullanıcılar tweet atabilir
  - Tweet içeriği için karakter limiti kontrolü
  - Tweet sahibinin bilgilerinin otomatik eklenmesi

- **Tweet Silme**
  - Sadece tweet sahibi kendi tweetini silebilir
  - Admin kullanıcılar tüm tweetleri silebilir

- **Tweet Güncelleme**
  - Sadece tweet sahibi kendi tweetini güncelleyebilir
  - Güncelleme zamanı kaydedilir

- **Tweet Görüntüleme**
  - Tüm kullanıcılar (giriş yapmadan da) tweetleri görüntüleyebilir
  - Kullanıcıya göre tweet filtreleme
  - Tarih sıralaması ile listeleme

### 💬 Etkileşim Özellikleri
- **Yorum (Comment) Sistemi**
  - Giriş yapmış kullanıcılar yorum yapabilir
  - Yorum sahibi kendi yorumunu düzenleyebilir/silebilir
  - Yorum listeleme ve filtreleme
  - Tweet bazlı yorum görüntüleme

- **Beğeni (Like) Sistemi**
  - Giriş yapmış kullanıcılar tweet beğenebilir
  - Kullanıcı aynı tweeti birden fazla beğenemez
  - Beğeni geri alma özelliği
  - Tweet'in toplam beğeni sayısını görüntüleme

- **Retweet Sistemi**
  - Giriş yapmış kullanıcılar retweet yapabilir
  - Bir tweet'i sadece bir kez retweet edebilme
  - Retweet geri alma özelliği
  - Retweet sayısı görüntüleme

### 🔍 Listeleme ve Filtreleme
- Tweet'leri kullanıcıya göre filtreleme
- Kullanıcının beğendiği tweetleri listeleme
- Kullanıcının yaptığı retweetleri görüntüleme
- Kullanıcının yorumlarını listeleme
- Tarih bazlı sıralama ve filtreleme

### 🔒 Güvenlik Özellikleri
- Basic Authentication ile güvenli erişim
- Yetkilendirme kontrolleri:
  - Sadece kendi tweetlerini silebilme/düzenleyebilme
  - Sadece kendi yorumlarını silebilme/düzenleyebilme
  - Sadece kendi beğenilerini geri alabilme
  - Sadece kendi retweetlerini geri alabilme

