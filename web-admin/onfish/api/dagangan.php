<?php
// array for JSON response
$response = array();
// include db connect class
require 'connect.php';

$id_jenis_ikan = $_POST['id_jenis_ikan'];

if($result = $db->query("SELECT * FROM dagangan inner join penjual on dagangan.id_penjual = penjual.id_penjual inner join jenis_ikan on dagangan.id_jenis_ikan = jenis_ikan.id_jenis_ikan WHERE jenis_ikan.id_jenis_ikan = '$id_jenis_ikan'")){
	if($count = $result->num_rows){
		$response["daftar"] = array();
		
		while($row = $result->fetch_object()){
			$data = array();
			$data["id_dagangan"] = $row->id_dagangan;
			$data["id_penjual"] = $row->id_penjual;
			$data["nama_penjual"] = $row->nama_penjual;
			$data["id_jenis_ikan"] = $row->id_jenis_ikan;
			$data["nama_ikan"] = $row->nama_ikan;
			$data["berat_tersedia"] = $row->berat_tersedia;
			$data["harga_per_kg"] = $row->harga_per_kg;
			$data["foto"] = "http://103.111.86.246/app/onfish/img/ikan_dagangan/".$row->foto_dagangan;
			$data["deskripsi"] = $row->deskripsi;
			$data["no_hp"] = $row->no_hp;
			$data["lat"] = $row->lat;
			$data["lon"] = $row->lon;
					
			
			array_push($response["daftar"], $data);
		}
		
		$response["success"] = 1;
		
		// echoing JSON response
		echo json_encode($response);
	}
		
		$result->free();
} else {
    // no datas found
    $response["success"] = 0;
    $response["message"] = "Tidak ada data ditemukan";
    // echo no users JSON
    echo json_encode($response);
}
?>
