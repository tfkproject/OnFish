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
                    <h1 class="page-header">Selamat datang, <?php echo $_SESSION['nama'];?>!</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
            <div class="row">
                <!--
                <div class="col-lg-3 col-md-6">
                    <div class="panel panel-primary">
                        <div class="panel-heading">
                            <div class="row">
                                <div class="col-xs-3">
                                    <i class="fa fa-users fa-5x"></i>
                                </div>
                                <div class="col-xs-9 text-right">
								<?php
								  $sql = "SELECT COUNT(*) as total_pelanggan FROM `pelanggan`";
								  $eksekusi = mysqli_query($koneksi, $sql);
								  $row = mysqli_fetch_array($eksekusi);
								  ?>
                                    <div class="huge"><?php echo $row['total_pelanggan']?></div>
                                    <div>Pelanggan Terdaftar!</div>
                                </div>
                            </div>
                        </div>
                        <a href="pelanggan.php">
                            <div class="panel-footer">
                                <span class="pull-left">Lihat Rincian</span>
                                <span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
                                <div class="clearfix"></div>
                            </div>
                        </a>
                    </div>
                </div>
                -->
                <div class="col-lg-3 col-md-6">
                    <div class="panel panel-primary">
                        <div class="panel-heading">
                            <div class="row">
                                <div class="col-xs-3">
                                    <i class="fa fa-users fa-5x"></i>
                                </div>
                                <div class="col-xs-9 text-right">
								<?php
								  $sql = "SELECT COUNT(*) as total_pelanggan FROM `pelanggan`";
								  $eksekusi = mysqli_query($koneksi, $sql);
								  $row = mysqli_fetch_array($eksekusi);
								  ?>
                                    <div class="huge"><?php echo $row['total_pelanggan']?></div>
                                    <div>Pelanggan!</div>
                                </div>
                            </div>
                        </div>
                        <a href="pelanggan.php">
                            <div class="panel-footer">
                                <span class="pull-left">Lihat Rincian</span>
                                <span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
                                <div class="clearfix"></div>
                            </div>
                        </a>
                    </div>
                </div>
                <div class="col-lg-3 col-md-6">
                    <div class="panel panel-green">
                        <div class="panel-heading">
                            <div class="row">
                                <div class="col-xs-3">
                                    <i class="fa fa-user fa-5x"></i>
                                </div>
                                <div class="col-xs-9 text-right">
								<?php
								  $sql = "SELECT COUNT(*) as total_petani FROM `petani`";
								  $eksekusi = mysqli_query($koneksi, $sql);
								  $row = mysqli_fetch_array($eksekusi);
								  ?>
                                    <div class="huge"><?php echo $row['total_petani']?></div>
                                    <div>Nelayan Terdaftar!</div>
                                </div>
                            </div>
                        </div>
                        <a href="petani.php">
                            <div class="panel-footer">
                                <span class="pull-left">Lihat Rincian</span>
                                <span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
                                <div class="clearfix"></div>
                            </div>
                        </a>
                    </div>
                </div>
                <div class="col-lg-3 col-md-6">
                    <div class="panel panel-red">
                        <div class="panel-heading">
                            <div class="row">
                                <div class="col-xs-3">
                                    <i class="fa fa-tag fa-5x"></i>
                                </div>
                                <div class="col-xs-9 text-right">
								<?php
								  $sql = "SELECT COUNT(*) as total_kategori FROM `kategori_ikan`";
								  $eksekusi = mysqli_query($koneksi, $sql);
								  $row = mysqli_fetch_array($eksekusi);
								  ?>
                                    <div class="huge"><?php echo $row['total_kategori']?></div>
                                    <div>Kategori!</div>
                                </div>
                            </div>
                        </div>
                        <a href="kategori.php">
                            <div class="panel-footer">
                                <span class="pull-left">Lihat Rincian</span>
                                <span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
                                <div class="clearfix"></div>
                            </div>
                        </a>
                    </div>
                </div>
                <div class="col-lg-3 col-md-6">
                    <div class="panel panel-yellow">
                        <div class="panel-heading">
                            <div class="row">
                                <div class="col-xs-3">
                                    <i class="fa fa-credit-card fa-5x"></i>
                                </div>
                                <div class="col-xs-9 text-right">
								<?php
								  $sql = "SELECT COUNT(*) as total_transaksi FROM `transaksi`";
								  $eksekusi = mysqli_query($koneksi, $sql);
								  $row = mysqli_fetch_array($eksekusi);
								  ?>
                                    <div class="huge"><?php echo $row['total_transaksi']?></div>
                                    <div>Total Transaksi!</div>
                                </div>
                            </div>
                        </div>
                        <a href="transaksi.php">
                            <div class="panel-footer">
                                <span class="pull-left">Lihat Rincian</span>
                                <span class="pull-right"><i class="fa fa-arrow-circle-right"></i></span>
                                <div class="clearfix"></div>
                            </div>
                        </a>
                    </div>
                </div>
                
            </div>
            <!-- /.row -->
            <!--<div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <i class="fa fa-bar-chart-o fa-fw"></i> Grafik keuangan tahun 2017
                            <div class="pull-right">
                                <div class="btn-group">
                                    <button type="button" class="btn btn-default btn-xs dropdown-toggle" data-toggle="dropdown">
                                        Actions
                                        <span class="caret"></span>
                                    </button>
                                    <ul class="dropdown-menu pull-right" role="menu">
                                        <li><a href="#"><i class="fa fa-refresh fa-fw"></i>&nbsp;Refresh</a>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                        <!-- /.panel-heading -->
                        <!--<div class="panel-body">
                            <div id="morris-area-chart"></div>
                        </div>
                        <!-- /.panel-body -->
                    <!--</div>
                    <!-- /.panel -->
                    
                <!--</div>
                <!-- /.col-lg-8 -->
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