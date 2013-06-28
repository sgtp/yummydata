package org.yummydata.monitor

import java.util.Date
import java.text.SimpleDateFormat
import java.util.Calendar
import org.yummydata.monitoraux.QEWrapper
import com.hp.hpl.jena.rdf.model.ModelFactory
import java.io.File
import java.io.FileOutputStream

object SparkleComputer extends App{
	val timeWindow=5
	val endpoints=EndPointsTeller.getEndpointsList();
	println("Computing last 7 days");
	val todayDate=new Date();
	val dateFormat=new SimpleDateFormat("yyyy-MM-dd");
	var dateString="\""+dateFormat.format(todayDate)+"\" "
	var cal=Calendar.getInstance();
	cal.setTime(todayDate);
	for(i<-1 to 6) {
	  cal.add(Calendar.DAY_OF_YEAR,-1)
	  dateString=dateString+" \""+dateFormat.format(cal.getTime())+"\" "
	}
	//println("---"+dateString)
	val totalResultModel=ModelFactory.createDefaultModel()
	for(e<-endpoints) {
	  println("Computing result for "+e);
	  val anResults=new AnalysisResultModel(e)
	  val resultURI=anResults.resultURI
	  val resultModel=anResults.resultModel
	  var starsCount=0
	  var sparklesScore:Double=0
	  val queryForVoidFound="select count(?s)" +
	  		"where {  " +
	  		"?s <http://yummydata.org/lang#testing> <"+e+"> . " +
	  		"?s <http://yummydata.org/lang#hasTestType>  <http://yummydata.org/lang#voidTest> ." +
	  		"?s <http://yummydata.org/lang#result> \"Found\" ." +
	  				"?s <http://yummydata.org/lang#hasDayDate> ?d ." +
	  				"values ?d {"+dateString+"}" +
	  						"}";
	  
	  val totalForN="select count (?s)  where { " +
	  		"?s <http://yummydata.org/lang#testing> <"+e+"> . " +
	  				"?s <http://yummydata.org/lang#hasTestType>  <http://yummydata.org/query#defaultPing>." +
	  				"?s <http://yummydata.org/lang#responseCode> ?c ." +
	  				"?s <http://yummydata.org/lang#hasDayDate> ?d ." +
	  				"values ?d {"+dateString+"}" +
	  						"}";
	  
	  val queryForN200="select count (?s)  where { " +
	  		"?s <http://yummydata.org/lang#testing> <"+e+"> . " +
	  				"?s <http://yummydata.org/lang#hasTestType>  <http://yummydata.org/query#defaultPing>." +
	  				"?s <http://yummydata.org/lang#responseCode> \"200\" ." +
	  				"?s <http://yummydata.org/lang#hasDayDate> ?d ." +
	  				"values ?d {"+dateString+"}" +
	  						"}";
	  val queryForNm1="select count (?s)  where { " +
	  		"?s <http://yummydata.org/lang#testing> <"+e+"> . " +
	  				"?s <http://yummydata.org/lang#hasTestType>  <http://yummydata.org/query#defaultPing>." +
	  				"?s <http://yummydata.org/lang#responseCode> \"-1\" ." +
	  				"?s <http://yummydata.org/lang#hasDayDate> ?d ." +
	  				"values ?d {"+dateString+"}" +
	  						"}";
	  val queryForN0="select count (?s)  where { " +
	  		"?s <http://yummydata.org/lang#testing> <"+e+"> . " +
	  				"?s <http://yummydata.org/lang#hasTestType>  <http://yummydata.org/query#defaultPing>." +
	  				"?s <http://yummydata.org/lang#responseCode> \"0\" ." +
	  				"?s <http://yummydata.org/lang#hasDayDate> ?d ." +
	  				"values ?d {"+dateString+"}" +
	  						"}";
	  val queryForNDelay=""  
	    
	  val voidFoundQ=new QEWrapper(queryForVoidFound,YummyInstance.yummyEndpoint);
	  val totalNQ =new QEWrapper(totalForN,YummyInstance.yummyEndpoint);
	  val total200Q=new QEWrapper(queryForN200,YummyInstance.yummyEndpoint);
	  val totalm1Q=new QEWrapper( queryForNm1,YummyInstance.yummyEndpoint);
	  val total0Q=new QEWrapper(queryForN0,YummyInstance.yummyEndpoint);
	  
	  voidFoundQ.execute();
	  val voidFound=(voidFoundQ.extractSingleValue()>0)
	  println("Void found: "+voidFound);
	  totalNQ.execute();
	  val totalN=totalNQ.extractSingleValue()
	  println("Total queries: "+totalN);
	  total200Q.execute();
	  val total200=total200Q.extractSingleValue()
	  println("Total 200: "+total200);
	  totalm1Q.execute();
	  val totalm1=totalm1Q.extractSingleValue()
	  println("Total -1: "+totalm1);
	  total0Q.execute();
	  val total0=total0Q.extractSingleValue()
	  println("Total 0: "+total0);
	  
	  if(voidFound) starsCount+=1
	  sparklesScore=(total200.toDouble*1.0+totalm1.toDouble*0.0+total0.toDouble*0.0)/totalN.toDouble
	  println("Starts: "+starsCount);
	  println("Sparkles: "+sparklesScore);
	  resultModel.add(resultModel.createResource(resultURI),
		    			resultModel.createProperty(YummyInstance.yummyLang+"stars"),
		    			resultModel.createLiteral((new Integer(starsCount)).toString())
		    			);
	  resultModel.add(resultModel.createResource(resultURI),
		    			resultModel.createProperty(YummyInstance.yummyLang+"sparkles"),
		    			resultModel.createLiteral((new java.lang.Double(sparklesScore)).toString())
		    			);
	  totalResultModel.add(resultModel)
	}
	val resultFile=new File(YummyInstance.yummyDir,YummyInstance.getShortDate+"-sparkles.ttl")
	val fo=new FileOutputStream(resultFile);
  
	totalResultModel.write(fo,"Turtle");
}