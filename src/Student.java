/**
 * 用于说明clone()的深层复制与浅复制
 *
 * 如果要避免clone()方法对非基本类型仅复制其引用，就要重写clone()，
 * 对每一个非基本类型调用其clone方法（该非基本类型应implements Cloneable接口）
 *
 * @author LightDance
 */
public final class Student implements Cloneable {
    String name;

    int age;

    Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    DeepTeacher teacher1 = new DeepTeacher("张老师", 28);

    ShallowTeacher teacher2 = new ShallowTeacher("李老师", 40);

    @Override
    public Object clone() throws CloneNotSupportedException{
        //在这里进行强转而不是return一个Object类，因为“能由类库完成的工作就不要让客户端完成”
        //Java1.5后的新机制，协变返回类型(covariant return type)，可返回被覆盖方法返回类型的子类，
        //以便于提供更多关于返回对象的信息
        Student newStudent = (Student) super.clone();
        newStudent.teacher1 = (DeepTeacher) teacher1.clone();
        return newStudent;
    }
    public static void main(String[] args) throws CloneNotSupportedException {
        Student s1 = new Student("小赵" , 18);
        Student s2 = (Student) s1.clone();

        System.out.println("s1: " + s1);
        System.out.println("克隆后的s2：" + s2);

        System.out.println(s2.teacher1);
        System.out.println(s2.teacher2);

        s1.teacher1.setAge(s1.teacher1.getAge() + 1);
        s1.teacher2.setAge(s1.teacher2.getAge() + 1);
        System.out.println("一年以后（对s1中两位老师年龄+1）：");

        System.out.println(s2.teacher1);
        System.out.println(s2.teacher2);

    }
}
