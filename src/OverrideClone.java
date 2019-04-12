import hashtable.AnotherHashTable;
import hashtable.BadHashTable;
import hashtable.RecommendHashTable;

import java.util.Collection;

/**
{@link Cloneable}接口与其他接口不一样，它里面啥都没有，仅仅是为了表示该类可以被克隆。
但如果单独只有它自己，还不能够实现这个目的。它实际上是一个混合接口(mixin interface),
由于自身没有clone()方法，而Object中的clone方法又是受保护的，因此只能借助反射；
但反射调用也有可能会失败，因为无法保证该对象一定具有可访问的clone方法。但尽管如此，
这种做法仍然在被广泛使用，所以下面讨论如何编写良好的clone方法，什么时候需要编写这个方法，
以及它的一些替代方式。
 *
那么这个自身啥都没有的接口都做了些啥？它决定了Object中受保护的clone方法的行为。
如果某对象实现了{@link Cloneable}接口，那么调用其clone方法就会返回该对象的逐域拷贝
(field-to-field copy),否则就会抛出{@link CloneNotSupportedException}.
这是一种极端非典型的接口使用方式，并不表明实现它的类应做什么，而改变了超类受保护方法的行为，
没有必要去练习这种使用方式。
 *
虽然没有强制规定，但实际上实现了该接口的类应该提供一个public Object clone()方法，
而为了实现这个方法，它跟它的所有子类都必须遵守一个复杂、非强制(unenforceable,或译为不可执行的?)、
缺少文档说明的协议，然后得到一种危险,脆弱,不符合Java语言规范的机制：不通过构造方法就创建对象。
 *
{@link Object#clone()}的通用规约是非常弱的，摘录如下：
 1.创建并返回该对象的副本，副本对象与原对象的关系视情况而定，但一般情况下应有
     x.clone() != x ;
     x.clone().getCLass() == x.getClass();
     x.clone().equals(x) == true ;
  但这几条都不是硬性要求
 2.一般而言，返回的实例应该通过调用父类的super.clone()获得({@link Object}除外)，
 只要该类以及其所有父类全部遵守规约，那么就自然地会满足
     x.clone().getCLass() == x.getClass().
 3.一般而言，副本对象应独立于被克隆对象。为了实现这一点，可能会需要对super.clone()所return
 的实例做一些修改，然后返回这个修改后的实例。(比如，复制完整的数据结构，更改对其引用等)。
 这里要做一点说明：如果被克隆对象中有非基本类型对象，而非基本类型对象又没有继承{@link Cloneable}
 接口，那么克隆时只会将引用复制过去，即克隆前后的两个对象共享这个非基本类型的变量，比如这个例子
 {@link Student}
 *
 这种机制和构造函数链有点类似，只是它没有被强制执行：
 如果一个类的克隆方法返回一个实例，这个实例不是通过调用super.clone()而是通过构造方法获得的，
 编译器虽然不会报错，但如果该类的子类调用了super.clone()，那么就会返回错误的类，
 则会导致子类的clone()也出现问题。如果重写clone()的类是final型，不会有子类，就不需要考虑这个问题，
 但如果声明为final的类，其clone()方法不调用Super.clone()，那么由于不依赖于Object.clone()，
 其实也没必要implements Cloneable接口。
 (???????一脸懵逼)
 *
如果要写一个实现Cloneable接口的类，首先要调用父类的clone()方法，获取一个功能正常齐全的父类副本，
此时子类中与父类的对应字段其值已经与父类相同了。倘若你的子类中只有基本类型或者不可变类型的成员变量，
就跟上面规约第3条类似那样，那么这时候这个super.clone()返回的对象就已经满足你的需求了；
但要注意对于不可变对象，不应该提供关于这种对象的clone方法，因为反正不可变，直接拿去用就好，
clone一下反而浪费时间。
 *
注意{@link Student#clone()}中，由于在{@link Object#clone()}中声明了有可能会抛出
{@link CloneNotSupportedException}异常，这是一个检查型异常，因此会提示开发者去处理它。
 *
另外，前面一直强调对于不可变的非基本类型，可以像基本类型那样处理，但是可变的非基本类型又如何？
比如前面item7中的Stack类，如果仅仅简单地return super.clone()，那么就会出现跟{@link Student}
相同的问题：仅仅克隆了非基本类型对象的引用，即类似C语言中指针的东西。这时候如果原对象(比如Stack)
中的Element非基本类型实例element被改变了，那么克隆出来的stack2.element也会跟着改变，
因为访问stack2中的element实际上通过这个引用，最终访问的还是stack1中的element对象。
因此说需要在重写clone方法时递归地调用其中可变非基本类型实例的super.clone()方法，
创建其中对应成员变量的副本，就像{@link Student#clone()}那样。
 *
注意无需将数组实例(array)的super.clone()结果强制转换为Object[]。对于数组，
其执行clone()的返回值在运行或编译时与其本身是相同的，因此clone()方法也是复制数组的首选方式。
实际上，复制数组应该是clone()唯一做得不错的地方了。
 *
另外还要注意，如果成员变量被声明为final型，那么由于clone过程中需要为其赋值，因此会发生错误，
进而抛出异常。这个应该不难理解，与之类似的情况还有序列化(serialization)。因此，
为了能使这个类的实例能被顺利地序列化，往往需要去掉一些成员变量的final修饰符。
 *
但有时候，仅仅是递归地调用父类的clone()方法还不够，例如这个{@link BadHashTable},
于是引申出一种克隆复杂对象的方式：{@link hashtable.RecommendHashTable}，
通过在Entry中添加自己定义的深层复制方法，防止复制对对象的引用从而造成不稳定性。
 *
此外，还有另一种方式去解决上述问题{@link hashtable.AnotherHashTable},
这种方式虽然简洁易读，但与跟“克隆”这一概念好像没什么关系，因为这种方式用“重新生成”
取代了数组的clone()，破坏了clone()方法的结构
 *
与构造函数类似，clone()也不应该在其方法体中调用可以被重写的方法。
否则其子类就仍然有机会调用那个过时的、被重写的方法，进而损坏克隆前后对象的结构。
所以，前面被调用的{@link RecommendHashTable.Entry#deepCopy1()},
{@link hashtable.AnotherHashTable.Entry#put(AnotherHashTable.Entry)}这样的方法，
其实都应该加上final或者private字段进行保护。(如果加上private关键字，
那么它可以算是非final方法的“辅助方法”helper method)
 *
虽然{@link Object#clone()}的声明中说可能会抛出{@link  CloneNotSupportedException},
但是重写这个方法时就没必要再加一句声明了，因为不抛出这个检查型异常可能会更容易编程。
 *
设计用于继承的类时有两种选择(允许继承并提供文档，或者禁止继承)，但无论哪一种，
这个类都不应该implements Cloneable. 可以模仿Object.clone()这样声明：
protected Object clone() throws CloneNotSupportedException;
这样子类就可以自己决定是否需要implements Cloneable了;
或者可以干脆禁止子类继承，扔个异常出来：
 @Override
 protected final Object clone() throws CloneNotSupportedException {
     throw new CloneNotSupportedException();
 }
 *
还有一点细节需要注意，如果写了实现Cloneable的线程安全类，应注意在Object类中，
clone方法并没有被加上synchronized锁，因此需要像其他方法一样处理好同步锁的问题，
或许需要实现一个返回super.clone()的synchronized clone()方法.
 *
综上所述，clone()这东西很难用，因此能替则替。更好的复制对象方式是提供一个“拷贝构造方法”
(copy constructor)或者“拷贝工厂”(copy factory)，接受一个类型为方法所在的类的参数，比如
{@link #OverrideClone(OverrideClone)}
与clone()相比，这种方式
 1.无需依赖存在风险且依赖其他非Java语言实现的克隆机制，
 2.不需要时时核对自己的实现方式是否与clone()的文档中所记载的相吻合（因为clone()没有强制的约束措施），
 3.不会与final字段发生冲突，不要求强制转换，也不会抛出多余的检查型异常(checked exception)
 4.可以接受一个接口类型的参数(这个类所实现的接口)。比如，为了方便可以将所有实现
 {@link java.util.Collection}的类的拷贝构造方法参数设置为Collection型。这种
 “基于接口的拷贝工厂or拷贝构造方法”(也称“转换工厂or转换构造方法”)，使客户端能自由选择副本类型，
 而不用与被克隆的类相同。
     比如，{@link java.util.TreeSet#TreeSet(Collection)}可以将原有对象复制并转换成任意实现了
     该接口的类，比如实现{@link java.util.HashSet}到{@link java.util.TreeSet}的转化。
 *
最后一句，难用，不要扩展它，尽量少调用它，顶多在复制数组时稍微用一下
 *
@author LightDance
 */
public class OverrideClone {

    public OverrideClone(OverrideClone object) {
        //...
    }

    public static OverrideClone newInstance(OverrideClone obj){
        //...
        return null;
    }
}
