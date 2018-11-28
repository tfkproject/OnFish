<?php
session_start();
//Koneksi kedatabase server anda
include('koneksi.php');

//parameter
$email	= $_POST['email'];
$pass	= $_POST['password'];

// Pengecekan kondisi email dan password
if(isset($email) && ($pass)){
   
   //function php untuk mengambil data dari database
   $sql = mysqli_query($koneksi, "select * from `admin` where email = '$email' and password = '$pass'")or die ("SQL Error: ".mysql_error());;
   $result = mysqli_num_rows($sql);
   $data = mysqli_fetch_array($sql);

   // Untuk menyimpan session login
    if ($result== 1){
		$_SESSION['nama'] = $data['nama'];
		header("location:home.php");
   }else{
	    //pesan ini akan muncul jika username dan pasword, dan akan dialihkan ke form login
		//$_SESSION['error']="Username atau Password Anda salah";
        header("location:index.php?login=salah");
 }
 }else{
 //pesan ini akan muncul jika form login kosong, dan akan dialihkan ke form login
 //$_SESSION['error']="Username atau password tidak boleh kosong"; 
 header("location:index.php?login=error");
}
?>