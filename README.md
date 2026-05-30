# Nida Butik Fashion Backend

Nida Butik, Spring Boot ile geliştirilmiş bir butik satış uygulamasıdır. Proje; müşteri, ürün, marka, kategori, tedarikçi, sipariş, sipariş kalemi ve ödeme süreçlerini ilişkisel veri tabanı mantığıyla yönetir. Backend tarafında REST API, Spring Security RBAC, DTO/Valid doğrulama, global hata yönetimi, transaction ve rollback kullanımları yer alır. Frontend tarafında ise görsel odaklı, Shopify benzeri sade bir butik vitrini bulunur.

## Proje Kapsamı

Bu proje orta düzey programlama final projesi için hazırlanmıştır. Ana amaç, gerçek bir butik satış sisteminde bulunabilecek temel veri tabanı tablolarını PostgreSQL üzerinde 3NF normalizasyon yaklaşımına uygun şekilde modellemek ve bu modeli katmanlı Spring Boot mimarisiyle yönetmektir.

Projede ürünler doğrudan müşteri siparişlerine bağlanmaz. Bunun yerine sipariş ve sipariş kalemi tabloları kullanılır. Ürün bilgisi marka, kategori ve tedarikçi tablolarından ayrıldığı için tekrar eden veriler azaltılır ve ilişkiler daha yönetilebilir hale gelir.

## Teknolojiler

- Java 17 veya üzeri
- Spring Boot 4.0.6
- Spring Web MVC
- Spring Data JPA
- Spring Security
- Jakarta Validation
- PostgreSQL
- Docker Compose
- PostgreSQL JDBC 42.7.11
- H2 Database, test ortamı için
- Maven Wrapper

## Mimari Katmanlar

Proje aşağıdaki paket yapısına sahiptir:

- `entity`: Veri tabanı tablolarını temsil eden JPA entity sınıfları.
- `repository`: CRUD işlemleri ve Derived Query Methods sorguları.
- `service`: İş kuralları, transaction yönetimi ve veri işleme mantığı.
- `controller`: Dışarıdan gelen HTTP isteklerini karşılayan REST endpoint'leri.
- `dto`: Request ve response veri transfer nesneleri.
- `exception`: Global hata yönetimi ve özel hata sınıfları.
- `config`: Güvenlik ve başlangıç verisi yapılandırmaları.

## Veri Tabanı Tasarımı

PostgreSQL Docker container üzerinden çalışır. Ana tablolar:

- `customers`: Müşteri bilgileri.
- `products`: Ürün bilgileri.
- `brands`: Marka bilgileri.
- `categories`: Kategori bilgileri.
- `suppliers`: Tedarikçi bilgileri.
- `customer_orders`: Sipariş ana kayıtları.
- `order_items`: Sipariş içindeki ürün kalemleri.
- `payments`: Ödeme kayıtları.

Bu yapı 3NF yaklaşımına uygun olacak şekilde ayrılmıştır. Örneğin marka adı veya kategori adı ürün tablosunda tekrar tekrar tutulmak yerine ayrı tablolarda saklanır. Siparişlerde birden fazla ürün olabileceği için `order_items` ara tablosu kullanılır.

## Kurulum ve Çalıştırma

Docker Desktop açıkken PostgreSQL container'ını başlatın:

```powershell
docker compose up -d
```

Uygulamayı çalıştırın:

```powershell
.\mvnw.cmd spring-boot:run
```

Tarayıcıdan açın:

```text
http://localhost:8080
```

8080 portu doluysa eski çalışan uygulamayı kapatın veya farklı port kullanın:

```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.arguments=--server.port=8081"
```

Bu durumda tarayıcı adresi:

```text
http://localhost:8081
```

Testleri çalıştırmak için:

```powershell
.\mvnw.cmd test
```

## Varsayılan Kullanıcılar

- USER: `user` / `1234`
- ADMIN: `admin` / `admin123`

RBAC kuralları:

- `GET /api/**`: USER veya ADMIN
- `POST /api/**`: ADMIN
- `PUT /api/**`: ADMIN
- `DELETE /api/**`: ADMIN

Statik butik vitrini herkes tarafından görüntülenebilir. API üzerindeki yazma işlemleri yalnızca ADMIN rolüyle yapılabilir.

## Derived Query Methods Örnekleri

Repository katmanında Spring Data JPA Derived Query Methods kullanılmıştır. Örnekler:

