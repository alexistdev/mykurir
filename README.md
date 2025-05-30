## MyKurir v.1.0 Apps
#### Aplikasi untuk mencatat basic pengiriman barang , dilengkapi dengan tracking number .

### Frontend
#### Fitur
- Angular 15
- Template: Porto
#### Installasi
- Jalankan di terminal: npm i
- Jalankan perintah: ng serve
<hr/>

### Backend

#### Fitur
- Java Springboot 3
- JDK 21
- MySQL

#### Installasi
- Buat database kosong dengan nama mykurir
- Lakukan pengaturan di : src/main/resources/application.properties
  <pre>
      spring.datasource.username=[username-db]
      spring.datasource.password=[password-db]
  </pre>

- Jalankan dengan perintah mvn spring-boot:run
- Registrasi di URL : [POST] http://localhost:8082/v1/api/auth/register
   <pre>
     
     {
    	"fullName": "Alexsander Hendra Wijaya",
    	"email" : "alexistdev@gmail.com",
    	"password": "1234"
     }
     
   </pre>
- Cek Database dan ubah role dari akun yang dibuat menjadi ADMIN
  <img src="https://github.com/alexistdev/mykurir/blob/main/IMAGES/gambar4.png?raw=true">
- Setup Auth di Imnsomnia atau Postman menjadi Basic Auth, dan masukkan email dan password dari akun yang didaftarkan sebelumnya
  <img src="https://github.com/alexistdev/mykurir/blob/main/IMAGES/gambar5.png?raw=true">

<hr />
#### LIST API:
<br />
<img src="https://github.com/alexistdev/mykurir/blob/main/IMAGES/gambar1.png?raw=true" width="1200px">
<br />
<img src="https://github.com/alexistdev/mykurir/blob/main/IMAGES/gambar3.png?raw=true" />
