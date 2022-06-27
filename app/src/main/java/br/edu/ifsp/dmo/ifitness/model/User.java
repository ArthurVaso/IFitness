package br.edu.ifsp.dmo.ifitness.model;

import java.util.Objects;
import java.util.UUID;

public class User {
    private String id;
    private String name;
    private String surname;
    private String birthdayDate;
    private String gender;
    private String phone;
    private String email;
    private String password;
    private String points;
    private String badges;
    private String image;

    public User(String name, String surname,
                String birthdayDate, String gender, String phone,
                String email, String password, String points,
                String badges, String image) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.surname = surname;
        this.birthdayDate = birthdayDate;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.points = points;
        this.badges = badges;
        this.image = image;
    }

    public User() {
        this("", "",
                "", "", "",
                "", "", "",
                "", "");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getBirthdayDate() {
        return birthdayDate;
    }

    public void setBirthdayDate(String birthdayDate) {
        this.birthdayDate = birthdayDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getBadges() {
        return badges;
    }

    public void setBadges(String badges) {
        this.badges = badges;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

/*1. A aplicação deve persistir os dados de todos os usuários do aplicativo em um banco
de dados remoto (no Firebase). Cada usuário possui código
            (string UUID, gerado pela aplicação),
            nome,
            data de nascimento,
            sexo,
            telefone,
            e-mail (utilizado para login),
            senha,
            pontuação (inicialmente zero, requisito 8) e a
            lista de emblemas adquiridos (inicialmente vazia, requisito 8).*/