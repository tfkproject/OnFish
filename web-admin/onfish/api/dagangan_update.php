<?php
include("koneksi.php");

if(isset($_POST)){
	$id_dagangan = $_POST['id_dagangan'];
	$id_penjual = $_POST['id_penjual'];
	$id_jenis_ikan= $_POST['id_jenis_ikan'];
	//$nama = $_POST['nama'];
	$jum_kg = $_POST['jum_kg'];
	$harga = $_POST['harga'];
	$desk = $_POST['desk'];
	$filename = $_POST['file_name'].".jpg";
	$image = $_POST['image'];

	//encode image 
	$decodedImage = base64_decode($image);
	
	$sql = "UPDATE `dagangan` SET 
		`id_penjual` = '$id_penjual', 
		`id_jenis_ikan` = '$id_jenis_ikan', 
		`berat_tersedia` = '$jum_kg', 
		`harga_per_kg` = '$harga', 
		`foto_dagangan` = '$filename', 
		`deskripsi` = '$desk'
		WHERE `id_dagangan` = '$id_dagangan'";

	$eksekusi = mysqli_query($koneksi, $sql);
	if ($eksekusi){
        //upload the image
        file_put_contents("../img/ikan_dagangan/".$filename, $decodedImage);
		
		echo '{"success":1,"message":"Data berhasil diperbaharui"}';
	}
	else{
		echo '{"success":0,"message":"Maaf, terjadi kesalahan"}';
	}
}
else{
	
}
?>