package org.yummydata.monitor

import java.text.SimpleDateFormat
import java.util.Date
import java.io.File

object YummyInstance {
	val yummyPrefix="http://yummydata.org"
	val yummyValues=yummyPrefix+"/values#";
	val yummyLang=yummyPrefix+"/lang#";
	val yummyQueries=yummyPrefix+"/query#";
	val yummyEndpoint="http://yummydata.org:3030/yummy/query"
	val yummyEndpointUpdate="http://127.0.0.1:3030/yummy/data"
	val yummyDirString=System.getProperty("user.home")+"/yummyResults";
	val yummyDir=new File(yummyDirString)
	if(!yummyDir.isDirectory()) yummyDir.mkdir()
	
	
	def getShortDate(): String = {
	  val dateFormat=new SimpleDateFormat("yyyy-MM-dd");
	  val date=dateFormat.format(new Date());
	  return date;
	}
	
}