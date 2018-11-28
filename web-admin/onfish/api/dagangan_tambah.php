<?php
include("koneksi.php");

if(isset($_POST)){
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
	
	$sql = "INSERT INTO `dagangan` (
		`id_dagangan`, 
		`id_penjual`, 
		`id_jenis_ikan`, 
		`berat_tersedia`, 
		`harga_per_kg`, 
		`foto_dagangan`, 
		`deskripsi`
		) VALUES (
			NULL, 
			'$id_penjual', 
			'$id_jenis_ikan', 
			'$jum_kg', 
			'$harga', 
			'$filename', 
			'$desk'
		)";
	$eksekusi = mysqli_query($koneksi, $sql);
	if ($eksekusi){
        //upload the image
        file_put_contents("../img/ikan_dagangan/".$filename, $decodedImage);
		
		echo '{"success":1,"message":"Data berhasil diupload"}';
	}
	else{
		echo '{"success":0,"message":"Maaf, terjadi kesalahan"}';
	}
}
else{
	
}
?>