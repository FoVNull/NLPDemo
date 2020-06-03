import breeze.linalg.DenseMatrix

import scala.collection.mutable

object Probability {
    def caculate(): mutable.HashMap[Int,Double] ={
        val res:mutable.HashMap[Int,Double]=mutable.HashMap()
        val prefix="D:/IntelliJ IDEA/IdeaProjects/NLPDemo/src/main/resources/"
        val reader = io.Source.fromFile(prefix+"search_history.csv")
        for(line <- reader.getLines()){
            val strs=line.split(",")
            res.put(strs(0).toInt,strs(1).toDouble/623400)
        }
        reader.close()
        res
    }

}
