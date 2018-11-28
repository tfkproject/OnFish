<?php
session_start();
if (empty($_SESSION)) {
	header("location:index.php"); // jika belum login, maka dikembalikan ke file form_login.php
}
else{
	include("koneksi.php");
?>
<!DOCTYPE html>
<html lang="en">

<?php
include("head.php");
?>

<body>

    <div id="wrapper">

	<?php include("nav.php"); ?>
	
	<div id="page-wrapper">
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">Data Transaksi</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            Tabel Data Transaksi
                        </div>
                        <!-- /.panel-heading -->
                        <div class="panel-body">
						
						<!-- /.table-responsive -->
                            <table width="100%" class="table table-striped table-bordered table-hover" id="dataTables-example">
                                <thead>
                                    <tr>
                                        <th>Kode Invoice</th>
                                        <th>Nama Pelanggan</th>
                                        <th>Total Bayar</th>
                                        <th>Waktu</th>
                                        <th>Jenis Pembayaran</th>
                                        <!--<th>Aksi</th>-->
                                    </tr>
                                </thead>
                                <tbody>
								<?php
									$query = "SELECT * FROM transaksi inner join pelanggan on transaksi.id_pelanggan = pelanggan.id_pelanggan where transaksi.id_pelanggan";
									$eksekusi = mysqli_query($koneksi, $query);
									while($row = mysqli_fetch_array($eksekusi)){
										$id = $row['id_transaksi'];
										$parameter = '?id_transaksi='.$id;
								?>
                                    <tr class="gradeU">
                                        <td>INV/<?php echo $row['id_transaksi'];?></td>
                                        <td><?php echo $row['nama_pelanggan'];?></td>
                                        <td>Rp. <?php echo $row['total_bayar'];?></td>
                                        <td><?php echo $row['waktu'];?></td>
                                        <td><?php $jenis = $row['jenis'];
                                        if($jenis == "A"){
                                            echo "Diantar";
                                        }
                                        if($jenis == "J"){
                                            echo "Dijemput";
                                        }?></td>
                                        <!--<td class="center">
                                            <?php
                                                $status =  $row['status'];
                                                if($status == "N"){
                                                    ?>
                                                    <font color="red">Belum Bayar</font>
                                                    <?php
                                                }
                                                if($status == "W"){
                                                    ?>
                                                    <font color="magenta">Menunggu Konfirmasi</font>
                                                    <?php
                                                }
                                                if($status == "Y"){
                                                     ?>
                                                    <font color="blue">Sudah Bayar</font>
                                                    <?php
                                                }
                                                if($status == "S"){
                                                    ?>
                                                    <font color="gray">Paket sedang dalam pengiriman</font>
                                                    <?php
                                                }
                                                if($status == "X"){
                                                    ?>
                                                    <font color="red">Paket belum diterima</font>
                                                    <?php
                                                }
                                                if($status == "D"){
                                                    ?>
                                                    <font color="green">Paket sudah diterima</font>
                                                    <?php
                                                }
                                            ?>
                                        </td>
                                        <td class="center">
                                            <?php
                                                if($status == "N"){
                                                    ?>
                                                    <button type="button" class="btn btn-warning btn-xs" disabled>Konfirmasi</button>
                                                    <?php
                                                }
                                                if($status == "W"){
                                                    ?>
                                                    <a target="_blank" href="<?php echo $row['file_bukti'];?>">
                                                        <button type="button" class="btn btn-success btn-xs">Bukti Pembayaran</button>
                                                    </a>
                                                    <a href="transaksi_konfirmasi.php<?php echo $parameter;?>">
                                                        <button type="button" class="btn btn-warning btn-xs">Konfirmasi</button>
                                                    </a>-->
                                                    <?php
                                                }
                                                if($status == "Y"){
                                                     ?>
                                                     <!--
                                                     <a href="transaksi_kirim_paket.php<?php echo $parameter;?>">
                                                        <button type="button" class="btn btn-primary btn-xs">Kirim kan paket</button>
                                                    </a>
                                                    -->
                                                    <!--<a target="_blank" href="<?php echo $row['file_bukti'];?>">
                                                        <button type="button" class="btn btn-primary btn-xs">Lihat Bukti Pembayaran</button>
                                                    </a>-->
                                                    <?php
                                                }
                                                if($status == "S"){
                                                    ?>
                                                    <!--<button type="button" class="btn btn-primary btn-xs" disabled>Kirim kan paket</button>
                                                    <?php
                                                }
                                                if($status == "X"){
                                                    ?>
                                                    <a href="transaksi_kirim_paket.php<?php echo $parameter;?>">
                                                        <button type="button" class="btn btn-primary btn-xs">Kirim ulang paket</button>
                                                    </a>
                                                    <?php
                                                }
                                                if($status == "D"){
                                                    ?>
                                                    <button type="button" class="btn btn-primary btn-xs" disabled>Kirim kan paket</button>
                                                    <?php
                                                }
                                            ?>
										<!--</td>-->
                                    </tr>
                                <?php
									}
								?>
                                    
                                </tbody>
                            </table>
                            
                        </div>
                        <!-- /.panel-body -->
                    </div>
                    <!-- /.panel -->
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
            
            
        </div>
        <!-- /#page-wrapper -->

    </div>
    <!-- /#wrapper -->

    <?php
	include("script.php");
	?>

</body>

</html>
<?php
}
?>