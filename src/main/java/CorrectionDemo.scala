import java.awt.{Color, GridLayout}

import breeze.linalg.DenseMatrix
import javax.swing.event.{DocumentEvent, DocumentListener}
import javax.swing.{BorderFactory, JFrame, JPanel, JScrollPane, JTextArea, JTextPane}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.util.control.Breaks

object CorrectionDemo {
    val prefix="D:/IntelliJ IDEA/IdeaProjects/NLPDemo/src/main/resources/"
    val pro:mutable.Map[Int, Double] =Probability.caculate()
    val disP:DenseMatrix[Double]=DenseMatrix((0.0,1.0,2.0),(1.0,0.55,0.1))
    def main(GL:Array[String]): Unit ={
        val frame:JFrame=new JFrame()
        val panel:JPanel=new JPanel()
        val input:JTextArea=new JTextArea()
        val output:JTextPane=new JTextPane()
        val outputScroll:JScrollPane=new JScrollPane();
        input.setLineWrap(true)
        input.setWrapStyleWord(true)
        input.setBorder(BorderFactory.createLineBorder(Color.BLACK))
        output.setEditable(false)
        outputScroll.setViewportView(output)
        panel.add(input);panel.add(outputScroll)
        panel.setLayout(new GridLayout(0,1,0,0))

        input.getDocument.addDocumentListener(new DocumentListener {
            override def insertUpdate(e: DocumentEvent): Unit = {
                val check=checkDistance(input.getText())
                if(!check.equals("")) output.setText(check)
                else {
                    val strs = input.getText().split(" ")
                    val results: ListBuffer[mutable.HashMap[String, (List[Int], Double)]] = ListBuffer()
                    for (i <- strs) results.addOne(caculateDis(i))
                    val res = mergeRes(results)
                    val stb = new StringBuilder
                    res.toSeq.sortWith(_._2 > _._2).foreach(x => stb.append(x._1 + "\r\n"))
                    output.setText(stb.toString())
                }
            }

            override def removeUpdate(e: DocumentEvent): Unit = {}

            override def changedUpdate(e: DocumentEvent): Unit = {}
        })

        frame.add(panel)
        frame.setTitle("Netflix标题检索纠错")
        frame.setResizable(false)
        frame.setDefaultCloseOperation(3)
        frame.setSize(400,300)
        frame.setLocationRelativeTo(null)
        //frame.setVisible(true)

        //控制台测试
        val str="norm of the north"//"teracei house"
        val strs=str.split(" ")
        val check=checkDistance(str)
        if(!check.equals("")) println(check)
        else {
            val results: ListBuffer[mutable.HashMap[String, (List[Int], Double)]] = ListBuffer()
            for (i <- strs) results.addOne(caculateDis(i))
            mergeRes(results).toSeq.sortWith(_._2 > _._2).foreach(x => println(x))
        }
    }

    //将编辑距离符合要求的词筛选出来
    def caculateDis(word:String): mutable.HashMap[String,(List[Int],Double)] ={
        val reWords:mutable.HashMap[String,(List[Int],Double)]=mutable.HashMap()
        val generator=Generator
        val bufferedSource = io.Source.fromFile(prefix+"dictionary.csv")
        for (line <- bufferedSource.getLines) {
            val strs=line.split(",")
            val distance=generator.minDistance(word,strs(0))
            if(distance < 3) {
                val list: ListBuffer[Int] = ListBuffer()
                var p:Double=0
                strs(1).split(" ").foreach(i => {
                    if(i!="") {
                        list.addOne(i.toInt)//该词属于哪个节目，把节目号记录上
                        p+=pro(i.toInt)*disP(1,distance)//需要将所有出现c的节目的概率相加才能得到w和c之间的概率
                    }
                })
                reWords.put(strs(0), Tuple2(list.toList,p))
            }
        }
        bufferedSource.close()
        reWords
    }

    //整合map
    def mergeRes(results:ListBuffer[mutable.HashMap[String,(List[Int],Double)]])
    : mutable.HashMap[String,Double] ={
        val res: mutable.HashMap[String, Double] = mutable.HashMap()

        val bufferedSource = io.Source.fromFile(prefix+"netflix_titles.csv")
        var count=0
        for (line <- bufferedSource.getLines) {
            if(count!=0) {
                val strs:Array[String]=line.split(",")
                val id=strs(0).toInt
                var flag=true;var p:Double=0
                val loop:Breaks=Breaks
                loop.breakable(results.foreach(map => {
                    var subFlag=false
                    map.foreach(entry=>{
                        if(entry._2._1.contains(id)) {p+=entry._2._2;subFlag=true;}
                    })
                    if(!subFlag) {flag=false;loop.break()}
                }))
                if(flag) res.put(strs(2),p)
            }
            count=1
        }
        bufferedSource.close()
        res
    }

    def checkDistance(word:String): String ={
        val bufferedSource = io.Source.fromFile(prefix+"netflix_titles.csv")
        var count=0
        for (line <- bufferedSource.getLines) {
            val strs=line.split(",")
            if(count>0){
                if(Generator.minDistance(word,strs(2))==0) return strs(2)
            }
            count=1
        }
        bufferedSource.close()
        ""
    }
}
