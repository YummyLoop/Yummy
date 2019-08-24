package yummyloop.example.test;

//public class test {}
open class kt(oa : A){
    open class A {
        var a1 = 1
        fun setA1(v : Int): A {
            a1=v
            return this
        }
    }
    class kt2(oA : A) : kt(oA){
        object  B : A(){
        }
        init {

        }
        constructor(ss : String, oa1 : A) : this(oa1.setA1(2)){

        }
    }

}