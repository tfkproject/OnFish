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
                    <h1 class="page-header">Data Dagangan</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            Tabel Data Dagangan
                        </div>
                        <!-- /.panel-heading -->
                        <div class="panel-body">
						
						<!-- /.table-responsive -->
							<!--<div class="well">
                                <a class="btn btn-primary btn-lg btn-block" href="dagangan_add.php"><i class="fa fa-plus"></i>&nbsp;Dagangan</a>
                            </div>-->
							
                            <table width="100%" class="table table-striped table-bordered table-hover" id="dataTables-example">
                                <thead>
                                    <tr>
                                        <th>Nama Ikan</th>
                                        <th>Nama Penjual</th>
                                        <th>Foto</th>
                                        <th>Harga Per Kg</th>
                                        <!--<th>Aksi</th>-->
                                    </tr>
                                </thead>
                                <tbody>
								<?php
									$query = "SELECT * FROM dagangan inner join penjual on dagangan.id_penjual = penjual.id_penjual inner join jenis_ikan on dagangan.id_jenis_ikan = jenis_ikan.id_jenis_ikan";
									$eksekusi = mysqli_query($koneksi, $query);
									while($row = mysqli_fetch_array($eksekusi)){
										$id = $row['id_dagangan'];
										$parameter = '?id_dagangan='.$id;
								?>
                                    <tr class="gradeU">
										<td><?php echo $row['nama_ikan'];?></td>
										<td><?php echo $row['nama_penjual'];?></td>
										<td><img src="<?php echo $row['foto_dagangan'];?>" width="80px"/></td>
										<td><?php echo $row['harga_per_kg'];?></td>
										<!--<td class="center">
											<a href="dagangan_edit.php<?php echo $parameter;?>">
												<button type="button" class="btn btn-success btn-xs"><i class="fa fa-pencil fa-fw"></i></button>
											</a>
											<a href="dagangan_hapus.php<?php echo $parameter;?>" onClick="return confirm('Yakin ingin menghapus data?');">
												<button type="button" class="btn btn-danger btn-xs"><i class="fa fa-trash fa-fw"></i></button>
											</a>
										</td>-->
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