<?php
require_once("libs/sparqllib.php");

error_reporting(E_ERROR | E_WARNING | E_PARSE | E_NOTICE);

$server="www.intellileaf.com:8000/sparql/";


$db = sparql_connect($server );
if( !$db ) { print $db->errno() . ": " . $db->error(). "\n"; exit; }
$db->ns( "yummy","http://yummydata.org/lang#" );
$db->ns( "rdfs","http://www.w3.org/2000/01/rdf-schema#" );
 
$sparql = "SELECT distinct * WHERE {
?dataPoint a yummy:sparkleResult .
?dataPoint yummy:testing ?endPoint .
?endpoint a yummy:endPoint .
?endpoint rdfs:label ?label .
?dataPoint yummy:onDate \"2013-06-17\" .
?dataPoint  yummy:sparkles ?sparkles .
?dataPoint  yummy:stars ?stars .

} 
ORDER by desc(?sparkles) ";

$result = $db->query( $sparql ); 
if( !$result ) { print $db->errno() . ": " . $db->error(). "\n"; exit; }
 
$fields = $result->field_array( $result );
/* 
print "<p>Number of rows: ".$result->num_rows( $result )." results.</p>";
print "<table class='example_table'>";
print "<tr>";
foreach( $fields as $field )
{
	print "<th>$field</th>";
}
print "</tr>";
while( $row = $result->fetch_array() )
{
	print "<tr>";
	foreach( $fields as $field )
	{
		print "<td>$row[$field]</td>";
	}
	print "</tr>";
}
print "</table>";
*/

# Just a bit of PHP ?
?>
<!DOCTYPE html>
<html>
  <head>
    <title>YummyData</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
     <link href="bootstrap/css/bootstrap.css" rel="stylesheet">
    <link href="bootstrap/css/bootstrap-responsive.css" rel="stylesheet">
	<!--
	<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.6.2/jquery.min.js"></script>
	<script type="text/javascript" src="https://www.google.com/jsapi"></script>
	<script type="text/javascript" id="sgvzlr_script" src="sgvizler-0.5/sgvizler.js"></script>
	<script type="text/javascript">
		sgvizler.option.namespace.npd = 'http://sws.ifi.uio.no/npd/';
      	sgvizler.option.namespace.npdv = 'http://sws.ifi.uio.no/vocab/npd#';
      	$(document).ready(sgvizler.go());
    </script>
    -->
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
   			<ul>
   			<li>About</li>
   			<li>Register a new site</li>
   			</ul>
   		</div>
   		<div class="span10">
   			<table class="table table-striped">
   				<caption>Todays's ranking</caption>
   				<thead>
    				<tr>
      					<th>Endpoint</th>
      					<th>URL</th>
      					<th>Score</th>
      					<th>Stars</th>
      					<th>Detail</th>
    				</tr>
  				</thead>
   				<tbody>
   				<?php
   					while( $row = $result->fetch_array()){
						print "<tr>";
   						print "<td>".$row["label"]."</td>";
						print "<td>".$row["endpoint"]."</td>";
						print "<td>".$row["sparkles"]."</td>";
						print "<td>";
						for($i=0; $i<intval($row["stars"]);$i++) {	//TODO this should ne a numeric to begin with
							print "<i class=\"icon-star\"></i>";
						}
						print "</td>";
						//print "<td> <a href=eDtail.php?endpoint=\"".$row[endpoint]."\"><i class=\"icon-info-sign\"></a> </td>";
						print "<td> <a class=\"icon-info-sign\" href=eDetail.php?endpoint=\"".$row[endpoint]."\"></a> </td>";

						print "</tr>\n";
					}

   				
   				
   				?>
   				</tbody>
   			</table>
   			
   			
   		</div>
   	</div>
           
    
    
    <!-- Scripts at the end for faster loading -->
    <script src="http://code.jquery.com/jquery.js"></script>
    <script src="bootstrap/js/bootstrap.min.js"></script>
  </body>
</html>