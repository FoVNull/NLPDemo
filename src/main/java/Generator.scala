import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object Generator {
    val results:mutable.HashSet[String]=mutable.HashSet()

    def generateWord(word:String,count:Int): mutable.HashSet[String] ={
        if(count>0){
            for(s <- delete(word)) generateWord(s,count-1)
            for(s <- insert(word)) generateWord(s,count-1)
            for(s <- update(word)) generateWord(s,count-1)
        }
        results
    }

    def delete(word:String): mutable.HashSet[String] ={
        val dRes:mutable.HashSet[String]=mutable.HashSet()
        val chars=word.toCharArray
        for(i <- chars.indices){
            val temp=word.toCharArray
            val stb:StringBuilder=new StringBuilder
            for(j <- temp.indices) if(j!=i) stb.append(temp(j))
            results.add(stb.toString())
            dRes.add(stb.toString())
        }
        dRes
    }

    def insert(word:String):mutable.HashSet[String]={
        val iRes:mutable.HashSet[String]=mutable.HashSet()
        val chars=word.toCharArray
        for(i <- 0 to chars.length) {
            for(c:Char <- 'a' to 'z') {
                val list: ListBuffer[Char] = ListBuffer()
                list.addAll(chars)
                list.insert(i, c)
                val stb:StringBuilder=new StringBuilder
                for(n <- list) stb.append(n)
                iRes.add(stb.toString())
                results.add(stb.toString())
            }
        }
        iRes
    }

    def update(word:String):mutable.HashSet[String]={
        val uRes:mutable.HashSet[String]=mutable.HashSet()
        val chars=word.toCharArray

        for(i <- chars.indices){
            for(c <- 'a' to 'z' if c!=chars(i)){
                val temp=word.toCharArray
                val stb:StringBuilder=new StringBuilder
                temp(i)=c
                for(n <- temp) stb.append(n)
                uRes.add(stb.toString())
                results.add(stb.toString())
            }
        }
        uRes
    }

    def minDistance(word1: String, word2: String): Int = {
        val len1=word1.length;val len2=word2.length
        val dp:Array[Array[Int]]=Array.ofDim[Int](len1+1, len2+1)

        //init
        for(i <- 0 to len1) dp(i)(0)=i
        for(i <- 0 to len2) dp(0)(i)=i

        for(i <- 1 to len1){
            for(j <- 1 to len2){
                val del = dp(i - 1)(j) + 1
                val ins = dp(i)(j-1) + 1
                var upd = dp(i-1)(j-1)
                //如果相同就不用+1了，这里用i-1,j-1是因为word长度和dp不一样
                if(word1.charAt(i-1)!=word2.charAt(j-1) &&
                  word1.charAt(i-1).toLower!=word2.charAt(j-1).toLower) upd+=1

                dp(i)(j)=Math.min(del,Math.min(ins,upd))
            }
        }
        dp(len1)(len2)
    }

}
