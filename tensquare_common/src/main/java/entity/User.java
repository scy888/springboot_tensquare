package entity;

import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringJoiner;

public class User implements Serializable {
    private Integer id;
    private String username;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date birthday;
    private int age;
    private String sex;
    private String address;
    private String password;
    private String mobile;
    private BigDecimal money;
    private Status status;

    public User() {
    }

    public User(Integer id, String username, Date birthday, int age, String sex, String address, String password, String mobile, BigDecimal money) {
        this.id = id;
        this.username = username;
        this.birthday = birthday;
        this.age = age;
        this.sex = sex;
        this.address = address;
        this.password = password;
        this.mobile = mobile;
        this.money = money;

    }

    public User(String username, Date birthday, int age, String sex, String address, String password, String mobile, BigDecimal money, Status status) {
        this.username = username;
        this.birthday = birthday;
        this.age = age;
        this.sex = sex;
        this.address = address;
        this.password = password;
        this.mobile = mobile;
        this.money = money;
        this.status = status;
    }

    /**
     * @Author:scyang @Date:2019/10/2 22:44 后台转换,给前端显示的是字符串日期
     */
    public String getBirthdayStr() {
        if (birthday == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(birthday);
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", User.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("username='" + username + "'")
                .add("birthday=" + birthday)
                .add("age=" + age)
                .add("sex='" + sex + "'")
                .add("address='" + address + "'")
                .add("password='" + password + "'")
                .add("mobile='" + mobile + "'")
                .add("money=" + money)
                .toString();
    }

    @Getter
    public enum Status {
        N,
        O,
        F;
    }
}
