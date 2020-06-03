package Training

import java.io.{File, PrintWriter}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object Simulation {
    def main(GL:Array[String]): Unit ={
        val prefix="D:/IntelliJ IDEA/IdeaProjects/NLPDemo/src/main/resources/"
        //createDic(prefix)
        userChoice(prefix)
    }

    def createDic(prefix:String): Unit ={ //构建字典
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

    def userChoice(prefix:String): Unit ={
        val reader=io.Source.fromFile(prefix+"netflix_titles.csv")
        val map:mutable.HashMap[Int,(Int, Int)]=mutable.HashMap()//scala语法糖，不需要明确指定Tuple类型
        var count = 0
        for (line <- reader.getLines) {
            if (count != 0) {
                val strs=line.split(",")
                val id=strs(0).toInt
                map.put(count,Tuple2(id,0))
            }
            count+=1
        }
        reader.close()
        count=map.size*100
        val size=map.size

        for(_ <- 0 to count){
            val order=1+(Math.random()*size).toInt
            map.put(order,(map(order)._1,map(order)._2+1))
        }

        val writer = new PrintWriter(new File(prefix+"search_history.csv" ))
        val stb:StringBuilder=new mutable.StringBuilder()
        map.foreach(entry => {
            val temp=new mutable.StringBuilder()
            temp.append(entry._2._1+","+entry._2._2)
            stb.append(temp.toString()+"\r\n")
        })
        writer.write(stb.toString())
        writer.close()
    }
}
