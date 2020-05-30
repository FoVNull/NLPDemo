package Training

import java.io.{File, PrintWriter}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object Simulation {
    def main(GL:Array[String]): Unit ={
        val prefix="D:/IntelliJ IDEA/IdeaProjects/NLPDemo/src/main/resources/"
        createDic(prefix)
    }

    def createDic(prefix:String): Unit ={
        val bufferedSource = io.Source.fromFile(prefix+"netflix_titles.csv")
        val map:mutable.HashMap[String,ListBuffer[Int]]=mutable.HashMap()
        val punctuations=Array(":","!","\\(","\\)","&","\\+","-","\\*","\\/")
        var count = 0
        for (line <- bufferedSource.getLines) {
            if(count!=0) {
                val cols = line.split(",")
                var str = cols(2)
                for (i <- punctuations) str = str.replaceAll(i, "")
                val strs = str.split(" ")
                strs.foreach(i => {
                    if(i!="") {
                        val list: ListBuffer[Int] = map.getOrElse(i, ListBuffer())
                        list.addOne(cols(0).toInt)
                        map.put(i, list)
                    }
                })
            }
            count=1
        }
        bufferedSource.close

        val writer = new PrintWriter(new File(prefix+"dictionary.csv" ))
        val stb:StringBuilder=new mutable.StringBuilder()
        map.foreach(x => {
            val temp=new mutable.StringBuilder()
            x._2.foreach(i => temp.append(" "+i))
            stb.append(x._1+","+temp+"\r\n")
        })
        writer.write(stb.toString())
        writer.close()
    }

}
