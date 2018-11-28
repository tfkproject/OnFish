<?php
session_start();
if (empty($_SESSION)) {
	header("location:index.php"); // jika belum login, maka dikembalikan ke file form_login.php
}
else{
	include("koneksi.php");
	include("rsa.php");
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
                    <h1 class="page-header">Data Pelanggan</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            Tabel Data Petani
                        </div>
                        <!-- /.panel-heading -->
                        <div class="panel-body">
						
						<!-- /.table-responsive -->

							
                            <table width="100%" class="table table-striped table-bordered table-hover" id="dataTables-example">
                                <thead>
                                    <tr>
                                        <th>Nama Pelanggan</th>
                                        <th>Email</th>
                                        <th>Alamat</th>
                                        <th>Nomor HP</th>
                                        <th>Aksi</th>
                                    </tr>
                                </thead>
                                <tbody>
								<?php
									$query = "select * from `pelanggan`";
									$eksekusi = mysqli_query($koneksi, $query);
									while($row = mysqli_fetch_array($eksekusi)){
										$id = $row['id_pelanggan'];
										$parameter = '?id_pelanggan='.$id;
								?>
                                    <tr class="gradeU">
										<td><?php echo $row['nama_pelanggan'];?></td>
										<td><?php echo $row['email'];?></td>
										<td><?php echo $row['alamat'];?></td>
										<td><?php echo $row['nomor_hp'];?></td>
                                        <td>
                                            <a href="pelanggan_hapus.php?id_pelanggan=<?php echo $row['id_pelanggan'];?>" class="btn btn-xs btn-danger" onClick="return confirm('Yakin ingin menghapus data?');"><i class="fa fa-trash"></i></a>
                                        </td>
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