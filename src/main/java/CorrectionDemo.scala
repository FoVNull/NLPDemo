import java.awt.{Color, GridLayout}

import javax.swing.event.{AncestorListener, DocumentEvent, DocumentListener}
import javax.swing.{BorderFactory, JFrame, JPanel, JScrollPane, JTextArea, JTextField, JTextPane}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object CorrectionDemo {
    val prefix="D:/IntelliJ IDEA/IdeaProjects/NLPDemo/src/main/resources/"
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
                val strs=input.getText().split(" ")
                val results:ListBuffer[mutable.HashMap[String,List[Int]]]=ListBuffer()
                for(i <- strs) results.addOne(caculateDis(i))
                val res=mergeRes(results)
                val stb=new StringBuilder
                res.foreach(x=>stb.append(x+"\r\n"))
                output.setText(stb.toString())
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
        frame.setVisible(true)

        /*
        //val strs="norm of the noth".split(" ")
        val strs="teracei house".split(" ")
        val results:ListBuffer[mutable.HashMap[String,List[Int]]]=ListBuffer()
        for(i <- strs) results.addOne(caculateDis(i))
        mergeRes(results)
         */

    }
    def caculateDis(word:String): mutable.HashMap[String,List[Int]] ={
        val reWords:mutable.HashMap[String,List[Int]]=mutable.HashMap()
        val generator=Generator
        val bufferedSource = io.Source.fromFile(prefix+"dictionary.csv")
        for (line <- bufferedSource.getLines) {
            val strs=line.split(",")
            if(generator.minDistance(word,strs(0))<3) {
                val list: ListBuffer[Int] = ListBuffer()
                strs(1).split(" ").foreach(i => {
                    if(i!="") list.addOne(i.toInt)
                })
                reWords.put(strs(0), list.toList)
            }
        }
        bufferedSource.close()
        reWords
    }

    def mergeRes(results:ListBuffer[mutable.HashMap[String,List[Int]]]): ListBuffer[String] ={
        val res:ListBuffer[String]=ListBuffer()
        val list:ListBuffer[ListBuffer[Int]]=ListBuffer()

        for(i <- results.indices) {
            val temp:ListBuffer[Int]=ListBuffer()
            results(i).foreach(x => {temp.addAll(x._2)})
            list.addOne(temp)
        }
        val bufferedSource = io.Source.fromFile(prefix+"netflix_titles.csv")
        var count=0
        for (line <- bufferedSource.getLines) {
            if(count!=0) {
                val strs:Array[String]=line.split(",")
                var flag=true
                list.foreach(i=>{
                    if(!i.contains(strs(0).toInt)) flag=false
                })
                if(flag) res.addOne(strs(2))
            }
            count=1
        }
        bufferedSource.close()
        res.foreach(i => println(i))

        res
    }
}
