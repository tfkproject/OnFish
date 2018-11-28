<?php
// array for JSON response
$response = array();
// include db connect class
require 'connect.php';

$id_pelanggan = $_POST['id_pelanggan'];

if($result = $db->query("SELECT * FROM item_beli inner join keranjang on item_beli.id_keranjang = keranjang.id_keranjang inner join dagangan on item_beli.id_dagangan = dagangan.id_dagangan inner join pelanggan on item_beli.id_pelanggan = pelanggan.id_pelanggan inner join jenis_ikan on dagangan.id_jenis_ikan = jenis_ikan.id_jenis_ikan inner join penjual on dagangan.id_penjual = penjual.id_penjual where pelanggan.id_pelanggan = '$id_pelanggan'")){
	if($count = $result->num_rows){
		$response["daftar"] = array();
		
		while($row = $result->fetch_object()){
			$data = array();
			$data["id_item_beli"] = $row->id_item_beli;
			$data["id_keranjang"] = $row->id_keranjang;
			$data["id_dagangan"] = $row->id_dagangan;
			$data["id_penjual"] = $row->id_penjual;
			$data["nama_penjual"] = $row->nama_penjual;
			$data["id_jenis_ikan"] = $row->id_jenis_ikan;
			$data["nama_ikan"] = $row->nama_ikan;
			$data["berat_tersedia"] = $row->berat_tersedia;
			$data["jum_kg"] = $row->jum_kg;
			$data["harga_total"] = $row->harga_total;
			$data["foto"] = "http://103.111.86.246/app/onfish/img/ikan_dagangan/".$row->foto_dagangan;
					
			
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
