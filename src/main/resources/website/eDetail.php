<?php
require_once("libs/sparqllib.php");
error_reporting(E_ERROR | E_WARNING | E_PARSE | E_NOTICE);

$endpoint=$_GET['endpoint'];
$endpointc=str_replace("\"","",$endpoint);
#print "+++".$endpointc;

$server="http://intellileaf.com:3030/yummy/query";

$db = sparql_connect($server );
if( !$db ) { print $db->errno() . ": " . $db->error(). "\n"; exit; }

$db->ns( "yummy","http://yummydata.org/lang#" );
$db->ns( "rdfs","http://www.w3.org/2000/01/rdf-schema#" );
 
$sparql = "SELECT * WHERE { ?test rdfs:label ?label . ?test yummy:appliesTo <".$endpointc."> . } order by ?label";

$result = $db->query( $sparql ); 
if( !$result ) { print $db->errno() . ": " . $db->error(). "\n"; exit; }

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
       
    <div class="row">
    	<div class="span12">
        	<h1>Yummy Data is Here!</h1>
        </div>
   	</div>
   	<div class="row">
   		<div class="span2">
   			<h2>Menu</h2>
   			Register your site<br/>
   			Contact innfo<br/>
   		</div>
   		<div class="span10">
   			<table class="table>"
   				<?php
   				
   				$i=0;
   				while( $row = $result->fetch_array()){
   					$chart=$row['$chart'];
   					if($chart=='') $chart="gSparkline";
   					
   					print "<tr>";
   					print "<td>".$row['label']."</td>";
   					print "<td">
   					print "<td>";
   					print "<div id=\"test".$i."\" 
   						data-sgvizler-endpoint=\"".$server."\"
      					data-sgvizler-query=\"
											PREFIX yummy: 	<http://yummydata.org/lang#> 

											SELECT ?value WHERE {
											?test yummy:testing <".$endpointc."> .
											?test  a <".$row['test']."> .
											?test  yummy:hasDate ?date .
											?test yummy:result ?value;
											}
											order by ?date\"
      					data-sgvizler-chart=\"".$chart."\"
      					data-sgvizler-loglevel=\"2\"
      					data-sgvizler-endpoint_output=\"xml\"
      					style=\"width:800px; height:400px;\">
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
  </body>
</html>