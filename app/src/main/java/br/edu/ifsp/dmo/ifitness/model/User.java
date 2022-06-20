package br.edu.ifsp.dmo.ifitness.model;

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