# Nida Butik

Nida Butik, takı ve kadın giyim odaklı bir butik satış sitesi için hazırlanmış Spring Boot tabanlı backend projesidir. Projede PostgreSQL, JPA, DTO, validation, Spring Security ile RBAC, transaction yönetimi ve Shopify tarzı bir statik vitrin yer alır.

## Kullanılan Teknolojiler

- Java 17
- Spring Boot 4.0.6
- Spring Web MVC
- Spring Data JPA
- Spring Security
- Jakarta Validation
- PostgreSQL
- Docker Compose
- Maven Wrapper
- Testler için H2

## Mimari Yapı

- `entity`: veritabanı tablolarını temsil eden modeller
- `repository`: CRUD işlemleri ve derived query metotları
- `service`: iş kuralları ve transaction yönetimi
- `controller`: HTTP endpoint’leri
- `dto`: request ve response nesneleri
- `exception`: merkezi hata yönetimi
- `config`: güvenlik ve başlangıç verileri

## Veritabanı Tasarımı

Şema 3NF kurallarına uygun olacak şekilde modellenmiştir ve şu tabloları içerir:

- `customers`
- `products`
- `brands`
- `categories`
- `suppliers`
- `genders`
- `order_statuses`
- `payment_methods`
- `payment_statuses`
- `orders`
- `order_items`
- `payments`

`products` tablosu marka, kategori ve tedarikçi tablolarına bağlıdır. `customers` tablosu cinsiyet bilgisini ayrı `genders` tablosu üzerinden tutar. `orders` sipariş durumunu `order_statuses` tablosu ile, `payments` ise ödeme yöntemi ve ödeme durumunu ayrı lookup tabloları ile yönetir. Bu yapı tekrar eden metinleri azaltır ve 3NF yaklaşımını güçlendirir.

## Tasarım Yönergesi

Ön yüz tasarımında [`shopify.com-DESIGN.md`](./shopify.com-DESIGN.md) dosyasındaki yönergeler esas alınmıştır.

## Karşılanan Gereksinimler

- `entity`, `repository`, `service`, `controller` paketleri mevcut.
- Request verileri için DTO ve validation kullanıldı.
- Merkezi exception yönetimi uygulandı.
- Repository katmanında derived query metotları yazıldı.
- Spring Security ile `USER` ve `ADMIN` rolleri tanımlandı.
- Ödeme akışı transaction ve rollback ile yönetildi.
- Statik vitrin görselleri `src/main/resources/static/images` altında yer alıyor.

## Güvenlik

- SQL injection riski azaltmak için JPA repository yapısı ve parametreli sorgular kullanıldı.
- XSS riskini azaltmak için statik vitrinde kullanıcıya basılan veriler DOM üzerinde güvenli şekilde işlendi.
- CSRF, stateless HTTP Basic API tasarımı ve kapalı CSRF filtresi ile devre dışı bırakıldı.
- `USER` rolü yalnızca `GET` isteklerini kullanabilir, `ADMIN` rolü yazma işlemlerini yönetir.
- Hata cevaplarında hassas sistem ayrıntıları döndürülmez.
- Spring Boot 4.0.6 ve pgJDBC 42.7.11 sürümleri için mevcut güvenlik bültenleri dikkate alındı.
- Uygulama yanıt başlıklarında CSP, frame options, referrer policy ve permissions policy tanımlıdır.

## Docker ile Çalıştırma

PostgreSQL container’ını başlat:

```powershell
docker compose up -d
```

Uygulamayı çalıştır:

```powershell
.\mvnw.cmd spring-boot:run
```

Tarayıcıdan aç:

```text
http://localhost:8080
```

Testleri çalıştır:

```powershell
.\mvnw.cmd test
```

## Varsayılan Kullanıcılar

- `user` / `1234`
- `admin` / `admin123`

Yetkilendirme kuralları:

- `GET /api/**`: `USER` veya `ADMIN`
- `POST /api/**`: `ADMIN`
- `PUT /api/**`: `ADMIN`
- `DELETE /api/**`: `ADMIN`

## Faydalı API’ler

- `GET /api/products`
- `GET /api/products/filter?minPrice=1000&maxPrice=3000&model=Elbise&brand=Zara%20Studio`
- `GET /api/customers/top-buyers?gender=FEMALE`
- `POST /api/orders`
- `POST /api/payments`
- `POST /api/payments/rollback-demo`

## Postman

Postman koleksiyonu ve environment dosyası `postman` klasöründe yer alır:

- `postman/Nida-Butik.postman_collection.json`
- `postman/Nida-Butik.postman_environment.json`

Koleksiyon içinde varsayılan `user` ve `admin` hesapları için Basic Auth bilgileri ve `baseUrl` tanımlıdır.

## Demo Veri

Uygulama açılırken aşağıdaki demo veriler eklenir:

- kadın ve erkek için birden fazla müşteri
- kategori, marka ve tedarikçi ilişkileri tanımlı ürünler
- ödenmiş ve açık durumdaki örnek siparişler
- tamamlanmış siparişlere ait ödeme kayıtları

Bu sayede proje IntelliJ IDEA, tarayıcı ve Postman üzerinde daha rahat gösterilebilir.
