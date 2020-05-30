object CorrectionDemo {
    def main(GL:Array[String]): Unit ={
        val generator=Generator
        //println(generator.generateWord("apple",2))
        val hs=generator.generateWord("apple",2)
        for(i <- hs) {
            val x=generator.minDistance("apple",i)
            if(x>2) println(i)
        }
    }
}