- `findByPriceBetween`
- `findByModelContainingIgnoreCase`
- `findByBrand_NameIgnoreCase`
- `findByCategory_NameIgnoreCase`
- `findTop8ByOrderByPriceDesc`
- `findByGender`
- `findTop8ByGenderOrderByCreatedAtDesc`
- `existsByEmailIgnoreCase`
- `existsByTransactionCodeIgnoreCase`

Ayrıca en çok ürün satın alan müşterileri bulmak için repository katmanında özel JPQL sorgusu da kullanılmıştır.

## Temel API Örnekleri

Ürünleri fiyat, model ve marka ile filtreleme:

```http
GET /api/products/filter?minPrice=1000&maxPrice=3000&model=Elbise&brand=Zara%20Studio
Authorization: Basic user:1234
```

En çok ürün satın alan ilk 8 kadın müşteriyi getirme:

```http
GET /api/customers/top-buyers?gender=FEMALE
Authorization: Basic user:1234
```

Sipariş oluşturma:

```http
POST /api/orders
Authorization: Basic admin:admin123
Content-Type: application/json
```

```json
{
  "customerId": 1,
  "items": [
    {
      "productId": 1,
      "quantity": 2
    }
  ]
}
```

Ödeme alma:

```http
POST /api/payments
Authorization: Basic admin:admin123
Content-Type: application/json
```

```json
{
  "orderId": 1,
  "method": "CREDIT_CARD",
  "transactionCode": "TXN-2026-0001"
}
```

Rollback demosu:

```http
POST /api/payments/rollback-demo
Authorization: Basic admin:admin123
Content-Type: application/json
```

Bu endpoint ödeme kaydı oluşturma akışını başlatır, ardından bilinçli hata fırlatır. Böylece transaction rollback davranışı gösterilebilir.

## DTO ve Valid Kullanımı

Dışarıdan gelen veriler doğrudan entity sınıflarına bağlanmaz. Bunun yerine request DTO sınıfları kullanılır. `@Valid`, `@NotBlank`, `@NotNull`, `@Positive`, `@Size`, `@Email` gibi doğrulama anotasyonlarıyla hatalı girişler controller seviyesinde yakalanır.

Validasyon hataları `exception` paketi içindeki global hata yönetimi ile standart JSON cevabına dönüştürülür.

## Transaction ve Rollback

Ödeme işlemleri `PaymentService` içinde transaction yönetimiyle çalışır. Aynı sipariş için ikinci ödeme yapılması veya aynı transaction kodunun tekrar kullanılması engellenir. Rollback demo endpoint'i, ödeme işleminden sonra hata fırlatarak veritabanı işleminin geri alınmasını gösterir.

## Güvenlik

Projede aşağıdaki güvenlik önlemleri uygulanmıştır:

- SQL injection riskine karşı Spring Data JPA repository yapısı ve parametreli sorgular kullanılır.
- DTO ve validation ile dış girdiler sınırlandırılır.
- XSS riskini azaltmak için frontend tarafında kullanıcıya yazdırılan veriler `textContent` ile işlenir.
- Content Security Policy header'ı tanımlıdır.
- CSRF, stateless Basic Auth API yapısı kullanıldığı için devre dışıdır.
- API yazma işlemleri ADMIN rolüyle sınırlandırılmıştır.
- Frame içine gömülme engellenir.
- Hata cevaplarında hassas sistem detayları döndürülmez.
- Şifreler BCrypt ile encode edilir.
- Spring Boot 4.0.6 ve PostgreSQL JDBC 42.7.11 sürümleri kullanılır.

## Sunum İçin Kontrol Listesi

1. Docker Desktop açık olsun.
2. Terminalde `docker compose up -d` komutunu çalıştırın.
3. IntelliJ IDEA veya terminal üzerinden projeyi başlatın.
4. Terminalde `Started NidaButikApplication` satırını görün.
5. Tarayıcıda `http://localhost:8080` adresini açın.
6. Postman'de GET istekleri için `user / 1234` kullanın.
7. POST, PUT ve DELETE istekleri için `admin / admin123` kullanın.
8. Ürün filtreleme, müşteri top-buyers, sipariş oluşturma ve ödeme endpoint'lerini gösterin.

## Notlar

Docker Desktop çalışıyorken `docker compose up -d` komutu PostgreSQL'i `localhost:5432` üzerinde başlatır. İlk açılışta demo kategori, marka, tedarikçi, müşteri ve ürün verileri otomatik eklenir.

Sunum sırasında kodlarda yorum satırı yerine sınıf ve paket yapısı üzerinden açıklama yapılması hedeflenmiştir.
