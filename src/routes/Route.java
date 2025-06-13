package routes;

import controllers.LoginController;

import java.util.Scanner;

public class Route {
    public void startLogin() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Digite o e-mail: ");
        String email = sc.nextLine();

        System.out.print("Digite a senha: ");
        String senha = sc.nextLine();

        LoginController controller = new LoginController();
        controller.fazerLogin(email, senha);
    }
}
