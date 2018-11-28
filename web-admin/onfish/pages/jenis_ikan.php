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
                    <h1 class="page-header">Data Jenis Ikan</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            Tabel Data Jenis Ikan
                        </div>
                        <!-- /.panel-heading -->
                        <div class="panel-body">
						
						<!-- /.table-responsive -->
							<div class="well">
                                <a class="btn btn-primary btn-lg btn-block" href="jenis_ikan_add.php"><i class="fa fa-plus"></i>&nbsp;Jenis Ikan</a>
                            </div>
							
                            <table width="100%" class="table table-striped table-bordered table-hover" id="dataTables-example">
                                <thead>
                                    <tr>
                                        <th>Nama Ikan</th>
                                        <th>Foto</th>
                                        <th>Aksi</th>
                                    </tr>
                                </thead>
                                <tbody>
								<?php
									$query = "select * from `jenis_ikan`";
									$eksekusi = mysqli_query($koneksi, $query);
									while($row = mysqli_fetch_array($eksekusi)){
										$id = $row['id_jenis_ikan'];
										$parameter = '?id_jenis_ikan='.$id;
								?>
                                    <tr class="gradeU">
										<td><?php echo $row['nama_ikan'];?></td>
										<td><img src="../img/ikan_jenis/<?php echo $row['foto'];?>" width="80px"/></td>
										<td class="center">
											<a href="jenis_ikan_edit.php<?php echo $parameter;?>">
												<button type="button" class="btn btn-success btn-xs"><i class="fa fa-pencil fa-fw"></i></button>
											</a>
											<a href="jenis_ikan_hapus.php<?php echo $parameter;?>" onClick="return confirm('Yakin ingin menghapus data?');">
												<button type="button" class="btn btn-danger btn-xs"><i class="fa fa-trash fa-fw"></i></button>
											</a>
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