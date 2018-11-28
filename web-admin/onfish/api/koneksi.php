<?php
//defenisi setup koneksi untuk database
$servername = "localhost";
$username = "root";
$password = "209999!1";
$dbname = "app_onfish";

$koneksi = mysqli_connect($servername,$username,$password,$dbname) or die("Error " . mysqli_error($koneksi));
?>