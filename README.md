## MyKurir v.1.0 Apps
#### Aplikasi untuk mencatat basic pengiriman barang , dilengkapi dengan tracking number .

### Frontend
===== Not Started ======

### Backend
#### Fitur
- Java Springboot 3
- MySQL

#### Installasi
- Buat database kosong dengan nama mykurir
- Lakukan pengaturan di : src/main/resources/application.properties
- Registrasi di URL : [POST] http://localhost:8082/v1/api/auth/register
   <pre>
     
     {
    	"fullName": "Alexsander Hendra Wijaya",
    	"email" : "alexistdev@gmail.com",
    	"password": "1234"
     }
     
   </pre>
- Cek Database dan ubah role dari akun yang dibuat menjadi ADMIN
- Setup Auth di Imnsomnia atau Postman menjadi Basic Auth, dan masukkan email dan password dari akun yang didaftarkan sebelumnya
  
#### LIST API:
<br />
<img src="https://github.com/alexistdev/mykurir/blob/main/IMAGES/gambar1.png?raw=true" width="1200px">
<br />
