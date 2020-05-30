import breeze.linalg._
import breeze.stats.distributions._
import breeze.stats.mean
import breeze.plot._

object BreezeDemo {
    def main(GL:Array[String]): Unit ={
        //arrayTest()
        figureTest()
    }
    def arrayTest(): Unit={
        var v:DenseVector[Double] = DenseVector.zeros(5)
        v = DenseVector(0.1, 0.2, 1.3, 2.5, 3.4)
        v(3 to 4) := 1.5//多值修改
        val x=v(0 until (v.length,2))
        println(x);

        var m:DenseMatrix[Int] = DenseMatrix.zeros[Int](3,3)
        m(::,1):=1//DenseVector(1,1,1)
        m(2,::) := DenseVector(1,2,3).t//Transpose Vector
        println(m);print("\n")

        m=DenseMatrix((1,2,3),(4,5,6),(7,8,9))//直接定义

        //val n=m.map(_*10)//m.map(x=>x*10)
        //println(n);print("\n")

        val a=m(::,*)+DenseVector(1,2,3)//每一列都加上Vector(1,2,3),*意味着foreach
        println(a);print("\n")

        val dm = DenseMatrix((1.0,2.0,3.0), (4.0,5.0,6.0))
        val y:DenseVector[Double]=mean(dm(*,::))//求每行的均值
        val z:Transpose[DenseVector[Double]]=mean(dm(::,*))//每列的均值
        println(y);println(z)
    }

    def probabilityTest(): Unit={
        val p = Poisson(3.0)

    }

    def figureTest(): Unit={
        val f = Figure()
        val p = f.subplot(0)

        val list=List(1,2,3,4)
        val list1=List(2,3,5,6)
        p +=plot(list,list1,'-')

        val x = linspace(0.0,1.0)
        //p += plot(x, x ^:^ 2.0) //将linspace中的元素做平方处理
        //p += plot(x, x ^:^ 3.0, '.')

        p.xlabel = "x axis"
        p.ylabel = "y axis"
        f.visible = true
        //f.saveas("lines.png")
    }

}
