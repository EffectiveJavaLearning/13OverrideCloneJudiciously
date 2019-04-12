/**
 * 深层复制,需要implements cloneable接口并重写Object.clone()
 *
 * @author LightDance
 */
public class DeepTeacher implements Cloneable {
    String name;

    int age;

    public DeepTeacher(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getClass().getName() + "@" + "name :" + name + " age : " + age;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
