# NLPDemo
一些小练习，主要用的包是ScalaNLP，Tensorflow，dl4j
<hr>
#1 拼写纠正(CorrectionDemo.scala)<br>
20.05.30 beta.v1<br>
以Netflix的节目单作为单词库，实现用户搜索节目时会对节目的拼写进行反馈，给出可能的节目名<br>
首先建立词典，把节目名中的单词都扣下来，筛掉标点 &nbsp;&nbsp;|&nbsp;&nbsp; /Training/Simulation.scala<br>
根据编辑距离将每个词的相似词筛选出来，构成多个Map<单词,List<节目标号>><br>
将Map进行整合，将出现于所有map中的节目筛选出来。<br>
