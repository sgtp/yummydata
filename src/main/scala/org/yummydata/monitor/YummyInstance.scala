package org.yummydata.monitor

import java.text.SimpleDateFormat
import java.util.Date

object YummyInstance {
	val yummyPrefix="http://yummydata.org"
	val yummyValues=yummyPrefix+"/values#";
	val yummyLang=yummyPrefix+"/lang#";
	val yummyQueries=yummyPrefix+"/query#";
	val yummyEndpoint="http://yummydata.org:3030/yummy/query"
	
	def getShortDate(): String = {
	  val dateFormat=new SimpleDateFormat("yyyy-MM-dd");
	  val date=dateFormat.format(new Date());
	  return date;
	}
	
}