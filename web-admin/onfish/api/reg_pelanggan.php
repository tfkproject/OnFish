<?php
	include "koneksi.php";
	//error_log(print_r($_POST, 1));
    
	$nama = $_POST['nama'];
	$email = $_POST['email'];
	$password = $_POST['password'];
	$nomor_hp = $_POST['nomor_hp'];
	$alamat = $_POST['alamat'];
	
	//cek dulu, apakah email yang di registrasi sudah terdaftar atau belum
	$q = "select nama_pelanggan from pelanggan where email =  '$email'";
	$hasil = mysqli_query ($koneksi, $q);
	$r = mysqli_num_rows($hasil);
	if($r != 0){
	    //berarti sudah ada data di dalamnya
	    while($row = mysqli_fetch_array($hasil)){
	        $nama_pel = $row['nama_pelanggan'];
	    }
	    echo '{"success":0,"message":"Maaf, email tersebut telah terdaftar atas nama '.$nama_pel.', gunakan email lain.","nama":""}';
	}else{
	    //berarti belum terdaftar
	    //input data ke database
    	$q2 = "INSERT INTO `pelanggan` (`id_pelanggan`, `nama_pelanggan`, `email`, `password`, `nomor_hp`, `alamat`) VALUES (NULL, '$nama', '$email', '$password', '$nomor_hp', '$alamat')";
    	$hasil2 = mysqli_query ($koneksi, $q2);
    	$id_pelanggan = mysqli_insert_id($koneksi);
        if ($hasil2){
    		echo '{"success":1,"message":"", "nama":"'.$nama.'", "id_pelanggan":'.$id_pelanggan.'}';
    	}
    	else{
    		echo '{"success":0,"message":"Maaf, terjadi kesalahan","nama":""}';
    	}
	}
	
?>