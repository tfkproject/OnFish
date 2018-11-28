<?php
include "../koneksi.php";

?>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
 
<title>OnFish Invoice</title>
<link rel="stylesheet" href="css/bootstrap.min.css"/>
</head>
<body>
<div class="container-fluid">
	<hr>
	<center><h4> Invoice Detail </h4></center>
	<hr>
	<div class="row">
    	<div class="col-lg-8">
        	<button style="margin-left:15px" onclick="myFunction()">Print Invoice</button>

			<script>
			function myFunction() {
				window.print();
			}
			</script>

        </div> <!-- end col-lg-8 -->
        <?php 
		    $q = mysqli_query($koneksi, "SELECT * FROM transaksi inner join pelanggan on transaksi.id_pelanggan = pelanggan.id_pelanggan where transaksi.id_transaksi='".$_GET['id_transaksi']."'");
			$data_t = mysqli_fetch_array($q);
		?>
        <div align="right" class="col-lg-4">
		<div style="margin-right:15px"><b>INV / <?php echo $data_t['id_transaksi']; ?></b><br />
    	DATE TIME : <b><?php echo $data_t['waktu']; ?></b><br />
		</div><!--<span class="glyphicon glyphicon-log-out"></span> &nbsp; <a href="logout.php">Klik di sini untuk Logout</a>
			-->
        </div><!-- end col lg-4 -->
    </div><!-- end row -->
    
    <div class="row">
    	<div class="col-lg-12">
        <div class="col-lg-2">
        </div>
        <div class="col-lg-8">
		<table id="data_member" class="table table-bordered">
				<tbody style="font-family:Verdana, Geneva, sans-serif; font-size:12px">
                    <br>
                    <tr align='left'>
                        <td>ID Pelanggan</td>
                        <td colspan="4">: <?php echo  $data_t['id_pelanggan']; ?></td>
                    </tr>
					<tr align='left'>
                        <td>Nama</td>
                        <td colspan="4">: <?php echo  $data_t['nama_pelanggan']; ?></td>
                    </tr>
					<tr align='left'>
                        <td>E-Mail</td>
                        <td colspan="4">: <?php echo  $data_t['email']; ?></td>
                    </tr>
				</tbody>
			</table>
			<br/>
			<label>Daftar paket yang dipesan</label>
			<table id="data_barang" class="table table-bordered">
				
				<thead>
                    <tr>
                        <td align="center" width="30%">Nama Pesanan</td>
                        <td align="center" width="30%">Nama Nelayan</td>
                        <td align="center" >Jumlah Per Kg</td>
                        <td align="center" >Jumlah Kg</td>
                        <td align="center" >Harga Total</td>
                    </tr>
					</thead>
				<tbody style="font-family:Verdana, Geneva, sans-serif; font-size:12px">
					<?php
					    $id_keranjang = $data_t['id_keranjang'];
					    
						$qr = mysqli_query($koneksi, "SELECT * FROM record_item inner join dagangan on record_item.id_dagangan = dagangan.id_dagangan inner join pelanggan on record_item.id_pelanggan = pelanggan.id_pelanggan inner join kategori_ikan on dagangan.id_kategori_ikan = kategori_ikan.id_kategori_ikan inner join petani on dagangan.id_petani = petani.id_petani where record_item.id_keranjang = '$id_keranjang'");
						while($row = mysqli_fetch_array($qr)){
					?>
					<tr align='right'>
                        <td><?php echo $row['nama_ikan'];?></td>
                        <td><?php echo $row['nama_petani'];?></td>
                        <td>Rp. <?php echo $row['harga_per_kg'];?></td>
						<td><?php echo $row['jum_kg'];?>&nbsp;Kg</td>
						<td>Rp. <?php echo $row['harga_total'];?></td>
                    </tr>
					<?php
					}
					?>
					<tr align='left'>
                        <td><b>Total Bayar</b></td>
						<td align='right' colspan="4"><b>Rp. <?php echo  $data_t['total_bayar']; ?></b></td>
                    </tr>
					<tr align='left'>
                        <td><b>Jenis Pembayaran</b></td>
						<td colspan="4">
						    <?php
						    $status = $data_t['jenis'];
						    if($status == 'A'){
						        echo "Diantar";
						    }
						    if($status == 'J'){
						        echo "Dijemput";
						    }
						    ?>
						</td>
                    </tr>
                </tbody>
        </table>  
            
        </div><!-- end tab pane -->	
        </div>
            
			
        </div><!-- end col lg 12 -->
		<hr>
		<i>End_of_invoice_<?php echo $_GET['id_transaksi']?></i>
    </div> <!-- end row -->
</div>

 
    <!-- Script js -->
    <script src="js/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <!-- End Script -->
</body>
</html>