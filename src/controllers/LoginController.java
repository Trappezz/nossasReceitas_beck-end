package controllers;

import services.LoginService;

public class LoginController {
    private final LoginService loginService = new LoginService();

    public void fazerLogin(String email, String senha) {
        boolean autenticado = loginService.autenticar(email, senha);

        if (autenticado) {
            System.out.println("Login bem-sucedido!");
        } else {
            System.out.println("E-mail ou senha incorretos.");
        }
    }
}
