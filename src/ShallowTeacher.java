/**
 * 浅复制
 *
 * @author LightDance
 */
public class ShallowTeacher {
    String name;
    int age;

    ShallowTeacher(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }

    public void setAge(int age) { this.age = age; }


    @Override
    public String toString() {
        return getClass().getName() + "@" + "name :" + name + " age : " + age;
    }
}
