<?php
	include "koneksi.php";
	//error_log(print_r($_POST, 1));
    $email = $_POST['email'];
    //$password = $_POST['password'];

	//select data ke database
	$q = "SELECT * FROM `penjual` WHERE `email` = '$email'";
	$hasil = mysqli_query ($koneksi, $q);
	$r = mysqli_num_rows($hasil);
    $row = mysqli_fetch_array($hasil);
    if ($r != 0 ){
		$id_penjual = $row['id_penjual'];
		$nama_penjual = $row['nama_penjual'];
		$email = $row['email'];
		$password = $row['password'];
		$nomor_hp = $row['no_hp'];
		$alamat = $row['alamat'];
		
		$json_data = '{"success":1,"message":"Selamat datang '.$nama_penjual.'","id_penjual":"'.$id_penjual.'","nama_penjual":"'.$nama_penjual.'","email":"'.$email.'","password":"'.$password.'","nomor_hp":"'.$nomor_hp.'","alamat":"'.$alamat.'"}';
        echo $json_data;
	}
	else{
		$json_data = '{"success":0,"message":"Maaf, email tidak terdaftar"}';
        echo $json_data;
	}
?>