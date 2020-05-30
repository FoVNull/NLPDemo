package Training

import java.io.{File, PrintWriter}
import scala.collection.mutable

object Simulation {
    def main(GL:Array[String]): Unit ={
        val prefix="D:/IntelliJ IDEA/IdeaProjects/NLPDemo/src/main/resources/"
        createDic(prefix)
    }

    def createDic(prefix:String): Unit ={
        val bufferedSource = io.Source.fromFile(prefix+"netflix_titles.csv")
        val sets:mutable.HashSet[String]=mutable.HashSet()
        val punctuations=Array(":","!","\\(","\\)","&")
        for (line <- bufferedSource.getLines) {
            val cols = line.split(",").map(_.trim)
            if(cols.length>2) {
                var str=cols(2)
                for(i <- punctuations) str=str.replaceAll(i,"")
                val strs=str.split(" ")
                strs.foreach(i => sets.add(i))
            }
        }
        bufferedSource.close

        val writer = new PrintWriter(new File(prefix+"dictionary.csv" ))
        val stb:StringBuilder=new mutable.StringBuilder()
        sets.foreach(x => stb.append(x+"\r\n"))
        writer.write(stb.toString())
        writer.close()
    }

}
