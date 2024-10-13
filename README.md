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
- Basic auth di postman/insomnia dengan email dan password yang sudah berhasil didaftarkan

#### LIST API:
<br />
<img src="https://github.com/alexistdev/mykurir/blob/main/IMAGES/gambar1.png?raw=true" width="1200px">
<br />
