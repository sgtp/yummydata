<?php
require_once("libs/sparqllib.php");
error_reporting(E_ERROR | E_WARNING | E_PARSE | E_NOTICE);

$endpoint=$_GET['endpoint'];
$endpointc=str_replace("\"","",$endpoint);
#print "+++".$endpointc;

$server="http://yummydata.org:3030/yummy/query";

$db = sparql_connect($server );
if( !$db ) { print $db->errno() . ": " . $db->error(). "\n"; exit; }

$db->ns( "yummy","http://yummydata.org/lang#" );
$db->ns( "rdfs","http://www.w3.org/2000/01/rdf-schema#" );
 
$charts=array(); 
 
$sparql = "SELECT ?test WHERE { graph ?g {?test rdfs:label ?label . ?test yummy:appliesTo <http://yummydata.org/lang#all> . }} order by ?label"; 
$result = $db->query( $sparql ); 
if( !$result ) { print $db->errno() . ": " . $db->error(). "\n"; exit; }
while( $row = $result->fetch_array()){
	array_push($charts,$row['test']);
}

 
$sparql = "SELECT ?test WHERE { graph ?g {?test rdfs:label ?label . ?test yummy:appliesTo <".$endpointc."> . }} order by ?label";
$result = $db->query( $sparql ); 
if( !$result ) { print $db->errno() . ": " . $db->error(). "\n"; exit; }
while( $row = $result->fetch_array()){
	array_push($charts,$row['test']);
}
?>
<!DOCTYPE html>
<html>
  <head>
    <title>YummyData</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
	<link href="bootstrap/css/bootstrap-responsive.css" rel="stylesheet">
	<link href="bootstrap/css/bootstrap.css" rel="stylesheet">
	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.min.js"></script>
	<script type="text/javascript" src="https://www.google.com/jsapi"></script>
	<script type="text/javascript" id="sgvzlr_script" src="sgvizler-0.5/sgvizler.js"></script>
	<script type="text/javascript">
		sgvizler.option.namespace.npd = 'http://sws.ifi.uio.no/npd/';
      	sgvizler.option.namespace.npdv = 'http://sws.ifi.uio.no/vocab/npd#';
      	$(document).ready(sgvizler.go());
    </script>
  </head>
  <body>
    <?php
    	#foreach($charts as $chart){
    	#	echo $chart."<br/>";
    	#}
    ?>   
    <div class="row">
    	<div class="span12">
        	<h1>Yummy Data, details for: 
        	<?php
        	echo $endpointc
        	?>
        	</h1>
        </div>
   	</div>
   	<div class="row">
   		<div class="span2">
   			<h2>Menu</h2>
   			Resgiter your site<br/>
   			Contact info<br/>
   		</div>
   		<div class="span10">
   			<table class="table>"
   				<?php
   				$i=0;
   				foreach($charts as $chart){
   					$sparql = "SELECT ?p ?o WHERE { graph ?g {<".$chart."> ?p ?o . }}";
					$innerResult = $db->query( $sparql ); 
					if( !$innerResult ) { print $db->errno() . ": " . $db->error(). "\n"; exit; }
					$chartType="gSparkline";
					$queryViz="";
					$label="a chart";
					while( $innerRow = $innerResult->fetch_array()){
						if($innerRow['p']=="http://yummydata.org/lang#prefChart") $chartType=$innerRow['o'];
						if($innerRow['p']=="http://yummydata.org/lang#queryViz") $queryViz=str_replace('$$endpoint$$',"<".$endpointc.">",$innerRow['o']);
						if($innerRow['p']=="http://www.w3.org/2000/01/rdf-schema#label") $label=$innerRow['o'];
					}
					if($chartType=="gTimeLine") $chartType="gTimeline"; #patch!
					if($chartType=="gSparkLine") $chartType="gSparkline"; #patch!
   					#echo "type: ".$chartType."<br/>";
					#echo "query: ".$queryViz."<br/>";
   					#echo "label: ".$label."<br/>";
   					print "<tr>";
   					print "<td>$label</td>";
   					print "<td">
   					print "<td>";
   					print "<div id=\"test".$i."\" 
   						data-sgvizler-endpoint=\"".$server."\"
      					data-sgvizler-query=\"".$queryViz."\"
      					data-sgvizler-chart=\"".$chartType."\"
      					data-sgvizler-loglevel=\"2\"
      					data-sgvizler-endpoint_output=\"xml\"
      					style=\"width:800px; height:300px;\">
      					</div>"; 
      				//TODO we should extract date properly typed
   					print "</td>";
   					print "</tr>";	
   					$i++;
   				}
   				
   				?>
   			
   			
   				   			</table>
   		</div>
   	</div>
           
    
    
    <!-- Scripts at the end for faster loading -->
    <!-- script src="http://code.jquery.com/jquery.js"></script -->
    <script src="bootstrap/js/bootstrap.min.js"></script>
    <script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-42296188-1', 'yummydata.org');
  ga('send', 'pageview');

</script>
  </body>
</html>