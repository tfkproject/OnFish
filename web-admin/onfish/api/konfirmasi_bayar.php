<?php
	include "koneksi.php";
	//error_log(print_r($_POST, 1));
    $filename = $_POST['filename']; //image file name
    $image = $_POST['image']; //image in string format
	
	$id_transaksi = $_POST['id_transaksi'];
	$status = "W";
	$link_foto = "http://genkan.ecomfish.com/img/pelanggan/".$filename.".jpg";
	
    //decode the image
    $decodedImage = base64_decode($image);
		
	//input data ke database
	$q = "UPDATE `transaksi` SET `status` = '$status', `file_bukti` = '$link_foto' WHERE `id_transaksi` = '$id_transaksi' ";
	$hasil = mysqli_query ($koneksi, $q);
    if ($hasil){
        //upload the image
        file_put_contents("../img/pelanggan/".$filename.".jpg", $decodedImage);
		
		echo '{"success":1,"message":"Data berhasil diupload"}';
	}
	else{
		echo '{"success":0,"message":"Maaf, terjadi kesalahan"}';
	}
?>